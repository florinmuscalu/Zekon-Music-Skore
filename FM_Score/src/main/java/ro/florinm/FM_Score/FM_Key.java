package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({FM_KeyType.Bar, FM_KeyType.Clef, FM_KeyType.Key})
@Retention(RetentionPolicy.SOURCE)
@interface FM_KeyType {
    int Bar = 0;
    int Clef = 1;
    int Key = 2;
}


public class FM_Key {
    //common
    @FM_KeyType
    int type;
    //if it's a Key
    @FM_NoteValue
    int note;
    @FM_Accidental
    int accidental;
    int octave;
    @FM_DurationValue
    int duration;
    boolean stemUp;
    boolean stem;
    String tie;
    String beam;
    String tuple;
    int stave;          //0 or 1
    int voice;          //0, 1, 2, 3...
    //if it's a Clef
    @FM_ClefValue
    int clef;
    //common for Key and Clef
    int chord;

    FM_Key(String key) {
        key = key.replace(" ", "").replace("\\", "").replace("\"", "").replace("[", "").replace("]", "").toLowerCase().trim();
        if (key.contains("bar")) {
            this.type = FM_KeyType.Bar;
            return;
        }
        String[] s = key.split(",");
        //for (int i = 0; i < s.length; i++) s[i] = s[i].trim();

        if (key.contains("bass")) {
            this.type = FM_KeyType.Clef;
            this.clef = FM_ClefValue.BASS;
            this.chord = Integer.parseInt(s[1]);
            return;
        }
        if (key.contains("treble")) {
            this.type = FM_KeyType.Clef;
            this.clef = FM_ClefValue.TREBLE;
            this.chord = Integer.parseInt(s[1]);
            return;
        }
        this.type = FM_KeyType.Key;

        //Find the note
        String temp = s[0];
        boolean found = false;
        if (temp.startsWith("do"))  {
            this.note = FM_NoteValue.DO;
            temp = temp.substring(2);
            found = true;
        }
        if (!found && temp.startsWith("re"))  {
            this.note = FM_NoteValue.RE;
            temp = temp.substring(2);
            found = true;
        }
        if (!found && temp.startsWith("mi"))  {
            this.note = FM_NoteValue.MI;
            temp = temp.substring(2);
            found = true;
        }
        if (!found && temp.startsWith("fa"))  {
            this.note = FM_NoteValue.FA;
            temp = temp.substring(2);
            found = true;
        }
        if (!found && temp.startsWith("sol"))  {
            this.note = FM_NoteValue.SOL;
            temp = temp.substring(3);
            found = true;
        }
        if (!found && temp.startsWith("la"))  {
            this.note = FM_NoteValue.LA;
            temp = temp.substring(2);
            found = true;
        }
        if (!found && temp.startsWith("si"))  {
            this.note = FM_NoteValue.SI;
            temp = temp.substring(2);
            found = true;
        }
        if (!found && temp.startsWith("c"))  {
            this.note = FM_NoteValue.DO;
            temp = temp.substring(1);
            found = true;
        }
        if (!found && temp.startsWith("d"))  {
            this.note = FM_NoteValue.RE;
            temp = temp.substring(1);
            found = true;
        }
        if (!found && temp.startsWith("e"))  {
            this.note = FM_NoteValue.MI;
            temp = temp.substring(1);
            found = true;
        }
        if (!found && temp.startsWith("f"))  {
            this.note = FM_NoteValue.FA;
            temp = temp.substring(1);
            found = true;
        }
        if (!found && temp.startsWith("g"))  {
            this.note = FM_NoteValue.SOL;
            temp = temp.substring(1);
            found = true;
        }
        if (!found && temp.startsWith("a"))  {
            this.note = FM_NoteValue.LA;
            temp = temp.substring(1);
            found = true;
        }
        if (!found && temp.startsWith("b"))  {
            this.note = FM_NoteValue.SI;
            temp = temp.substring(1);
            found = true;
        }
        if (!found && temp.startsWith("r"))  {
            this.note = FM_NoteValue.REST;
            temp = temp.substring(1);
        }

        //accidental
        int accidental = 0;
        found = false;
        if (temp.contains("(")) accidental = FM_Accidental.Courtesy;
        if (temp.contains("###")) {
            accidental = accidental + FM_Accidental.TripleSharp;
            found = true;
        }
        if (!found && temp.contains("##")) {
            accidental = accidental + FM_Accidental.DoubleSharp;
            found = true;
        }
        if (!found && temp.contains("#")) {
            accidental = accidental + FM_Accidental.Sharp;
            found = true;
        }
        if (!found && temp.contains("bbb")) {
            accidental = accidental + FM_Accidental.TripleFlat;
            found = true;
        }
        if (!found && temp.contains("bb")) {
            accidental = accidental + FM_Accidental.DoubleFlat;
            found = true;
        }
        if (!found && temp.contains("b")) {
            accidental = accidental + FM_Accidental.Flat;
            found = true;
        }
        if (!found && temp.contains("n")) {
            accidental = accidental + FM_Accidental.Natural;
        }
        this.accidental = accidental;

        //octave
        if (temp.equals("")) this.octave = 0;
        else this.octave = Integer.parseInt(temp.substring(temp.length() - 1));

        //duration
        this.duration = FM_DurationValue.NOTE_WHOLE;
        if (s[1].equals("wd")) this.duration = FM_DurationValue.NOTE_WHOLE_D;
        if (s[1].equals("wdr")) this.duration = FM_DurationValue.NOTE_WHOLE_D;
        if (s[1].equals("h")) this.duration = FM_DurationValue.NOTE_HALF;
        if (s[1].equals("hr")) this.duration = FM_DurationValue.NOTE_HALF;
        if (s[1].equals("hd")) this.duration = FM_DurationValue.NOTE_HALF_D;
        if (s[1].equals("hdr")) this.duration = FM_DurationValue.NOTE_HALF_D;
        if (s[1].equals("q")) this.duration = FM_DurationValue.NOTE_QUARTER;
        if (s[1].equals("qr")) this.duration = FM_DurationValue.NOTE_QUARTER;
        if (s[1].equals("qd")) this.duration = FM_DurationValue.NOTE_QUARTER_D;
        if (s[1].equals("qdr")) this.duration = FM_DurationValue.NOTE_QUARTER_D;
        if (s[1].equals("8")) this.duration = FM_DurationValue.NOTE_EIGHTH;
        if (s[1].equals("8r")) this.duration = FM_DurationValue.NOTE_EIGHTH;
        if (s[1].equals("8d")) this.duration = FM_DurationValue.NOTE_EIGHTH_D;
        if (s[1].equals("8dr")) this.duration = FM_DurationValue.NOTE_EIGHTH_D;
        if (s[1].equals("16")) this.duration = FM_DurationValue.NOTE_SIXTEENTH;
        if (s[1].equals("16r")) this.duration = FM_DurationValue.NOTE_SIXTEENTH;
        if (s[1].equals("16d")) this.duration = FM_DurationValue.NOTE_SIXTEENTH_D;
        if (s[1].equals("16dr")) this.duration = FM_DurationValue.NOTE_SIXTEENTH_D;
        if (s[1].equals("32")) this.duration = FM_DurationValue.NOTE_THIRTY_SECOND;
        if (s[1].equals("32r")) this.duration = FM_DurationValue.NOTE_THIRTY_SECOND;
        if (s[1].equals("32d")) this.duration = FM_DurationValue.NOTE_THIRTY_SECOND_D;
        if (s[1].equals("32dr")) this.duration = FM_DurationValue.NOTE_THIRTY_SECOND_D;

        this.stemUp = !s[2].equals("down");
        this.stem = !s[2].equals("none");
        this.tie = s[3];
        this.beam = s[4];
        this.tuple = s[5];
        this.stave = Integer.parseInt(s[6]);
        this.voice = Integer.parseInt(s[7]);
        this.chord = Integer.parseInt(s[8]);
    }
}