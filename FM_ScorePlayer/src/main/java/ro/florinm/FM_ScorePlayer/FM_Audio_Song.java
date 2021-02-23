package ro.florinm.FM_ScorePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

class FM_Audio_Song {
    CountDownLatch prepared;
    boolean harmonic = false;
    List<FM_Audio_Measure> measures = new ArrayList<>();
    String keySignature;
}
