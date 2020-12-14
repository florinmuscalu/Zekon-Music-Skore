package ro.florinm.FM_Score;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

class FM_TimeSignature extends FM_BaseNote {
    @FM_TimeSignatureValue int timesig;

    public FM_TimeSignature(Context context, @FM_TimeSignatureValue int timeSig, FM_Score Score) {
        super(FM_NoteType.KEY_SIGNATURE, Score);
        this.timesig = timeSig;
    }

    public float getDisplacement() {
        return 0.0f;
    }

    public String asString() {
        return FM_Const._4;
    }

    protected float WidthAccidental(){
        return 0;
    }
    protected float WidthNoteNoStem() {
        String s = asString();
        if (s.equals("")) return 0;
        FM_Const.AdjustFont(score, asString(), 2);
        return score.Font.measureText(asString());
    }
    protected float WidthNote() {
        return WidthNoDotNoStem();
    }
    protected  float WidthDot(){
        return 0;
    }

    public void DrawNote(Canvas canvas) {
        if (!isVisible()) return;
        super.DrawNote(canvas);

        score.Font.setColor(color);
        float dx = StartX + getPaddingLeft();
        float y = StartY1;
        FM_Const.AdjustFont(this.score, FM_Const._4, 2);
        if (timesig == FM_TimeSignatureValue._4_4) {
            canvas.drawText(FM_Const._4, dx, y + 1 * score.getDistanceBetweenStaveLines(), score.Font);
            canvas.drawText(FM_Const._4, dx, y + 3 * score.getDistanceBetweenStaveLines(), score.Font);
        }
        if (timesig == FM_TimeSignatureValue._2_4) {
            canvas.drawText(FM_Const._2, dx, y + 1 * score.getDistanceBetweenStaveLines(), score.Font);
            canvas.drawText(FM_Const._4, dx, y + 3 * score.getDistanceBetweenStaveLines(), score.Font);
        }
        if (timesig == FM_TimeSignatureValue._3_4) {
            canvas.drawText(FM_Const._3, dx, y + 1 * score.getDistanceBetweenStaveLines(), score.Font);
            canvas.drawText(FM_Const._4, dx, y + 3 * score.getDistanceBetweenStaveLines(), score.Font);
        }
        if (timesig == FM_TimeSignatureValue._3_2) {
            canvas.drawText(FM_Const._3, dx, y + 1 * score.getDistanceBetweenStaveLines(), score.Font);
            canvas.drawText(FM_Const._2, dx, y + 3 * score.getDistanceBetweenStaveLines(), score.Font);
        }
        if (timesig == FM_TimeSignatureValue._3_8) {
            canvas.drawText(FM_Const._3, dx, y + 1 * score.getDistanceBetweenStaveLines(), score.Font);
            canvas.drawText(FM_Const._8, dx, y + 3 * score.getDistanceBetweenStaveLines(), score.Font);
        }
    }

    public float Left(){
        return StartX + paddingLeft;
    };
    public float Bottom() {
        return StartY1 + 4 * score.getDistanceBetweenStaveLines();
    }
    public float Right() {
        return StartX + Width();
    }
    public float Top(){
        return StartY1;
    }

    private float Height() {
        return 4 * score.getDistanceBetweenStaveLines();
    }
}
