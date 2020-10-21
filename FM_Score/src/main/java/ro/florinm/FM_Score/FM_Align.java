package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

@IntDef({FM_Align.ALIGN_LEFT_NOTES, FM_Align.ALIGN_CENTER_MEASURES, FM_Align.ALIGN_CENTER_NOTES, FM_Align.ALIGN_LEFT_MEASURES})
public @interface FM_Align {
    int ALIGN_LEFT_NOTES = 0;
    int ALIGN_CENTER_NOTES = 2;
    int ALIGN_CENTER_MEASURES = 1;
    int ALIGN_LEFT_MEASURES = 3;
}