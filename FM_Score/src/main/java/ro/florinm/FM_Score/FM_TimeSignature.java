package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

@IntDef({FM_TimeSignature.None, FM_TimeSignature._2_4, FM_TimeSignature._3_4, FM_TimeSignature._4_4, FM_TimeSignature._3_2})
public @interface FM_TimeSignature {
    int None = 0;
    int _2_4 = 1;
    int _3_4 = 2;
    int _4_4 = 3;
    int _3_2 = 4;
}
