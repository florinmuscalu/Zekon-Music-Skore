package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

@IntDef({FM_NotationSystem.ENGLISH, FM_NotationSystem.GERMAN, FM_NotationSystem.ITALIAN, FM_NotationSystem.JAPANESE})
public @interface FM_NotationSystem {
    int ENGLISH = 0;
    int GERMAN = 1;
    int ITALIAN = 2;
    int JAPANESE = 3;
}