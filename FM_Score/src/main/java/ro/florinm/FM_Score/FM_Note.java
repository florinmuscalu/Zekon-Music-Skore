package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class FM_Note extends FM_BaseNote {
    @FM_NoteValue
    final Integer Note;
    int Octave;
    @FM_DurationValue
    final int duration;
    @FM_Accidental
    private final int Accidental;
    boolean stem;
    boolean stem_up, beam, tuple;
    float ys, startX;

    public FM_Note(FM_Score Stave, @FM_NoteValue int Note, int Octave, @FM_Accidental int accidental, @FM_DurationValue int duration, boolean stem, boolean stem_up) {
        super(FM_NoteType.NOTE, Stave);
        this.Note = Note;
        this.Accidental = accidental;
        this.Octave = Octave;
        this.duration = duration;
        this.stem = stem;
        this.stem_up = stem_up;
        this.beam = false;
        this.tuple = false;
    }

    public int getOctave() {
        return Octave;
    }

    public void setOctave(int o) {
        Octave = o;
    }

    public float getDisplacement() {
        float offset = 0.0f;
        if (Note.equals(FM_NoteValue.DO)) offset = 0.0f;
        if (Note.equals(FM_NoteValue.RE)) offset = -0.5f;
        if (Note.equals(FM_NoteValue.MI)) offset = -1.0f;
        if (Note.equals(FM_NoteValue.FA)) offset = -1.5f;
        if (Note.equals(FM_NoteValue.SOL)) offset = -2.0f;
        if (Note.equals(FM_NoteValue.LA)) offset = -2.5f;
        if (Note.equals(FM_NoteValue.SI)) offset = -3.0f;
        if (clef.equals(FM_ClefValue.TREBLE)) {
            offset = offset + 5.0f + 3.5f * (4 - Octave);
            return offset;
        }
        if (clef.equals(FM_ClefValue.BASS)) {
            offset = offset + 6.0f + 3.5f * (2 - Octave);
            return offset;
        }
        return 0.0f;
    }

    public String toStringAccidental(){
        String s1 = "";
        if (Accidental == FM_Accidental.Natural) s1 = FM_Const.Natural+" ";
        if (Accidental == FM_Accidental.Flat) s1 = FM_Const.Flat+" ";
        if (Accidental == FM_Accidental.Sharp) s1 = FM_Const.Sharp+" ";
        if (Accidental == FM_Accidental.DoubleSharp) s1 = FM_Const.DoubleSharp+" ";
        if (Accidental == FM_Accidental.DoubleFlat) s1 = FM_Const.DoubleFlat+" ";
        if (Accidental == FM_Accidental.TripleSharp) s1 = FM_Const.Sharp+" "+FM_Const.DoubleSharp+" ";
        if (Accidental == FM_Accidental.TripleFlat) s1 = FM_Const.TripleFlat+" ";
        return s1;
    }

    public String toStringDot(){
        String s2 = "";
        if (duration>50) s2 = " " + FM_Const.Dot;
        return s2;
    }

    public String toStringNote() {
        return toStringNote(stem);
    }

    public String toStringNote(boolean stem){
        if (duration == 1 || duration == 51) return FM_Const._1Note;
        if (stem && !beam) {
            if (stem_up) {
                if (duration == 2 || duration == 52) return   FM_Const._2Note_up;
                if (duration == 4 || duration == 54) return   FM_Const._4Note_up;
                if (duration == 8 || duration == 58) return   FM_Const._8Note_up;
                if (duration == 16 || duration == 516) return FM_Const._16Note_up;
                if (duration == 32 || duration == 532) return FM_Const._32Note_up;
            } else {
                if (duration == 2 || duration == 52) return   FM_Const._2Note_down;
                if (duration == 4 || duration == 54) return   FM_Const._4Note_down;
                if (duration == 8 || duration == 58) return   FM_Const._8Note_down;
                if (duration == 16 || duration == 516) return FM_Const._16Note_down;
                if (duration == 32 || duration == 532) return FM_Const._32Note_down;
            }
        } else {
            if (duration == 2 || duration == 52) return FM_Const.EmptyNote;
            return FM_Const.FillNote;
        }
        return "";
    }

    public String toString() {
        return toStringAccidental() + toStringNote() + toStringDot();
    }

    public float WidthAll(Paint font) {
        return WidthAll(font, false);
    }

    public float WidthAll(Paint font, boolean stem) {
        return padding + WidthAccidental(font) + paddingNote + WidthNote(font, stem) + paddingDot + WidthDot(font) + paddingExtra;
    }

    public float WidthDot(Paint font) {
        FM_Const.AdjustFont(font, FM_Const.Sharp, Stave.getDistanceBetweenStaveLines() * 2);
        return font.measureText(toStringDot());
    }

    public float WidthAccidental(Paint font) {
        FM_Const.AdjustFont(font, FM_Const.Sharp, Stave.getDistanceBetweenStaveLines() * 2);
        return font.measureText(toStringAccidental());
    }

    public float WidthNote(Paint font) {
        return WidthNote(font, false);
    }
//
//    float percent(Paint font){
//        boolean tmp_stem = stem;
//        stem = false;
//        Rect bounds = new Rect();
//        font.getTextBounds(toStringNote(), 0, 1, bounds);
//        stem = tmp_stem;
//        return Stave.getDistanceBetweenStaveLines()/bounds.height();
//    }

    public float WidthNote(Paint font, boolean stem) {
        FM_Const.AdjustFont(font, toStringNote(false), Stave.getDistanceBetweenStaveLines());
        return font.measureText(toStringNote(stem));
    }

    public float Height(Paint font, boolean all) {
        FM_Const.AdjustFont(font, toStringNote(false), Stave.getDistanceBetweenStaveLines());
        boolean tmp_beam = beam;
        if (all) beam = false;
        Rect bounds = new Rect();
        String s = toStringNote();
        font.getTextBounds(s, 0, s.length(), bounds);
        beam = tmp_beam;
        return bounds.height();
    }

    public float WidthAllNoDot(Paint font) {
        return WidthAll(font) - WidthDot(font) - paddingDot;
    }

    public void DrawNote(Canvas canvas) {
        if (!isVisible()) return;
        super.DrawNote(canvas);

        ys = StartY1;
        startX = StartX;
        float ly;
        float dy;
        boolean l = false;
        float offset = getDisplacement();
        if (offset >= 5.0f || offset < 0.0f) l = true;
        dy = StartY1 + offset * Stave.getDistanceBetweenStaveLines();
        if (l) {
            if (offset >= 5.0f)
                for (int i = 5; i <= offset; i++) {
                    ly = StartY1 + i * Stave.getDistanceBetweenStaveLines();
                    float tX = StartX + padding + WidthAccidental(Stave.StaveFont) + paddingNote - Stave.getDistanceBetweenStaveLines() / 3;
                    float tXe = StartX + padding + WidthAccidental(Stave.StaveFont) + paddingNote + WidthNote(Stave.StaveFont) + Stave.getDistanceBetweenStaveLines() / 3;
                    float tY = ly;
                    float tYe = ly + FM_Const.dpTOpx(context,1);
                    canvas.drawRect(tX, tY,tXe, tYe, Stave.StaveLineColor);
                }
            if (offset < 0.0f)
                for (int i = -1; i >= offset; i--) {
                    ly = StartY1 + i * Stave.getDistanceBetweenStaveLines();
                    float tX = StartX + padding + WidthAccidental(Stave.StaveFont) + paddingNote - Stave.getDistanceBetweenStaveLines() / 3;
                    float tXe = StartX + padding + WidthAccidental(Stave.StaveFont) + paddingNote + WidthNote(Stave.StaveFont) + Stave.getDistanceBetweenStaveLines() / 3;
                    float tY = ly;
                    float tYe = ly + FM_Const.dpTOpx(context,1);
                    canvas.drawRect(tX, tY,tXe, tYe, Stave.StaveLineColor);
                }
        }
        float width_accidental = WidthAccidental(Stave.StaveFont);
        float width_note_no_stem = WidthNote(Stave.StaveFont, false);
        float width_note_stem = WidthNote(Stave.StaveFont, stem);
        float width_dot = WidthDot(Stave.StaveFont);
        Stave.StaveFont.setColor(Color);

        FM_Const.AdjustFont(Stave.StaveFont, FM_Const.Sharp, Stave.getDistanceBetweenStaveLines() * 2);
        canvas.drawText(toStringAccidental(), StartX + padding, dy, Stave.StaveFont);

        FM_Const.AdjustFont(Stave.StaveFont, toStringNote(false), Stave.getDistanceBetweenStaveLines());
        canvas.drawText(toStringNote(), StartX + padding + width_accidental + paddingNote, dy, Stave.StaveFont);

        FM_Const.AdjustFont(Stave.StaveFont, toStringAccidental(), Stave.getDistanceBetweenStaveLines() * 2);
        canvas.drawText(toStringDot(),  StartX + padding + width_accidental + paddingNote + width_note_no_stem + paddingDot, dy, Stave.StaveFont);

        if (DrawBoundingBox) {
            Paint p = new Paint();
            p.setColor(android.graphics.Color.argb(255, 255, 0, 0));
            float bx = StartX + padding;
            float tx = StartX + padding + width_accidental + paddingNote + width_note_stem + paddingDot + width_dot;
            float by;
            float ty;
            if (stem_up) {
                by = dy + 0.5f * Stave.getDistanceBetweenStaveLines();
                ty = by - Height(Stave.StaveFont, stem);
            } else {
                by = dy - 0.5f * Stave.getDistanceBetweenStaveLines();
                ty = by + Height(Stave.StaveFont, stem);
            }
            canvas.drawLine(bx, by, bx, ty, p);
            canvas.drawLine(bx, ty, tx, ty, p);
            canvas.drawLine(tx, ty, tx, by, p);
            canvas.drawLine(tx, by, bx, by, p);
        }
    }
}
