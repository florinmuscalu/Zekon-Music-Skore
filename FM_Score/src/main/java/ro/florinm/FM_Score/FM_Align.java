package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

@IntDef({FM_Align.ALIGN_LEFT_NOTES, FM_Align.ALIGN_LEFT_MEASURES, FM_Align.CENTER})
public @interface FM_Align {
    int ALIGN_LEFT_NOTES = 0;
    int ALIGN_LEFT_MEASURES = 1;
    int CENTER = 2;
}