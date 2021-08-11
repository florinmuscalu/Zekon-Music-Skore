package tech.zekon.FM_Score;

import android.graphics.Canvas;
import android.graphics.Rect;

class FM_Clef extends FM_BaseNote {
    @FM_ClefValue int clef;

    FM_Clef(FM_ScoreBase Score, @FM_ClefValue int clef, int staff) {
        super(FM_NoteType.CLEF, Score);
        this.clef = clef;
        this.stave = staff;
    }

    FM_Clef(FM_ScoreBase Score, @FM_ClefValue int clef, int staff, int Color) {
        super(FM_NoteType.CLEF, Score);
        this.clef = clef;
        this.stave = staff;
        this.color = Color;
    }

    float getDisplacement() {
        if (this.clef == FM_ClefValue.TREBLE) return 3.0f;
        else if (this.clef == FM_ClefValue.BASS) return 1.0f;
        return 1.0f;
    }

    String asString() {
        String s = "";
        if (this.clef == FM_ClefValue.BASS) return FM_Const.BassClef;
        if (this.clef == FM_ClefValue.TREBLE) return FM_Const.TrebleClef;
        return s;
    }

    private float lineSpan() {
        if (this.clef == FM_ClefValue.BASS) return 3.5f;
        if (this.clef == FM_ClefValue.TREBLE) return 7;
        return 1;
    }

    protected float WidthAccidental(){
        return 0;
    }
    protected float WidthNoteNoStem(){
        if (score.score == null) return 0;
        FM_Const.AdjustFont(score.score, FM_Const._4, 2);
        float w = score.score.Font.measureText(FM_Const.TrebleClef) + 2 * FM_Const.dpTOpx(score.score.context, FM_Const.DEFAULT_EXTRA_PADDING);
        float w1 = score.score.Font.measureText(FM_Const.BassClef) + 2 * FM_Const.dpTOpx(score.score.context, FM_Const.DEFAULT_EXTRA_PADDING);
        return Math.max(w, w1);
    }
    protected float WidthNote(){
        return WidthNoDotNoStem();
    }
    protected float WidthDot(){
        return 0;
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
        if (!isVisible()) return;
        super.DrawNote(canvas);
        score.score.Font.setColor(this.color);
        FM_Const.AdjustFont(score.score, FM_Const._4, 2);
        canvas.drawText(asString(), StartX + paddingLeft, StartY1 + getDisplacement() * score.score.getDistanceBetweenStaveLines(), score.score.Font);
    }
    float Left(){
        return StartX;
    }
    float Bottom() {
        if (score.score == null) return 0;
        return StartY1 + getDisplacement() * score.score.getDistanceBetweenStaveLines() + BottomMargin();
    }
    float Right() {
        return StartX + paddingLeft + Width();
    }
    float Top(){
        if (score.score == null) return 0;
        return StartY1 + getDisplacement() * score.score.getDistanceBetweenStaveLines() + TopMargin();
    }
}
