package ro.florinm.FM_Score;

import android.graphics.Canvas;

public class FM_BarNote extends FM_BaseNote {

    public FM_BarNote(FM_Score score) {
        super(FM_NoteType.BAR, score);
    }

    public FM_BarNote(FM_Score score, int Color) {
        super(FM_NoteType.BAR, score);
        this.color = Color;
    }

    public float getDisplacement() {
        return 0.0f;
    }

    public String asString() {
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

    public void DrawNote(Canvas canvas) {
        if (!isVisible()) return;
        super.DrawNote(canvas);
        float BarYs = StartY1;
        float BarYe = 0;
        if (StartY2 == 0)  BarYe = StartY1 + 4 * score.getDistanceBetweenStaveLines();
        else BarYe = StartY2 + 4 * score.getDistanceBetweenStaveLines();
        canvas.drawRect(StartX + paddingLeft, BarYs, StartX + paddingLeft + FM_Const.dpTOpx(score.getContext(),1), BarYe, score.Font);
        score.Font.setColor(score.getColor());
    }

    public float Left(){
        return StartX + paddingLeft;
    };
    public float Bottom() {
        return StartY1;
    }
    public float Right() {
        return StartX + paddingLeft + WidthNote();
    }
    public float Top(){
        float BarYe = 0;
        if (StartY2 == 0)  BarYe = StartY1 + 4 * score.getDistanceBetweenStaveLines();
        else BarYe = StartY2 + 4 * score.getDistanceBetweenStaveLines();
        return BarYe;
    }
}