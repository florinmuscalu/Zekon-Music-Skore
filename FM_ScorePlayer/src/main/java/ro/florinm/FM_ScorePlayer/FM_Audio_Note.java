package ro.florinm.FM_ScorePlayer;

class FM_Audio_Note {
    String voice;
    String note;
    String duration;
    long playDuration;              //a chord can be composed of notes of different durations. This is the longest duration of all
    long pauseDuration;             //this is the shortest duration of all.
    Boolean legato_start = false;
    Boolean legato_end = false;
    FM_AudioTrack audioT = null;    //for chords, an audio track is composed automatically
    int audioInt = -1;              //for single notes
    boolean NextPause;
}
