package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

@IntDef({FM_Accidental.None, FM_Accidental.Natural, FM_Accidental.Flat, FM_Accidental.Sharp, FM_Accidental.DoubleSharp, FM_Accidental.DoubleFlat, FM_Accidental.TripleSharp, FM_Accidental.TripleFlat, FM_Accidental.Courtesy})
public @interface FM_Accidental {
    int None = 0;
    int Natural = 1;
    int Flat = 2;
    int Sharp = 3;
    int DoubleSharp = 4;
    int DoubleFlat = 5;
    int TripleSharp = 6;
    int TripleFlat = 7;
    int Courtesy = 100;
}
