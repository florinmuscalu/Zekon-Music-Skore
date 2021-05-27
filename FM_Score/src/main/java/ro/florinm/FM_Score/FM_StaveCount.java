package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({FM_StaveCount._1, FM_StaveCount._2})
@Retention(RetentionPolicy.SOURCE)
public @interface FM_StaveCount {
    int _1 = 1;
    int _2 = 2;
}
