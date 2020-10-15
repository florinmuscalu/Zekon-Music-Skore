package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.graphics.Paint;

public class FM_BarNote extends FM_BaseNote {

    public FM_BarNote(FM_Score score) {
        super(FM_NoteType.BAR, score);
        //Stave.addStaffNote(this, clef);
    }

    public float getDisplacement() {
        return 0.0f;
    }

    public String asString() {
        return "";
    }

    @Override
    public float WidthAll(boolean all) {
        return WidthAll();
    }

    public float WidthAll() {
        return paddingLeft + paddingRight;
    }

    public float WidthAccidental() {
        return 0;
    }

    public float WidthAllNoDot() {
        return WidthAll();
    }
    public float WidthNote() {
        return 0;
    }

    public void DrawNote(Canvas canvas) {
        if (!isVisible()) return;
        super.DrawNote(canvas);

        float BarYs = StartY1;
        float BarYe = 0;
        if (StartY2 == 0)  BarYe = StartY1 + 4 * score.getDistanceBetweenStaveLines();
        else BarYe = StartY2 + 4 * score.getDistanceBetweenStaveLines();
        Paint C = new Paint();
        C.setAntiAlias(true);
        C.setColor(color);
        canvas.drawRect(StartX + paddingLeft, BarYs, StartX + paddingLeft + FM_Const.dpTOpx(score.getContext(),1), BarYe, C);
    }

    public float Left(){
        return StartX + paddingLeft;
    };
    public float Bottom() {
        return StartY1;
    }
    public float Right() {
        return StartX + paddingLeft + FM_Const.dpTOpx(score.getContext(),1);
    }
    public float Top(){
        float BarYe = 0;
        if (StartY2 == 0)  BarYe = StartY1 + 4 * score.getDistanceBetweenStaveLines();
        else BarYe = StartY2 + 4 * score.getDistanceBetweenStaveLines();
        return BarYe;
    }
}