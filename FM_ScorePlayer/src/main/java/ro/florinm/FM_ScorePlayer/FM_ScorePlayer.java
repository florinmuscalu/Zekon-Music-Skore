package ro.florinm.FM_ScorePlayer;

import android.content.Context;
import android.os.SystemClock;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import ro.florinm.FM_Score.FM_Score;

public class FM_ScorePlayer {
    private static FM_ScorePlayer mInstance = null;
    private FM_Audio_Song song;
    private FM_SoundPool soundPlayer;
    volatile int SoundsLoaded;
    private int temp_timesig_n;
    private int temp_timesig_d;
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
                    mInstance.temp_timesig_n = 4;
                    mInstance.temp_timesig_d = 4;
                    mInstance.soundPlayer = null;
                    new Thread(() -> {
                        mInstance.soundPlayer = new FM_SoundPool(context);
                        mInstance.setTimeSignature(mInstance.temp_timesig_n, mInstance.temp_timesig_d);
                    }).start();
                }
            }
        }
        return mInstance;
    }

    public void ProgressSetScore(FM_Score score){
        this.score = score;
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
        temp_timesig_n = n;
        temp_timesig_d = d;
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

    /**
     * @param obj The song in Json format. Check the documentation for how the Json should look like.
     * @param harmonic Set it to true if obj contains a harmonic melody. If it's melodic, set it to false
     */
    public void LoadFromJson(JSONObject obj, boolean harmonic, int tempo) {
        song = null;
        score = null;
        new Thread(() -> {
            try {
                while (SoundsLoaded != 100) SystemClock.sleep(25);
                soundPlayer.ClearAudioTracks();
                String timesignature = obj.optString("timesignature", "4/4");
                setTimeSignature(FM_Helper.getTimeSignature_n(timesignature), FM_Helper.getTimeSignature_d(timesignature));
                if (harmonic)
                    song = FM_Helper.generateHarmonicSong(obj.optString("keysignature", "DO"), obj.getJSONArray("keys"), tempo);
                else
                    song = FM_Helper.generatMelodicSong(obj.optString("keysignature", "DO"), obj.getJSONArray("keys"), tempo);
                Prepare(tempo);
                if (score != null) score.ProgressReset();
            } catch (Exception ignored) {}
        }).start();
    }

    public void Play(int tempo) {
        if (song != null) Play(1, song.measures.size(), 0, false, tempo);
    }

    public void Prepare(int tempo) {
        if (song != null) Play(1, song.measures.size(), 0, true, tempo);
    }

    public void Play(int measure_start, int measure_end, int tempo) {
        if (song != null) Play(measure_start, measure_end, 0, false, tempo);
    }

    public void Prepare(int measure_start, int measure_end, int tempo) {
        if (song != null) Play(measure_start, measure_end, 0, true, tempo);
    }

    public void Play(int measure_start, int measure_end, int notes, int tempo) {
        if (song != null) Play(measure_start, measure_end, notes, false, tempo);
    }

    public void Prepare(int measure_start, int measure_end, int notes, int tempo) {
        if (song != null) Play(measure_start, measure_end, notes, true, tempo);
    }

    private FM_Audio_Note LoadNote(FM_Audio_Note note, int tempo) {
        FM_Audio_Note ret;
        String[] n = note.note.split(",");
        n = FM_Helper.computeNote(song.keysignature, n);
        String[] d = note.duration.split(",");
        ret = note;
        List<Integer> tracks = new ArrayList<>();
        ret.audioInt = 0;
        ret.pauseDuration = FM_SoundPool.GetDurationFromStr(d[0], tempo, 0);
        ret.playDuration = FM_SoundPool.GetDurationFromStr(d[0], tempo, 0);
        for (String s : d) {
            if (ret.pauseDuration > FM_SoundPool.GetDurationFromStr(s, tempo, 0)) ret.pauseDuration = FM_SoundPool.GetDurationFromStr(s, tempo,0);
            if (ret.playDuration  < FM_SoundPool.GetDurationFromStr(s, tempo, 0)) ret.playDuration =  FM_SoundPool.GetDurationFromStr(s, tempo,0);
        }
        
        if (n.length == 1 ){
                ret.audioInt = soundPlayer.GetIndex(n[0].trim());
                ret.audioT = null;
            }
        else {
            for (String s : n) tracks.add(soundPlayer.GetIndex(s.trim()));
            ret.audioT = null;
            ret.audioT = soundPlayer.CreateTrack(tracks, d, tempo);
        }
        return ret;
    }

    private void Play(int measure_start, int measure_end, int notes, Boolean prepare, int tempo) {
        if (FM_SoundPool.playing) return;
        if (SoundsLoaded != 100) return;
        if (song == null) return;
        if (prepare) {
            song.prepared = new CountDownLatch(1);
            PlayMelodic(song, measure_start, measure_end, notes, true, tempo);
        } else {
            FM_SoundPool.playing = true;
            new Thread(() -> {
                try {
                    song.prepared.await();
                } catch (Exception ignored) { }
                SystemClock.sleep(200);
                PlayMelodic(song, measure_start, measure_end, notes, false, tempo);
            }).start();
        }
    }

    private void PlayMelodic(final FM_Audio_Song song, int measure_start, int measure_end, int notes, Boolean prepare, int tempo) {
        if (measure_end > song.measures.size()) measure_end = song.measures.size();
        if (measure_end == song.measures.size()) notes = 0;
        List<FM_Audio_Note> ListNotes = new ArrayList<>();
        for (int i = measure_start - 1; i < measure_end; i++) {
            FM_Helper.StartMeasure();
            for (int j = 0; j < song.measures.get(i).notes.size(); j++)
                ListNotes.add(LoadNote(song.measures.get(i).notes.get(j), tempo));
        }
        if (notes != 0) {
            FM_Helper.StartMeasure();
            int cnt = notes;
            if (cnt > song.measures.get(measure_end).notes.size())
                cnt = song.measures.get(measure_end).notes.size();
            for (int j = 0; j < cnt; j++)
                ListNotes.add(LoadNote(song.measures.get(measure_end).notes.get(j), tempo));
        }
        for (int i = 1; i < ListNotes.size(); i++) if (ListNotes.get(i).audioInt == -1) ListNotes.get(i - 1).NextPause = true;
        song.prepared.countDown();

        if (!prepare)
            new Thread(() -> {
                boolean in_legato = false;
                FM_SoundPool.playing = true;
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
            }).start();
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

    /**
     * @param keys List of keys to be played. TO DO: add format
     * @param simultaneously - if true, play all the keys simultaneously, like a chord
     * @param prepare - play or prepare?
     * @param duration - the duration for each key. If duration is -1, use the TEMPO_DURATION as duration
     */
    public void PlayKeys(final String keys, final Boolean simultaneously, final Boolean prepare, final String duration, final int tempo) {
        if (FM_SoundPool.playing && !prepare) return;
        if (!prepare) FM_SoundPool.playing = true;
        new Thread(() -> {
            int d = (int) FM_SoundPool.GetDurationFromStr(duration, tempo, 4);
            String[] k = keys.replace("[", "").replace("]", "").replace("\"", "").replace("\\", "").toLowerCase().split(",");
            final int[] Tracks = new int[k.length];
            for (int i = 0; i < k.length; i++) Tracks[i] = soundPlayer.GetIndex(k[i].trim());
            if (!simultaneously) {
                if (!prepare) {
                    for (int i : Tracks) {
                        if (FM_SoundPool.playing) {
                            playKey(i);
                            FM_SoundPool.CustomDelay(d, false);
                            stopKey(i);
                        }
                    }
                    FM_SoundPool.playing = false;
                }
            } else {
                FM_AudioTrack t = soundPlayer.CreateTrack(Tracks, d);
                if (!prepare && t != null) {
                    FM_SoundPool.playing = true;
                    t.Play(d, false);
                    FM_SoundPool.CustomDelay(d, false);
                    FM_SoundPool.playing = false;
                }
            }
        }).start();
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
