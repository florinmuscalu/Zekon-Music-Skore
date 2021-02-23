package ro.florinm.FM_Score;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

class FM_KeySignature extends FM_BaseNote {
    @FM_KeySignatureValue
    private final int value;
    @FM_ClefValue
    private final int clef;

    List<Float> displacement = new ArrayList<>();
    List<Integer> accidental = new ArrayList<>();

    FM_KeySignature(Context context, @FM_ClefValue int clef, @FM_KeySignatureValue int key, FM_Score Score) {
        super(FM_NoteType.KEY_SIGNATURE, Score);
        this.value = key;
        this.clef = clef;
        Build();
    }

    private void Build() {
        accidental.clear();
        displacement.clear();
        int cntSharp = 0;
        int cntFlat = 0;
        if (value == FM_KeySignatureValue.DO || value == FM_KeySignatureValue.LAm) return;
        if (value == FM_KeySignatureValue.SOL || value == FM_KeySignatureValue.MIm) cntSharp = 1;
        if (value == FM_KeySignatureValue.RE || value == FM_KeySignatureValue.SIm) cntSharp = 2;
        if (value == FM_KeySignatureValue.LA || value == FM_KeySignatureValue.FAsharpm) cntSharp = 3;
        if (value == FM_KeySignatureValue.MI || value == FM_KeySignatureValue.DOsharpm) cntSharp = 4;
        if (value == FM_KeySignatureValue.SI || value == FM_KeySignatureValue.SOLsharpm) cntSharp = 5;
        if (value == FM_KeySignatureValue.FAsharp || value == FM_KeySignatureValue.REsharpm) cntSharp = 6;
        if (value == FM_KeySignatureValue.DOsharp || value == FM_KeySignatureValue.LAsharpm) cntSharp = 7;

        if (value == FM_KeySignatureValue.FA || value == FM_KeySignatureValue.REm) cntFlat = 1;
        if (value == FM_KeySignatureValue.SIb || value == FM_KeySignatureValue.SOLm) cntFlat = 2;
        if (value == FM_KeySignatureValue.MIb || value == FM_KeySignatureValue.DOm) cntFlat = 3;
        if (value == FM_KeySignatureValue.LAb || value == FM_KeySignatureValue.FAm) cntFlat = 4;
        if (value == FM_KeySignatureValue.REb || value == FM_KeySignatureValue.SIbm) cntFlat = 5;
        if (value == FM_KeySignatureValue.SOLb || value == FM_KeySignatureValue.MIbm) cntFlat = 6;
        if (value == FM_KeySignatureValue.DOb || value == FM_KeySignatureValue.LAbm) cntFlat = 7;

        for (int i = 0; i < cntSharp; i++) accidental.add(FM_Accidental.Sharp);
        for (int i = 0; i < cntFlat; i++) accidental.add(FM_Accidental.Flat);

        if (cntSharp >= cntFlat) {
            if (clef == FM_ClefValue.TREBLE) {
                displacement.add(0.0f);
                displacement.add(1.5f);
                displacement.add(-0.5f);
                displacement.add(1.0f);
                displacement.add(2.5f);
                displacement.add(0.5f);
                displacement.add(2.0f);
            }
            if (clef == FM_ClefValue.BASS) {
                displacement.add(1 + 0.0f);
                displacement.add(1 + 1.5f);
                displacement.add(1 - 0.5f);
                displacement.add(1 + 1.0f);
                displacement.add(1 + 2.5f);
                displacement.add(1 + 0.5f);
                displacement.add(1 + 2.0f);
            }
        } else {
            if (clef == FM_ClefValue.TREBLE) {
                displacement.add( 2.0f);
                displacement.add( 0.5f);
                displacement.add( 2.5f);
                displacement.add( 1.0f);
                displacement.add( 3.0f);
                displacement.add( 1.5f);
                displacement.add( 3.5f);
            }
            if (clef == FM_ClefValue.BASS) {
                displacement.add(1 + 2.0f);
                displacement.add(1 + 0.5f);
                displacement.add(1 + 2.5f);
                displacement.add(1 + 1.0f);
                displacement.add(1 + 3.0f);
                displacement.add(1 + 1.5f);
                displacement.add(1 + 3.5f);
            }
        }
    }


    float getDisplacement() {
        return 0.0f;
    }

    String asString() {
        String s = "";
        for (int i = 0; i < accidental.size(); i++) {
            if (accidental.get(i) == FM_Accidental.Sharp) s = s + FM_Const.Sharp;
            if (accidental.get(i) == FM_Accidental.Flat) s = s + FM_Const.Flat;
        }
        return s;
    }

    protected float WidthAccidental(){
        return 0;
    }
    protected float WidthNoteNoStem() {
        String s = asString();
        if (s.equals("")) return 0;
        FM_Const.AdjustFont(score, asString(), 2);
        return score.Font.measureText(asString()) + (accidental.size() - 1) * FM_Const.dpTOpx(score.getContext(), 2);
    }
    protected float WidthNote() {
        return WidthNoDotNoStem();
    }
    protected  float WidthDot(){
        return 0;
    }

    void DrawNote(Canvas canvas) {
        if (!isVisible()) return;
        super.DrawNote(canvas);

        float dx = StartX + getPaddingLeft();
        for (int i = 0; i < accidental.size(); i++) {
            float offset = displacement.get(i);
            float dy = StartY1 + offset * score.getDistanceBetweenStaveLines();

            FM_Const.AdjustFont(score, asString(), 2);
            if (accidental.get(i) == FM_Accidental.Sharp) {
                canvas.drawText(FM_Const.Sharp, dx, dy, score.Font);
                dx = dx + score.Font.measureText(FM_Const.Sharp) + FM_Const.dpTOpx(score.getContext(), 2);
            }
            if (accidental.get(i) == FM_Accidental.Flat) {
                canvas.drawText(FM_Const.Flat, dx, dy, score.Font);
                dx = dx + score.Font.measureText(FM_Const.Flat) + FM_Const.dpTOpx(score.getContext(), 2);
            }
        }
    }

    float Left(){
        return StartX + paddingLeft;
    };
    float Bottom() {
        float dy = 0;
        float maxdy = -100000;
        for (int i = 0; i < accidental.size(); i++) {
            float offset = displacement.get(i);
            dy = StartY1 + offset * score.getDistanceBetweenStaveLines();
            if (dy > maxdy) maxdy = dy;
        }
        return maxdy + Height();
    }
    float Right() {
        return StartX + Width();
    }
    float Top(){
        float mindy = Bottom();
        float dy = 0;
        for (int i = 0; i < accidental.size(); i++) {
            float offset = displacement.get(i);
            dy = StartY1 + offset * score.getDistanceBetweenStaveLines();
            if (dy < mindy) mindy = dy;
        }
        return mindy - Height();
    }

    private float Height() {
        FM_Const.AdjustFont(score, asString(), 1);
        Rect bounds = new Rect();
        String s = asString();
        score.Font.getTextBounds(s, 0, s.length(), bounds);
        return bounds.height();
    }
}
