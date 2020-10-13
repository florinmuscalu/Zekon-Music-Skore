package ro.florinm.FM_Score;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class FM_Const {
    //notes
    public static final String _1Note = "\ue0a2";
    public static final String _2Note_up = "\ue1d3";
    public static final String _2Note_down = "\ue1d4";
    public static final String _4Note_up = "\ue1d5";
    public static final String _4Note_down = "\ue1d6";
    public static final String _8Note_up = "\ue1d7";
    public static final String _8Note_down = "\ue1d8";
    public static final String _16Note_up = "\ue1d9";
    public static final String _16Note_down = "\ue1da";
    public static final String _32Note_up = "\ue1db";
    public static final String _32Note_down = "\ue1dc";
    public static final String FillNote = "\ue1af";
    public static final String EmptyNote = "\ue1b0";

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

    public static float dpTOpx(Context context, float dp){
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static float spTOpx(Context context, float sp){
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, r.getDisplayMetrics());
    }

    public static int distanceBetweenNotes(FM_Note n1, FM_Note n2){
        if (!n1.clef.equals(n2.clef)) return 10;
        return (n1.Note - n2.Note) + ((n1.Octave - n2.Octave) * 7);
    }

    public static float slope(float x1, float y1, float x2, float y2){
        return (y2 - y1) / (x2 - x1);
    }

    public static float getY2(float slope, float x1, float y1, float x2){
        return y1 + slope * (x2 - x1);
    }

    public static int keyToNote(String key){
        key = key.replace("\\","").replace("\"","").replace("[","").replace("]","").toLowerCase().trim();
        if (key.startsWith("c")) return FM_NoteValue.DO;
        if (key.startsWith("d")) return FM_NoteValue.RE;
        if (key.startsWith("e")) return FM_NoteValue.MI;
        if (key.startsWith("f")) return FM_NoteValue.FA;
        if (key.startsWith("g")) return FM_NoteValue.SOL;
        if (key.startsWith("a")) return FM_NoteValue.LA;
        if (key.startsWith("b")) return FM_NoteValue.SI;
        return FM_NoteValue.DO;
    }

    public static int keyToOctave(String key){
        key = key.replace("\\","").replace("\"","").replace("[","").replace("]","").toLowerCase().trim();
        return Integer.parseInt(key.substring(key.length()-1));
    }
}