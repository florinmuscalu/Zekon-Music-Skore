package ro.florinm.FM_Score;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FM_ScoreBase {
    @FM_TimeSignatureValue
    protected int TimeSignature_n;    //numerator
    @FM_TimeSignatureValue
    protected int TimeSignature_d;    //denominator
    @FM_KeySignatureValue
    protected int KeySignature;
    @FM_StaveCount
    protected int StaveCount;
    private int VoiceCount;

    FM_KeySignature FirstStaveKey, SecondStaveKey;

    @FM_ClefValue
    protected int FirstStaveClef, SecondStaveClef;
    protected final FM_Score score;
    protected final List<FM_BaseNote> StaveNotes = new ArrayList<>();
    public final List<FM_Tie> Ties = new ArrayList<>();
    protected final List<FM_Tuplet> Tuplets = new ArrayList<>();
    protected final List<FM_Beam> Beams = new ArrayList<>();

    public FM_ScoreBase(FM_Score score) {
        this.score = score;
        StaveCount = FM_StaveCount._1;
        setVoiceCount(1);
        setFirstStaveClef(FM_ClefValue.TREBLE);
        setSecondStaveClef(FM_ClefValue.BASS);
        setTimeSignature(FM_TimeSignatureValue.None, FM_TimeSignatureValue.None);
        setKeySignature(FM_KeySignatureValue.DO);
    }

    /**
     * @return Return the numerator
     */
    public int getTimeSignature_n() {
        return TimeSignature_n;
    }

    /**
     * @return Return the denominator
     */
    public int getTimeSignature_d() {
        return TimeSignature_d;
    }

    public void setTimeSignature(@FM_TimeSignatureValue int numerator, @FM_TimeSignatureValue int denominator) {
        TimeSignature_n = numerator;
        TimeSignature_d = denominator;
    }

    public int getStaveCount() {
        return StaveCount;
    }

    public int getVoiceCount() {
        return VoiceCount;
    }

    public void setVoiceCount(int voiceCount) {
        VoiceCount = voiceCount;
        //invalidate();
        //requestLayout();
    }

    public int getFirstStaveClef() {
        return FirstStaveClef;
    }

    public void setFirstStaveClef(@FM_ClefValue int firstStaveClef) {
        FirstStaveClef = firstStaveClef;
        setKeySignature(KeySignature);
    }

    public int getSecondStaveClef() {
        return SecondStaveClef;
    }

    public void setSecondStaveClef(@FM_ClefValue int secondStaveClef) {
        SecondStaveClef = secondStaveClef;
        setKeySignature(KeySignature);
    }

    public Integer getKeySignature() {
        return KeySignature;
    }

    public void setKeySignature(Integer keySignature) {
        KeySignature = keySignature;
        FirstStaveKey = new FM_KeySignature( FirstStaveClef, keySignature, this);
        SecondStaveKey = new FM_KeySignature(SecondStaveClef, keySignature, this);
        //invalidate();
        //requestLayout();
    }

    public void clearStaveNotes() {
        StaveNotes.clear();
        Tuplets.clear();
        Beams.clear();
        Ties.clear();
        StaveCount = FM_StaveCount._1;
        setVoiceCount(1);
        setFirstStaveClef(FM_ClefValue.TREBLE);
        setSecondStaveClef(FM_ClefValue.BASS);
        setTimeSignature(FM_TimeSignatureValue.None, FM_TimeSignatureValue.None);
        setKeySignature(FM_KeySignatureValue.DO);
    }


    public void addStaveNote(FM_BaseNote n) {
        addStaveNote(n, 0);
    }

    public void addStaveNote(FM_BaseNote n, int stave) {
        if (n instanceof FM_BarNote) stave = 0;
        if (stave == 1) StaveCount = FM_StaveCount._2;
        n.stave = stave;
        StaveNotes.add(n);
    }

    public void addChord(List<FM_BaseNote> n, List<Integer> stave) {
        if (n.get(0) instanceof FM_Clef) {
            n.get(0).stave = stave.get(0);
            StaveNotes.add(n.get(0));
        } else {
            FM_Chord C = new FM_Chord(this);
            for (int i = 0; i < n.size(); i++) {
                if (stave.get(i) == 1) StaveCount = FM_StaveCount._2;
                n.get(i).stave = stave.get(i);
                C.addNote(n.get(i));
            }
            C.Compute();
            StaveNotes.add(C);
        }
    }

    HashMap<String, List<FM_Note>> TieNotes = new HashMap<>();

    public void AddToTie(String tie, FM_Note n) {
        if (TieNotes.containsKey(tie)) {
            Objects.requireNonNull(TieNotes.get(tie)).add(n);
            EndTie(tie);
        } else {
            List <FM_Note> note_list = new ArrayList<>();
            note_list.add(n);
            TieNotes.put(tie, note_list);
        }
    }

    protected void EndTie(String tie) {
        List <FM_Note> note_list = TieNotes.get(tie);
        assert note_list != null;
        if (note_list.size() != 2) return;
        if (note_list.get(0).stave != note_list.get(1).stave || note_list.get(0).octave != note_list.get(1).octave || !note_list.get(0).note.equals(note_list.get(1).note))
            return;
        FM_Tie t;
        if (tie.toLowerCase().contains("a")) t = new FM_Tie(score, true);
        else if (tie.toLowerCase().contains("b")) t = new FM_Tie(score, false);
        else t = new FM_Tie(score);
        t.AddStart(note_list.get(0));
        t.AddEnd(note_list.get(1));
        note_list.get(0).isTieStart = true;
        note_list.get(1).isTieEnd = true;
        Ties.add(t);
        TieNotes.remove(tie);
    }

    private boolean inTuplet = false;
    private int TupletPosition = 0;
    private int TupletSize = 0;
    private int currentTuplet = 0;
    List<FM_BaseNote> TupletNotes;

    public void BeginTuplet(String s) {
        inTuplet = true;
        TupletNotes = new ArrayList<>();
        TupletPosition = 1;
        TupletSize = 0;
        if (s.toLowerCase().contains("a")) {
            TupletPosition = 0;
            try {
                String size = s.toLowerCase().substring(s.toLowerCase().indexOf("a") + 1);
                TupletSize = Integer.parseInt(size);
            } catch (Exception ignored) {}
        }
        if (s.toLowerCase().contains("b")) {
            TupletPosition = 1;
            try {
                String size = s.toLowerCase().substring(s.toLowerCase().indexOf("b") + 1);
                TupletSize = Integer.parseInt(size);
            } catch (Exception ignored) {}
        }
    }

    public void AddToTuplet(FM_BaseNote n) {
        if (inTuplet) TupletNotes.add(n);
    }

    public void EndTuplet() {
        inTuplet = false;
        for (int i = 0; i < TupletNotes.size(); i++)
            if (!(TupletNotes.get(i) instanceof FM_Note || TupletNotes.get(i) instanceof FM_Pause)) return;
        int stave = TupletNotes.get(0).stave;
        if (TupletSize == 0) {
            int minDuration = 6000;
            int maxDuration = 0;
            int allDuration = 0;
            TupletSize = TupletNotes.size();
            for (int i = 0; i < TupletSize; i++) {
                int d = (int) (FM_Const.getDurationMs(TupletNotes.get(i).duration) * 1000);
                allDuration += d;
                if (d < minDuration) minDuration = d;
                if (d > maxDuration) maxDuration = d;
            }
            if (allDuration % minDuration == 0) TupletSize = allDuration / minDuration;
            if (allDuration % maxDuration == 0) TupletSize = allDuration / maxDuration;
            //while (allDuration != cnt * minDuration) cnt++;
        }
        for (int i = 0; i < TupletNotes.size(); i++)
            if ((TupletNotes.get(i).stave != stave) /*|| (TupletNotes.get(i)).duration != duration*/) return;
        FM_Tuplet t = new FM_Tuplet(score, TupletSize, currentTuplet, TupletPosition);
        currentTuplet++;
        for (int i = 0; i < TupletNotes.size(); i++) {
            TupletNotes.get(i).tuplet = true;
            TupletNotes.get(i).tupletSize = TupletNotes.size();
            t.AddNote(TupletNotes.get(i));
        }
        Tuplets.add(t);
    }

    private boolean inBeam = false;
    private int currentBeam = 0;
    List<FM_BaseNote> BeamNotes;

    public void BeginBeam() {
        inBeam = true;
        BeamNotes = new ArrayList<>();
    }

    public void AddToBeam(FM_Note n) {
        if (inBeam) BeamNotes.add(n);
    }

    public void EndBeam() {
        inBeam = false;
        if (BeamNotes.size() < 2) return;
        for (int i = 0; i < BeamNotes.size(); i++)
            if (!(BeamNotes.get(i) instanceof FM_Note)) return;
        for (int i = 0; i < BeamNotes.size(); i++) {
            int d = ((FM_Note) BeamNotes.get(i)).duration;
            if (d == FM_DurationValue.NOTE_WHOLE) return;
            if (d == FM_DurationValue.NOTE_WHOLE_D) return;
            if (d == FM_DurationValue.NOTE_HALF) return;
            if (d == FM_DurationValue.NOTE_HALF_D) return;
            if (d == FM_DurationValue.NOTE_QUARTER) return;
            if (d == FM_DurationValue.NOTE_QUARTER_D) return;
        }
        FM_Beam t = new FM_Beam(score, currentBeam);
        currentBeam++;
        for (int i = 0; i < BeamNotes.size(); i++) {
            ((FM_Note) BeamNotes.get(i)).beam = true;
            t.AddNote((FM_Note) BeamNotes.get(i));
        }
        Beams.add(t);
    }

    public int getNoteCount() {
        return StaveNotes.size();
    }

    public FM_BaseNote getNote(int index) {
        if (index < 0) return null;
        if (index > StaveNotes.size() - 1) return null;
        return StaveNotes.get(index);
    }

    public FM_BaseNote getLastNote() {
        if (getNoteCount() == 0) return null;
        return StaveNotes.get(StaveNotes.size() - 1);
    }

    public int LoadFromJson(JSONObject obj) {
        List<String> key_list = new ArrayList<>();
        List<String> clef_list = new ArrayList<>();
        String keySignature;
        String timeSignature;
        try {
            JSONArray keys = obj.getJSONArray("keys");
            for (int k = 0; k < keys.length(); k++) key_list.add(keys.getJSONArray(k).toString());
            JSONArray clef = obj.getJSONArray("clef");
            for (int k = 0; k < clef.length(); k++) clef_list.add(clef.getString(k));
            timeSignature = obj.optString("timesignature", "4/4");
            keySignature = obj.optString("keysignature", "DO");
        } catch (JSONException e) {
            return -1;
        }

        clearStaveNotes();
        setTimeSignature(FM_Const.getTimeSignature_n(timeSignature), FM_Const.getTimeSignature_d(timeSignature));
        setKeySignature(FM_Const.StringToKeySignature(keySignature));

        int firstStaveClef = FM_ClefValue.TREBLE;
        int secondStaveClef = FM_ClefValue.BASS;

        if (clef_list.size() >= 1) {
            if (clef_list.get(0).equals("treble")) {
                setFirstStaveClef(FM_ClefValue.TREBLE);
                firstStaveClef = FM_ClefValue.TREBLE;
            }
            else {
                setFirstStaveClef(FM_ClefValue.BASS);
                firstStaveClef = FM_ClefValue.BASS;
            }
        }
        if (clef_list.size() > 1) {
            if (clef_list.get(1).equals("treble")) {
                setSecondStaveClef(FM_ClefValue.TREBLE);
                secondStaveClef = FM_ClefValue.TREBLE;
            }
            else {
                setSecondStaveClef(FM_ClefValue.BASS);
                secondStaveClef = FM_ClefValue.BASS;
            }
        }
        int originalFirstStaveClef = firstStaveClef;
        int originalSecondStaveClef = secondStaveClef;

        int i = 0;
        String beam = "";
        String tie;
        String tuple = "";
        HashMap<Integer, List<FM_BaseNote>> Notes = new HashMap();
        HashMap<Integer, List<Integer>> Staves = new HashMap();
        while (i < key_list.size()) {
            FM_Key key = new FM_Key(key_list.get(i));
            if (key.type == FM_KeyType.Bar) {
                for (Integer k : Notes.keySet())
                    addChord(Objects.requireNonNull(Notes.get(k)), Staves.get(k));
                Notes.clear();
                Staves.clear();
                addStaveNote(new FM_BarNote(this));
                i++;
                continue;
            }
            if (key.type == FM_KeyType.Clef) {
                firstStaveClef = key.clef;
                FM_BaseNote c;
                if (firstStaveClef == FM_ClefValue.BASS) c = new FM_Clef(this, FM_ClefValue.BASS, 0);
                else c = new FM_Clef(this, FM_ClefValue.TREBLE, 0);
                List<FM_BaseNote> Note_List = new ArrayList<>();
                List<Integer> stave_List = new ArrayList<>();
                Note_List.add(c);
                stave_List.add(0);
                Notes.put(key.chord, Note_List);
                Staves.put(key.chord, stave_List);
                i++;
                continue;
            }

            if (!beam.equals("") && (key.beam.equals("") || !key.beam.equals(beam))) EndBeam();
            if (!tuple.equals("") && (key.tuple.equals("") || !key.tuple.equals(tuple))) EndTuplet();

            if (!key.beam.equals(beam) && !key.beam.equals("")) BeginBeam();
            if (!key.tuple.equals(tuple) && !key.tuple.equals("")) BeginTuplet(key.tuple);

            beam = key.beam;
            tie = key.tie;
            tuple = key.tuple;

            FM_BaseNote n;
            if (key.note == FM_NoteValue.REST) {
                n = new FM_Pause(this, key.duration, key.octave, key.voice);
            } else {
                int clef = firstStaveClef;
                if (key.stave == 1) clef = secondStaveClef;
                n = new FM_Note(this, key.note, key.octave, clef, key.accidental, key.duration, key.voice, key.stemUp);
                n.stem = key.stem;
                if (!beam.equals("")) AddToBeam((FM_Note) n);
                if (!tie.equals("")) AddToTie(tie, (FM_Note) n);
            }
            if (!tuple.equals("")) AddToTuplet(n);
            List<FM_BaseNote> Note_List = Notes.get(key.chord);
            List<Integer> stave_List = Staves.get(key.chord);
            if (Note_List == null) Note_List = new ArrayList();
            if (stave_List == null) stave_List = new ArrayList();
            Note_List.add(n);
            stave_List.add(key.stave);
            Notes.put(key.chord, Note_List);
            Staves.put(key.chord, stave_List);
            i++;
        }
        for (Integer k : Notes.keySet()) addChord(Objects.requireNonNull(Notes.get(k)), Staves.get(k));
        Notes.clear();
        Staves.clear();
        if (!beam.equals("")) EndBeam();
        if (!tuple.equals("")) EndTuplet();
        setFirstStaveClef(originalFirstStaveClef);
        setSecondStaveClef(originalSecondStaveClef);
        return 0;
    }
}