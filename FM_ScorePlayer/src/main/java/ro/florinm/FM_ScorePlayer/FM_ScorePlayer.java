package ro.florinm.FM_ScorePlayer;

import android.content.Context;
import android.media.SoundPool;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class FM_ScorePlayer {
    private FM_Audio_Song song;
    private final FM_SoundPool soundPlayer;
    private boolean playing_step, playing;

    /**
     * @param context The Application's context.
     */
    public FM_ScorePlayer(Context context) {
        super();
        soundPlayer = new FM_SoundPool(context);
        playing = false;
        playing_step = false;
        setTempo(60);
    }

    /**
     * @param tempo the tempo used for playing the song.
     */
    public void setTempo(int tempo) {
        soundPlayer.TEMPO = 60000.0 / tempo;
    }

    /**
     * @return get the tempo used for playing the song.
     */
    public int getTempo(){
        return (int) (60000.0 / soundPlayer.TEMPO);
    }

    /**
     * @param obj The song in Json format. Check the documentation for how the Json should look like.
     * @return 0 if loading is successful, -1 otherwise.
     */
    public int LoadFromJson(JSONObject obj) {
        try {
            song = FM_Helper.generateHarmonicSong(obj.optString("keysignature", "DO"), obj.getJSONArray("keys"));
        } catch (JSONException e) {
            return -1;
        }
        return 0;
    }

    public void Play() {
        Play(1, song.measures.size(), 0, false);
    }

    public void Prepare() {
        Play(1, song.measures.size(), 0, true);
    }

    private void Play(int measure_start, int measure_end) {
        Play(measure_start, measure_end, 0, false);
    }

    private void Prepare(int measure_start, int measure_end) {
        Play(measure_start, measure_end, 0, true);
    }

    private void Play(int measure_start, int measure_end, int notes) {
        Play(measure_start, measure_end, notes, false);
    }

    private void Prepare(int measure_start, int measure_end, int notes) {
        Play(measure_start, measure_end, notes, true);
    }

    private void Play(int measure_start, int measure_end, int notes, Boolean prepare) {
        if (prepare) {
            song.prepared = false;
            PlayHarmonic(song, measure_start, measure_end, notes, prepare);
        } else {
            new Thread(() -> {
                while (!song.prepared)
                    try {
                        sleep(10);
                    } catch (Exception ignored) { }

                try {
                    sleep(200);
                } catch (Exception ignored) { }
                playing = true;
                PlayHarmonic(song, measure_start, measure_end, notes, prepare);
            }).start();
        }
    }

    private FM_Audio_Note LoadNote(FM_Audio_Note note) {
        FM_Audio_Note ret;
        String[] n = note.note.split(",");
        n = FM_Helper.computeNote(song.keysignature, n);
        String[] d = note.duration.split(",");
        ret = note;
        List<Integer> tracks = new ArrayList<>();
        for (String s : n) tracks.add(soundPlayer.GetIndex(s.trim()));
        ret.audioT = null;
        ret.pauseDuration = d[0];
        ret.playDuration = d[0];
        for (String s : d) {
            if (soundPlayer.GetDurationFromStr(ret.pauseDuration) > soundPlayer.GetDurationFromStr(s))
                ret.pauseDuration = s;
            if (soundPlayer.GetDurationFromStr(ret.playDuration) < soundPlayer.GetDurationFromStr(s))
                ret.playDuration = s;
        }
        ret.audioT = soundPlayer.CreateTrack(tracks, d);
        return ret;
    }

    private void PlayHarmonic(final FM_Audio_Song song, int measure_start, int measure_end, int notes, Boolean prepare) {
        if (measure_end > song.measures.size()) measure_end = song.measures.size();
        if (measure_end == song.measures.size()) notes = 0;
        List<FM_Audio_Note> ListNotes = new ArrayList<>();
        for (int i = measure_start - 1; i < measure_end; i++) {
            FM_Helper.StartMeasure();
            for (int j = 0; j < song.measures.get(i).notes.size(); j++)
                ListNotes.add(LoadNote(song.measures.get(i).notes.get(j)));
        }
        if (notes != 0) {
            FM_Helper.StartMeasure();
            int cnt = notes;
            if (cnt > song.measures.get(measure_end).notes.size())
                cnt = song.measures.get(measure_end).notes.size();
            for (int j = 0; j < cnt; j++)
                ListNotes.add(LoadNote(song.measures.get(measure_end).notes.get(j)));
        }
        ///for (int i = 1; i < ListNotes.size(); i++) if (ListNotes.get(i).audioINT == -1) ListNotes.get(i - 1).NextPause = true;

        song.prepared = true;
        if (!prepare)
            new Thread(() -> {
                playing_step = true;
                for (FM_Audio_Note n : ListNotes) {
                    if (!playing) continue;
                    n.audioT.Play(soundPlayer.GetDurationFromStr(n.playDuration), n.NextPause);
                    FM_SoundPool.SleepHarmonic(soundPlayer.GetDurationFromStr(n.pauseDuration));
                }
                playing_step = false;
            }).start();
    }

    public void StopPlaying() {
        playing = false;
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
}
