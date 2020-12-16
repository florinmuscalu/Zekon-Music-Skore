package ro.florinm.FM_ScorePlayer;

public class FM_Audio_Note {
    String note;
    String duration;
    String playDuration;
    String pauseDuration;
    Boolean legato_start = false;
    Boolean legato_end = false;
    FM_AudioTrack audioT = null;
    int audioInt = -1;
    boolean NextPause;
}
