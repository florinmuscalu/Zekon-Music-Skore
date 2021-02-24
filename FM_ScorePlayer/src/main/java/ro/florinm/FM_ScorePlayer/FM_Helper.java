package ro.florinm.FM_ScorePlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import ro.florinm.FM_Score.FM_Accidental;
import ro.florinm.FM_Score.FM_BaseNote;
import ro.florinm.FM_Score.FM_Chord;
import ro.florinm.FM_Score.FM_KeySignatureValue;
import ro.florinm.FM_Score.FM_Note;
import ro.florinm.FM_Score.FM_NoteType;
import ro.florinm.FM_Score.FM_NoteValue;
import ro.florinm.FM_Score.FM_Score;

class FM_Helper {
    static String do_a = "";
    static String re_a = "";
    static String mi_a = "";
    static String fa_a = "";
    static String sol_a = "";
    static String la_a = "";
    static String si_a = "";

    static void StartMeasure() {
        do_a = "c";
        re_a = "d";
        mi_a = "e";
        fa_a = "f";
        sol_a = "g";
        la_a = "a";
        si_a = "b";
    }

    static String ApplyTonality(@FM_KeySignatureValue int keySignature, String key) {
        //send it only the note, without the octave!!
        if (key.contains("n") ||
                key.contains("#") ||
                key.contains("##") ||
                key.contains("###") ||
                (key.endsWith("b") && key.length() == 2) ||
                (key.endsWith("bb") && key.length() == 3) ||
                (key.endsWith("bbb") && key.length() == 4))
            return key;
        if (keySignature == FM_KeySignatureValue.SOL || keySignature == FM_KeySignatureValue.MIm) {
            if (key.equals("f")) return key + "#";
        }
        if (keySignature == FM_KeySignatureValue.RE || keySignature == FM_KeySignatureValue.SIm) {
            if (key.equals("f") || key.equals("c")) return key + "#";
        }
        if (keySignature == FM_KeySignatureValue.LA || keySignature == FM_KeySignatureValue.FAsharpm) {
            if (key.equals("f") || key.equals("c") || key.equals("g")) return key + "#";
        }
        if (keySignature == FM_KeySignatureValue.MI || keySignature == FM_KeySignatureValue.DOsharpm) {
            if (key.equals("f") || key.equals("c") || key.equals("g") || key.equals("d"))
                return key + "#";
        }
        if (keySignature == FM_KeySignatureValue.SI || keySignature == FM_KeySignatureValue.SOLsharpm) {
            if (key.equals("f") || key.equals("c") || key.equals("g") || key.equals("d") || key.equals("a"))
                return key + "#";
        }
        if (keySignature == FM_KeySignatureValue.FA || keySignature == FM_KeySignatureValue.REm) {
            if (key.equals("b")) return key + "b";
        }
        if (keySignature == FM_KeySignatureValue.SIb || keySignature == FM_KeySignatureValue.SOLm) {
            if (key.equals("b") || key.equals("e")) return key + "b";
        }
        if (keySignature == FM_KeySignatureValue.MIb || keySignature == FM_KeySignatureValue.DOm) {
            if (key.equals("b") || key.equals("e") || key.equals("a")) return key + "b";
        }
        if (keySignature == FM_KeySignatureValue.LAb || keySignature == FM_KeySignatureValue.FAm) {
            if (key.equals("b") || key.equals("e") || key.equals("a") || key.equals("d"))
                return key + "b";
        }
        if (keySignature == FM_KeySignatureValue.REb || keySignature == FM_KeySignatureValue.SIbm) {
            if (key.equals("b") || key.equals("e") || key.equals("a") || key.equals("d") || key.equals("g"))
                return key + "b";
        }
        return key;
    }

    static String NoteToString(FM_Note note, @FM_KeySignatureValue int keySignature) {
        String result = "";
        @FM_NoteValue int value = note.getNote();
        if (value == FM_NoteValue.DO) result = "c";
        if (value == FM_NoteValue.RE) result = "d";
        if (value == FM_NoteValue.MI) result = "e";
        if (value == FM_NoteValue.FA) result = "f";
        if (value == FM_NoteValue.SOL) result = "g";
        if (value == FM_NoteValue.LA) result = "a";
        if (value == FM_NoteValue.SI) result = "b";
        @FM_Accidental int accidental = note.getAccidental();
        if (accidental > 100) accidental = accidental - 100;
        if (accidental == FM_Accidental.Natural) result = result + "n";
        if (accidental == FM_Accidental.Flat) result = result + "b";
        if (accidental == FM_Accidental.Sharp) result = result + "#";
        if (accidental == FM_Accidental.DoubleSharp) result = result + "##";
        if (accidental == FM_Accidental.DoubleFlat) result = result + "bb";
        if (accidental == FM_Accidental.TripleSharp) result = result + "###";
        if (accidental == FM_Accidental.TripleFlat) result = result + "bbb";
        result = computeNote(keySignature, result);
        result = result + "/" + note.getOctave();
        return result;
    }

