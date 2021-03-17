package ro.florinm.FM_Score;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.TypedValue;

public class FM_Const {
    static final int DEFAULT_EXTRA_PADDING = 5;
    //notes
    static final String _1Note = "\ue0a2";
    static final String _2Note_up = "\ue1d3";
    static final String _2Note_down = "\ue1d4";
    static final String _4Note_up = "\ue1D5";
    static final String _4Note_down = "\ue1d6";
    static final String _8Note_up = "\ue1d7";
    static final String _8Note_down = "\ue1d8";
    static final String _16Note_up = "\ue1d9";
    static final String _16Note_down = "\ue1da";
    static final String _32Note_up = "\ue1db";
    static final String _32Note_down = "\ue1dc";
    static final String FillNote = "\ue1af";
    static final String EmptyNote = "\ue1b0";
    //public static final String FillNote = "\ue0a4";
    //public static final String EmptyNote = "\ue0a3";

    //clefs
    static final String TrebleClef = "\uD834\uDD1E";
    static final String BassClef = "\uD834\uDD22";

    //parenthesis
    static final String ParenthesisLeft = "\ue092";
    static final String ParenthesisRight = "\ue093";

    //brackets
    static final String Bracket = "\ue000";

    //numbers
    static final String _2 = "\ue082";
    static final String _3 = "\ue083";
    static final String _4 = "\ue084";
    static final String _5 = "\ue085";
    static final String _6 = "\ue086";
    static final String _7 = "\ue087";
    static final String _8 = "\ue088";
    static final String _9 = "\ue089";

    static final String _2_b = "\ue927";
    static final String _3_b = "\ue928";
    static final String _4_b = "\ue929";

    static final String _3_small = "\uea54";
    static final String _5_small = "\uea57";

    //accidentals
    static final String Flat = "\ue260";
    static final String Natural = "\ue261";
    static final String Sharp = "\ue262";
    static final String DoubleSharp = "\ue263";
    static final String DoubleFlat = "\ue264";
    //public static final String TripleSharp = "\ue265";
    static final String TripleSharp = "\ue262 \ue263";

    static final String TripleFlat = "\ue266";
    static final String Dot = "\ue1e7";

    //pauses
    static final String Pause_1 = "\ue4e3";
    static final String Pause_2 = "\ue4e4";
    static final String Pause_4 = "\ue4e5";
    static final String Pause_8 = "\ue4e6";
    static final String Pause_16 = "\ue4e7";
    static final String Pause_32 = "\ue4e8";

    //Tie
    static final String Tie = "\ue551";

    //Tuplet

    public static float dpTOpx(Context context, float dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static float pxTOdp(Context context, float px) {
        Resources r = context.getResources();
        float tmp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, r.getDisplayMetrics());
        return px / tmp;
    }

    static int distanceBetweenNotes(FM_BaseNote n1, FM_BaseNote n2) {
        if (n1.stave != n2.stave) return 10;
        return (n1.note - n2.note) + ((n1.octave - n2.octave) * 7);
    }

    static float slope(float x1, float y1, float x2, float y2) {
        return slope(0.2f, x1, y1, x2, y2);
    }

    static float slope(float maxSlope, float x1, float y1, float x2, float y2) {
        float ret =  (y2 - y1) / (x2 - x1);
        if (ret > maxSlope) ret =  maxSlope;
        if (ret <-maxSlope) ret = -maxSlope;
        return ret;
    }



