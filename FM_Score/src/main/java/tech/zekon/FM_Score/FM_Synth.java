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
 * {@link #renderPcm} produces offline PCM for audio export on an independent {@code tsf_copy}
 * (shares the read-only samples, separate voices), so it runs concurrently with live play.
 *
 * <p>TSF is not thread-safe per instance: the live instance's render/note calls are serialized
 * on {@link #lock}, and stream start/stop is serialized on {@link #streamLock}.
 */
class FM_Synth {

    static final int SAMPLE_RATE = 44100;
    private static final int BLOCK_FRAMES = 1024;   // ~23 ms render block
    private static final float VELOCITY = 100f / 127f;
    private static final long TAIL_MS = 1500;       // release tail rendered after the last note

    /** False if the native synth library couldn't load — the synth then stays silent instead of crashing. */
    private static final boolean NATIVE_OK = loadNative();

    private static boolean loadNative() {
        try {
            System.loadLibrary("fmsynth");
            return true;
        } catch (Throwable t) {
            FM_Log.e("FM_Synth", "Native synth library failed to load; MIDI instruments unavailable", t);
            return false;
        }
    }

    private static FM_Synth mInstance;

    static synchronized FM_Synth getInstance(Context context) {
        if (mInstance == null) mInstance = new FM_Synth(context.getApplicationContext());
        return mInstance;
    }

    private final Context context;
    private final Object lock = new Object();            // serializes native render/note calls (TSF is not thread-safe)
    private final Object streamLock = new Object();      // serializes stream lifecycle (start/stop) across threads
    // 1..88, re-trigger guard for the live keyboard. Touched from the UI thread (play/stopKey) and
    // cleared from stop()/allNotesOff(); races are benign (at worst a missed re-trigger guard).
    private final boolean[] activeKeys = new boolean[89];

    private volatile long handle = 0;
    private volatile boolean ready = false;
    private volatile boolean loading = false;
    private int currentProgram = 0;
    private volatile boolean[] playable;   // [1..88] = key has a sample in the current program; null = unknown

    // live streaming
    private volatile boolean streaming = false;
    private volatile boolean startWhenReady = false;   // a start() arrived before the SoundFont finished loading
    private Thread renderThread;
    private final Object wake = new Object();           // wakes the idle render thread when a note is played / on stop
    private boolean woken;                              // guarded by `wake`

    private FM_Synth(Context context) {
        this.context = context;
    }

    boolean isReady() {
        return ready;
    }

    /** Kicks off the one-time async SoundFont load. Safe to call repeatedly. No-op if the native lib is missing. */
    void ensureLoaded() {
        if (!NATIVE_OK || ready || loading) return;
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
        synchronized (streamLock) {
            startWhenReady = false;
            synchronized (lock) {
                nativeSetProgram(handle, 0, program);
            }
            playable = computePlayable(program);   // which keys this instrument can actually play
            startStreaming();
        }
    }

    /** True if {@code key} (1..88) is mapped in the current instrument; true while the range is still unknown. */
    boolean isKeyPlayable(int key) {
        boolean[] p = playable;
        return p == null || key < 1 || key >= p.length || p[key];
    }

    /** Maps the program's covered MIDI notes to a per-key (1..88) playable flag via {@link FM_ScorePlayer#keyToMidi}. */
    private boolean[] computePlayable(int program) {
        boolean[] covered = nativeCoveredNotes(handle, program);
        boolean[] pk = new boolean[89];
        if (covered != null) {
            for (int key = 1; key <= 88; key++) {
                int note = FM_ScorePlayer.keyToMidi(key);
                pk[key] = note >= 0 && note < covered.length && covered[note];
            }
        }
        return pk;
    }

    /** Stops live playback and releases the audio stream (e.g. when switching back to piano). */
    void stop() {
        synchronized (streamLock) {
            startWhenReady = false;   // cancel any pending auto-start (e.g. app paused mid-load)
            stopStreaming();
        }
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
            nativeNoteOn(handle, 0, FM_ScorePlayer.keyToMidi(key), VELOCITY);
        }
        wakeRenderThread();   // resume streaming if it went idle while silent
    }

    private void wakeRenderThread() {
        synchronized (wake) {
            woken = true;
            wake.notify();
        }
    }

    void stopKey(int key) {
        if (!ready || key < 1 || key > 88) return;
        activeKeys[key] = false;
        synchronized (lock) {
            nativeNoteOff(handle, 0, FM_ScorePlayer.keyToMidi(key));
        }
    }

    boolean isKeyNotPlaying(int key) {
        return key < 1 || key > 88 || !activeKeys[key];
    }

    // Always called while holding streamLock (from start()); the guard below is therefore atomic.
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
            short[] buf = new short[BLOCK_FRAMES];
            boolean paused = true;   // stay silent (and cheap) until the first note
            try {
                while (streaming) {
                    int active;
                    synchronized (lock) {
                        nativeRender(handle, buf, BLOCK_FRAMES);
                        active = nativeActiveVoiceCount(handle);
                    }
                    if (active > 0) {
                        if (paused) {
                            track.play();
                            paused = false;
                        }
                        track.write(buf, 0, BLOCK_FRAMES);   // blocks, pacing the loop
                    } else {
                        // Nothing sounding (including release tails): stop feeding the track and
                        // sleep until a note wakes us, instead of spinning out silence.
                        if (!paused) {
                            track.pause();
                            track.flush();
                            paused = true;
                        }
                        synchronized (wake) {
                            while (streaming && !woken) {
                                try {
                                    wake.wait(1000);
                                } catch (InterruptedException ignored) {
                                }
                            }
                            woken = false;
                        }
                    }
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
        synchronized (streamLock) {
            streaming = false;
            wakeRenderThread();   // in case it's idle-waiting, so it sees streaming==false and exits promptly
            Thread t = renderThread;
            renderThread = null;
            if (t != null) {
                try {
                    t.join(500);   // the render thread only takes `lock`, never streamLock — no deadlock
                } catch (InterruptedException ignored) {
                }
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
        if (keys.length == 0) return new short[0];
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
            int note = FM_ScorePlayer.keyToMidi(keys[i]);
            times[i] = startMs[i];
            midi[i] = note;
            on[i] = true;
            times[n + i] = end;
            midi[n + i] = note;
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

        // Render on an independent copy of the synth: it shares the (read-only) decoded samples
        // but has its own voice state, so the export never touches the live `lock` and never has
        // to stop live playback — both can render concurrently and safely.
        long h = nativeCopy(handle, SAMPLE_RATE);
        if (h == 0) return new short[0];
        try {
            nativeSetProgram(h, 0, program);
            int cursor = 0;
            for (int idx : order) {
                int target = (int) (times[idx] * SAMPLE_RATE / 1000L);
                cursor = renderInto(h, out, block, cursor, target);
                if (on[idx]) nativeNoteOn(h, 0, midi[idx], VELOCITY);
                else nativeNoteOff(h, 0, midi[idx]);
            }
            renderInto(h, out, block, cursor, totalFrames);
        } finally {
            nativeFree(h);
        }
        return out;
    }

    /** Renders frames into {@code out[from..toFrame)} in blocks on synth {@code h}; returns the new cursor. */
    private int renderInto(long h, short[] out, short[] block, int from, int toFrame) {
        if (toFrame > out.length) toFrame = out.length;
        int cursor = from;
        while (cursor < toFrame) {
            int count = Math.min(BLOCK_FRAMES, toFrame - cursor);
            nativeRender(h, block, count);
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

    /** Returns an independent synth sharing this one's loaded samples (for offline render); free with nativeFree. */
    private native long nativeCopy(long handle, int sampleRate);

    private native int nativeSetProgram(long handle, int channel, int program);

    /** Returns a boolean[128] marking which MIDI notes the GM {@code program} has a sample region for. */
    private native boolean[] nativeCoveredNotes(long handle, int program);

    private native void nativeNoteOn(long handle, int channel, int key, float velocity);

    private native void nativeNoteOff(long handle, int channel, int key);

    private native void nativeAllNotesOff(long handle);

    private native int nativeActiveVoiceCount(long handle);

    private native void nativeRender(long handle, short[] out, int frames);

    private native void nativeFree(long handle);
}
