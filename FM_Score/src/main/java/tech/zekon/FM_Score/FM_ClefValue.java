package tech.zekon.FM_Score;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({FM_ClefValue.TREBLE, FM_ClefValue.BASS})
@Retention(RetentionPolicy.SOURCE)
public @interface FM_ClefValue {
    int TREBLE = 0;
    int BASS = 1;
}
