package ro.florinm.FM_Score;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.media.SoundPool;
import android.os.SystemClock;
import android.util.SparseArray;
import android.util.SparseIntArray;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

class FM_AudioSubTrack implements Comparable<FM_AudioSubTrack>{
    int track;
    int duration;
    FM_AudioSubTrack(int track, int duration){
        super();
        this.track = track;
        this.duration = duration;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        FM_AudioSubTrack other = (FM_AudioSubTrack) obj;
        return this.duration == other.duration && this.track == other.track;
    }

    @Override
    public int compareTo(FM_AudioSubTrack f) {
        if (track > f.track || track == f.track) return 1;
        return -1;
    }
}

class FM_AudioTrack {
    protected int AccessIndex = 0; //Used to remove old, unused tracks
    private final FM_AudioSubTrack track1, track2, track3, track4, track5, track6, track7;
    Boolean loading;
    Context context;
    short[] output;

    Boolean Check(FM_AudioSubTrack track1, FM_AudioSubTrack track2, FM_AudioSubTrack track3, FM_AudioSubTrack track4, FM_AudioSubTrack track5, FM_AudioSubTrack track6, FM_AudioSubTrack track7) {
        List<FM_AudioSubTrack> self = new ArrayList<>();
        List<FM_AudioSubTrack> checked = new ArrayList<>();
        if (this.track1 != null) self.add(this.track1);
        if (this.track2 != null) self.add(this.track2);
        if (this.track3 != null) self.add(this.track3);
        if (this.track4 != null) self.add(this.track4);
        if (this.track5 != null) self.add(this.track5);
        if (this.track6 != null) self.add(this.track6);
        if (this.track7 != null) self.add(this.track7);
        Collections.sort(self);
        if (track1 != null) checked.add(track1);
        if (track2 != null) checked.add(track2);
        if (track3 != null) checked.add(track3);
        if (track4 != null) checked.add(track4);
        if (track5 != null) checked.add(track5);
        if (track6 != null) checked.add(track6);
        if (track7 != null) checked.add(track7);
        Collections.sort(checked);
        if (self.size() != checked.size()) return false;
        for (int i = 0; i < self.size(); i++)
            if (!self.get(i).equals(checked.get(i))) return false;
        return true;
    }

