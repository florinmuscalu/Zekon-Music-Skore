package tech.zekon.FM_Score;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class FM_ScorePlayer {
    private static FM_ScorePlayer mInstance = null;
    private Context context;
    private FM_Audio_Song song;
    private FM_SoundPool soundPlayer;
    /** Current live instrument: {@code -1} = piano (recorded samples); {@code 0..127} = a GM program via the SoundFont synth. */
    private volatile int instrumentProgram = -1;
    private FM_Synth synth;
    volatile int SoundsLoaded;
    CountDownLatch SoundsLoadedCDL = new CountDownLatch(1);
    public CountDownLatch SongLoadedCDL = new CountDownLatch(1);
    private int tempTimeSig_n;
    private int tempTimeSig_d;
    private FM_ScoreBase score;
    private boolean showProgress = false;

    private FM_ScorePlayer() {}
    /**
     * @param context The Application's context.
     */
    public static FM_ScorePlayer getInstance(Context context) {
        if (mInstance == null) {
            synchronized (FM_ScorePlayer.class) {
                if (mInstance == null) {
                    mInstance = new FM_ScorePlayer();
                    mInstance.context = context.getApplicationContext();
                    mInstance.instrumentProgram = -1;
                    mInstance.score = null;
                    mInstance.showProgress = false;
                    mInstance.SoundsLoaded = 0;
                    mInstance.tempTimeSig_n = 4;
                    mInstance.tempTimeSig_d = 4;
                    mInstance.soundPlayer = null;
                    new Thread(() -> {
                        mInstance.soundPlayer = new FM_SoundPool(context);
                        mInstance.setTimeSignature(mInstance.tempTimeSig_n, mInstance.tempTimeSig_d);
                    }).start();
                }
            }
        }
        return mInstance;
    }

    /**
     * @return return the percent of loading the assets. 100 for all done.
     */
    public int AssetsLoaded() {
        return SoundsLoaded;
    }

    /**
     * @return return true something is playing
     */
    public boolean isPlaying() {
        return FM_SoundPool.playing;
    }

    public void setTimeSignature(int n, int d) {
        tempTimeSig_n = n;
        tempTimeSig_d = d;
        if (soundPlayer != null) {
            FM_SoundPool.TIME_SIGNATURE_N = n;
            FM_SoundPool.TIME_SIGNATURE_D = d;
        }
    }

    /**
     * @return get the quarter duration given the tempo
     */

    public long getQuarterDuration(int tempo){
        return FM_SoundPool.GetDurationInMs(FM_DurationValue.NOTE_QUARTER,0 ,tempo, 0);
    }

    public void LoadFromScore(FM_Score obj, int tempo) {
        LoadFromScore(obj.ScoreBase, tempo);
    }

    public void LoadFromScore(FM_ScoreBase obj, int tempo) {
        song = null;
        this.score = obj;
        if (FM_SoundPool.playing) StopPlaying();
        SongLoadedCDL = new CountDownLatch(1);
        Thread loadingThread = new Thread(() -> {
            try {
                SoundsLoadedCDL.await();
                soundPlayer.ClearAudioTracks();
                setTimeSignature(obj.getTimeSignature_n(), obj.getTimeSignature_d());
                song = FM_Helper.generateSongFromScore(obj, tempo);
                if (showProgress && obj.score != null) obj.score.ProgressReset();
                SongLoadedCDL.countDown();
            } catch (Exception e) {
                FM_Log.e("FM_ScorePlayer", "Failed to load song from score", e);
            }
        });
        loadingThread.start();
    }

    public void Play() {
        if (FM_SoundPool.playing) return;
        FM_SoundPool.playing = true;
        new Thread(() -> {
            try {
                SongLoadedCDL.await();
            } catch (Exception ignored) {
            }
            //SystemClock.sleep(200);
            Play(song, 1, song.measures.size(), 0);
        }).start();
    }

    public void Play(int measure_start, int measure_end) {
        Play(measure_start, measure_end, 0);
    }

    public void Play(int measure_start, int measure_end, int notes) {
        if (FM_SoundPool.playing) return;
        FM_SoundPool.playing = true;
        new Thread(() -> {
            try {
                SongLoadedCDL.await();
            } catch (Exception ignored) {
            }
            //SystemClock.sleep(200);
            Play(song, measure_start, measure_end, notes);
        }).start();
    }

    private void Play(final FM_Audio_Song song, int measure_start, int measure_end, int notes) {
        if (measure_end > song.measures.size()) measure_end = song.measures.size();
        if (measure_end == song.measures.size()) notes = 0;
        List<FM_Audio_Note> ListNotes = new ArrayList<>();
        for (int i = measure_start - 1; i < measure_end; i++) {
            ListNotes.addAll(song.measures.get(i).notes);
        }
        if (notes != 0) {
            int cnt = notes;
            if (cnt > song.measures.get(measure_end).notes.size())
                cnt = song.measures.get(measure_end).notes.size();
            for (int j = 0; j < cnt; j++)
                ListNotes.add(song.measures.get(measure_end).notes.get(j));
        }

        //play silent key
        soundPlayer.playSilentKey(48);
        FM_SoundPool.CustomDelay(50, false);
        soundPlayer.stopKey(48);

        //start playing
        boolean in_legato = false;
        if (showProgress && score.score != null) score.score.ProgressSetStart(measure_start);
        long lastDuration = 0;
        for (int i = 0; i < ListNotes.size(); i++) {
            FM_Audio_Note n = ListNotes.get(i);
            boolean isEnd = false;
            if (i == ListNotes.size() - 1) isEnd = true;
            if (!FM_SoundPool.playing) break;
            if (showProgress && score.score != null) score.score.ProgressAdvance();

            if (in_legato || (!isEnd && n.legato)) {
                if (n.audioIntInLegato > 0) soundPlayer.playKey(n.audioIntInLegato, n.NextPause);
                if (n.audioIntInLegato == 0)
                    n.audioTrackInLegato.Play(n.playDurationInTie, n.NextPause);
                lastDuration = n.playDurationInTie - n.pauseDuration;
            } else {
                if (n.audioIntOutsideLegato > 0)
                    soundPlayer.playKey(n.audioIntOutsideLegato, n.NextPause);
                if (n.audioIntOutsideLegato == 0)
                    n.audioTrackOutsideLegato.Play(n.playDurationOutsideTie, n.NextPause);
                lastDuration = n.playDurationOutsideTie - n.pauseDuration;
            }
            FM_SoundPool.CustomDelay(n.pauseDuration, false);
            if (in_legato || (!isEnd && n.legato)) {
                if (n.audioIntInLegato > 0) soundPlayer.stopKey(n.audioIntInLegato);
            } else {
                if (n.audioIntOutsideLegato > 0) soundPlayer.stopKey(n.audioIntOutsideLegato);
            }
            in_legato = n.legato;
        }
        FM_SoundPool.CustomDelay(lastDuration, false);
        soundPlayer.StopAllSound();
        if (showProgress && score.score != null) score.score.ProgressReset();
        FM_SoundPool.playing = false;
    }

    public void StopPlaying() {
        soundPlayer.StopAllSound();
        if (synth != null) synth.allNotesOff();
    }


    //Below: functions for a Piano Keyboard

    /**
     * Check to make sure that a certain key is not currently playing.
     * @param key The index of the key you want to check. It starts from 1 (A/0) to 88 (c/8).
     * @return True if the key is not currently playing
     */
    public boolean isKeyNotPlaying(int key) {
        try {
            if (key < 1 || key > 88) return false;
            if (instrumentProgram >= 0 && synth != null) return synth.isKeyNotPlaying(key);
            return soundPlayer.isKeyNotPlaying(key);
        } catch (Exception e) {
            FM_Log.w("FM_ScorePlayer", "isKeyNotPlaying failed for key " + key, e);
            return false;
        }
    }

    /**
     * Start playing the key you specify.
     * @param key The index of the key you want to check. It starts from 1 (A/0) to 88 (c/8).
     */
    public void playKey(int key) {
        playKey(key, 1f);
    }

    /**
     * Start playing {@code key} at {@code velocity} (0..1, e.g. from touch position).
     * Routes to the piano samples or the SoundFont synth depending on the current instrument.
     */
    public void playKey(int key, float velocity) {
        if (key < 1 || key > 88) return;
        if (instrumentProgram >= 0 && synth != null) synth.playKey(key, velocity);
        else soundPlayer.playKey(key, velocity);
    }
    /**
     * Start playing the key you specify.
     * @param key The index of the key you want to check. It starts from 1 (A/0) to 88 (c/8).
     */
    public void stopKey(int key) {
        if (key < 1 || key > 88) return;
        try {
            if (instrumentProgram >= 0 && synth != null) synth.stopKey(key);
            else soundPlayer.stopKey(key);
        } catch (Exception e) {
            FM_Log.w("FM_ScorePlayer", "stopKey failed for key " + key, e);
        }
    }

    // Semitone offsets, relative to each octave's C, in the order the keyboard assigns key indices
    // within an octave: C, D, C#, E, D#, F, G, F#, A, G#, B, A# (white-then-black draw order).
    private static final int[] KEY_OCTAVE_OFFSETS = {0, 2, 1, 4, 3, 5, 7, 6, 9, 8, 11, 10};

    /**
     * Maps a keyboard key index (1..88, in the app's white/black draw order — NOT chromatic) to its
     * MIDI note number (A0 = 21). The sample engine indexes WAVs in this same order; the SoundFont
     * synth and MIDI export must use this to avoid swapping neighbouring notes.
     */
    public static int keyToMidi(int key) {
        if (key == 1) return 21;   // A0
        if (key == 2) return 23;   // B0
        if (key == 3) return 22;   // A#0
        int n = key - 4;           // 0-based from C1 (MIDI 24)
        return 24 + (n / 12) * 12 + KEY_OCTAVE_OFFSETS[n % 12];
    }

    // ---- Instruments ----

    /**
     * Selects the live keyboard instrument. {@code -1} restores the piano (recorded samples);
     * a GM program {@code 0..127} switches to that SoundFont instrument. The SoundFont loads
     * lazily on first non-piano use.
     */
    public void setInstrument(int gmProgram) {
        if (gmProgram < 0) {
            instrumentProgram = -1;
            if (synth != null) synth.stop();
            return;
        }
        if (synth == null) synth = FM_Synth.getInstance(context);
        instrumentProgram = gmProgram;
        synth.start(gmProgram);
    }

    /** Current live instrument: {@code -1} = piano, else the GM program. */
    public int getInstrumentProgram() {
        return instrumentProgram;
    }

    /**
     * Whether key {@code 1..88} can be played by the current instrument. The piano covers all keys;
     * a SoundFont instrument only covers the keys its preset has samples for (others should be
     * shown disabled). Returns true while the SoundFont is still loading (range not yet known).
     */
    public boolean isKeyAvailable(int key) {
        if (instrumentProgram < 0) return true;
        if (synth == null || !synth.isReady()) return true;
        return synth.isKeyPlayable(key);
    }

    /** Pauses live SoundFont streaming (call from the foreground Activity's {@code onPause}). */
    public void pauseLiveInstrument() {
        if (synth != null && instrumentProgram >= 0) synth.stop();
    }

    /** Resumes live SoundFont streaming for the selected instrument (call from {@code onResume}). */
    public void resumeLiveInstrument() {
        if (synth != null && instrumentProgram >= 0) synth.start(instrumentProgram);
    }

    /** True once the SoundFont has finished loading (always false until a non-piano instrument is used). */
    public boolean isSoundFontReady() {
        return synth != null && synth.isReady();
    }

    /**
     * Begins loading the SoundFont now (async), without selecting an instrument or starting audio.
     * Call this at app startup when the saved instrument is a SoundFont one, so it decodes in
     * parallel with the piano samples instead of only when the keyboard appears.
     */
    public void preloadSoundFont() {
        if (synth == null) synth = FM_Synth.getInstance(context);
        synth.ensureLoaded();
    }

    /** PCM sample rate of {@link #renderInstrumentPcm}. */
    public int getInstrumentSampleRate() {
        return FM_Synth.SAMPLE_RATE;
    }

    /**
     * Offline-renders a performance with a GM instrument to mono 16-bit PCM (for audio export).
     * Each parallel-array entry is one note: chromatic key 1..88, start and duration in ms.
     */
    public short[] renderInstrumentPcm(int gmProgram, int[] keys, long[] startMs, long[] durMs) {
        if (synth == null) synth = FM_Synth.getInstance(context);
        synth.ensureLoaded();
        return synth.renderPcm(gmProgram, keys, startMs, durMs);
    }

    public boolean isFirstMeasureComplete(){
        if (song.measures.size()<2) return true;
        int r_duration = 0;
        int f_duration = 0;
        for (int i = 0; i < song.measures.get(1).notes.size(); i++) r_duration = r_duration + (int) song.measures.get(1).notes.get(i).pauseDuration;
        for (int i = 0; i < song.measures.get(0).notes.size(); i++) f_duration = f_duration + (int) song.measures.get(0).notes.get(i).pauseDuration;
        return Math.abs(r_duration - f_duration) < 5;
    }

    public FM_Audio_Song getSongObject() {
        return song;
    }

    public boolean isShowProgress() {
        return showProgress;
    }

    public void setShowProgress(boolean showProgress) {
        this.showProgress = showProgress;
    }

    public void CustomDelay(int durationMs) {
        FM_SoundPool.CustomDelay(durationMs, false);
    }

    public void StartPlaying() {
        FM_SoundPool.playing = true;
    }
}
