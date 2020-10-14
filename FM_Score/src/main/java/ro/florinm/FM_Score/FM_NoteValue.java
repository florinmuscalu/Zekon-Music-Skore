package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

@IntDef({FM_NoteValue.DO, FM_NoteValue.RE, FM_NoteValue.MI, FM_NoteValue.FA, FM_NoteValue.SOL, FM_NoteValue.LA, FM_NoteValue.SI})
public @interface FM_NoteValue {
    int DO = 0;
    int RE = 1;
    int MI = 2;
    int FA = 3;
    int SOL= 4;
    int LA = 5;
    int SI = 6;
}