    FM_AudioTrack(Context context, FM_AudioSubTrack track1, FM_AudioSubTrack track2, FM_AudioSubTrack track3, FM_AudioSubTrack track4, FM_AudioSubTrack track5, FM_AudioSubTrack track6, FM_AudioSubTrack track7) {
        super();
        this.context = context;
        this.track1 = track1;
        this.track2 = track2;
        this.track3 = track3;
        this.track4 = track4;
        this.track5 = track5;
        this.track6 = track6;
        this.track7 = track7;
        this.loading = true;
        try {
            InputStream in1 = null;
            InputStream in2 = null;
            InputStream in3 = null;
            InputStream in4 = null;
            InputStream in5 = null;
            InputStream in6 = null;
            InputStream in7 = null;
            if (track1 != null)
                in1 = context.getAssets().open(FM_SoundPool.assetFiles.get(track1.track));
            if (track2 != null)
                in2 = context.getAssets().open(FM_SoundPool.assetFiles.get(track2.track));
            if (track3 != null)
                in3 = context.getAssets().open(FM_SoundPool.assetFiles.get(track3.track));
            if (track4 != null)
                in4 = context.getAssets().open(FM_SoundPool.assetFiles.get(track4.track));
            if (track5 != null)
                in5 = context.getAssets().open(FM_SoundPool.assetFiles.get(track5.track));
            if (track6 != null)
                in6 = context.getAssets().open(FM_SoundPool.assetFiles.get(track6.track));
            if (track7 != null)
                in7 = context.getAssets().open(FM_SoundPool.assetFiles.get(track7.track));

            short[] music1 = null;
            short[] music2 = null;
            short[] music3 = null;
            short[] music4 = null;
            short[] music5 = null;
            short[] music6 = null;
            short[] music7 = null;
            if (track1 != null) {
                music1 = sampleToShortArray(in1);
                in1.close();
            }
            if (track2 != null) {
                music2 = sampleToShortArray(in2);
                in2.close();
            }
            if (track3 != null) {
                music3 = sampleToShortArray(in3);
                in3.close();
            }
            if (track4 != null) {
                music4 = sampleToShortArray(in4);
                in4.close();
            }
            if (track5 != null) {
                music5 = sampleToShortArray(in5);
                in5.close();
            }
            if (track6 != null) {
                music6 = sampleToShortArray(in6);
                in6.close();
            }
            if (track7 != null) {
                music7 = sampleToShortArray(in7);
                in7.close();
            }

            int l = 0;
            int fallback = FM_SoundPool.FALLBACK_DURATION * 24;

            if (track1 != null && l < track1.duration * 24 + fallback)
                l = track1.duration * 24 + fallback;
            if (track2 != null && l < track2.duration * 24 + fallback)
                l = track2.duration * 24 + fallback;
            if (track3 != null && l < track3.duration * 24 + fallback)
                l = track3.duration * 24 + fallback;
            if (track4 != null && l < track4.duration * 24 + fallback)
                l = track4.duration * 24 + fallback;
            if (track5 != null && l < track5.duration * 24 + fallback)
                l = track5.duration * 24 + fallback;
            if (track6 != null && l < track6.duration * 24 + fallback)
                l = track6.duration * 24 + fallback;
            if (track7 != null && l < track7.duration * 24 + fallback)
                l = track7.duration * 24 + fallback;

            output = new short[l];

            int td1 = 0;
            int td2 = 0;
            int td3 = 0;
            int td4 = 0;
            int td5 = 0;
            int td6 = 0;
            int td7 = 0;
            if (track1 != null) td1 = track1.duration * 24;
            if (track2 != null) td2 = track2.duration * 24;
            if (track3 != null) td3 = track3.duration * 24;
            if (track4 != null) td4 = track4.duration * 24;
            if (track5 != null) td5 = track5.duration * 24;
            if (track6 != null) td6 = track6.duration * 24;
            if (track7 != null) td7 = track7.duration * 24;
            for (int i = 0; i < output.length; i++) {
                float sample1 = 0;
                float sample2 = 0;
                float sample3 = 0;
                float sample4 = 0;
                float sample5 = 0;
                float sample6 = 0;
                float sample7 = 0;
                if (track1 != null)
                    if (i < td1) {
                        sample1 = music1[i] / 32768.0f;
                    } else {
                        if (i > td1 + fallback) sample1 = 0;
                        else
                            sample1 = (music1[i] / 32768.0f) * (td1 + fallback - i) / fallback;
                    }
                if (track2 != null)
                    if (i < td2) {
                        sample2 = music2[i] / 32768.0f;
                    } else {
                        if (i > td2 + fallback) sample2 = 0;
                        else
                            sample2 = (music2[i] / 32768.0f) * (td2 + fallback - i) / fallback;
                    }
                if (track3 != null)
                    if (i < td3) {
                        sample3 = music3[i] / 32768.0f;
                    } else {
                        if (i > td3 + fallback) sample3 = 0;
                        else
                            sample3 = (music3[i] / 32768.0f) * (td3 + fallback - i) / fallback;
                    }
                if (track4 != null)
                    if (i < td4) {
                        sample4 = music4[i] / 32768.0f;
                    } else {
                        if (i > td4 + fallback) sample4 = 0;
                        else
                            sample4 = (music4[i] / 32768.0f) * (td4 + fallback - i) / fallback;
                    }
                if (track5 != null)
                    if (i < td5) {
                        sample5 = music5[i] / 32768.0f;
                    } else {
                        if (i > td5 + fallback) sample5 = 0;
                        else
                            sample5 = (music5[i] / 32768.0f) * (td5 + fallback - i) / fallback;
                    }
                if (track6 != null)
                    if (i < td6) {
                        sample6 = music6[i] / 32768.0f;
                    } else {
                        if (i > td6 + fallback) sample6 = 0;
                        else
                            sample6 = (music6[i] / 32768.0f) * (td6 + fallback - i) / fallback;
                    }
                if (track7 != null)
                    if (i < td7) {
                        sample7 = music7[i] / 32768.0f;
                    } else {
                        if (i > td7 + fallback) sample7 = 0;
                        else
                            sample7 = (music7[i] / 32768.0f) * (td7 + fallback - i) / fallback;
                    }
                float mixed = sample1 + sample2 + sample3 + sample4 + sample5 + sample6 + sample7;
                mixed *= 0.8;
                if (mixed > 1.0f) mixed = 1.0f;
                if (mixed < -1.0f) mixed = -1.0f;

                output[i] = (short) (mixed * 32768.0f);
            }
            loading = false;
        } catch (Exception ignored) {
        }
    }

    private static short swapBytes(byte byte0, byte byte1) {
        return (short) ((byte1 & 0xff) << 8 | (byte0 & 0xff));
    }

    private static byte[] sampleToByteArray(InputStream sample) throws IOException {
        ByteArrayOutputStream bOutStream = new ByteArrayOutputStream();
        BufferedInputStream bis = new BufferedInputStream(sample);
        byte[] header = new byte[44];
        if (bis.read(header) == -1) throw new IOException();
        int bufferSize = 1024 * 8;
        byte[] buffer = new byte[bufferSize];
        while (bis.read(buffer) != -1) {
            bOutStream.write(buffer);
        }
        byte[] outputByteArray = bOutStream.toByteArray();
        bis.close();
        bOutStream.close();

        return outputByteArray;
    }

    private static short[] sampleToShortArray(InputStream sample) throws IOException {

        short[] outputArray = new short[sample.available() / 2];
        byte[] outputByteArray = sampleToByteArray(sample);
        for (int i = 0, j = 0; i < outputByteArray.length; i += 2, j++) {
            try {
                outputArray[j] = swapBytes(outputByteArray[i], outputByteArray[i + 1]);
            } catch (Exception ignored) {
            }
        }
        return outputArray;
    }

    void Play(long duration, boolean NextPause) {
        //if (playing) return;
        if (output.length == 0) return;
        new Thread(() -> {
            while (loading) SystemClock.sleep(10);
            AudioTrack audioTrack = new AudioTrack.Builder()
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .setPerformanceMode(AudioTrack.PERFORMANCE_MODE_LOW_LATENCY)
                    .setAudioAttributes(new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .build())
                    .setAudioFormat(new AudioFormat.Builder()
                            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                            .setSampleRate(24000)
                            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                            .build())
                    .setBufferSizeInBytes(output.length * 2)
                    .build();
            audioTrack.setPlaybackHeadPosition(44);
            audioTrack.write(output, 0, output.length);
            audioTrack.play();
            FM_SoundPool.CustomDelay(duration, false);
            float d = 0;
            float fall = FM_SoundPool.FALLBACK_DURATION;
            if (NextPause)
                fall = fall / 5f;
            while (d < fall) {
                d += FM_SoundPool.CustomDelay(2, true);
                float p = 1 - d / fall;
                audioTrack.setVolume(p);
            }
            audioTrack.stop();
            audioTrack.release();
        }).start();
    }
}

