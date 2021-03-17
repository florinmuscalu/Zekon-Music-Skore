package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

@IntDef({FM_KeyType.Bar, FM_KeyType.Clef, FM_KeyType.Key})
@interface FM_KeyType {
    int Bar = 0;
    int Clef = 1;
    int Key = 2;
}


public class FM_BaseKey {
    @FM_KeyType int type;
}

class FM_KeyKey extends FM_BaseKey {
    @FM_NoteValue int note;
    @FM_Accidental int accidental;
    int octave;
    @FM_DurationValue int duration;
    boolean stemUp;
    String tie;
    String beam;
    String tuple;
    int stave;          //0 or 1
    int voice;          //0, 1, 2, 3...
    int chord;
}

class FM_KeyBar extends FM_BaseKey {
    FM_KeyBar() {
        type = FM_KeyType.Bar;
    }
}

class FM_KeyClef extends FM_BaseKey {
    @FM_ClefValue int clef;
    int chord;
    FM_KeyClef(@FM_ClefValue int clef, int chord){
        this.type = FM_KeyType.Clef;
        this.clef = clef;
        this.chord = chord;
    }
}