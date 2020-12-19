package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

@IntDef({FM_TimeSignatureValue.None, FM_TimeSignatureValue._2_4, FM_TimeSignatureValue._3_4, FM_TimeSignatureValue._4_4, FM_TimeSignatureValue._3_2, FM_TimeSignatureValue._3_8, FM_TimeSignatureValue._6_8})
public @interface FM_TimeSignatureValue {
    int None = 0;
    int _2_4 = 24;
    int _3_4 = 34;
    int _4_4 = 44;
    int _3_2 = 32;
    int _3_8 = 38;
    int _6_8 = 68;
}