class FM_SoundPool {
    static int TIME_SIGNATURE_N;
    static int TIME_SIGNATURE_D;
    static int FALLBACK_DURATION = 250; //fallback duration for sounds
    static int MAX_TRACKS = 500;
    private final Context context;
    ArrayList<FM_AudioTrack> Tracks = new ArrayList<>();


    private void updateTrackList(int index) {
        Tracks.get(index).AccessIndex = -1;
        for (FM_AudioTrack t : Tracks) {
            t.AccessIndex += 1;
        }
        for (int i = Tracks.size() - 1; i >= 0; i--)
            if (Tracks.get(i).AccessIndex > MAX_TRACKS) Tracks.remove(i);
    }

    void ClearAudioTracks() {
        Tracks.clear();
    }

    FM_AudioTrack CreateTrack(List<Integer> tracks, List<Integer> d) {
        FM_AudioSubTrack[] track;
        track = new FM_AudioSubTrack[7];
        for (int i = 0; i < tracks.size(); i++)
            if (tracks.get(i) != -1)
                track[i] = new FM_AudioSubTrack(tracks.get(i), d.get(i));
        return CheckAndCreate(track);
    }

    private FM_AudioTrack CheckAndCreate(FM_AudioSubTrack[] track) {
        FM_AudioTrack t;
        for (int i = 0; i < Tracks.size(); i++)
            if (Tracks.get(i).Check(track[0], track[1], track[2], track[3], track[4], track[5], track[6])) {
                t = Tracks.get(i);
                updateTrackList(i);
                return t;
            }
        t = new FM_AudioTrack(context, track[0], track[1], track[2], track[3], track[4], track[5], track[6]);
        Tracks.add(t);
        updateTrackList(Tracks.size() - 1);
        return t;
    }

    //hold the playing threads
    private final SparseArray<PlayThread> threadMap;
    volatile static boolean playing;
    //hold the audio files
    protected static final SparseArray<String> assetFiles = new SparseArray<>();
    protected static final Map<String, Integer> KeyMapping = new HashMap<>();
    //the sound pool object
    private final SoundPool sndPool;
    //hold the loaded audio files
    private static final SparseIntArray soundMap = new SparseIntArray();

