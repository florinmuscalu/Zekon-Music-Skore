package tech.zekon.FM_Score;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({FM_NotationSystem.ENGLISH, FM_NotationSystem.GERMAN, FM_NotationSystem.ITALIAN, FM_NotationSystem.JAPANESE, FM_NotationSystem.INDIAN, FM_NotationSystem.KOREAN})
@Retention(RetentionPolicy.SOURCE)
public @interface FM_NotationSystem {
    int NONE = 0;
    int ENGLISH = 1;
    int GERMAN = 2;
    int ITALIAN = 3;
    int JAPANESE = 4;
    int INDIAN = 5;
    int KOREAN = 6;
    int CYRILLIC = 7;
}