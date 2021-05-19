package ro.florinm.FM_Score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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
        if (keySignature == FM_KeySignatureValue.FAsharp || keySignature == FM_KeySignatureValue.REsharpm) {
            if (key.equals("f") || key.equals("c") || key.equals("g") || key.equals("d") || key.equals("a") || key.equals("e"))
                return key + "#";
        }
        if (keySignature == FM_KeySignatureValue.DOsharp || keySignature == FM_KeySignatureValue.LAsharpm) {
            if (key.equals("f") || key.equals("c") || key.equals("g") || key.equals("d") || key.equals("a") || key.equals("e") || key.equals("b"))
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
        if (keySignature == FM_KeySignatureValue.SOLb || keySignature == FM_KeySignatureValue.MIbm) {
            if (key.equals("b") || key.equals("e") || key.equals("a") || key.equals("d") || key.equals("g") || key.equals("c"))
                return key + "b";
        }
        if (keySignature == FM_KeySignatureValue.DOb || keySignature == FM_KeySignatureValue.LAbm) {
            if (key.equals("b") || key.equals("e") || key.equals("a") || key.equals("d") || key.equals("g") || key.equals("c") || key.equals("f"))
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

    static FM_Audio_Song TempSongToSong(FM_Temp_Audio_Song input, FM_ScoreBase score, int tempo) {
        FM_Audio_Song result = new FM_Audio_Song();
        result.keySignature = input.keySignature;
        FM_Audio_Measure m = new FM_Audio_Measure();
        result.measures.add(m);
        int measure = 0;
        HashMap<Integer, Integer> voice_duration = new HashMap<>();
        StartMeasure();
        FM_Audio_Note oldNote = null;

        //go through the chords
        for (int i = 0; i < input.chords.size(); i++) {
            //if starting a new measure, increase the measure index and clear the durations
            if (measure != input.chords.get(i).measure) {
                m = new FM_Audio_Measure();
                result.measures.add(m);
                measure += 1;
                StartMeasure();
                voice_duration.clear();
            }

            //from the durations subrtract the smallest
            int min;
            try {
                min = Collections.min(voice_duration.values());
            } catch(Exception ignored) {
                min = 0;
            }
            for (int vd : voice_duration.keySet()) {
                voice_duration.put(vd, voice_duration.getOrDefault(vd, 0) - min);
            }

            FM_Audio_Note n = new FM_Audio_Note();
            List<Integer> tracksInTie = new ArrayList<>();              //tracks if we are in a legato
            List<Integer> durationsInTie = new ArrayList<>();           //duration of each track if we are in a legato
            List<Integer> tracksOutsideTie = new ArrayList<>();         //tracks if we are outside a legato
            List<Integer> durationsOutsideTie = new ArrayList<>();      //duration of each track if we are outside a legato
            //clear everything
            n.legato = false;
            n.audioIntInLegato = -1;
            n.audioIntOutsideLegato = -1;
            n.audioTrackInLegato = null;
            n.audioTrackOutsideLegato = null;
            boolean is_pause = true;
            boolean is_chord = false;
            //begin processing, going through all the notes in the current chord
            for (int nIndex = 0; nIndex < input.chords.get(i).notes.size(); nIndex++) {
                FM_BaseNote note = input.chords.get(i).notes.get(nIndex);
                int d = FM_SoundPool.GetDurationInMs(note.getDuration(), note.tupletSize, tempo, input.timeSignature_d);    //the duration
                voice_duration.put(note.voice, voice_duration.getOrDefault(note.voice, 0) + d);                   //add the duration to the duration list
                //if the current note is a PAUSE
                if (note.getType() == FM_NoteType.PAUSE) {
                    durationsOutsideTie.add(d);
                    durationsInTie.add(d);
                    tracksInTie.add(-1);
                    tracksOutsideTie.add(-1);
                    if (oldNote != null) oldNote.NextPause = true;
                }
                //if the current note is a NOTE
                if (note.getType() == FM_NoteType.NOTE) {
                    int track = FM_SoundPool.GetIndex(NoteToString((FM_Note) note, input.keySignature));
                    // if the note is not the beginning nor the end of a tie
                    if (!((FM_Note) note).isTieStart && !((FM_Note) note).isTieEnd) {
                        tracksOutsideTie.add(track);
                        durationsOutsideTie.add(d);
                        tracksInTie.add(track);
                        durationsInTie.add(d);
                        is_pause = false;
                    }
                    //if the note is the end of a tie
                    else if (!((FM_Note) note).isTieStart) {
                        tracksOutsideTie.add(track);
                        durationsOutsideTie.add(d);
                        tracksInTie.add(-1);
                        durationsInTie.add(0);
                        is_pause = false;
                    }
                    //if the note is the beginning of a tie
                    else {
                        n.legato = true;
                        tracksOutsideTie.add(track);
                        durationsOutsideTie.add(d);
                        tracksInTie.add(track);
                        //find the duration of the note that is the end of this particular tie
                        int lDuration = 0;
                        for (int t = 0; t < score.Ties.size(); t++)
                            if (score.Ties.get(t).s == note)
                                lDuration = FM_SoundPool.GetDurationInMs(score.Ties.get(t).e.getDuration(), score.Ties.get(t).e.tupletSize, tempo, input.timeSignature_d);
                        durationsInTie.add((int) d + lDuration);
                        is_pause = false;
                        is_chord = true;
                    }
                }
            }
            n.pauseDuration = Collections.min(voice_duration.values());
            n.playDurationInTie = Collections.max(durationsInTie);
            n.playDurationOutsideTie = Collections.max(durationsOutsideTie);
            if (!is_pause) {
                int track_count = 0;
                int track = -1;
                for (int t : tracksInTie)         //cate track-uri non-pauza sunt?
                    if (t != -1) {
                        track_count += 1;
                        track = t;
                    }
                if (track_count == 1 && !is_chord) {
                    n.audioIntInLegato = track;
                    n.audioTrackInLegato = null;
                } else {
                    n.audioIntInLegato = 0;
                    n.audioTrackInLegato = FM_SoundPool.getInstance().CreateTrack(tracksInTie, durationsInTie);
                }

                track_count = 0;
                track = -1;
                for (int t : tracksOutsideTie)         //cate track-uri non-pauza sunt?
                    if (t != -1) {
                        track_count += 1;
                        track = t;
                    }
                if (track_count == 1 && !is_chord) {
                    n.audioIntOutsideLegato = track;
                    n.audioTrackOutsideLegato = null;
                } else {
                    n.audioIntOutsideLegato = 0;
                    n.audioTrackOutsideLegato = FM_SoundPool.getInstance().CreateTrack(tracksOutsideTie, durationsOutsideTie);
                }
            }
            m.notes.add(n);
            oldNote = n;
        }
        return result;
    }


    static FM_Audio_Song generateSongFromScore(FM_ScoreBase obj, int tempo) {
        FM_Temp_Audio_Song song = new FM_Temp_Audio_Song();
        song.keySignature = obj.getKeySignature();
        song.timeSignature_d = obj.getTimeSignature_d();
        int measure = 0;
        for (int i = 0; i < obj.getNoteCount(); i++) {
            FM_BaseNote note = obj.getNote(i);
            if (note.getType() == FM_NoteType.BAR) measure += 1;
            else {
                if (obj.getNote(i).getType() == FM_NoteType.NOTE || obj.getNote(i).getType() == FM_NoteType.PAUSE || obj.getNote(i).getType() == FM_NoteType.CHORD) {
                    FM_Temp_Audio_Note temp_chord = new FM_Temp_Audio_Note();
                    temp_chord.measure = measure;
                    if (note.getType() == FM_NoteType.NOTE) temp_chord.notes.add(note);
                    if (note.getType() == FM_NoteType.CHORD) {
                        FM_Chord chord = (FM_Chord) note;
                        for (int j = 0; j < chord.Notes.size(); j++) {
                            if (chord.Notes.get(j).getType() == FM_NoteType.NOTE) temp_chord.notes.add(chord.Notes.get(j));
                            if (chord.Notes.get(j).getType() == FM_NoteType.PAUSE) temp_chord.notes.add(chord.Notes.get(j));
                        }
                    }
                    if (note.getType() == FM_NoteType.PAUSE) temp_chord.notes.add(note);
                    song.chords.add(temp_chord);
                }
            }
        }
        return TempSongToSong(song, obj, tempo);
    }
}