    static {
        assetFiles.put(1, "sounds/_0a.wav");
        assetFiles.put(2, "sounds/_0b.wav");
        assetFiles.put(3, "sounds/_0ad.wav");
        assetFiles.put(4, "sounds/_1c.wav");
        assetFiles.put(5, "sounds/_1d.wav");
        assetFiles.put(6, "sounds/_1cd.wav");
        assetFiles.put(7, "sounds/_1e.wav");
        assetFiles.put(8, "sounds/_1dd.wav");
        assetFiles.put(9, "sounds/_1f.wav");
        assetFiles.put(10, "sounds/_1g.wav");
        assetFiles.put(11, "sounds/_1fd.wav");
        assetFiles.put(12, "sounds/_1a.wav");
        assetFiles.put(13, "sounds/_1gd.wav");
        assetFiles.put(14, "sounds/_1b.wav");
        assetFiles.put(15, "sounds/_1ad.wav");
        assetFiles.put(16, "sounds/_2c.wav");
        assetFiles.put(17, "sounds/_2d.wav");
        assetFiles.put(18, "sounds/_2cd.wav");
        assetFiles.put(19, "sounds/_2e.wav");
        assetFiles.put(20, "sounds/_2dd.wav");
        assetFiles.put(21, "sounds/_2f.wav");
        assetFiles.put(22, "sounds/_2g.wav");
        assetFiles.put(23, "sounds/_2fd.wav");
        assetFiles.put(24, "sounds/_2a.wav");
        assetFiles.put(25, "sounds/_2gd.wav");
        assetFiles.put(26, "sounds/_2b.wav");
        assetFiles.put(27, "sounds/_2ad.wav");
        assetFiles.put(28, "sounds/_3c.wav");
        assetFiles.put(29, "sounds/_3d.wav");
        assetFiles.put(30, "sounds/_3cd.wav");
        assetFiles.put(31, "sounds/_3e.wav");
        assetFiles.put(32, "sounds/_3dd.wav");
        assetFiles.put(33, "sounds/_3f.wav");
        assetFiles.put(34, "sounds/_3g.wav");
        assetFiles.put(35, "sounds/_3fd.wav");
        assetFiles.put(36, "sounds/_3a.wav");
        assetFiles.put(37, "sounds/_3gd.wav");
        assetFiles.put(38, "sounds/_3b.wav");
        assetFiles.put(39, "sounds/_3ad.wav");
        assetFiles.put(40, "sounds/_4c.wav");
        assetFiles.put(41, "sounds/_4d.wav");
        assetFiles.put(42, "sounds/_4cd.wav");
        assetFiles.put(43, "sounds/_4e.wav");
        assetFiles.put(44, "sounds/_4dd.wav");
        assetFiles.put(45, "sounds/_4f.wav");
        assetFiles.put(46, "sounds/_4g.wav");
        assetFiles.put(47, "sounds/_4fd.wav");
        assetFiles.put(48, "sounds/_4a.wav");
        assetFiles.put(49, "sounds/_4gd.wav");
        assetFiles.put(50, "sounds/_4b.wav");
        assetFiles.put(51, "sounds/_4ad.wav");
        assetFiles.put(52, "sounds/_5c.wav");
        assetFiles.put(53, "sounds/_5d.wav");
        assetFiles.put(54, "sounds/_5cd.wav");
        assetFiles.put(55, "sounds/_5e.wav");
        assetFiles.put(56, "sounds/_5dd.wav");
        assetFiles.put(57, "sounds/_5f.wav");
        assetFiles.put(58, "sounds/_5g.wav");
        assetFiles.put(59, "sounds/_5fd.wav");
        assetFiles.put(60, "sounds/_5a.wav");
        assetFiles.put(61, "sounds/_5gd.wav");
        assetFiles.put(62, "sounds/_5b.wav");
        assetFiles.put(63, "sounds/_5ad.wav");
        assetFiles.put(64, "sounds/_6c.wav");
        assetFiles.put(65, "sounds/_6d.wav");
        assetFiles.put(66, "sounds/_6cd.wav");
        assetFiles.put(67, "sounds/_6e.wav");
        assetFiles.put(68, "sounds/_6dd.wav");
        assetFiles.put(69, "sounds/_6f.wav");
        assetFiles.put(70, "sounds/_6g.wav");
        assetFiles.put(71, "sounds/_6fd.wav");
        assetFiles.put(72, "sounds/_6a.wav");
        assetFiles.put(73, "sounds/_6gd.wav");
        assetFiles.put(74, "sounds/_6b.wav");
        assetFiles.put(75, "sounds/_6ad.wav");
        assetFiles.put(76, "sounds/_7c.wav");
        assetFiles.put(77, "sounds/_7d.wav");
        assetFiles.put(78, "sounds/_7cd.wav");
        assetFiles.put(79, "sounds/_7e.wav");
        assetFiles.put(80, "sounds/_7dd.wav");
        assetFiles.put(81, "sounds/_7f.wav");
        assetFiles.put(82, "sounds/_7g.wav");
        assetFiles.put(83, "sounds/_7fd.wav");
        assetFiles.put(84, "sounds/_7a.wav");
        assetFiles.put(85, "sounds/_7gd.wav");
        assetFiles.put(86, "sounds/_7b.wav");
        assetFiles.put(87, "sounds/_7ad.wav");
        assetFiles.put(88, "sounds/_8c.wav");

        KeyMapping.put("a/0", 1);
        KeyMapping.put("an/0", 1);
        KeyMapping.put("bbb/0", 1);
        KeyMapping.put("a#/0", 3);
        KeyMapping.put("bb/0", 3);
        KeyMapping.put("cbb/1", 3);
        KeyMapping.put("b/0", 2);
        KeyMapping.put("bn/0", 2);
        KeyMapping.put("a##/0", 2);
        KeyMapping.put("cb/1", 2);

        KeyMapping.put("c/1", 4);
        KeyMapping.put("cn/1", 4);
        KeyMapping.put("b#/0", 4);
        KeyMapping.put("dbb/1", 4);
        KeyMapping.put("a###/0", 4);
        KeyMapping.put("c#/1", 6);
        KeyMapping.put("b##/0", 6);
        KeyMapping.put("db/1", 6);
        KeyMapping.put("d/1", 5);
        KeyMapping.put("dn/1", 5);
        KeyMapping.put("c##/1", 5);
        KeyMapping.put("ebb/1", 5);
        KeyMapping.put("b###/0", 5);
        KeyMapping.put("d#/1", 8);
        KeyMapping.put("eb/1", 8);
        KeyMapping.put("fbb/1", 8);
        KeyMapping.put("c###/1", 8);
        KeyMapping.put("e/1", 7);
        KeyMapping.put("en/1", 7);
        KeyMapping.put("d##/1", 7);
        KeyMapping.put("fb/1", 7);
        KeyMapping.put("f/1", 9);
        KeyMapping.put("fn/1", 9);
        KeyMapping.put("e#/1", 9);
        KeyMapping.put("gbb/1", 9);
        KeyMapping.put("d###/1", 9);
        KeyMapping.put("f#/1", 11);
        KeyMapping.put("e##/1", 11);
        KeyMapping.put("gb/1", 11);
        KeyMapping.put("g/1", 10);
        KeyMapping.put("gn/1", 10);
        KeyMapping.put("f##/1", 10);
        KeyMapping.put("abb/1", 10);
        KeyMapping.put("e###/1", 10);
        KeyMapping.put("g#/1", 13);
        KeyMapping.put("ab/1", 13);
        KeyMapping.put("f###/1", 13);
        KeyMapping.put("a/1", 12);
        KeyMapping.put("an/1", 12);
        KeyMapping.put("g##/1", 12);
        KeyMapping.put("bbb/1", 12);
        KeyMapping.put("a#/1", 15);
        KeyMapping.put("bb/1", 15);
        KeyMapping.put("cbb/2", 15);
        KeyMapping.put("g###/1", 15);
        KeyMapping.put("b/1", 14);
        KeyMapping.put("bn/1", 14);
        KeyMapping.put("a##/1", 14);
        KeyMapping.put("cb/2", 14);

        KeyMapping.put("c/2", 16);
        KeyMapping.put("cn/2", 16);
        KeyMapping.put("b#/1", 16);
        KeyMapping.put("dbb/2", 16);
        KeyMapping.put("a###/1", 16);
        KeyMapping.put("c#/2", 18);
        KeyMapping.put("b##/1", 18);
        KeyMapping.put("db/2", 18);
        KeyMapping.put("d/2", 17);
        KeyMapping.put("dn/2", 17);
        KeyMapping.put("c##/2", 17);
        KeyMapping.put("ebb/2", 17);
        KeyMapping.put("b###/1", 17);
        KeyMapping.put("d#/2", 20);
        KeyMapping.put("eb/2", 20);
        KeyMapping.put("fbb/2", 20);
        KeyMapping.put("c###/2", 20);
        KeyMapping.put("e/2", 19);
        KeyMapping.put("en/2", 19);
        KeyMapping.put("d##/2", 19);
        KeyMapping.put("fb/2", 19);
        KeyMapping.put("f/2", 21);
        KeyMapping.put("fn/2", 21);
        KeyMapping.put("e#/2", 21);
        KeyMapping.put("gbb/2", 21);
        KeyMapping.put("d###/2", 21);
        KeyMapping.put("f#/2", 23);
        KeyMapping.put("e##/2", 23);
        KeyMapping.put("gb/2", 23);
        KeyMapping.put("g/2", 22);
        KeyMapping.put("gn/2", 22);
        KeyMapping.put("f##/2", 22);
        KeyMapping.put("abb/2", 22);
        KeyMapping.put("e###/2", 22);
        KeyMapping.put("g#/2", 25);
        KeyMapping.put("ab/2", 25);
        KeyMapping.put("f###/2", 25);
        KeyMapping.put("a/2", 24);
        KeyMapping.put("an/2", 24);
        KeyMapping.put("g##/2", 24);
        KeyMapping.put("bbb/2", 24);
        KeyMapping.put("a#/2", 27);
        KeyMapping.put("bb/2", 27);
        KeyMapping.put("cbb/3", 27);
        KeyMapping.put("g###/2", 27);
        KeyMapping.put("b/2", 26);
        KeyMapping.put("bn/2", 26);
        KeyMapping.put("a##/2", 26);
        KeyMapping.put("cb/3", 26);

        KeyMapping.put("c/3", 28);
        KeyMapping.put("cn/3", 28);
        KeyMapping.put("b#/2", 28);
        KeyMapping.put("dbb/3", 28);
        KeyMapping.put("a###/2", 28);
        KeyMapping.put("c#/3", 30);
        KeyMapping.put("b##/2", 30);
        KeyMapping.put("db/3", 30);
        KeyMapping.put("d/3", 29);
        KeyMapping.put("dn/3", 29);
        KeyMapping.put("c##/3", 29);
        KeyMapping.put("ebb/3", 29);
        KeyMapping.put("b###/2", 29);
        KeyMapping.put("d#/3", 32);
        KeyMapping.put("eb/3", 32);
        KeyMapping.put("fbb/3", 32);
        KeyMapping.put("c###/3", 32);
        KeyMapping.put("e/3", 31);
        KeyMapping.put("en/3", 31);
        KeyMapping.put("d##/3", 31);
        KeyMapping.put("fb/3", 31);
        KeyMapping.put("f/3", 33);
        KeyMapping.put("fn/3", 33);
        KeyMapping.put("e#/3", 33);
        KeyMapping.put("gbb/3", 33);
        KeyMapping.put("d###/3", 33);
        KeyMapping.put("f#/3", 35);
        KeyMapping.put("e##/3", 35);
        KeyMapping.put("gb/3", 35);
        KeyMapping.put("g/3", 34);
        KeyMapping.put("gn/3", 34);
        KeyMapping.put("f##/3", 34);
        KeyMapping.put("abb/3", 34);
        KeyMapping.put("e###/3", 34);
        KeyMapping.put("g#/3", 37);
        KeyMapping.put("ab/3", 37);
        KeyMapping.put("f###/3", 37);
        KeyMapping.put("a/3", 36);
        KeyMapping.put("an/3", 36);
        KeyMapping.put("g##/3", 36);
        KeyMapping.put("bbb/3", 36);
        KeyMapping.put("a#/3", 39);
        KeyMapping.put("bb/3", 39);
        KeyMapping.put("cbb/4", 39);
        KeyMapping.put("g###/3", 39);
        KeyMapping.put("b/3", 38);
        KeyMapping.put("bn/3", 38);
        KeyMapping.put("a##/3", 38);
        KeyMapping.put("cb/4", 38);

        KeyMapping.put("c/4", 40);
        KeyMapping.put("cn/4", 40);
        KeyMapping.put("b#/3", 40);
        KeyMapping.put("dbb/4", 40);
        KeyMapping.put("a###/3", 40);
        KeyMapping.put("c#/4", 42);
        KeyMapping.put("b##/3", 42);
        KeyMapping.put("db/4", 42);
        KeyMapping.put("d/4", 41);
        KeyMapping.put("dn/4", 41);
        KeyMapping.put("c##/4", 41);
        KeyMapping.put("ebb/4", 41);
        KeyMapping.put("b###/3", 41);
        KeyMapping.put("d#/4", 44);
        KeyMapping.put("eb/4", 44);
        KeyMapping.put("fbb/4", 44);
        KeyMapping.put("c###/4", 44);
        KeyMapping.put("e/4", 43);
        KeyMapping.put("en/4", 43);
        KeyMapping.put("d##/4", 43);
        KeyMapping.put("fb/4", 43);
        KeyMapping.put("f/4", 45);
        KeyMapping.put("fn/4", 45);
        KeyMapping.put("e#/4", 45);
        KeyMapping.put("gbb/4", 45);
        KeyMapping.put("d###/4", 45);
        KeyMapping.put("f#/4", 47);
        KeyMapping.put("e##/4", 47);
        KeyMapping.put("gb/4", 47);
        KeyMapping.put("g/4", 46);
        KeyMapping.put("gn/4", 46);
        KeyMapping.put("f##/4", 46);
        KeyMapping.put("abb/4", 46);
        KeyMapping.put("e###/4", 46);
        KeyMapping.put("g#/4", 49);
        KeyMapping.put("ab/4", 49);
        KeyMapping.put("f###/4", 49);
        KeyMapping.put("a/4", 48);
        KeyMapping.put("an/4", 48);
        KeyMapping.put("g##/4", 48);
        KeyMapping.put("bbb/4", 48);
        KeyMapping.put("a#/4", 51);
        KeyMapping.put("bb/4", 51);
        KeyMapping.put("cbb/5", 51);
        KeyMapping.put("g###/4", 51);
        KeyMapping.put("b/4", 50);
        KeyMapping.put("bn/4", 50);
        KeyMapping.put("a##/4", 50);
        KeyMapping.put("cb/5", 50);

        KeyMapping.put("c/5", 52);
        KeyMapping.put("cn/5", 52);
        KeyMapping.put("b#/4", 52);
        KeyMapping.put("dbb/5", 52);
        KeyMapping.put("a###/4", 52);
        KeyMapping.put("c#/5", 54);
        KeyMapping.put("b##/4", 54);
        KeyMapping.put("db/5", 54);
        KeyMapping.put("d/5", 53);
        KeyMapping.put("dn/5", 53);
        KeyMapping.put("c##/5", 53);
        KeyMapping.put("ebb/5", 53);
        KeyMapping.put("b###/4", 53);
        KeyMapping.put("d#/5", 56);
        KeyMapping.put("eb/5", 56);
        KeyMapping.put("fbb/5", 56);
        KeyMapping.put("c###/5", 56);
        KeyMapping.put("e/5", 55);
        KeyMapping.put("en/5", 55);
        KeyMapping.put("d##/5", 55);
        KeyMapping.put("fb/5", 55);
        KeyMapping.put("f/5", 57);
        KeyMapping.put("fn/5", 57);
        KeyMapping.put("e#/5", 57);
        KeyMapping.put("gbb/5", 57);
        KeyMapping.put("d###/5", 57);
        KeyMapping.put("f#/5", 59);
        KeyMapping.put("e##/5", 59);
        KeyMapping.put("gb/5", 59);
        KeyMapping.put("g/5", 58);
        KeyMapping.put("gn/5", 58);
        KeyMapping.put("f##/5", 58);
        KeyMapping.put("abb/5", 58);
        KeyMapping.put("e###/5", 58);
        KeyMapping.put("g#/5", 61);
        KeyMapping.put("ab/5", 61);
        KeyMapping.put("f###/5", 61);
        KeyMapping.put("a/5", 60);
        KeyMapping.put("an/5", 60);
        KeyMapping.put("g##/5", 60);
        KeyMapping.put("bbb/5", 60);
        KeyMapping.put("a#/5", 63);
        KeyMapping.put("bb/5", 63);
        KeyMapping.put("cbb/6", 63);
        KeyMapping.put("g###/5", 63);
        KeyMapping.put("b/5", 62);
        KeyMapping.put("bn/5", 62);
        KeyMapping.put("a##/5", 62);
        KeyMapping.put("cb/6", 62);

        KeyMapping.put("c/6", 52 + 12);
        KeyMapping.put("cn/6", 52 + 12);
        KeyMapping.put("b#/5", 52 + 12);
        KeyMapping.put("dbb/6", 52 + 12);
        KeyMapping.put("a###/5", 52 + 12);
        KeyMapping.put("c#/6", 54 + 12);
        KeyMapping.put("b##/5", 54 + 12);
        KeyMapping.put("db/6", 54 + 12);
        KeyMapping.put("d/6", 53 + 12);
        KeyMapping.put("dn/6", 53 + 12);
        KeyMapping.put("c##/6", 53 + 12);
        KeyMapping.put("ebb/6", 53 + 12);
        KeyMapping.put("b###/5", 53 + 12);
        KeyMapping.put("d#/6", 56 + 12);
        KeyMapping.put("eb/6", 56 + 12);
        KeyMapping.put("fbb/6", 56 + 12);
        KeyMapping.put("c###/6", 56 + 12);
        KeyMapping.put("e/6", 55 + 12);
        KeyMapping.put("en/6", 55 + 12);
        KeyMapping.put("d##/6", 55 + 12);
        KeyMapping.put("fb/6", 55 + 12);
        KeyMapping.put("f/6", 57 + 12);
        KeyMapping.put("fn/6", 57 + 12);
        KeyMapping.put("e#/6", 57 + 12);
        KeyMapping.put("gbb/6", 57 + 12);
        KeyMapping.put("d###/6", 57 + 12);
        KeyMapping.put("f#/6", 59 + 12);
        KeyMapping.put("e##/6", 59 + 12);
        KeyMapping.put("gb/6", 59 + 12);
        KeyMapping.put("g/6", 58 + 12);
        KeyMapping.put("gn/6", 58 + 12);
        KeyMapping.put("f##/6", 58 + 12);
        KeyMapping.put("abb/6", 58 + 12);
        KeyMapping.put("e###/6", 58 + 12);
        KeyMapping.put("g#/6", 61 + 12);
        KeyMapping.put("ab/6", 61 + 12);
        KeyMapping.put("f###/6", 61 + 12);
        KeyMapping.put("a/6", 60 + 12);
        KeyMapping.put("an/6", 60 + 12);
        KeyMapping.put("g##/6", 60 + 12);
        KeyMapping.put("bbb/6", 60 + 12);
        KeyMapping.put("a#/6", 63 + 12);
        KeyMapping.put("bb/6", 63 + 12);
        KeyMapping.put("cbb/7", 63 + 12);
        KeyMapping.put("g###/6", 63 + 12);
        KeyMapping.put("b/6", 62 + 12);
        KeyMapping.put("bn/6", 62 + 12);
        KeyMapping.put("a##/6", 62 + 12);
        KeyMapping.put("cb/7", 62 + 12);

        KeyMapping.put("c/7", 52 + 12 + 12);
        KeyMapping.put("cn/7", 52 + 12 + 12);
        KeyMapping.put("b#/6", 52 + 12 + 12);
        KeyMapping.put("dbb/7", 52 + 12 + 12);
        KeyMapping.put("a###/6", 52 + 12 + 12);
        KeyMapping.put("c#/7", 54 + 12 + 12);
        KeyMapping.put("b##/6", 54 + 12 + 12);
        KeyMapping.put("db/7", 54 + 12 + 12);
        KeyMapping.put("d/7", 53 + 12 + 12);
        KeyMapping.put("dn/7", 53 + 12 + 12);
        KeyMapping.put("c##/7", 53 + 12 + 12);
        KeyMapping.put("ebb/7", 53 + 12 + 12);
        KeyMapping.put("b###/6", 53 + 12 + 12);
        KeyMapping.put("d#/7", 56 + 12 + 12);
        KeyMapping.put("eb/7", 56 + 12 + 12);
        KeyMapping.put("fbb/7", 56 + 12 + 12);
        KeyMapping.put("c###/7", 56 + 12 + 12);
        KeyMapping.put("e/7", 55 + 12 + 12);
        KeyMapping.put("en/7", 55 + 12 + 12);
        KeyMapping.put("d##/7", 55 + 12 + 12);
        KeyMapping.put("fb/7", 55 + 12 + 12);
        KeyMapping.put("f/7", 57 + 12 + 12);
        KeyMapping.put("fn/7", 57 + 12 + 12);
        KeyMapping.put("e#/7", 57 + 12 + 12);
        KeyMapping.put("gbb/7", 57 + 12 + 12);
        KeyMapping.put("d###/7", 57 + 12 + 12);
        KeyMapping.put("f#/7", 59 + 12 + 12);
        KeyMapping.put("e##/7", 59 + 12 + 12);
        KeyMapping.put("gb/7", 59 + 12 + 12);
        KeyMapping.put("g/7", 58 + 12 + 12);
        KeyMapping.put("gn/7", 58 + 12 + 12);
        KeyMapping.put("f##/7", 58 + 12 + 12);
        KeyMapping.put("abb/7", 58 + 12 + 12);
        KeyMapping.put("e###/7", 58 + 12 + 12);
        KeyMapping.put("g#/7", 61 + 12 + 12);
        KeyMapping.put("ab/7", 61 + 12 + 12);
        KeyMapping.put("f###/7", 61 + 12 + 12);
        KeyMapping.put("a/7", 60 + 12 + 12);
        KeyMapping.put("an/7", 60 + 12 + 12);
        KeyMapping.put("g##/7", 60 + 12 + 12);
        KeyMapping.put("bbb/7", 60 + 12 + 12);
        KeyMapping.put("a#/7", 63 + 12 + 12);
        KeyMapping.put("bb/7", 63 + 12 + 12);
        KeyMapping.put("cbb/8", 63 + 12 + 12);
        KeyMapping.put("g###/7", 63 + 12 + 12);
        KeyMapping.put("b/7", 62 + 12 + 12);
        KeyMapping.put("bn/7", 62 + 12 + 12);
        KeyMapping.put("a##/7", 62 + 12 + 12);
        KeyMapping.put("cb/8", 62 + 12 + 12);

        KeyMapping.put("c/8", 88);
        KeyMapping.put("cn/8", 88);
        KeyMapping.put("b#/7", 88);
        KeyMapping.put("dbb/8", 88);
        KeyMapping.put("a###/7", 88);
    }

