package tech.zekon.FM_Score;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({FM_BoundingBoxType.None, FM_BoundingBoxType.Note, FM_BoundingBoxType.Chord})
@Retention(RetentionPolicy.SOURCE)
public @interface FM_BoundingBoxType {
    int None = 0;
    int Note = 1;
    int Chord = 2;
}
