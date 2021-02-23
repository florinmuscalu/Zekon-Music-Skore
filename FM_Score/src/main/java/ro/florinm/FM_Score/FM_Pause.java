package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.graphics.Rect;

public class FM_Pause extends FM_BaseNote {
    public FM_Pause(FM_Score Score, @FM_DurationValue int duration) {
        super(FM_NoteType.PAUSE, Score);
        this.duration = duration;
    }

    public FM_Pause(FM_Score Score, @FM_DurationValue int duration, int Color) {
        super(FM_NoteType.PAUSE, Score);
        this.duration = duration;
        this.color = Color;
    }

    float getDisplacement() {
        if (duration == 1 || duration == 51) return 1.0f;
        if (duration == 2 || duration == 52) return 2.0f;
        if (duration == 4 || duration == 54) return 2.0f;
        if (duration == 8 || duration == 58) return 2.0f;
        if (duration == 16 || duration == 66) return 1.0f;
        if (duration == 32 || duration == 82) return 2.0f;

        return 2.0f;
    }

    String asString() {
        String s = "";
        if (duration == 1 || duration == 51) return FM_Const.Pause_1;
        if (duration == 2 || duration == 52) return FM_Const.Pause_2;
        if (duration == 4 || duration == 54) return FM_Const.Pause_4;
        if (duration == 8 || duration == 58) return FM_Const.Pause_8;
        if (duration == 16 || duration == 66) return FM_Const.Pause_16;
        if (duration == 32 || duration == 82) return FM_Const.Pause_32;
        return s;
    }

    private int lineSpan() {
        int s = 0;
        if (duration == 1 || duration == 51) return 1;
        if (duration == 2 || duration == 52) return 1;
        if (duration == 4 || duration == 54) return 3;
        if (duration == 8 || duration == 58) return 2;
        if (duration == 16 || duration == 66) return 3;
        if (duration == 32 || duration == 82) return 4;
        return s;
    }

    String asStringDot(){
        String s2 = "";
        if (duration>50) s2 = " " + FM_Const.Dot;
        return s2;
    }

    float WidthAccidental() {
        return 0;
    }
    protected float WidthNoteNoStem(){
        FM_Const.AdjustFont(score, FM_Const.Pause_8, 2);
        return score.Font.measureText(asString());
    }
    protected float WidthNote(){
        return WidthNoteNoStem();
    }
    protected float WidthDot(){
        FM_Const.AdjustFont(score, FM_Const.Pause_8, 2);
        return score.Font.measureText(asStringDot());
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

    void DrawNote(Canvas canvas) {
        if (!isVisible()) return;
        super.DrawNote(canvas);

        FM_Const.AdjustFont(score, FM_Const.Pause_8, 2);
        canvas.drawText(asString(), StartX + paddingLeft + WidthAccidental() + paddingNote, StartY1 + getDisplacement() * score.getDistanceBetweenStaveLines(), score.Font);
        score.Font.setColor(score.getColor());
        canvas.drawText(asStringDot(),  StartX + paddingLeft + WidthAccidental() + paddingNote + WidthNote() + paddingDot, StartY1 + (getDisplacement() + 0.5f) * score.getDistanceBetweenStaveLines(), score.Font);
    }
    float Left(){
        return StartX + paddingLeft;
    };
    float Bottom() {
        return StartY1 + getDisplacement() * score.getDistanceBetweenStaveLines() + BottomMargin();
    }
    float Right() {
        return StartX + paddingLeft + WidthAccidental() + paddingNote + WidthNote() + paddingDot + WidthDot();
    }
    float Top(){
        return StartY1 + getDisplacement() * score.getDistanceBetweenStaveLines() + TopMargin();
    }
}