    static String computeNote(@FM_KeySignatureValue int keySignature, String note) {
        String initial_note = note;
        String tmp = ApplyTonality(keySignature, note);
        if (note.startsWith("c")) {
            if (!note.equals("c")) do_a = note;
            else note = do_a;
        }
        if (note.startsWith("d")) {
            if (!note.equals("d")) re_a = note;
            else note = re_a;
        }
        if (note.startsWith("e")) {
            if (!note.equals("e")) mi_a = note;
            else note = mi_a;
        }
        if (note.startsWith("f")) {
            if (!note.equals("f")) fa_a = note;
            else note = fa_a;
        }
        if (note.startsWith("g")) {
            if (!note.equals("g")) sol_a = note;
            else note = sol_a;
        }
        if (note.startsWith("a")) {
            if (!note.equals("a")) la_a = note;
            else note = la_a;
        }
        if (note.startsWith("b")) {
            if (!note.equals("b")) si_a = note;
            else note = si_a;
        }
        if (initial_note.equals(note)) note = tmp;
        return note;
    }


    static FM_Audio_Song generateSongFromScore(FM_Score obj, int tempo) {
        FM_Audio_Song song = new FM_Audio_Song();
        song.keySignature = obj.getKeySignature();
        FM_Audio_Measure m = new FM_Audio_Measure();
        song.measures.add(m);
        StartMeasure();
        FM_Audio_Note oldNote = null;
        for (int i = 0; i < obj.getNoteCount(); i++) {
            FM_BaseNote note = obj.getNote(i);
            if (note.getType() == FM_NoteType.BAR) {
                m = new FM_Audio_Measure();
                song.measures.add(m);
                StartMeasure();
            } else {
                if (obj.getNote(i).getType() == FM_NoteType.NOTE || obj.getNote(i).getType() == FM_NoteType.PAUSE || obj.getNote(i).getType() == FM_NoteType.CHORD) {
                    FM_Audio_Note n = new FM_Audio_Note();
                    if (note.getType() == FM_NoteType.NOTE) {
                        if (((FM_Note) note).isTieStart) n.legato_start = true;
                        if (((FM_Note) note).isTieEnd) n.legato_end = true;
                        n.playDuration = FM_SoundPool.GetDurationInMs(note.getDuration(), ((FM_Note) note).tupletStr, tempo, obj.getTimeSignature_d());
                        n.pauseDuration = n.playDuration;
                        n.audioT = null;
                        n.audioInt = FM_SoundPool.GetIndex(NoteToString((FM_Note) note, obj.getKeySignature()));
                    }
                    if (note.getType() == FM_NoteType.CHORD) {
                        FM_Chord chord = (FM_Chord) note;
                        List<Integer> tracks = new ArrayList<>();
                        List<Integer> durations = new ArrayList<>();
                        for (int j = 0; j < chord.Notes.size(); j++) {
                            if (chord.Notes.get(j).getType() == FM_NoteType.NOTE) {
                                durations.add(FM_SoundPool.GetDurationInMs(chord.Notes.get(j).getDuration(), ((FM_Note) chord.Notes.get(j)).tupletStr, tempo, obj.getTimeSignature_d()));
                                tracks.add(FM_SoundPool.GetIndex(NoteToString((FM_Note) chord.Notes.get(j), obj.getKeySignature())));
                                if (((FM_Note) chord.Notes.get(j)).isTieStart)
                                    n.legato_start = true;
                                if (((FM_Note) chord.Notes.get(j)).isTieEnd) n.legato_end = true;
                            }
                            if (chord.Notes.get(j).getType() == FM_NoteType.PAUSE) {
                                durations.add(FM_SoundPool.GetDurationInMs(chord.Notes.get(j).getDuration(), "", tempo, obj.getTimeSignature_d()));
                                tracks.add(-1);
                            }
                        }
                        if (tracks.size() == 1) {
                            n.playDuration = durations.get(0);
                            n.pauseDuration = n.playDuration;
                            n.audioT = null;
                            n.audioInt = tracks.get(0);
                            if (n.audioInt == -1 && oldNote != null) oldNote.NextPause = true;
                        } else {
                            n.playDuration = Collections.max(durations);
                            n.pauseDuration = Collections.min(durations);
                            n.audioInt = 0;
                            n.audioT = null;
                            n.audioT = FM_SoundPool.getInstance().CreateTrack(tracks, durations);
                        }
                    }
                    if (note.getType() == FM_NoteType.PAUSE) {
                        n.playDuration = FM_SoundPool.GetDurationInMs(note.getDuration(), "", tempo, obj.getTimeSignature_d());
                        n.pauseDuration = n.playDuration;
                        n.audioT = null;
                        n.audioInt = -1;
                        if (oldNote != null) oldNote.NextPause = true;
                    }

                    m.notes.add(n);
                    oldNote = n;
                }
            }
        }
        return song;
    }
}
