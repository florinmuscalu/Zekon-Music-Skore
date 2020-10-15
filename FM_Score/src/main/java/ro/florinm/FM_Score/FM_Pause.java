package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class FM_Pause extends FM_BaseNote {
    @FM_DurationValue
    private final int duration;

    public FM_Pause(FM_Score Score, @FM_DurationValue int duration) {
        super(FM_NoteType.PAUSE, Score);
        this.duration = duration;
    }

    public float getDisplacement() {
        if (duration == 1 || duration == 51) return 1.0f;
        if (duration == 2 || duration == 52) return 2.0f;
        if (duration == 4 || duration == 54) return 2.0f;
        if (duration == 8 || duration == 58) return 2.0f;
        if (duration == 16 || duration == 516) return 1.0f;
        if (duration == 32 || duration == 532) return 2.0f;

        return 2.0f;
    }

    public String asString() {
        String s = "";
        if (duration == 1 || duration == 51) return FM_Const.Pause_1;
        if (duration == 2 || duration == 52) return FM_Const.Pause_2;
        if (duration == 4 || duration == 54) return FM_Const.Pause_4;
        if (duration == 8 || duration == 58) return FM_Const.Pause_8;
        if (duration == 16 || duration == 516) return FM_Const.Pause_16;
        if (duration == 32 || duration == 532) return FM_Const.Pause_32;
        return s;
    }

    private int lineSpan() {
        int s = 0;
        if (duration == 1 || duration == 51) return 1;
        if (duration == 2 || duration == 52) return 1;
        if (duration == 4 || duration == 54) return 3;
        if (duration == 8 || duration == 58) return 2;
        if (duration == 16 || duration == 516) return 3;
        if (duration == 32 || duration == 532) return 4;
        return s;
    }

    @Override
    public float WidthAll(boolean all) {
        return WidthAll();
    }

    public float WidthAll() {
        FM_Const.AdjustFont(score, FM_Const.Pause_8, 2);
        return paddingLeft + score.Font.measureText(asString()) + paddingRight;
    }

    public float WidthAccidental() {
        return 0;
    }

    public float WidthAllNoDot() {
        return WidthAll();
    }
    public float WidthNote() {
        return WidthAll();
    }

    private float BottomMargin() {
        FM_Const.AdjustFont(score, asString(), lineSpan());
        Rect bounds = new Rect();
        String s = asString();
        score.Font.getTextBounds(s, 0, s.length(), bounds);
        return bounds.bottom;
    }

    private float TopMargin() {
        FM_Const.AdjustFont(score, asString(), lineSpan());
        Rect bounds = new Rect();
        String s = asString();
        score.Font.getTextBounds(s, 0, s.length(), bounds);
        return bounds.top;
    }

    public void DrawNote(Canvas canvas) {
        if (!isVisible()) return;
        super.DrawNote(canvas);

        FM_Const.AdjustFont(score, FM_Const.Pause_8, 2);
        canvas.drawText(asString(), StartX + paddingLeft, StartY1 + getDisplacement() * score.getDistanceBetweenStaveLines(), score.Font);
    }
    public float Left(){
        return StartX + paddingLeft;
    };
    public float Bottom() {
        return StartY1 + getDisplacement() * score.getDistanceBetweenStaveLines() + BottomMargin();
    }
    public float Right() {
        return StartX + WidthAll();
    }
    public float Top(){
        return StartY1 + getDisplacement() * score.getDistanceBetweenStaveLines() + TopMargin();
    }
}
