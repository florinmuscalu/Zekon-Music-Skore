package tech.zekon.FM_Score;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({FM_TimeSignatureValue.None, FM_TimeSignatureValue._2, FM_TimeSignatureValue._3, FM_TimeSignatureValue._4, FM_TimeSignatureValue._5, FM_TimeSignatureValue._6, FM_TimeSignatureValue._7, FM_TimeSignatureValue._8, FM_TimeSignatureValue._9})
public @interface FM_TimeSignatureValue {
    int None = 0;
    int _2 = 2;
    int _3 = 3;
    int _4 = 4;
    int _5 = 5;
    int _6 = 6;
    int _7 = 7;
    int _8 = 8;
    int _9 = 9;
}
