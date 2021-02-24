package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.graphics.Rect;

class FM_Clef extends FM_BaseNote {
    @FM_ClefValue int clef;

    FM_Clef(FM_Score Score, @FM_ClefValue int clef, int staff) {
        super(FM_NoteType.CLEF, Score);
        this.clef = clef;
        this.stave = staff;
    }

    FM_Clef(FM_Score Score, @FM_ClefValue int clef, int staff, int Color) {
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
        FM_Const.AdjustFont(score, FM_Const._4, 2);
        float w = score.Font.measureText(FM_Const.TrebleClef) + 2 * FM_Const.dpTOpx(score.context, FM_Const.DEFAULT_EXTRA_PADDING);
        float w1 = score.Font.measureText(FM_Const.BassClef) + 2 * FM_Const.dpTOpx(score.context, FM_Const.DEFAULT_EXTRA_PADDING);
        return Math.max(w, w1);
    }
    protected float WidthNote(){
        return WidthNoDotNoStem();
    }
    protected float WidthDot(){
        return 0;
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
        score.Font.setColor(this.color);
        FM_Const.AdjustFont(score, FM_Const._4, 2);
        canvas.drawText(asString(), StartX + paddingLeft, StartY1 + getDisplacement() * score.getDistanceBetweenStaveLines(), score.Font);
    }
    float Left(){
        return StartX;
    }
    float Bottom() {
        return StartY1 + getDisplacement() * score.getDistanceBetweenStaveLines() + BottomMargin();
    }
    float Right() {
        return StartX + paddingLeft + Width();
    }
    float Top(){
        return StartY1 + getDisplacement() * score.getDistanceBetweenStaveLines() + TopMargin();
    }
}
