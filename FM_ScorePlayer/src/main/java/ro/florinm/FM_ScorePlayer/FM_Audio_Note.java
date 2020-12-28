package ro.florinm.FM_ScorePlayer;

public class FM_Audio_Note {
    public String voice;
    public String note;
    public String duration;
    public long playDuration;
    public long pauseDuration;
    public Boolean legato_start = false;
    public Boolean legato_end = false;
    public FM_AudioTrack audioT = null;
    public int audioInt = -1;
    public boolean NextPause;
}
