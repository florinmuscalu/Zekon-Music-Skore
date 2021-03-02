package ro.florinm.FM_Score;

import java.util.ArrayList;
import java.util.List;

public class FM_Audio_Song {
    public List<FM_Audio_Measure> measures = new ArrayList<>();
    @FM_KeySignatureValue
    int keySignature;
}

class FM_Temp_Audio_Song {
    List<FM_Temp_Audio_Note> chords = new ArrayList<>();
    @FM_KeySignatureValue
    int keySignature;
    int timeSignature_d;
}
