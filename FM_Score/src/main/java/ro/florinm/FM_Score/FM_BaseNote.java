package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class FM_BaseNote{
    @FM_NoteType
    private final int type;
    protected int color;
    @FM_ClefValue
    Integer clef;

    protected float paddingLeft;
    protected float getPaddingLeft() { return paddingLeft; }
    protected void setPaddingLeft(float p) { paddingLeft = p; }
    protected abstract float WidthAccidental();
    protected float paddingNote;
    protected void setPaddingNote(float p) { paddingNote = p; }
    protected float getPaddingNote() { return paddingNote; }
    protected abstract float WidthNoteNoStem();
    protected abstract float WidthNote();
    protected float paddingDot;
    protected void setPaddingDot(float p) { paddingDot = p; }
    protected float getPaddingDot(float p) { return paddingDot; }
    protected abstract float WidthDot();
    public float Width() { return paddingLeft + WidthAccidental() + paddingNote + WidthNote() + paddingDot + WidthDot(); }
    public float WidthNoDot() { return paddingLeft + WidthAccidental() + paddingNote + WidthNote(); }
    public float WidthNoStem(){ return paddingLeft + WidthAccidental() + paddingNote + WidthNoteNoStem() + paddingDot + WidthDot(); }
    public float WidthNoDotNoStem(){ return paddingLeft + WidthAccidental() + paddingNote + WidthNoteNoStem(); }

    boolean visible;
    FM_Score score;
    float StartX;
    float StartY1, StartY2;
    int line;
    @FM_NoteValue
    Integer note;
    int octave;
    @FM_Accidental
    protected int accidental;
    @FM_DurationValue
    int duration;
    boolean stem;
    boolean stem_up;


    protected FM_BaseNote(@FM_NoteType int type, FM_Score score) {
        this.type = type;
        paddingDot = 0f;
        paddingNote = 0f;
        setPaddingLeft(FM_Const.dpTOpx(score.getContext(), 4));
        clef = FM_ClefValue.TREBLE;
        this.score = score;
        this.visible = true;
        this.line = 1;
        color = score.getColor();
        note = 0;
        octave = 0;
        accidental = FM_Accidental.None;
        stem = false;
        stem_up = true;
    }

    public int getAccidental() {
        return accidental;
    }
    public void setAccidental(@FM_Accidental int a) {
        accidental = a;
    }

    @FM_NoteType
    public int getType() {
        return type;
    }
    @FM_ClefValue
    public int getClef(){
        return clef;
    }

    protected void setClef(@FM_ClefValue int Clef){
        this.clef = Clef;
    }

    protected void SetDrawParameters(float StartX, float StartY1, float StartY2){
        this.StartX = StartX;
        this.StartY1 = StartY1;
        this.StartY2 = StartY2;
    }
    protected void DrawNote(Canvas canvas){
        score.Font.setColor(color);
        if (score.getShowBoundingBoxes() == FM_BoundingBoxType.None) return;

        if ((score.getShowBoundingBoxes() == FM_BoundingBoxType.Chord && this instanceof  FM_Chord) ||
                (score.getShowBoundingBoxes() == FM_BoundingBoxType.Note && !(this instanceof  FM_Chord))) {
            Paint p = new Paint();
            p.setColor(android.graphics.Color.argb(255, 255, 0, 0));
            if (this instanceof FM_Chord) p.setColor(android.graphics.Color.argb(255, 255, 0, 255));
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
    protected abstract float getDisplacement();
    protected abstract String asString();

    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }

    public boolean isVisible() {
        return visible;
    }
    public void setVisible(boolean visible) {
        if (!visible) line = -1;
        else line = 1;
        this.visible = visible;
    }

    protected abstract float Left();
    protected abstract float Bottom();
    protected abstract float Right();
    protected abstract float Top();
}