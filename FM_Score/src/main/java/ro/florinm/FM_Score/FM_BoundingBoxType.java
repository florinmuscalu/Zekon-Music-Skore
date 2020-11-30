package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

@IntDef({FM_BoundingBoxType.None, FM_BoundingBoxType.Note, FM_BoundingBoxType.Chord})
public @interface FM_BoundingBoxType {
    int None = 0;
    int Note = 1;
    int Chord = 2;
}
