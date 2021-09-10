package tech.zekon.FM_Score;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Rect;

public class FM_Pause extends FM_BaseNote {
    int adjustment = 1;
    public FM_Pause(FM_ScoreBase Score, @FM_DurationValue int duration, int adjustment, int voice) {
        super(FM_NoteType.PAUSE, Score);
        this.duration = duration;
        this.adjustment = adjustment;
        this.voice = voice;
    }

    public FM_Pause(FM_ScoreBase Score, @FM_DurationValue int duration, int adjustment, int voice, int Color) {
        super(FM_NoteType.PAUSE, Score);
        this.duration = duration;
        this.color = Color;
        this.adjustment = adjustment;
        this.voice = voice;
    }

    float getDisplacement() {
        if (duration == 1 || duration == 51) return -adjustment + 1.0f;
        else {
            if (duration == 2 || duration == 52) return -adjustment + 2.0f;
            else {
                if (duration == 4 || duration == 54) return -adjustment + 2.0f;
                else {
                    if (duration == 8 || duration == 58) return -adjustment + 2.0f;
                    else {
                        if (duration == 16 || duration == 66) return -adjustment + 1.0f;
                        else {
                            if (duration == 32 || duration == 82) return -adjustment + 2.0f;
                        }
                    }
                }
            }
        }
        return -adjustment + 2.0f;
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

    protected float WidthAccidental() {
        return 0;
    }
    protected float WidthNoteNoStem(){
        if (score.score == null) return 0;
        FM_Const.AdjustFont(score.score, FM_Const.Pause_8, 2);
        return score.score.Font.measureText(asString());
    }
    protected float WidthNote(){
        return WidthNoteNoStem();
    }
    protected float WidthDot(){
        if (score.score == null) return 0;
        FM_Const.AdjustFont(score.score, FM_Const.Pause_8, 2);
        return score.score.Font.measureText(asStringDot());
    }

    private float BottomMargin() {
        if (score.score == null) return 0;
        FM_Const.AdjustFont(score.score, asString(), lineSpan());
        Rect bounds = new Rect();
        String s = asString();
        score.score.Font.getTextBounds(s, 0, s.length(), bounds);
        return bounds.bottom;
    }

    private float TopMargin() {
        if (score.score == null) return 0;
        FM_Const.AdjustFont(score.score, asString(), lineSpan());
        Rect bounds = new Rect();
        String s = asString();
        score.score.Font.getTextBounds(s, 0, s.length(), bounds);
        return bounds.top;
    }

    void DrawNote(Canvas canvas) {
        if (score.score == null) return;
        if (!isBlurred() && !isVisible()) return;
        super.DrawNote(canvas);
        if (isBlurred())
            score.score.Font.setMaskFilter(new BlurMaskFilter(30, BlurMaskFilter.Blur.NORMAL));
        else score.score.Font.setMaskFilter(null);
        FM_Const.AdjustFont(score.score, FM_Const.Pause_8, 2);
        canvas.drawText(asString(), StartX + paddingLeft + WidthAccidental() + paddingNote, StartY1 + getDisplacement() * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        score.score.Font.setColor(score.score.getColor());
        canvas.drawText(asStringDot(),  StartX + paddingLeft + WidthAccidental() + paddingNote + WidthNote() + paddingDot, StartY1 + (getDisplacement() + 0.5f) * score.score.getDistanceBetweenStaveLines(), score.score.Font);
        score.score.Font.setMaskFilter(null);
    }
    float Left(){
        return StartX + paddingLeft;
    }
    float Bottom() {
        if (score.score == null) return 0;
        return StartY1 + getDisplacement() * score.score.getDistanceBetweenStaveLines() + BottomMargin();
    }
    float Right() {
        if (score.score == null) return 0;
        return StartX + paddingLeft + WidthAccidental() + paddingNote + WidthNote() + paddingDot + WidthDot();
    }
    float Top(){
        if (score.score == null) return 0;
        return StartY1 + getDisplacement() * score.score.getDistanceBetweenStaveLines() + TopMargin();
    }
}
