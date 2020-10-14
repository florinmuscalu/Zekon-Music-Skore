package ro.florinm.FM_Score;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

class FM_KeySignature extends FM_BaseNote {
    private final Context context;
    @FM_KeySignatureValue
    private final int value;
    @FM_ClefValue
    private final int clef;

    List<Float> displacement = new ArrayList<>();
    List<Integer> accidental = new ArrayList<>();

    public FM_KeySignature(Context context, @FM_ClefValue int clef, @FM_KeySignatureValue int key, int Color, FM_Score Stave) {
        super(FM_NoteType.KEY_SIGNATURE, Stave);
        this.context = context;
        this.value = key;
        this.clef = clef;
        this.Color = Color;
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


    public float getDisplacement() {
        return 0.0f;
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < accidental.size(); i++) {
            if (accidental.get(i) == FM_Accidental.Sharp) s = s + FM_Const.Sharp;
            if (accidental.get(i) == FM_Accidental.Flat) s = s + FM_Const.Flat;
        }
        return s;
    }

    @Override
    public float WidthAll(Paint font, boolean all) {
        return WidthAll(font);
    }

    public float WidthAll(Paint font) {
        FM_Const.AdjustFont(font, toString(), Stave.getDistanceBetweenStaveLines() * 2);
        return padding + font.measureText(toString()) + (accidental.size() - 1) * FM_Const.dpTOpx(context, 2) + paddingExtra + FM_Const.spTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING);
    }

    public float WidthAccidental(Paint font) {
        return 0;
    }

    public float WidthAllNoDot(Paint font) {
        return WidthAll(font);
    }
    public float WidthNote(Paint font) {
        return 0;
    }

    public void DrawNote(Canvas canvas) {
        if (!isVisible()) return;
        Stave.StaveFont.setColor(Color);
        float dx = StartX;
        for (int i = 0; i < accidental.size(); i++) {
            float offset = displacement.get(i);
            float dy = StartY1 + offset * Stave.getDistanceBetweenStaveLines();

            FM_Const.AdjustFont(Stave.StaveFont, toString(), Stave.getDistanceBetweenStaveLines() * 2);
            if (accidental.get(i) == FM_Accidental.Sharp) {
                canvas.drawText(FM_Const.Sharp, dx, dy, Stave.StaveFont);
                dx = dx + Stave.StaveFont.measureText(FM_Const.Sharp)+ + FM_Const.dpTOpx(context, 2);
            }
            if (accidental.get(i) == FM_Accidental.Flat) {
                canvas.drawText(FM_Const.Flat, dx, dy, Stave.StaveFont);
                dx = dx + Stave.StaveFont.measureText(FM_Const.Flat) + FM_Const.dpTOpx(context, 2);
            }
        }
    }

}
