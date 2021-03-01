package ro.florinm.FM_Score;

import java.util.ArrayList;
import java.util.List;

class FM_Audio_Note {
    long playDurationInTie;              //a chord can be composed of notes of different durations. This is the longest duration of all
    long playDurationOutsideTie;         //a chord can be composed of notes of different durations. This is the longest duration of all
    long pauseDuration;             //this is the shortest duration of all.
    Boolean legato = false;
    FM_AudioTrack audioTrackInLegato = null;        //for chords, an audio track is composed automatically
    FM_AudioTrack audioTrackOutsideLegato = null;   //for chords, an audio track is composed automatically
    int audioIntInLegato = -1;              //for single notes
    int audioIntOutsideLegato = -1;              //for single notes
    boolean NextPause;
}

class FM_Temp_Audio_Note {
    int measure;
    List<FM_BaseNote> notes = new ArrayList<>();
}
