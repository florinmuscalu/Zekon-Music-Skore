package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({FM_NotationSystem.ENGLISH, FM_NotationSystem.GERMAN, FM_NotationSystem.ITALIAN, FM_NotationSystem.JAPANESE, FM_NotationSystem.INDIAN, FM_NotationSystem.KOREAN})
@Retention(RetentionPolicy.SOURCE)
public @interface FM_NotationSystem {
    int ENGLISH = 0;
    int GERMAN = 1;
    int ITALIAN = 2;
    int JAPANESE = 3;
    int INDIAN = 4;
    int KOREAN = 5;
}