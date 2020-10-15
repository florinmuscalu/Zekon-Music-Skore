package ro.florinm.FM_Score;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.TypedValue;

public class FM_Const {
    public static final int DEFAULT_EXTRA_PADDING = 3;
    //notes
    public static final String _1Note = "\ue0a2";
    public static final String _2Note_up = "\ue1d3";
    public static final String _2Note_down = "\ue1d4";
    public static final String _4Note_up = "\ue1D5";
    public static final String _4Note_down = "\ue1d6";
    public static final String _8Note_up = "\ue1d7";
    public static final String _8Note_down = "\ue1d8";
    public static final String _16Note_up = "\ue1d9";
    public static final String _16Note_down = "\ue1da";
    public static final String _32Note_up = "\ue1db";
    public static final String _32Note_down = "\ue1dc";
    public static final String FillNote = "\ue1af";
    public static final String EmptyNote = "\ue1b0";
    //public static final String FillNote = "\ue0a4";
    //public static final String EmptyNote = "\ue0a3";

    //clefs
    public static final String TrebleClef = "\uD834\uDD1E";
    public static final String BassClef = "\uD834\uDD22";

    //brackets
    public static final String Bracket = "\ue000";

    //numbers
    public static final String _2 = "\ue082";
    public static final String _3 = "\ue083";
    public static final String _4 = "\ue084";
    public static final String _5 = "\ue085";

    public static final String _2_b = "\ue927";
    public static final String _3_b = "\ue928";
    public static final String _4_b = "\ue929";

    public static final String _3_small = "\uea54";
    public static final String _5_small = "\uea57";

    //accidentals
    public static final String Flat = "\ue260";
    public static final String Natural = "\ue261";
    public static final String Sharp = "\ue262";
    public static final String DoubleSharp = "\ue263";
    public static final String DoubleFlat = "\ue264";
    //public static final String TripleSharp = "\ue265";
    public static final String TripleSharp = "\ue262 \ue263";

    public static final String TripleFlat = "\ue266";
    public static final String Dot = "\ue1e7";

    //pauses
    public static final String Pause_1 = "\ue4e3";
    public static final String Pause_2 = "\ue4e4";
    public static final String Pause_4 = "\ue4e5";
    public static final String Pause_8 = "\ue4e6";
    public static final String Pause_16 = "\ue4e7";
    public static final String Pause_32 = "\ue4e8";

    //Tie
    public static final String Tie = "\ue551";

    //Tuplet

    public static float dpTOpx(Context context, float dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static int distanceBetweenNotes(FM_Note n1, FM_Note n2) {
        if (!n1.clef.equals(n2.clef)) return 10;
        return (n1.note - n2.note) + ((n1.octave - n2.octave) * 7);
    }

    public static float slope(float x1, float y1, float x2, float y2) {
        return (y2 - y1) / (x2 - x1);
    }

    public static float getY2(float slope, float x1, float y1, float x2) {
        return y1 + slope * (x2 - x1);
    }

    public static int keyToNote(String key) {
        return keyToNote(key, 0);
    }

    public static int keyCount(String key) {
        key = key.replace("\\", "").replace("\"", "").replace("[", "").replace("]", "").toLowerCase().trim();
        String[] s = key.split(",");
        return s.length;
    }

    public static int keyToNote(String key, int pos) {
        key = key.replace("\\", "").replace("\"", "").replace("[", "").replace("]", "").toLowerCase().trim();
        String[] s = key.split(",");
        key = s[pos];
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
        return FM_NoteValue.DO;
    }

    public static int keyToOctave(String key) {
        return keyToOctave(key, 0);
    }

    public static int keyToOctave(String key, int pos) {
        key = key.replace("\\", "").replace("\"", "").replace("[", "").replace("]", "").toLowerCase().trim();
        String[] s = key.split(",");
        key = s[pos];
        return Integer.parseInt(key.substring(key.length() - 1));
    }

    public static int keyToAccidental(String key, int pos) {
        key = key.replace("\\", "").replace("\"", "").replace("[", "").replace("]", "").toLowerCase().trim();
        String[] s = key.split(",");
        key = s[pos].substring(1);
        if (key.contains("###")) return FM_Accidental.TripleSharp;
        if (key.contains("##")) return FM_Accidental.DoubleSharp;
        if (key.contains("#")) return FM_Accidental.Sharp;
        if (key.contains("bbb")) return FM_Accidental.TripleFlat;
        if (key.contains("bb")) return FM_Accidental.DoubleFlat;
        if (key.contains("b")) return FM_Accidental.Flat;
        if (key.contains("n")) return FM_Accidental.Natural;
        return FM_Accidental.None;
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

    static void AdjustFont(FM_Score Score, String text, int stave_lines_cnt) {
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
}