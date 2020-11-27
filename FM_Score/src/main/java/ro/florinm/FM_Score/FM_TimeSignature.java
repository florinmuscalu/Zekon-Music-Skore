package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

@IntDef({FM_TimeSignature.None, FM_TimeSignature._2_4, FM_TimeSignature._3_4, FM_TimeSignature._4_4, FM_TimeSignature._3_2, FM_TimeSignature._3_8})
public @interface FM_TimeSignature {
    int None = 0;
    int _2_4 = 24;
    int _3_4 = 34;
    int _4_4 = 44;
    int _3_2 = 32;
    int _3_8 = 38;
}
