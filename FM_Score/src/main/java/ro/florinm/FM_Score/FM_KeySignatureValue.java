package ro.florinm.FM_Score;

import androidx.annotation.IntDef;

@IntDef({FM_KeySignatureValue.DO, FM_KeySignatureValue.FA, FM_KeySignatureValue.SIb, FM_KeySignatureValue.MIb, FM_KeySignatureValue.LAb, FM_KeySignatureValue.REb, FM_KeySignatureValue.SOLb, FM_KeySignatureValue.DOb,
        FM_KeySignatureValue.SOL, FM_KeySignatureValue.RE, FM_KeySignatureValue.LA, FM_KeySignatureValue.MI, FM_KeySignatureValue.SI, FM_KeySignatureValue.FAsharp, FM_KeySignatureValue.DOsharp, FM_KeySignatureValue.LAm,
        FM_KeySignatureValue.REm, FM_KeySignatureValue.SOLm, FM_KeySignatureValue.DOm, FM_KeySignatureValue.FAm, FM_KeySignatureValue.SIbm, FM_KeySignatureValue.MIbm, FM_KeySignatureValue.LAbm, FM_KeySignatureValue.MIm,
        FM_KeySignatureValue.FAsharpm, FM_KeySignatureValue.DOsharpm, FM_KeySignatureValue.SOLsharpm, FM_KeySignatureValue.REsharpm, FM_KeySignatureValue.LAsharpm})
public @interface FM_KeySignatureValue {
    int DO = 0;
    int FA = 1;
    int SIb = 2;
    int MIb = 3;
    int LAb = 4;
    int REb = 5;
    int SOLb = 6;
    int DOb = 7;
    int SOL = 8;
    int RE = 9;
    int LA = 10;
    int MI = 11;
    int SI = 12;
    int FAsharp = 13;
    int DOsharp = 14;
    int LAm = 15;
    int REm = 16;
    int SOLm = 17;
    int DOm = 18;
    int FAm = 19;
    int SIbm = 20;
    int MIbm = 21;
    int LAbm = 22;
    int MIm = 23;
    int SIm = 24;
    int FAsharpm = 25;
    int DOsharpm = 26;
    int SOLsharpm = 27;
    int REsharpm = 28;
    int LAsharpm = 29;
}