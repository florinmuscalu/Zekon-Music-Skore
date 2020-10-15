package ro.florinm.FM_Score;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class FM_BaseNote{
    @FM_NoteType
    private final int type;
    protected int Color;
    @FM_ClefValue
    Integer clef;
    Context context;
    Float padding;
    Float paddingNote;
    Float paddingDot;
    Float paddingExtra;
    private boolean Visible;
    FM_Score Stave;
    float StartX;
    float StartY1, StartY2;
    int line;


    public FM_BaseNote(@FM_NoteType int type, FM_Score Stave) {
        this.type = type;
        paddingDot = 0f;
        paddingNote = 0f;
        padding = 0f;
        paddingExtra = 0f;
        clef = FM_ClefValue.TREBLE;
        this.Stave = Stave;
        this.Visible = true;
        this.line = 1;
    }

    protected void setClef(@FM_ClefValue int Clef){
        this.clef = Clef;
    }

    protected void setContext(Context context){
        this.context = context;
        padding = FM_Const.spTOpx(context, 4);
        paddingExtra = FM_Const.spTOpx(context, 8);
    }

    public void SetDrawParameters(float x, float ys1, float ys2){
        StartX = x;
        StartY1 = ys1;
        StartY2 = ys2;
    }
    public void DrawNote(Canvas canvas){
        if (Stave.getDrawBoundingBox()) {
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
    public abstract String toString();
    public abstract float WidthAll(Paint font, boolean all);
    public abstract float WidthAll(Paint font);
    public abstract float WidthAccidental(Paint font);
    public abstract float WidthNote(Paint font);
    public abstract float WidthAllNoDot(Paint font);
    public int getColor() {
        return Color;
    }
    public void setColor(int color) {
        Color = color;
    }

    public float getPadding() {
        return padding;
    }
    public void setPadding(float p) {
        padding = p;
    }

    public void setPaddingNote(float p) {
        paddingNote = p;
    }

    public void setPaddingDot(float p) {
        paddingDot = p;
    }

    public void setPaddingExtra(float p) {
        paddingExtra = p;
    }

    public boolean isVisible() {
        return Visible;
    }

    public void setVisible(boolean visible) {
        if (!visible) line = -1;
        else line = 1;
        Visible = visible;
    }

    public abstract float Left();
    public abstract float Bottom();
    public abstract float Right();
    public abstract float Top();
}