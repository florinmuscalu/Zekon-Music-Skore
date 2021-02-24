package ro.florinm.FM_ScorePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import ro.florinm.FM_Score.FM_KeySignatureValue;

public class FM_Audio_Song {
    public List<FM_Audio_Measure> measures = new ArrayList<>();
    @FM_KeySignatureValue
    int keySignature;
}
