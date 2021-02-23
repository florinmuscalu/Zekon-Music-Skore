package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

@IntDef({FM_DurationValue.NOTE_WHOLE, FM_DurationValue.NOTE_HALF, FM_DurationValue.NOTE_QUARTER, FM_DurationValue.NOTE_EIGHTH, FM_DurationValue.NOTE_SIXTEENTH, FM_DurationValue.NOTE_THIRTY_SECOND,
FM_DurationValue.NOTE_WHOLE_D, FM_DurationValue.NOTE_HALF_D, FM_DurationValue.NOTE_QUARTER_D, FM_DurationValue.NOTE_EIGHTH_D, FM_DurationValue.NOTE_SIXTEENTH_D, FM_DurationValue.NOTE_THIRTY_SECOND_D})
public @interface FM_DurationValue {
    int NOTE_WHOLE = 1;
    int NOTE_WHOLE_D = 51;
    int NOTE_HALF = 2;
    int NOTE_HALF_D = 52;
    int NOTE_QUARTER = 4;
    int NOTE_QUARTER_D = 54;
    int NOTE_EIGHTH = 8;
    int NOTE_EIGHTH_D = 58;
    int NOTE_SIXTEENTH = 16;
    int NOTE_SIXTEENTH_D = 66;
    int NOTE_THIRTY_SECOND = 32;
    int NOTE_THIRTY_SECOND_D = 82;
}
