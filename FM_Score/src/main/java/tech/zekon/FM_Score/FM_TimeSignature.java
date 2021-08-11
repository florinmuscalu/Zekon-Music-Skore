package tech.zekon.FM_Score;

import android.graphics.Canvas;

class FM_TimeSignature extends FM_BaseNote {
    @FM_TimeSignatureValue int timeSigN;
    @FM_TimeSignatureValue int timeSigD;

    FM_TimeSignature(@FM_TimeSignatureValue int timeSig_n, @FM_TimeSignatureValue int timeSig_d, FM_ScoreBase Score) {
        super(FM_NoteType.KEY_SIGNATURE, Score);
        this.timeSigN = timeSig_n;
        this.timeSigD = timeSig_d;
    }

    float getDisplacement() {
        return 0.0f;
    }

    String asString() {
        return FM_Const._4;
    }

    protected float WidthAccidental(){
        return 0;
    }
    protected float WidthNoteNoStem() {
        if (score.score == null) return 0;
        String s = asString();
        if (s.equals("")) return 0;
        FM_Const.AdjustFont(score.score, asString(), 2);
        return score.score.Font.measureText(asString());
    }
    protected float WidthNote() {
        return WidthNoDotNoStem();
    }
    protected  float WidthDot(){
        return 0;
    }

    void DrawNote(Canvas canvas) {
        if (score.score == null) return;
        if (!isVisible()) return;
        super.DrawNote(canvas);

        score.score.Font.setColor(color);
        float dx = StartX + getPaddingLeft();
        float y = StartY1;
        FM_Const.AdjustFont(score.score, FM_Const._4, 2);

        if (timeSigN == FM_TimeSignatureValue._2) canvas.drawText(FM_Const._2, dx, y + 1 * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        if (timeSigN == FM_TimeSignatureValue._3) canvas.drawText(FM_Const._3, dx, y + 1 * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        if (timeSigN == FM_TimeSignatureValue._4) canvas.drawText(FM_Const._4, dx, y + 1 * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        if (timeSigN == FM_TimeSignatureValue._5) canvas.drawText(FM_Const._5, dx, y + 1 * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        if (timeSigN == FM_TimeSignatureValue._6) canvas.drawText(FM_Const._6, dx, y + 1 * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        if (timeSigN == FM_TimeSignatureValue._7) canvas.drawText(FM_Const._7, dx, y + 1 * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        if (timeSigN == FM_TimeSignatureValue._8) canvas.drawText(FM_Const._8, dx, y + 1 * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        if (timeSigN == FM_TimeSignatureValue._9) canvas.drawText(FM_Const._9, dx, y + 1 * score.score.getDistanceBetweenStaveLines(), score.score.Font);

        if (timeSigD == FM_TimeSignatureValue._2) canvas.drawText(FM_Const._2, dx, y + 3 * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        if (timeSigD == FM_TimeSignatureValue._3) canvas.drawText(FM_Const._3, dx, y + 3 * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        if (timeSigD == FM_TimeSignatureValue._4) canvas.drawText(FM_Const._4, dx, y + 3 * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        if (timeSigD == FM_TimeSignatureValue._5) canvas.drawText(FM_Const._5, dx, y + 3 * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        if (timeSigD == FM_TimeSignatureValue._6) canvas.drawText(FM_Const._6, dx, y + 3 * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        if (timeSigD == FM_TimeSignatureValue._7) canvas.drawText(FM_Const._7, dx, y + 3 * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        if (timeSigD == FM_TimeSignatureValue._8) canvas.drawText(FM_Const._8, dx, y + 3 * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        if (timeSigD == FM_TimeSignatureValue._9) canvas.drawText(FM_Const._9, dx, y + 3 * score.score.getDistanceBetweenStaveLines(), score.score.Font);
    }

    float Left(){
        return StartX + paddingLeft;
    }
    float Bottom() {
        return StartY1 + 4 * score.score.getDistanceBetweenStaveLines();
    }
    float Right() {
        return StartX + Width();
    }
    float Top(){
        return StartY1;
    }

    private float Height() {
        return 4 * score.score.getDistanceBetweenStaveLines();
    }
}