    @SuppressLint("StaticFieldLeak")
    static FM_SoundPool mInstance;

    static FM_SoundPool getInstance(){
        return mInstance;
    }

    FM_SoundPool(Context context) {
        this.context = context;
        AssetManager assetManager = context.getAssets();
        threadMap = new SparseArray<>();
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        sndPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .setMaxStreams(20)
                .build();

        FM_ScorePlayer.getInstance(context).SoundsLoaded = 0;
        AtomicInteger loaded_count = new AtomicInteger();
        sndPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if (status == 0) {
                loaded_count.getAndIncrement();
                float f = loaded_count.floatValue();
                FM_ScorePlayer.getInstance(context).SoundsLoaded = (int) ((f / 88.0f) * 100);
                if (FM_ScorePlayer.getInstance(context).SoundsLoaded == 100) FM_ScorePlayer.getInstance(context).SoundsLoadedCDL.countDown();
            }
        });
        AssetFileDescriptor fileDescriptor;
        for (int i = 1; i <= 88; i++) {
            try {
                fileDescriptor = assetManager.openFd(assetFiles.get(i));
                soundMap.put(i, sndPool.load(fileDescriptor, 1));
            } catch (IOException ignored) {
            }
        }
        mInstance = this;
    }

    void playKey(int key) {
        playKey(key, false);
    }

    void playKey(int key, boolean NextPause) {
        if (key == -1) return;
        if (isKeyNotPlaying(key)) {
            PlayThread thread = new PlayThread(key, NextPause);
            thread.start();
            threadMap.put(key, thread);
        }
    }

    void stopKey(int key) {
        if (key == -1) return;
        try {
            PlayThread thread = threadMap.get(key);
            if (thread != null) {
                thread.Stop();
                threadMap.remove(key);
            }
        } catch (Exception ignored) {
        }
    }

    boolean isKeyNotPlaying(int key) {
        return threadMap.get(key) == null;
    }

    static private String TranslateKey(String Key) {
        Key = Key.replace("do", "c");
        Key = Key.replace("re", "d");
        Key = Key.replace("mi", "e");
        Key = Key.replace("fa", "f");
        Key = Key.replace("sol", "g");
        Key = Key.replace("la", "a");
        Key = Key.replace("si", "b");
        return Key;
    }

    static Integer GetIndex(String Key) {
        Key = TranslateKey(Key.toLowerCase());
        if (Key.startsWith("r"))
            return -1;
        Key = Key.replace("n", "");
        Integer r = KeyMapping.get(Key);
        if (r != null)
            return r;
        else
            return 1;
    }

    static int GetDurationInMs(@FM_DurationValue int duration, int tupletSize, int tempo, int time_signature_d) {
        if (tempo == 0) tempo = 60;
        if (time_signature_d == 0) time_signature_d = TIME_SIGNATURE_D;
        if (time_signature_d == 0) time_signature_d = 4;

        int time_signature_n = TIME_SIGNATURE_N;
        if (time_signature_n == 0) time_signature_d = 4;
        int multiply = 1;       //simple meter (2, 3 and 4)
        if (time_signature_n == 6 || time_signature_n == 9 || time_signature_n == 12) multiply = 3; //compound meter

        float d = (60.0f / (tempo * multiply)) * (4.0f / time_signature_d) * 4000.0f;
        //if (time_signature_d == 8) d = d * (4f/3f);

        if (duration > 50) {
            d = d * 1.5f;
            duration = duration - 50;
        }
        if (tupletSize == 3) d = d * 2f / 3.0f;
        if (tupletSize == 5) d = d * 4f / 5.0f;
        if (tupletSize == 6) d = d * 4f / 6.0f;
        if (tupletSize == 2) d = d * 3f / 4.0f;
        if (tupletSize == 4) d = d * 3f / 4.0f;

        if (duration == FM_DurationValue.NOTE_WHOLE) return (int) (d * 1);
        if (duration == FM_DurationValue.NOTE_HALF) return (int) (d * 1 / 2f);
        if (duration == FM_DurationValue.NOTE_QUARTER) return (int) (d * 1 / 4f);
        if (duration == FM_DurationValue.NOTE_EIGHTH) return (int) (d * 1 / 8f);
        if (duration == FM_DurationValue.NOTE_SIXTEENTH) return (int) (d * 1 / 16f);
        if (duration == FM_DurationValue.NOTE_THIRTY_SECOND) return (int) (d * 1 / 32f);
        return 0;
    }

    static float CustomDelay(long duration, boolean coolDown) {
        long current = System.nanoTime();
        long start = current;
        long end = current + duration * 1000000;
        while (current < end) {
            current = System.nanoTime();
            if ((!coolDown) && (!playing)) return duration;
        }
        return (current - start) / 1000000f;
    }

    void StopAllSound() {
        playing = false;
        for (int i = 0; i < threadMap.size(); i++) {
            PlayThread thread = threadMap.get(i);
            if (thread != null) {
                thread.Stop();
                threadMap.remove(i);
            }
        }
    }

    class PlayThread extends Thread {
        private final int key;
        private final CountDownLatch stop;
        private final boolean NextPause;

        PlayThread(int key, boolean NextPause) {
            this.NextPause = NextPause;
            this.key = key;
            this.stop = new CountDownLatch(1);
        }

        void Stop() {
            stop.countDown();
        }

        @Override
        public void run() {
            int stream = sndPool.play(soundMap.get(key), 1, 1, 100, 0, 1);
            try {
                stop.await();
            } catch (InterruptedException ignored) {}
            //start fallback sequence
            float d = 0f;
            float fall = FALLBACK_DURATION;
            if (NextPause)
                fall = fall / 5f;
            while (d < fall) {
                d += CustomDelay(2, true);
                float p = 1f - d / fall;
                sndPool.setVolume(stream, p, p);
            }
            sndPool.setVolume(stream, 0, 0);
            sndPool.stop(stream);
        }
    }
}
