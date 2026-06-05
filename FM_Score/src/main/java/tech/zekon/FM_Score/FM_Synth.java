package tech.zekon.FM_Score;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * General-MIDI instrument synthesizer backed by TinySoundFont (native, see
 * {@code src/main/cpp/tsf_jni.c}). Used for every non-piano instrument; the piano
 * keeps its own recorded-sample engine ({@link FM_SoundPool}).
 *
 * <p>The SoundFont ({@code assets/soundfont.sf3}, ~22&nbsp;MB) is heavy to load and
 * decode, so it is loaded lazily on the first non-piano selection — piano-only users
 * never pay for it. Live playing streams {@code tsf_render_short} into a {@link AudioTrack};
 * {@link #renderPcm} produces offline PCM for audio export. TSF is not thread-safe, so all
 * native render/note calls are serialized on {@link #lock}.
 */
class FM_Synth {

    static final int SAMPLE_RATE = 44100;
    private static final int BLOCK_FRAMES = 1024;   // ~23 ms render block
    private static final float VELOCITY = 100f / 127f;
    private static final int KEY_TO_MIDI = 20;      // key 1 (A0) -> MIDI note 21
    private static final long TAIL_MS = 1500;       // release tail rendered after the last note

    static {
        System.loadLibrary("fmsynth");
    }

    private static FM_Synth mInstance;

    static synchronized FM_Synth getInstance(Context context) {
        if (mInstance == null) mInstance = new FM_Synth(context.getApplicationContext());
        return mInstance;
    }

    private final Context context;
    private final Object lock = new Object();
    private final boolean[] activeKeys = new boolean[89];   // 1..88, for re-trigger guard

    private volatile long handle = 0;
    private volatile boolean ready = false;
    private volatile boolean loading = false;
    private int currentProgram = 0;

    // live streaming
    private volatile boolean streaming = false;
    private volatile boolean startWhenReady = false;   // a start() arrived before the SoundFont finished loading
    private Thread renderThread;

    private FM_Synth(Context context) {
        this.context = context;
    }

    boolean isReady() {
        return ready;
    }

    /** Kicks off the one-time async SoundFont load. Safe to call repeatedly. */
    void ensureLoaded() {
        if (ready || loading) return;
        synchronized (this) {
            if (ready || loading) return;
            loading = true;
        }
        new Thread(() -> {
            try {
                byte[] sf = readAsset("soundfont.sf3");
                long h = nativeLoad(sf, SAMPLE_RATE);
                if (h != 0) {
                    handle = h;
                    ready = true;
                }
            } catch (Throwable t) {
                FM_Log.e("FM_Synth", "Failed to load SoundFont", t);
            } finally {
                loading = false;
            }
            // If an instrument was selected before the SoundFont finished loading (e.g. it was the
            // saved instrument restored at startup), begin streaming it now.
            if (ready && startWhenReady) start(currentProgram);
        }, "FM_Synth-load").start();
    }

    // ---- live playing ----

    /** Selects the current GM program (0..127) and (re)starts the live render stream. */
    void start(int program) {
        currentProgram = program;
        if (!ready) {
            // SoundFont still loading: remember the intent so the load thread starts us when ready.
            startWhenReady = true;
            ensureLoaded();
            return;
        }
        startWhenReady = false;
        synchronized (lock) {
            nativeSetProgram(handle, 0, program);
        }
        startStreaming();
    }

    /** Stops live playback and releases the audio stream (e.g. when switching back to piano). */
    void stop() {
        startWhenReady = false;   // cancel any pending auto-start (e.g. app paused mid-load)
        stopStreaming();
        for (int i = 1; i <= 88; i++) activeKeys[i] = false;
        if (ready) {
            synchronized (lock) {
                nativeAllNotesOff(handle);
            }
        }
    }

    /** Silences all currently-sounding notes without tearing down the live stream. */
    void allNotesOff() {
        for (int i = 1; i <= 88; i++) activeKeys[i] = false;
        if (!ready) return;
        synchronized (lock) {
            nativeAllNotesOff(handle);
        }
    }

    void playKey(int key) {
        if (!ready || key < 1 || key > 88) return;
        activeKeys[key] = true;
        synchronized (lock) {
            nativeNoteOn(handle, 0, key + KEY_TO_MIDI, VELOCITY);
        }
    }

    void stopKey(int key) {
        if (!ready || key < 1 || key > 88) return;
        activeKeys[key] = false;
        synchronized (lock) {
            nativeNoteOff(handle, 0, key + KEY_TO_MIDI);
        }
    }

    boolean isKeyNotPlaying(int key) {
        return key < 1 || key > 88 || !activeKeys[key];
    }

    private void startStreaming() {
        if (streaming) return;
        streaming = true;
        renderThread = new Thread(() -> {
            int minBuf = AudioTrack.getMinBufferSize(SAMPLE_RATE,
                    AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
            int bufBytes = Math.max(minBuf, BLOCK_FRAMES * 2 * 4);
            AudioTrack track = new AudioTrack.Builder()
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build())
                    .setAudioFormat(new AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(SAMPLE_RATE)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build())
                    .setTransferMode(AudioTrack.MODE_STREAM)
                    .setBufferSizeInBytes(bufBytes)
                    .build();
            track.play();
            short[] buf = new short[BLOCK_FRAMES];
            try {
                while (streaming) {
                    synchronized (lock) {
                        nativeRender(handle, buf, BLOCK_FRAMES);
                    }
                    track.write(buf, 0, BLOCK_FRAMES);   // blocks, pacing the loop
                }
            } catch (Exception e) {
                FM_Log.w("FM_Synth", "Render loop stopped", e);
            } finally {
                try {
                    track.stop();
                    track.release();
                } catch (Exception ignored) {
                }
            }
        }, "FM_Synth-render");
        renderThread.start();
    }

    private void stopStreaming() {
        streaming = false;
        Thread t = renderThread;
        renderThread = null;
        if (t != null) {
            try {
                t.join(500);
            } catch (InterruptedException ignored) {
            }
        }
    }

    // ---- offline render (for audio export) ----

    /**
     * Renders a performance to mono 16-bit PCM at {@link #SAMPLE_RATE} using {@code program}.
     * Each entry of the parallel arrays is one note: chromatic key 1..88, start, and duration
     * in milliseconds. Returns an empty array if the SoundFont is not loaded yet.
     */
    short[] renderPcm(int program, int[] keys, long[] startMs, long[] durMs) {
        if (!ready) {
            // Export runs off the main thread, so it's safe to block until the SoundFont loads.
            ensureLoaded();
            for (int i = 0; i < 200 && !ready; i++) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {
                }
            }
            if (!ready) return new short[0];
        }
        int n = keys.length;

        // Flatten to time-ordered note on/off events (release before press at the same instant).
        long[] times = new long[n * 2];
        int[] midi = new int[n * 2];
        boolean[] on = new boolean[n * 2];
        long maxEnd = 0;
        for (int i = 0; i < n; i++) {
            long end = startMs[i] + durMs[i];
            if (end > maxEnd) maxEnd = end;
            times[i] = startMs[i];
            midi[i] = keys[i] + KEY_TO_MIDI;
            on[i] = true;
            times[n + i] = end;
            midi[n + i] = keys[i] + KEY_TO_MIDI;
            on[n + i] = false;
        }
        Integer[] order = new Integer[n * 2];
        for (int i = 0; i < order.length; i++) order[i] = i;
        java.util.Arrays.sort(order, (a, b) -> {
            if (times[a] != times[b]) return Long.compare(times[a], times[b]);
            return Boolean.compare(on[a], on[b]);   // false (off) before true (on)
        });

        long totalMs = maxEnd + TAIL_MS;
        int totalFrames = (int) (totalMs * SAMPLE_RATE / 1000L);
        short[] out = new short[totalFrames];
        short[] block = new short[BLOCK_FRAMES];

        // The live stream and the offline render share one native instance, so stop the
        // stream (joining its thread) before driving the synth ourselves, then restart it.
        boolean wasStreaming = streaming;
        if (wasStreaming) stopStreaming();
        synchronized (lock) {
            nativeReset(handle);
            nativeSetProgram(handle, 0, program);

            int cursor = 0;
            for (int idx : order) {
                int target = (int) (times[idx] * SAMPLE_RATE / 1000L);
                cursor = renderInto(out, block, cursor, target);
                if (on[idx]) nativeNoteOn(handle, 0, midi[idx], VELOCITY);
                else nativeNoteOff(handle, 0, midi[idx]);
            }
            renderInto(out, block, cursor, totalFrames);
            nativeReset(handle);
        }
        if (wasStreaming) start(currentProgram);
        return out;
    }

    /** Renders frames into {@code out[from..toFrame)} in blocks; returns the new cursor. */
    private int renderInto(short[] out, short[] block, int from, int toFrame) {
        if (toFrame > out.length) toFrame = out.length;
        int cursor = from;
        while (cursor < toFrame) {
            int count = Math.min(BLOCK_FRAMES, toFrame - cursor);
            nativeRender(handle, block, count);
            System.arraycopy(block, 0, out, cursor, count);
            cursor += count;
        }
        return cursor;
    }

    private byte[] readAsset(String name) throws Exception {
        try (InputStream in = context.getAssets().open(name)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream(1 << 20);
            byte[] buf = new byte[1 << 16];
            int r;
            while ((r = in.read(buf)) != -1) out.write(buf, 0, r);
            return out.toByteArray();
        }
    }

    // ---- native ----

    private native long nativeLoad(byte[] data, int sampleRate);

    private native int nativeSetProgram(long handle, int channel, int program);

    private native void nativeNoteOn(long handle, int channel, int key, float velocity);

    private native void nativeNoteOff(long handle, int channel, int key);

    private native void nativeAllNotesOff(long handle);

    private native void nativeRender(long handle, short[] out, int frames);

    private native void nativeReset(long handle);

    private native void nativeFree(long handle);
}
