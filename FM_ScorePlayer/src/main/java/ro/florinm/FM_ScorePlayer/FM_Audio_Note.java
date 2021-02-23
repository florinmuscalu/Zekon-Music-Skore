package ro.florinm.FM_ScorePlayer;

class FM_Audio_Note {
    String voice;
    String note;
    String duration;
    long playDuration;
    long pauseDuration;
    Boolean legato_start = false;
    Boolean legato_end = false;
    FM_AudioTrack audioT = null;
    int audioInt = -1;
    boolean NextPause;
}
