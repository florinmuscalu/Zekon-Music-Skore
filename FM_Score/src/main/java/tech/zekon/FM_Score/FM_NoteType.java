package tech.zekon.FM_Score;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({FM_NoteType.NOTE, FM_NoteType.BAR, FM_NoteType.PAUSE, FM_NoteType.CHORD, FM_NoteType.KEY_SIGNATURE, FM_NoteType.CLEF})
public @interface FM_NoteType {
    int NOTE = 0;
    int BAR = 1;
    int PAUSE = 2;
    int CHORD = 3;
    int KEY_SIGNATURE = 4;
    int CLEF = 5;
}
