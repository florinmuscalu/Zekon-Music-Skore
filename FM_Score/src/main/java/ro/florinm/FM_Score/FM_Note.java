package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class FM_Note extends FM_BaseNote {
    @FM_NoteValue
    final Integer note;
    int octave;
    @FM_DurationValue
    final int duration;
    @FM_Accidental
    private int accidental;
    boolean stem;
    boolean stem_up, beam, tuple;
    float ys, startX;

    public FM_Note(FM_Score Score, @FM_NoteValue int Note, int Octave, @FM_Accidental int Accidental, @FM_DurationValue int Duration) {
        super(FM_NoteType.NOTE, Score);
        this.note = Note;
        this.accidental = Accidental;
        this.octave = Octave;
        this.duration = Duration;
        this.stem = true;
        this.stem_up = true;
        this.beam = false;
        this.tuple = false;
    }

    public FM_Note(FM_Score Score, @FM_NoteValue int Note, int Octave, @FM_Accidental int Accidental, @FM_DurationValue int Duration, int Color) {
        super(FM_NoteType.NOTE, Score);
        this.note = Note;
        this.accidental = Accidental;
        this.octave = Octave;
        this.duration = Duration;
        this.stem = true;
        this.stem_up = true;
        this.beam = false;
        this.tuple = false;
        this.color = Color;
    }

    public FM_Note(FM_Score Score, @FM_NoteValue int Note, int Octave, @FM_Accidental int Accidental, @FM_DurationValue int Duration, boolean Stem, boolean StemUp) {
        super(FM_NoteType.NOTE, Score);
        this.note = Note;
        this.accidental = Accidental;
        this.octave = Octave;
        this.duration = Duration;
        this.stem = Stem;
        this.stem_up = StemUp;
        this.beam = false;
        this.tuple = false;
    }

    public FM_Note(FM_Score Score, @FM_NoteValue int Note, int Octave, @FM_Accidental int Accidental, @FM_DurationValue int Duration, boolean Stem, boolean StemUp, int Color) {
        super(FM_NoteType.NOTE, Score);
        this.note = Note;
        this.accidental = Accidental;
        this.octave = Octave;
        this.duration = Duration;
        this.stem = Stem;
        this.stem_up = StemUp;
        this.beam = false;
        this.tuple = false;
        this.color = Color;
    }

    public int getAccidental() {
        return accidental;
    }
    public void setAccidental(@FM_Accidental int a) {
        accidental = a;
    }

    public int getOctave() {
        return octave;
    }
    public void setOctave(int o) {
        octave = o;
    }

    public float getDisplacement() {
        float offset = 0.0f;
        if (note.equals(FM_NoteValue.DO)) offset = 0.0f;
        if (note.equals(FM_NoteValue.RE)) offset = -0.5f;
        if (note.equals(FM_NoteValue.MI)) offset = -1.0f;
        if (note.equals(FM_NoteValue.FA)) offset = -1.5f;
        if (note.equals(FM_NoteValue.SOL)) offset = -2.0f;
        if (note.equals(FM_NoteValue.LA)) offset = -2.5f;
        if (note.equals(FM_NoteValue.SI)) offset = -3.0f;
        if (clef.equals(FM_ClefValue.TREBLE)) {
            offset = offset + 5.0f + 3.5f * (4 - octave);
            return offset;
        }
        if (clef.equals(FM_ClefValue.BASS)) {
            offset = offset + 6.0f + 3.5f * (2 - octave);
            return offset;
        }
        return 0.0f;
    }

    public String asStringAccidental(){
        String s1 = "";
        if (accidental == FM_Accidental.Natural) s1 = FM_Const.Natural+" ";
        if (accidental == FM_Accidental.Flat) s1 = FM_Const.Flat+" ";
        if (accidental == FM_Accidental.Sharp) s1 = FM_Const.Sharp+" ";
        if (accidental == FM_Accidental.DoubleSharp) s1 = FM_Const.DoubleSharp+" ";
        if (accidental == FM_Accidental.DoubleFlat) s1 = FM_Const.DoubleFlat+" ";
        if (accidental == FM_Accidental.TripleSharp) s1 = FM_Const.Sharp+" "+FM_Const.DoubleSharp+" ";
        if (accidental == FM_Accidental.TripleFlat) s1 = FM_Const.TripleFlat+" ";
        return s1;
    }

    public String asStringDot(){
        String s2 = "";
        if (duration>50) s2 = " " + FM_Const.Dot;
        return s2;
    }

    public String asStringNote() {
        return asStringNote(stem);
    }

    public String asStringNote(boolean stem){
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

    public String asString() {
        return asStringAccidental() + asStringNote() + asStringDot();
    }

    public float WidthAll() {
        return WidthAll(false);
    }

    public float WidthAll(boolean Stem) {
        return WidthAccidental() + paddingNote + WidthNote(Stem) + paddingDot + WidthDot() + paddingRight;
    }

    public float WidthDot() {
        FM_Const.AdjustFont(score, FM_Const.Sharp, 2);
        return score.Font.measureText(asStringDot());
    }

    public float WidthAccidental() {
        FM_Const.AdjustFont(score, FM_Const.Sharp, 2);
        return score.Font.measureText(asStringAccidental());
    }

    public float WidthNote() {
        return WidthNote(false);
    }

    public float WidthNote(boolean stem) {
        FM_Const.AdjustFont(score, asStringNote(false), 1);
        return score.Font.measureText(asStringNote(stem));
    }

    public float Height(boolean all) {
        FM_Const.AdjustFont(score, asStringNote(false), 1);
        boolean tmp_beam = beam;
        if (all) beam = false;
        Rect bounds = new Rect();
        String s = asStringNote();
        score.Font.getTextBounds(s, 0, s.length(), bounds);
        beam = tmp_beam;
        return bounds.height();
    }

    public float WidthAllNoDot() {
        return WidthAll() - WidthDot() - paddingDot;
    }

    public void DrawNote(Canvas canvas) {
        if (!isVisible()) return;
        super.DrawNote(canvas);

        score.Font.setColor(score.getColor());
        ys = StartY1;
        startX = StartX;
        float ly;
        float dy;
        boolean l = false;
        float offset = getDisplacement();
        if (offset >= 5.0f || offset < 0.0f) l = true;
        dy = StartY1 + offset * score.getDistanceBetweenStaveLines();
        if (l) {
            if (offset >= 5.0f)
                for (int i = 5; i <= offset; i++) {
                    ly = StartY1 + i * score.getDistanceBetweenStaveLines();
                    float tX = StartX + paddingLeft + WidthAccidental() + paddingNote - score.getDistanceBetweenStaveLines() / 3;
                    float tXe = StartX + paddingLeft + WidthAccidental() + paddingNote + WidthNote() + score.getDistanceBetweenStaveLines() / 3;
                    float tY = ly - FM_Const.dpTOpx(score.getContext(),0.5f);
                    float tYe = ly + FM_Const.dpTOpx(score.getContext(),0.5f);
                    canvas.drawRect(tX, tY,tXe, tYe, score.Font);
                }
            if (offset < 0.0f)
                for (int i = -1; i >= offset; i--) {
                    ly = StartY1 + i * score.getDistanceBetweenStaveLines();
                    float tX = StartX + paddingLeft + WidthAccidental() + paddingNote - score.getDistanceBetweenStaveLines() / 3;
                    float tXe = StartX + paddingLeft + WidthAccidental() + paddingNote + WidthNote() + score.getDistanceBetweenStaveLines() / 3;
                    float tY = ly - FM_Const.dpTOpx(score.getContext(),0.5f);
                    float tYe = ly + FM_Const.dpTOpx(score.getContext(),0.5f);
                    canvas.drawRect(tX, tY,tXe, tYe, score.Font);
                }
        }
        float width_accidental = WidthAccidental();
        float width_note_no_stem = WidthNote(false);
        float width_note_stem = WidthNote(stem);
        float width_dot = WidthDot();

        score.Font.setColor(color);

        FM_Const.AdjustFont(score, FM_Const.Sharp, 2);
        canvas.drawText(asStringAccidental(), StartX + paddingLeft, dy, score.Font);

        FM_Const.AdjustFont(score, asStringNote(false), 1);
        canvas.drawText(asStringNote(), StartX + paddingLeft + width_accidental + paddingNote, dy, score.Font);

        FM_Const.AdjustFont(score, FM_Const.Sharp, 2);
        canvas.drawText(asStringDot(),  StartX + paddingLeft + width_accidental + paddingNote + width_note_no_stem + paddingDot, dy, score.Font);

        score.Font.setColor(score.getColor());
    }
    public float Left(){
        return  StartX + paddingLeft;
    };
    public float Bottom() {
        float offset = getDisplacement();
        float dy = StartY1 + offset * score.getDistanceBetweenStaveLines();
        if (stem_up) {
            return dy + 0.5f * score.getDistanceBetweenStaveLines();
        } else {
            return dy - 0.5f * score.getDistanceBetweenStaveLines();
        }
    }
    public float Right() {
        float width_accidental = WidthAccidental();
        float width_note_stem = WidthNote(stem);
        float width_dot = WidthDot();
        return StartX + paddingLeft + width_accidental + paddingNote + width_note_stem + paddingDot + width_dot;
    }
    public float Top(){
        float by = Bottom();
        if (stem_up) {
            return by - Height(stem);
        } else {
            return by + Height(stem);
        }
    }
}
