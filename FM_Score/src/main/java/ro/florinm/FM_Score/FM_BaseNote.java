package ro.florinm.FM_Score;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class FM_BaseNote{
    @FM_NoteType
    private final int type;
    protected int color;
    @FM_ClefValue
    Integer clef;
    Float paddingLeft;
    Float paddingNote;
    Float paddingDot;
    Float paddingRight;
    private boolean visible;
    FM_Score score;
    float StartX;
    float StartY1, StartY2;
    int line;


    public FM_BaseNote(@FM_NoteType int type, FM_Score score) {
        this.type = type;
        paddingDot = 0f;
        paddingNote = 0f;
        paddingLeft = FM_Const.dpTOpx(score.getContext(), 4);
        paddingRight = FM_Const.dpTOpx(score.getContext(), 8);
        clef = FM_ClefValue.TREBLE;
        this.score = score;
        this.visible = true;
        this.line = 1;
    }

    protected void setClef(@FM_ClefValue int Clef){
        this.clef = Clef;
    }

    public void SetDrawParameters(float StartX, float StartY1, float StartY2){
        this.StartX = StartX;
        this.StartY1 = StartY1;
        this.StartY2 = StartY2;
    }
    public void DrawNote(Canvas canvas){
        score.Font.setColor(color);
        if (score.getDrawBoundingBox()) {
            Paint p = new Paint();
            p.setColor(android.graphics.Color.argb(255, 255, 0, 0));
            float bx = Left();
            float tx = Right();
            float by = Bottom();
            float ty = Top();
            canvas.drawLine(bx, by, bx, ty, p);
            canvas.drawLine(bx, ty, tx, ty, p);
            canvas.drawLine(tx, ty, tx, by, p);
            canvas.drawLine(tx, by, bx, by, p);
        }
    };
    public abstract float getDisplacement();
    public abstract String asString();
    public abstract float WidthAll(boolean Stem);
    public abstract float WidthAll();
    public abstract float WidthAccidental();
    public abstract float WidthNote();
    public abstract float WidthAllNoDot();

    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }

    public float getPaddingLeft() {
        return paddingLeft;
    }
    public void setPaddingLeft(float p) {
        paddingLeft = p;
    }

    public void setPaddingNote(float p) {
        paddingNote = p;
    }

    public void setPaddingDot(float p) {
        paddingDot = p;
    }

    public void setPaddingRight(float p) {
        paddingRight = p;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        if (!visible) line = -1;
        else line = 1;
        this.visible = visible;
    }

    public abstract float Left();
    public abstract float Bottom();
    public abstract float Right();
    public abstract float Top();
}