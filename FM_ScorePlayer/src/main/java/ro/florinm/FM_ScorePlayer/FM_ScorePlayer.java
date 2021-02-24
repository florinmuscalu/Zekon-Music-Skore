package ro.florinm.FM_ScorePlayer;

import android.content.Context;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import ro.florinm.FM_Score.FM_Score;

public class FM_ScorePlayer {
    private static FM_ScorePlayer mInstance = null;
    private FM_Audio_Song song;
    private FM_SoundPool soundPlayer;
    volatile int SoundsLoaded;
    CountDownLatch SoundsLoadedCDL = new CountDownLatch(1);
    CountDownLatch SongLoadedCDL = new CountDownLatch(1);
    private int tempTimeSig_n;
    private int tempTimeSig_d;
    private FM_Score score;


    private FM_ScorePlayer() {}
    /**
     * @param context The Application's context.
     */
    public static FM_ScorePlayer getInstance(Context context) {
        if (mInstance == null) {
            synchronized (FM_ScorePlayer.class) {
                if (mInstance == null) {
                    mInstance = new FM_ScorePlayer();
                    mInstance.score = null;
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
            FM_SoundPool.time_signature_n = n;
            FM_SoundPool.TIME_SIGNATURE_D = d;
        }
    }

    /**
     * @return get the tempo used for playing the song.
     */

    public long getTempoDuration(int tempo){
        float multiply = (60.0f / tempo) * (FM_SoundPool.TIME_SIGNATURE_D / 4.0f);
        //float multiply = (60.0f * FM_SoundPool.time_signature_n) / (FM_SoundPool.TEMPO * FM_SoundPool.time_signature_d);
        return (long) (multiply * 1000f);
    }

    public void LoadFromScore(FM_Score obj, int tempo) {
        song = null;
        score = null;
        this.score = obj;
        if (FM_SoundPool.playing) StopPlaying();
        SongLoadedCDL = new CountDownLatch(1);
        new Thread(() -> {
            try {
                SoundsLoadedCDL.await();
                soundPlayer.ClearAudioTracks();
                setTimeSignature(obj.getTimeSignature_n(), obj.getTimeSignature_d());
                song = FM_Helper.generateSongFromScore(obj, tempo);
                obj.ProgressReset();
                SongLoadedCDL.countDown();
            } catch (Exception ignored) {
            }
        }).start();
    }

    public void Play() {
        if (FM_SoundPool.playing) return;
        new Thread(() -> {
            try {
                FM_SoundPool.playing = true;
                SongLoadedCDL.await();
            } catch (Exception ignored) {
            }
            SystemClock.sleep(200);
            Play(song, 1, song.measures.size(), 0);
        }).start();
    }

    public void Play(int measure_start, int measure_end) {
        if (FM_SoundPool.playing) return;
        new Thread(() -> {
            try {
                FM_SoundPool.playing = true;
                SongLoadedCDL.await();
            } catch (Exception ignored) {
            }
            SystemClock.sleep(200);
            Play(song, measure_start, measure_end, 0);
        }).start();
    }

    private void Play(int measure_start, int measure_end, int notes) {
        if (FM_SoundPool.playing) return;
        new Thread(() -> {
            try {
                FM_SoundPool.playing = true;
                SongLoadedCDL.await();
            } catch (Exception ignored) {
            }
            SystemClock.sleep(200);
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

        boolean in_legato = false;
        if (score != null) score.ProgressReset();
        for (FM_Audio_Note n : ListNotes) {
            if (!FM_SoundPool.playing) continue;
            if (score != null) score.ProgressAdvance();
            if (n.audioInt != 0) {
                if (!(n.legato_end && in_legato))
                    soundPlayer.playKey(n.audioInt, n.NextPause);
                if (n.legato_start) in_legato = true;
                if (n.legato_end) in_legato = false;
                FM_SoundPool.CustomDelay(n.playDuration, false);
                if (!n.legato_start || !FM_SoundPool.playing) soundPlayer.stopKey(n.audioInt);
            } else {
                n.audioT.Play(n.playDuration, n.NextPause);
                FM_SoundPool.CustomDelay(n.pauseDuration, false);
            }
        }
        if (score != null) score.ProgressReset();
        FM_SoundPool.playing = false;
    }

    public void StopPlaying() {
        soundPlayer.StopAllSound();
    }


    //Below: functions for a Piano Keyboard

    /**
     * Check to make sure that a certain key is not currently playing.
     * @param key The index of the key you want to check. It starts from 1 (A/0) to 88 (c/8).
     * @return True if the key is not currently playing
     */
    public boolean isKeyNotPlaying(int key) {
        if (key<1 || key>88) return false;
        return soundPlayer.isKeyNotPlaying(key);
    }
    /**
     * Start playing the key you specify.
     * @param key The index of the key you want to check. It starts from 1 (A/0) to 88 (c/8).
     */
    public void playKey(int key) {
        if (key<1 || key>88) return;
        soundPlayer.playKey(key);
    }
    /**
     * Start playing the key you specify.
     * @param key The index of the key you want to check. It starts from 1 (A/0) to 88 (c/8).
     */
    public void stopKey(int key) {
        if (key<1 || key>88) return;
        soundPlayer.stopKey(key);
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
}
