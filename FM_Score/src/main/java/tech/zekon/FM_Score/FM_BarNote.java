package tech.zekon.FM_Score;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;

public class FM_BarNote extends FM_BaseNote {
    protected boolean lineEnd = false;        //set it to true if it is a line-end bar
    public FM_BarNote(FM_ScoreBase score) {
        super(FM_NoteType.BAR, score);
    }
    public FM_BarNote(FM_ScoreBase score, int Color) {
        super(FM_NoteType.BAR, score);
        this.color = Color;

    }

    float getDisplacement() {
        return 0.0f;
    }
    String asString() {
        return "";
    }
    protected float WidthAccidental(){
        return 0;
    }
    @Override
    protected float WidthNoteNoStem() {
        if (score != null)
            return FM_Const.dpTOpx(score.score.getContext(),1);
        else return 0;
    }
    @Override
    protected float WidthNote(){
        return WidthNoteNoStem();
    }
    protected float WidthDot() {
        return 0;
    }
    void DrawNote(Canvas canvas) {
        if (!isBlurred() && !isVisible()) return;
        super.DrawNote(canvas);
        if (score == null) return;
        if (isBlurred())
            score.score.Font.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
        else score.score.Font.setMaskFilter(null);
        float BarYs = StartY1;
        float BarYe;
        if (StartY2 == 0)  BarYe = StartY1 + 4 * score.score.getDistanceBetweenStaveLines();
        else BarYe = StartY2 + 4 * score.score.getDistanceBetweenStaveLines();
        canvas.drawRect(StartX + paddingLeft, BarYs, StartX + paddingLeft + FM_Const.dpTOpx(score.score.getContext(),1), BarYe, score.score.Font);
        score.score.Font.setColor(score.score.getColor());
        score.score.Font.setMaskFilter(null);
    }
    float Left(){
        return StartX;
    }
    float Bottom() {
        return StartY1;
    }
    float Right() {
        return StartX + paddingLeft + WidthNote();
    }
    float Top(){
        if (score == null) return 0;
        if (StartY2 == 0)
            return StartY1 + 4 * score.score.getDistanceBetweenStaveLines();
        return StartY2 + 4 * score.score.getDistanceBetweenStaveLines();
    }
}