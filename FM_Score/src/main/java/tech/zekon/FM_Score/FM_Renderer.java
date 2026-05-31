package tech.zekon.FM_Score;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Offline renderer: mixes recorded note events through the bundled piano samples into
 * 16-bit mono PCM at {@link #SAMPLE_RATE}, for exporting a performance as audio.
 *
 * <p>Uses the same sample assets the live engine plays, but renders ahead-of-time (no
 * SoundPool / AudioTrack), so it must be run off the main thread. The PCM can be written
 * as a WAV ({@link #renderWav}) or handed to a compressor (see {@link #renderPcm}).
 */
public final class FM_Renderer {

    public static final int SAMPLE_RATE = 24000;
    private static final int SAMPLES_PER_MS = SAMPLE_RATE / 1000; // 24
    private static final int FADE_MS = 220;     // release fade once a key is lifted
    private static final int ATTACK_MS = 5;     // short fade-in to avoid onset clicks
    private static final double HEADROOM = 28000.0; // peak ceiling (~-1.4 dBFS) so lossy AAC won't clip
    private static final int HEADER_BYTES = 44; // WAV header length in the bundled samples

    /** One note to render: piano key {@code sound} (1..88), starting at {@code startMs}, held for {@code durationMs}. */
    public static final class Note {
        public final int sound;
        public final long startMs;
        public final long durationMs;

        public Note(int sound, long startMs, long durationMs) {
            this.sound = sound;
            this.startMs = startMs;
            this.durationMs = durationMs;
        }
    }

    private FM_Renderer() {
    }

    /** Mixes {@code notes} into normalized 16-bit mono PCM at {@link #SAMPLE_RATE}. */
    public static short[] renderPcm(Context context, List<Note> notes) throws IOException {
        long endMs = 0;
        for (Note n : notes) endMs = Math.max(endMs, n.startMs + n.durationMs + FADE_MS);
        int total = Math.max((int) (endMs * SAMPLES_PER_MS) + 1, 1);

        double[] mix = new double[total];
        Map<Integer, short[]> cache = new HashMap<>();
        int fadeSamples = FADE_MS * SAMPLES_PER_MS;
        int attackSamples = ATTACK_MS * SAMPLES_PER_MS;

        for (Note n : notes) {
            short[] sample = cache.get(n.sound);
            if (sample == null) {
                sample = loadSample(context, n.sound);
                cache.put(n.sound, sample);
            }
            if (sample.length == 0) continue;
            int start = (int) (n.startMs * SAMPLES_PER_MS);
            int held = (int) (n.durationMs * SAMPLES_PER_MS);
            int len = Math.min(sample.length, held + fadeSamples);
            for (int i = 0; i < len; i++) {
                int pos = start + i;
                if (pos < 0 || pos >= mix.length) continue;
                double gain = 1.0;
                if (i < attackSamples) gain = (double) i / attackSamples;    // anti-click attack fade-in
                if (i > held) {
                    double release = 1.0 - (double) (i - held) / fadeSamples; // release fade-out
                    if (release < gain) gain = release;
                }
                if (gain < 0) gain = 0;
                mix[pos] += sample[i] * gain;
            }
        }

        double peak = 0;
        for (double v : mix) {
            double a = Math.abs(v);
            if (a > peak) peak = a;
        }
        double scale = (peak > HEADROOM) ? HEADROOM / peak : 1.0; // keep headroom so lossy AAC won't clip

        short[] pcm = new short[total];
        for (int i = 0; i < total; i++) {
            int s = (int) Math.round(mix[i] * scale);
            if (s > 32767) s = 32767;
            else if (s < -32768) s = -32768;
            pcm[i] = (short) s;
        }
        return pcm;
    }

    /** Renders {@code notes} to a 16-bit mono PCM WAV written to {@code out}. */
    public static void renderWav(Context context, List<Note> notes, OutputStream out) throws IOException {
        short[] pcm = renderPcm(context, notes);
        int dataSize = pcm.length * 2;

        byte[] header = new byte[HEADER_BYTES];
        putAscii(header, 0, "RIFF");
        putIntLE(header, 4, 36 + dataSize);
        putAscii(header, 8, "WAVE");
        putAscii(header, 12, "fmt ");
        putIntLE(header, 16, 16);
        putShortLE(header, 20, (short) 1);          // PCM
        putShortLE(header, 22, (short) 1);          // mono
        putIntLE(header, 24, SAMPLE_RATE);
        putIntLE(header, 28, SAMPLE_RATE * 2);      // byte rate
        putShortLE(header, 32, (short) 2);          // block align
        putShortLE(header, 34, (short) 16);         // bits per sample
        putAscii(header, 36, "data");
        putIntLE(header, 40, dataSize);
        out.write(header);

        byte[] pcmBytes = new byte[dataSize];
        for (int i = 0; i < pcm.length; i++) {
            pcmBytes[i * 2] = (byte) (pcm[i] & 0xff);
            pcmBytes[i * 2 + 1] = (byte) ((pcm[i] >> 8) & 0xff);
        }
        out.write(pcmBytes);
        out.flush();
    }

    private static short[] loadSample(Context context, int sound) throws IOException {
        String path = FM_SoundPool.assetFiles.get(sound);
        if (path == null) return new short[0];
        AssetManager am = context.getAssets();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (BufferedInputStream in = new BufferedInputStream(am.open(path))) {
            byte[] buf = new byte[8192];
            int read;
            while ((read = in.read(buf)) != -1) bos.write(buf, 0, read);
        }
        byte[] bytes = bos.toByteArray();
        int dataOffset = findDataChunk(bytes); // header size varies (fmt extensions, LIST/fact chunks)
        if (dataOffset < 0) return new short[0];
        int dataLen = bytes.length - dataOffset;
        short[] samples = new short[dataLen / 2];
        for (int i = 0, j = dataOffset; i < samples.length; i++, j += 2) {
            samples[i] = (short) ((bytes[j] & 0xff) | (bytes[j + 1] << 8)); // little-endian PCM16
        }
        return samples;
    }

    /** Byte offset of the WAV "data" chunk payload, walking the chunk list, or -1 if absent. */
    private static int findDataChunk(byte[] b) {
        if (b.length < 12) return -1;
        int p = 12; // skip RIFF(4) size(4) WAVE(4)
        while (p + 8 <= b.length) {
            int size = (b[p + 4] & 0xff) | ((b[p + 5] & 0xff) << 8)
                    | ((b[p + 6] & 0xff) << 16) | ((b[p + 7] & 0xff) << 24);
            if (b[p] == 'd' && b[p + 1] == 'a' && b[p + 2] == 't' && b[p + 3] == 'a') {
                return p + 8;
            }
            if (size < 0) break;
            p += 8 + size + (size & 1); // chunks are word-aligned
        }
        return -1;
    }

    private static void putAscii(byte[] b, int off, String s) {
        for (int i = 0; i < s.length(); i++) b[off + i] = (byte) s.charAt(i);
    }

    private static void putIntLE(byte[] b, int off, int v) {
        b[off] = (byte) (v & 0xff);
        b[off + 1] = (byte) ((v >> 8) & 0xff);
        b[off + 2] = (byte) ((v >> 16) & 0xff);
        b[off + 3] = (byte) ((v >> 24) & 0xff);
    }

    private static void putShortLE(byte[] b, int off, short v) {
        b[off] = (byte) (v & 0xff);
        b[off + 1] = (byte) ((v >> 8) & 0xff);
    }
}
