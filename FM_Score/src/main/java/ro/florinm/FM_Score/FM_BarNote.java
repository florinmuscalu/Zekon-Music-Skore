package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.graphics.Paint;

public class FM_BarNote extends FM_BaseNote {

    public FM_BarNote(FM_Score Stave) {
        super(FM_NoteType.BAR, Stave);
        Stave.addStaffNote(this, clef);
    }

    public float getDisplacement() {
        return 0.0f;
    }

    public String toString() {
        return "";
    }

    @Override
    public float WidthAll(Paint font, boolean all) {
        return WidthAll(font);
    }

    public float WidthAll(Paint font) {
        return padding + paddingExtra;
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
        super.DrawNote(canvas);

        float BarYs = StartY1;
        float BarYe = 0;
        if (StartY2 == 0)  BarYe = StartY1 + 4 * Stave.getDistanceBetweenStaveLines();
        else BarYe = StartY2 + 4 * Stave.getDistanceBetweenStaveLines();
        Paint C = new Paint();
        C.setAntiAlias(true);
        C.setColor(Color);
        canvas.drawRect(StartX + padding, BarYs, StartX + padding + FM_Const.dpTOpx(context,1), BarYe, C);
        //canvas.drawLine(StartX + padding, BarYs, StartX + padding, BarYe, C);
        //canvas.drawLine(StartX + padding - 1, BarYs, StartX + padding - 1, BarYe, C);
        //canvas.drawLine(StartX + padding + 1, BarYs, StartX + padding + 1, BarYe, C);
    }
}