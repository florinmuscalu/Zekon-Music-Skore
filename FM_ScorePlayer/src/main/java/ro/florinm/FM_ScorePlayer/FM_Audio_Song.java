package ro.florinm.FM_ScorePlayer;

import java.util.ArrayList;
import java.util.List;

public class FM_Audio_Song {
    boolean prepared = false;
    boolean harmonic = false;
    List<FM_Audio_Measure> measures = new ArrayList<>();
    String keysignature;
}