    public static FM_BaseKey getKey(String key) {
        key = key.replace("\\", "").replace("\"", "").replace("[", "").replace("]", "").toLowerCase().trim();
        if (key.contains("bar")) {
            return new FM_KeyBar();
        }
        String[] s = key.split(",");
        if (key.contains("bass")) {
            return new FM_KeyClef(FM_ClefValue.BASS, Integer.parseInt(s[1]));
        }
        if (key.contains("treble")) {
            return new FM_KeyClef(FM_ClefValue.TREBLE, Integer.parseInt(s[1]));
        }
        FM_KeyKey k = new FM_KeyKey();
        k.type = FM_KeyType.Key;

        //Find the note
        String temp = s[0];
        boolean found = false;
        if (temp.startsWith("do"))  {
            k.note = FM_NoteValue.DO;
            temp = temp.substring(2);
            found = true;
        }
        if (!found && temp.startsWith("re"))  {
            k.note = FM_NoteValue.RE;
            temp = temp.substring(2);
            found = true;
        }
        if (!found && temp.startsWith("mi"))  {
            k.note = FM_NoteValue.MI;
            temp = temp.substring(2);
            found = true;
        }
        if (!found && temp.startsWith("fa"))  {
            k.note = FM_NoteValue.FA;
            temp = temp.substring(2);
            found = true;
        }
        if (!found && temp.startsWith("sol"))  {
            k.note = FM_NoteValue.SOL;
            temp = temp.substring(3);
            found = true;
        }
        if (!found && temp.startsWith("la"))  {
            k.note = FM_NoteValue.LA;
            temp = temp.substring(2);
            found = true;
        }
        if (!found && temp.startsWith("si"))  {
            k.note = FM_NoteValue.SI;
            temp = temp.substring(2);
            found = true;
        }
        if (!found && temp.startsWith("c"))  {
            k.note = FM_NoteValue.DO;
            temp = temp.substring(1);
            found = true;
        }
        if (!found && temp.startsWith("d"))  {
            k.note = FM_NoteValue.RE;
            temp = temp.substring(1);
            found = true;
        }
        if (!found && temp.startsWith("e"))  {
            k.note = FM_NoteValue.MI;
            temp = temp.substring(1);
            found = true;
        }
        if (!found && temp.startsWith("f"))  {
            k.note = FM_NoteValue.FA;
            temp = temp.substring(1);
            found = true;
        }
        if (!found && temp.startsWith("g"))  {
            k.note = FM_NoteValue.SOL;
            temp = temp.substring(1);
            found = true;
        }
        if (!found && temp.startsWith("a"))  {
            k.note = FM_NoteValue.LA;
            temp = temp.substring(1);
            found = true;
        }
        if (!found && temp.startsWith("b"))  {
            k.note = FM_NoteValue.SI;
            temp = temp.substring(1);
            found = true;
        }
        if (!found && temp.startsWith("r"))  {
            k.note = FM_NoteValue.REST;
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
        k.accidental = accidental;

        //octave
        if (temp.equals("")) k.octave = 0;
        else k.octave = Integer.parseInt(temp.substring(temp.length() - 1));

        //duration
        k.duration = FM_DurationValue.NOTE_WHOLE;
        if (s[1].equals("wd")) k.duration = FM_DurationValue.NOTE_WHOLE_D;
        if (s[1].equals("wdr")) k.duration = FM_DurationValue.NOTE_WHOLE_D;
        if (s[1].equals("h")) k.duration = FM_DurationValue.NOTE_HALF;
        if (s[1].equals("hr")) k.duration = FM_DurationValue.NOTE_HALF;
        if (s[1].equals("hd")) k.duration = FM_DurationValue.NOTE_HALF_D;
        if (s[1].equals("hdr")) k.duration = FM_DurationValue.NOTE_HALF_D;
        if (s[1].equals("q")) k.duration = FM_DurationValue.NOTE_QUARTER;
        if (s[1].equals("qr")) k.duration = FM_DurationValue.NOTE_QUARTER;
        if (s[1].equals("qd")) k.duration = FM_DurationValue.NOTE_QUARTER_D;
        if (s[1].equals("qdr")) k.duration = FM_DurationValue.NOTE_QUARTER_D;
        if (s[1].equals("8")) k.duration = FM_DurationValue.NOTE_EIGHTH;
        if (s[1].equals("8r")) k.duration = FM_DurationValue.NOTE_EIGHTH;
        if (s[1].equals("8d")) k.duration = FM_DurationValue.NOTE_EIGHTH_D;
        if (s[1].equals("8dr")) k.duration = FM_DurationValue.NOTE_EIGHTH_D;
        if (s[1].equals("16")) k.duration = FM_DurationValue.NOTE_SIXTEENTH;
        if (s[1].equals("16r")) k.duration = FM_DurationValue.NOTE_SIXTEENTH;
        if (s[1].equals("16d")) k.duration = FM_DurationValue.NOTE_SIXTEENTH_D;
        if (s[1].equals("16dr")) k.duration = FM_DurationValue.NOTE_SIXTEENTH_D;
        if (s[1].equals("32")) k.duration = FM_DurationValue.NOTE_THIRTY_SECOND;
        if (s[1].equals("32r")) k.duration = FM_DurationValue.NOTE_THIRTY_SECOND;
        if (s[1].equals("32d")) k.duration = FM_DurationValue.NOTE_THIRTY_SECOND_D;
        if (s[1].equals("32dr")) k.duration = FM_DurationValue.NOTE_THIRTY_SECOND_D;

        k.stemUp = s[2].equals("up");
        k.tie = s[3];
        k.beam = s[4];
        k.tuple = s[5];
        k.stave = Integer.parseInt(s[6]);
        k.voice = Integer.parseInt(s[7]);
        k.chord = Integer.parseInt(s[8]);
        return k;
    }

    static float getY2(float slope, float x1, float y1, float x2) {
        return y1 + slope * (x2 - x1);
    }

    public static int keyCount(String key) {
        key = key.replace("\\", "").replace("\"", "").replace("[", "").replace("]", "").toLowerCase().trim();
        String[] s = key.split(",");
        return s.length;
    }

    public static int keyToNote(String key) {
        return keyToNote(key, 0);
    }

    public static int keyToNote(String key, int index) {
        key = key.replace("\\", "").replace("\"", "").replace("[", "").replace("]", "").toLowerCase().trim();
        String[] s = key.split(",");
        key = s[index];
        if (key.startsWith("do")) return FM_NoteValue.DO;
        if (key.startsWith("re")) return FM_NoteValue.RE;
        if (key.startsWith("mi")) return FM_NoteValue.MI;
        if (key.startsWith("fa")) return FM_NoteValue.FA;
        if (key.startsWith("sol")) return FM_NoteValue.SOL;
        if (key.startsWith("la")) return FM_NoteValue.LA;
        if (key.startsWith("si")) return FM_NoteValue.SI;

        if (key.startsWith("c")) return FM_NoteValue.DO;
        if (key.startsWith("d")) return FM_NoteValue.RE;
        if (key.startsWith("e")) return FM_NoteValue.MI;
        if (key.startsWith("f")) return FM_NoteValue.FA;
        if (key.startsWith("g")) return FM_NoteValue.SOL;
        if (key.startsWith("a")) return FM_NoteValue.LA;
        if (key.startsWith("b")) return FM_NoteValue.SI;

        if (key.startsWith("r")) return FM_NoteValue.REST;
        return FM_NoteValue.DO;
    }

    public static int keyToOctave(String key) {
        return keyToOctave(key, 0);
    }

    public static int keyToOctave(String key, int index) {
        key = key.replace("\\", "").replace("\"", "").replace("[", "").replace("]", "").toLowerCase().trim();
        String[] s = key.split(",");
        key = s[index].trim();
        if (key.equals("r")) return 0;
        return Integer.parseInt(key.substring(key.length() - 1));
    }

    static int keyToDuration(String key, int pos) {
        key = key.replace("\\", "").replace("\"", "").replace("[", "").replace("]", "").toLowerCase().trim();
        String[] s = key.split(",");
        key = s[pos].trim();
        if (key.equals("w")) return FM_DurationValue.NOTE_WHOLE;
        if (key.equals("wr")) return FM_DurationValue.NOTE_WHOLE;
        if (key.equals("wd")) return FM_DurationValue.NOTE_WHOLE_D;
        if (key.equals("wdr")) return FM_DurationValue.NOTE_WHOLE_D;
        if (key.equals("h")) return FM_DurationValue.NOTE_HALF;
        if (key.equals("hr")) return FM_DurationValue.NOTE_HALF;
        if (key.equals("hd")) return FM_DurationValue.NOTE_HALF_D;
        if (key.equals("hdr")) return FM_DurationValue.NOTE_HALF_D;
        if (key.equals("q")) return FM_DurationValue.NOTE_QUARTER;
        if (key.equals("qr")) return FM_DurationValue.NOTE_QUARTER;
        if (key.equals("qd")) return FM_DurationValue.NOTE_QUARTER_D;
        if (key.equals("qdr")) return FM_DurationValue.NOTE_QUARTER_D;
        if (key.equals("8")) return FM_DurationValue.NOTE_EIGHTH;
        if (key.equals("8r")) return FM_DurationValue.NOTE_EIGHTH;
        if (key.equals("8d")) return FM_DurationValue.NOTE_EIGHTH_D;
        if (key.equals("8dr")) return FM_DurationValue.NOTE_EIGHTH_D;
        if (key.equals("16")) return FM_DurationValue.NOTE_SIXTEENTH;
        if (key.equals("16r")) return FM_DurationValue.NOTE_SIXTEENTH;
        if (key.equals("16d")) return FM_DurationValue.NOTE_SIXTEENTH_D;
        if (key.equals("16dr")) return FM_DurationValue.NOTE_SIXTEENTH_D;
        if (key.equals("32")) return FM_DurationValue.NOTE_THIRTY_SECOND;
        if (key.equals("32r")) return FM_DurationValue.NOTE_THIRTY_SECOND;
        if (key.equals("32d")) return FM_DurationValue.NOTE_THIRTY_SECOND_D;
        if (key.equals("32dr")) return FM_DurationValue.NOTE_THIRTY_SECOND_D;
        return FM_DurationValue.NOTE_WHOLE;
    }

    public static int keyToAccidental(String key, int pos) {
        key = key.replace("\\", "").replace("\"", "").replace("[", "").replace("]", "").toLowerCase().trim();
        String[] s = key.split(",");
        key = s[pos].trim().substring(1);
        int courtesy = 0;
        if (key.contains("(")) courtesy = FM_Accidental.Courtesy;
        if (key.contains("###")) return courtesy + FM_Accidental.TripleSharp;
        if (key.contains("##")) return courtesy + FM_Accidental.DoubleSharp;
        if (key.contains("#")) return courtesy + FM_Accidental.Sharp;
        if (key.contains("bbb")) return courtesy + FM_Accidental.TripleFlat;
        if (key.contains("bb")) return courtesy + FM_Accidental.DoubleFlat;
        if (key.contains("b")) return courtesy + FM_Accidental.Flat;
        if (key.contains("n")) return courtesy + FM_Accidental.Natural;
        return FM_Accidental.None;
    }

    static boolean keyToStem(String key, int pos) {
        key = key.replace("\\", "").replace("\"", "").replace("[", "").replace("]", "").toLowerCase().trim();
        String[] s = key.split(",");
        key = s[pos].trim();
        return key.equals("up");
    }

    static String keyToElement(String key, int pos) {
        key = key.replace("\\", "").replace("\"", "").replace("[", "").replace("]", "").toLowerCase().trim();
        String[] s = key.split(",");
        key = s[pos].trim();
        return key;
    }

    public static int StringToKeySignature(String s) {
        s = s.toLowerCase().trim();
        if (s.equals("do")) return FM_KeySignatureValue.DO;
        if (s.equals("fa")) return FM_KeySignatureValue.FA;
        if (s.equals("sib")) return FM_KeySignatureValue.SIb;
        if (s.equals("mib")) return FM_KeySignatureValue.MIb;
        if (s.equals("lab")) return FM_KeySignatureValue.LAb;
        if (s.equals("reb")) return FM_KeySignatureValue.REb;
        if (s.equals("solb")) return FM_KeySignatureValue.SOLb;
        if (s.equals("dob")) return FM_KeySignatureValue.DOb;
        if (s.equals("sol")) return FM_KeySignatureValue.SOL;
        if (s.equals("re")) return FM_KeySignatureValue.RE;
        if (s.equals("la")) return FM_KeySignatureValue.LA;
        if (s.equals("mi")) return FM_KeySignatureValue.MI;
        if (s.equals("si")) return FM_KeySignatureValue.SI;
        if (s.equals("fa#")) return FM_KeySignatureValue.FAsharp;
        if (s.equals("do#")) return FM_KeySignatureValue.DOsharp;
        if (s.equals("lam")) return FM_KeySignatureValue.LAm;
        if (s.equals("rem")) return FM_KeySignatureValue.REm;
        if (s.equals("solm")) return FM_KeySignatureValue.SOLm;
        if (s.equals("dom")) return FM_KeySignatureValue.DOm;
        if (s.equals("fam")) return FM_KeySignatureValue.FAm;
        if (s.equals("sibm")) return FM_KeySignatureValue.SIbm;
        if (s.equals("mibm")) return FM_KeySignatureValue.MIbm;
        if (s.equals("labm")) return FM_KeySignatureValue.LAbm;
        if (s.equals("mim")) return FM_KeySignatureValue.MIm;
        if (s.equals("sim")) return FM_KeySignatureValue.SIm;
        if (s.equals("fa#m")) return FM_KeySignatureValue.FAsharpm;
        if (s.equals("do#m")) return FM_KeySignatureValue.DOsharpm;
        if (s.equals("sol#m")) return FM_KeySignatureValue.SOLsharpm;
        if (s.equals("re#m")) return FM_KeySignatureValue.REsharpm;
        if (s.equals("la#m")) return FM_KeySignatureValue.LAsharpm;
        return FM_KeySignatureValue.DO;
    }

    static void AdjustFont(FM_Score Score, String text, float stave_lines_cnt) {
        if (text.equals("")) {
            Score.Font.setTextSize(10);
            return;
        }
        float height = Score.getDistanceBetweenStaveLines() * stave_lines_cnt + dpTOpx(Score.getContext(), 1);
        Score.Font.setTextSize(100f);
        Rect bounds = new Rect();
        Score.Font.getTextBounds(text, 0, text.length(), bounds);
        Score.Font.setTextSize(100f * height / bounds.height());
    }

    public String ConvertNote(String note, @FM_NotationSystem int NotationSystem){
        note = note.toLowerCase().trim();
        int n = 0;
        if (note.equals("c") || note.equals("do") || note.equals("ha")) n = 1;
        if ((n==0) && (note.equals("d") || note.equals("re") || note.equals("ni"))) n = 2;
        if ((n==0) && (note.equals("e") || note.equals("mi") || note.equals("ho"))) n = 3;
        if ((n==0) && (note.equals("f") || note.equals("fa") || note.equals("he"))) n = 4;
        if ((n==0) && (note.equals("g") || note.equals("sol") || note.equals("to"))) n = 5;
        if ((n==0) && (note.equals("a") || note.equals("la") || note.equals("i"))) n = 6;
        if ((n==0) && (note.equals("b") || note.equals("h") || note.equals("si") || note.equals("ro"))) n = 7;
        if (NotationSystem == FM_NotationSystem.ENGLISH){
            if (n == 1) return "c";
            if (n == 2) return "d";
            if (n == 3) return "e";
            if (n == 4) return "f";
            if (n == 5) return "g";
            if (n == 6) return "a";
            if (n == 7) return "b";
        }
        if (NotationSystem == FM_NotationSystem.GERMAN){
            if (n == 1) return "c";
            if (n == 2) return "d";
            if (n == 3) return "e";
            if (n == 4) return "f";
            if (n == 5) return "g";
            if (n == 6) return "a";
            if (n == 7) return "h";
        }
        if (NotationSystem == FM_NotationSystem.ITALIAN){
            if (n == 1) return "do";
            if (n == 2) return "re";
            if (n == 3) return "mi";
            if (n == 4) return "fa";
            if (n == 5) return "sol";
            if (n == 6) return "la";
            if (n == 7) return "si";
        }
        if (NotationSystem == FM_NotationSystem.JAPANESE){
            if (n == 1) return "ha";
            if (n == 2) return "ni";
            if (n == 3) return "ho";
            if (n == 4) return "he";
            if (n == 5) return "to";
            if (n == 6) return "i";
            if (n == 7) return "ro";
        }
        return "";
    }

    @FM_TimeSignatureValue
    static int getTimeSignature_n(String s){
        int ret = FM_TimeSignatureValue.None;
        if (s.startsWith("2")) ret = FM_TimeSignatureValue._2;
        if (s.startsWith("3")) ret = FM_TimeSignatureValue._3;
        if (s.startsWith("4")) ret = FM_TimeSignatureValue._4;
        if (s.startsWith("5")) ret = FM_TimeSignatureValue._5;
        if (s.startsWith("6")) ret = FM_TimeSignatureValue._6;
        if (s.startsWith("7")) ret = FM_TimeSignatureValue._7;
        if (s.startsWith("8")) ret = FM_TimeSignatureValue._8;
        if (s.startsWith("9")) ret = FM_TimeSignatureValue._9;
        return ret;
    }

    @FM_TimeSignatureValue
    static int getTimeSignature_d(String s){
        int ret = FM_TimeSignatureValue.None;
        if (s.endsWith("2")) ret = FM_TimeSignatureValue._2;
        if (s.endsWith("3")) ret = FM_TimeSignatureValue._3;
        if (s.endsWith("4")) ret = FM_TimeSignatureValue._4;
        if (s.endsWith("5")) ret = FM_TimeSignatureValue._5;
        if (s.endsWith("6")) ret = FM_TimeSignatureValue._6;
        if (s.endsWith("7")) ret = FM_TimeSignatureValue._7;
        if (s.endsWith("8")) ret = FM_TimeSignatureValue._8;
        if (s.endsWith("9")) ret = FM_TimeSignatureValue._9;
        return ret;
    }

    static float getDurationMs(@FM_DurationValue int duration){
        if (duration == FM_DurationValue.NOTE_WHOLE) return 4;
        if (duration == FM_DurationValue.NOTE_WHOLE_D) return 6;
        if (duration == FM_DurationValue.NOTE_HALF) return 2;
        if (duration == FM_DurationValue.NOTE_HALF_D) return 3;
        if (duration == FM_DurationValue.NOTE_QUARTER) return 1;
        if (duration == FM_DurationValue.NOTE_QUARTER_D) return 1.5f;
        if (duration == FM_DurationValue.NOTE_EIGHTH) return 0.5f;
        if (duration == FM_DurationValue.NOTE_EIGHTH_D) return 0.5f + 0.25f;
        if (duration == FM_DurationValue.NOTE_SIXTEENTH) return 0.25f;
        if (duration == FM_DurationValue.NOTE_SIXTEENTH_D) return 0.25f + 1/8f;
        if (duration == FM_DurationValue.NOTE_THIRTY_SECOND) return 1/8f;
        if (duration == FM_DurationValue.NOTE_THIRTY_SECOND_D) return 1/8f + 1/16f;
        return 0;
    }
}