package ro.florinm.FM_ScorePlayer;

import android.content.Context;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class FM_ScorePlayer {
    private FM_Audio_Song song;
    private FM_SoundPool soundPlayer;
    static int SoundsLoaded;
    private int temp_tempo;
    private int temp_timesig_n;
    private int temp_timesig_d;
    /**
     * @param context The Application's context.
     */
    public FM_ScorePlayer(Context context) {
        super();
        SoundsLoaded = 0;
        temp_tempo = 60;
        temp_timesig_n = 4;
        temp_timesig_d = 4;
        soundPlayer = null;
        FM_SoundPool.playing = false;
        new Thread(() -> {
            soundPlayer = new FM_SoundPool(context);
            setTempo(temp_tempo);
            setTimeSignature(temp_timesig_n, temp_timesig_d);
        }).start();
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

    /**
     * @param tempo the tempo used for playing the song.
     */
    public void setTempo(int tempo) {
        temp_tempo = tempo;
        if (soundPlayer != null) soundPlayer.TEMPO = tempo;
    }

    public void setTimeSignature(int n, int d) {
        temp_timesig_n = n;
        temp_timesig_d = d;
        if (soundPlayer != null) {
            soundPlayer.time_signature_n = n;
            soundPlayer.time_signature_d = d;
        }
    }

    /**
     * @return get the tempo used for playing the song.
     */
    public int getTempo(){
        return soundPlayer.TEMPO;
    }

    public long getTempo(boolean ms){
        float multiply = (60.0f * soundPlayer.time_signature_n) / (soundPlayer.TEMPO * soundPlayer.time_signature_d);
        return (long) (multiply * 1000f);
    }

    /**
     * @param obj The song in Json format. Check the documentation for how the Json should look like.
     * @param harmonic Set it to true if obj contains a harmonic melody. If it's melodic, set it to false
     */
    public void LoadFromJson(JSONObject obj, boolean harmonic) {
        song = null;
        new Thread(() -> {
            try {
                while (SoundsLoaded != 100) sleep(25);
                soundPlayer.ClearAudioTracks();
                String timesignature = obj.optString("timesignature", "4/4");
                setTimeSignature(FM_Helper.getTimeSignature_n(timesignature), FM_Helper.getTimeSignature_d(timesignature));
                if (harmonic)
                    song = FM_Helper.generateHarmonicSong(obj.optString("keysignature", "DO"), obj.getJSONArray("keys"));
                else
                    song = FM_Helper.generatMelodicSong(obj.optString("keysignature", "DO"), obj.getJSONArray("keys"));
                Prepare();
            } catch (Exception ignored) {}
        }).start();
    }

    public void Play() {
        if (song != null) Play(1, song.measures.size(), 0, false);
    }

    public void Prepare() {
        if (song != null) Play(1, song.measures.size(), 0, true);
    }

    public void Play(int measure_start, int measure_end) {
        if (song != null) Play(measure_start, measure_end, 0, false);
    }

    public void Prepare(int measure_start, int measure_end) {
        if (song != null) Play(measure_start, measure_end, 0, true);
    }

    public void Play(int measure_start, int measure_end, int notes) {
        if (song != null) Play(measure_start, measure_end, notes, false);
    }

    public void Prepare(int measure_start, int measure_end, int notes) {
        if (song != null) Play(measure_start, measure_end, notes, true);
    }

    private FM_Audio_Note LoadNote(FM_Audio_Note note) {
        FM_Audio_Note ret;
        String[] n = note.note.split(",");
        n = FM_Helper.computeNote(song.keysignature, n);
        String[] d = note.duration.split(",");
        ret = note;
        List<Integer> tracks = new ArrayList<>();
        ret.audioInt = 0;
        ret.pauseDuration = d[0];
        ret.playDuration = d[0];
        for (String s : d) {
            if (soundPlayer.GetDurationFromStr(ret.pauseDuration) > soundPlayer.GetDurationFromStr(s))
                ret.pauseDuration = s;
            if (soundPlayer.GetDurationFromStr(ret.playDuration) < soundPlayer.GetDurationFromStr(s))
                ret.playDuration = s;
        }
        
        if (n.length == 1 ){
                ret.audioInt = soundPlayer.GetIndex(n[0].trim());
                ret.audioT = null;
            }
        else {
            for (String s : n) tracks.add(soundPlayer.GetIndex(s.trim()));
            ret.audioT = null;
            ret.audioT = soundPlayer.CreateTrack(tracks, d);
        }
        return ret;
    }

    private void Play(int measure_start, int measure_end, int notes, Boolean prepare) {
        if (FM_SoundPool.playing) return;
        if (SoundsLoaded != 100) return;
        if (song == null) return;
        if (prepare) {
            song.prepared = false;
            if (song.harmonic) PlayHarmonic(song, measure_start, measure_end, notes, true);
                          else PlayMelodic(song, measure_start, measure_end, notes, true);
        } else {
            FM_SoundPool.playing = true;
            new Thread(() -> {
                while (!song.prepared)
                    try {
                        sleep(10);
                    } catch (Exception ignored) { }

                try {
                    sleep(200);
                } catch (Exception ignored) { }
                if (song.harmonic) PlayHarmonic(song, measure_start, measure_end, notes, false);
                              else PlayMelodic(song, measure_start, measure_end, notes, false);
            }).start();
        }
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
        song.prepared = true;
        if (!prepare)
            new Thread(() -> {
                FM_SoundPool.playing = true;
                for (FM_Audio_Note n : ListNotes) {
                    if (!FM_SoundPool.playing) continue;
                    n.audioT.Play(soundPlayer.GetDurationFromStr(n.playDuration), n.NextPause);
                    FM_SoundPool.SleepMelodic(soundPlayer.GetDurationFromStr(n.pauseDuration));
                }
                FM_SoundPool.playing = false;
            }).start();
    }

    private void PlayMelodic(final FM_Audio_Song song, int measure_start, int measure_end, int notes, Boolean prepare) {
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
        for (int i = 1; i < ListNotes.size(); i++) if (ListNotes.get(i).audioInt == -1) ListNotes.get(i - 1).NextPause = true;
        song.prepared = true;

        if (!prepare)
            new Thread(() -> {
                boolean in_legato = false;
                FM_SoundPool.playing = true;
                for (FM_Audio_Note n : ListNotes) {
                    if (!FM_SoundPool.playing) continue;
                    if (n.audioInt != 0) {
                        if (!(n.legato_end && in_legato))
                            soundPlayer.playKey(n.audioInt, n.NextPause);
                        if (n.legato_start) in_legato = true;
                        if (n.legato_end) in_legato = false;
                        FM_SoundPool.SleepMelodic(soundPlayer.GetDurationFromStr(n.playDuration));
                        if (!n.legato_start) soundPlayer.stopKey(n.audioInt);
                    } else {
                        n.audioT.Play(soundPlayer.GetDurationFromStr(n.playDuration), n.NextPause);
                        FM_SoundPool.SleepMelodic(soundPlayer.GetDurationFromStr(n.pauseDuration));
                    }
                }
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
    public void PlayKeys(final String keys, final Boolean simultaneously, final Boolean prepare, final long duration) {
        if (FM_SoundPool.playing && !prepare) return;
        if (!prepare) FM_SoundPool.playing = true;
        new Thread(() -> {
            int d = (int) duration;
            if (duration == -1) d = (int) soundPlayer.TEMPO;
            String[] k = keys.replace("[", "").replace("]", "").replace("\"", "").replace("\\", "").toLowerCase().split(",");
            final int[] Tracks = new int[k.length];
            for (int i = 0; i < k.length; i++) Tracks[i] = soundPlayer.GetIndex(k[i].trim());
            if (!simultaneously) {
                if (!prepare) {
                    for (int i : Tracks) {
                        if (FM_SoundPool.playing) {
                            playKey(i);
                            FM_SoundPool.SleepMelodic(d);
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
                    FM_SoundPool.SleepMelodic(d);
                    FM_SoundPool.playing = false;
                }
            }
        }).start();
    }

    public boolean isFirstMeasureComplete(){
        if (song.measures.size()<2) return true;
        int r_duration = 0;
        int f_duration = 0;
        for (int i = 0; i < song.measures.get(1).notes.size(); i++) r_duration = r_duration + soundPlayer.GetDurationFromStr(song.measures.get(1).notes.get(i).pauseDuration);
        for (int i = 0; i < song.measures.get(0).notes.size(); i++) f_duration = f_duration + soundPlayer.GetDurationFromStr(song.measures.get(0).notes.get(i).pauseDuration);
        return Math.abs(r_duration - f_duration) < 5;
    }

    public FM_Audio_Song getSongObject() {
        return song;
    }
}
