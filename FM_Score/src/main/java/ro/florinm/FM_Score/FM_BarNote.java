package ro.florinm.FM_Score;

import android.graphics.Canvas;

public class FM_BarNote extends FM_BaseNote {
    protected boolean lineEnd = false;        //set it to true if it is a line-end bar
    public FM_BarNote(FM_Score score) {
        super(FM_NoteType.BAR, score);
    }
    public FM_BarNote(FM_Score score, int Color) {
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
        return FM_Const.dpTOpx(score.getContext(),1);
    }
    @Override
    protected float WidthNote(){
        return WidthNoteNoStem();
    }
    protected float WidthDot() {
        return 0;
    }
    void DrawNote(Canvas canvas) {
        if (!isVisible()) return;
        super.DrawNote(canvas);
        float BarYs = StartY1;
        float BarYe;
        if (StartY2 == 0)  BarYe = StartY1 + 4 * score.getDistanceBetweenStaveLines();
        else BarYe = StartY2 + 4 * score.getDistanceBetweenStaveLines();
        canvas.drawRect(StartX + paddingLeft, BarYs, StartX + paddingLeft + FM_Const.dpTOpx(score.getContext(),1), BarYe, score.Font);
        score.Font.setColor(score.getColor());
    }
    float Left(){
        return StartX;
    };
    float Bottom() {
        return StartY1;
    }
    float Right() {
        return StartX + paddingLeft + WidthNote();
    }
    float Top(){
        if (StartY2 == 0)
            return StartY1 + 4 * score.getDistanceBetweenStaveLines();
        return StartY2 + 4 * score.getDistanceBetweenStaveLines();
    }
}