package tech.zekon.FM_Score;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class FM_BaseNote{
    @FM_NoteType
    private final int type;
    protected int color;
    boolean tuplet = false;
    int tupletSize = 0;
    int voice = 0;
    
    //On which staff do you want this Note? First (0) or Second (1). First staff uses FM_Score.FirstStaffKey, second one uses FM_Score.SecondStaffKey
    int stave;

    protected float paddingLeft;
    protected float getPaddingLeft() { return paddingLeft; }
    protected void setPaddingLeft(float p) { paddingLeft = p; }
    protected abstract float WidthAccidental();
    protected float paddingNote;
    protected void setPaddingNote(float p) { paddingNote = p; }
    protected abstract float WidthNoteNoStem();
    protected abstract float WidthNote();
    protected float paddingDot;
    protected void setPaddingDot(float p) { paddingDot = p; }
    protected abstract float WidthDot();
    float Width() { return paddingLeft + WidthAccidental() + paddingNote + WidthNote() + paddingDot + WidthDot(); }
    //float WidthNoDot() { return paddingLeft + WidthAccidental() + paddingNote + WidthNote(); }
    //float WidthNoStem(){ return paddingLeft + WidthAccidental() + paddingNote + WidthNoteNoStem() + paddingDot + WidthDot(); }
    float WidthNoDotNoStem(){ return paddingLeft + WidthAccidental() + paddingNote + WidthNoteNoStem(); }

    boolean visible;
    FM_ScoreBase score;
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


    protected FM_BaseNote(@FM_NoteType int type, FM_ScoreBase score) {
        this.type = type;
        paddingDot = 0f;
        paddingNote = 0f;
        if (score.score != null) setPaddingLeft(FM_Const.dpTOpx(score.score.getContext(), 4));
        stave = 0;
        this.score = score;
        this.visible = true;
        this.line = 1;
        if (score.score != null) color = score.score.getColor();
        note = FM_NoteValue.DO;
        octave = 0;
        accidental = FM_Accidental.None;
        stem = false;
        stem_up = true;
    }

    void RemoveAccidental() {
        accidental = FM_Accidental.None;
    }
    protected void SetDrawParameters(float StartX, float StartY1, float StartY2){
        this.StartX = StartX;
        this.StartY1 = StartY1;
        this.StartY2 = StartY2;
    }
    void DrawNote(Canvas canvas){
        if (score.score == null) return;
        score.score.Font.setColor(color);
        if (score.score.getShowBoundingBoxes() == FM_BoundingBoxType.None) return;

        if ((score.score.getShowBoundingBoxes() == FM_BoundingBoxType.Chord && this instanceof  FM_Chord) ||
                (score.score.getShowBoundingBoxes() == FM_BoundingBoxType.Note && !(this instanceof  FM_Chord))) {
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
    }
    abstract float getDisplacement();
    abstract String asString();
    abstract float Left();
    abstract float Bottom();
    abstract float Right();
    abstract float Top();

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
        this.visible = visible;
    }
    public int getAccidental() {
        return accidental;
    }
    @FM_NoteType public int getType() {
        return type;
    }
    public int getStave() {return stave; }
    @FM_DurationValue
    public int getDuration() {
        return duration;
    }
}