package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

@IntDef({FM_ClefValue.TREBLE, FM_ClefValue.BASS})
public @interface FM_ClefValue {
    int TREBLE = 0;
    int BASS = 1;
}
