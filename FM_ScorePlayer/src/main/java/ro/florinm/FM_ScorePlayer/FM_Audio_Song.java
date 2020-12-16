package ro.florinm.FM_ScorePlayer;

import java.util.ArrayList;
import java.util.List;

public class FM_Audio_Song {
    public boolean prepared = false;
    public boolean harmonic = false;
    public List<FM_Audio_Measure> measures = new ArrayList<>();
    public String keysignature;
}
