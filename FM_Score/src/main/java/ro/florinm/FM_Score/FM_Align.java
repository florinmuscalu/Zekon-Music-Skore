package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({FM_Align.ALIGN_LEFT_NOTES, FM_Align.ALIGN_CENTER_MEASURES, FM_Align.ALIGN_CENTER_NOTES, FM_Align.ALIGN_LEFT_MEASURES, FM_Align.ALIGN_LEFT_LAST_MEASURE, FM_Align.ALIGN_CENTER_NOTES_ALL})
@Retention(RetentionPolicy.SOURCE)
public @interface FM_Align {
    int ALIGN_LEFT_NOTES = 0;
    int ALIGN_CENTER_NOTES = 2;
    int ALIGN_CENTER_MEASURES = 1;
    int ALIGN_LEFT_MEASURES = 3;
    int ALIGN_LEFT_LAST_MEASURE = 4;
    int ALIGN_CENTER_NOTES_ALL = 5;
}