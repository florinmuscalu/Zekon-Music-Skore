package tech.zekon.FM_Score;

import android.graphics.Canvas;
import android.graphics.Rect;

public class FM_Note extends FM_BaseNote {
    boolean beam;
    float ys, startX;
    float StemTopY = 0f;
    public boolean isTieStart = false;
    public boolean isTieEnd = false;
    @FM_ClefValue int clef;

    public FM_Note(FM_Score Score, @FM_NoteValue int Note, int Octave, @FM_ClefValue int clef, @FM_Accidental int Accidental, @FM_DurationValue int Duration, int voice) {
        super(FM_NoteType.NOTE, Score.ScoreBase);
        this.note = Note;
        this.accidental = Accidental;
        this.octave = Octave;
        this.duration = Duration;
        this.stem = true;
        this.stem_up = true;
        this.beam = false;
        this.voice = voice;
        this.clef = clef;
    }
    public FM_Note(FM_ScoreBase Score, @FM_NoteValue int Note, int Octave, @FM_ClefValue int clef, @FM_Accidental int Accidental, @FM_DurationValue int Duration, int voice) {
        super(FM_NoteType.NOTE, Score);
        this.note = Note;
        this.accidental = Accidental;
        this.octave = Octave;
        this.duration = Duration;
        this.stem = true;
        this.stem_up = true;
        this.beam = false;
        this.voice = voice;
        this.clef = clef;
    }

    public FM_Note(FM_ScoreBase Score, @FM_NoteValue int Note, int Octave, @FM_ClefValue int clef, @FM_Accidental int Accidental, @FM_DurationValue int Duration, int voice, int Color) {
        super(FM_NoteType.NOTE, Score);
        this.note = Note;
        this.accidental = Accidental;
        this.octave = Octave;
        this.duration = Duration;
        this.stem = true;
        this.stem_up = true;
        this.beam = false;
        this.tuplet = false;
        this.color = Color;
        this.voice = voice;
        this.clef = clef;
    }

    public FM_Note(FM_Score Score, @FM_NoteValue int Note, int Octave, @FM_ClefValue int clef, @FM_Accidental int Accidental, @FM_DurationValue int Duration, int voice, int Color) {
        super(FM_NoteType.NOTE, Score.ScoreBase);
        this.note = Note;
        this.accidental = Accidental;
        this.octave = Octave;
        this.duration = Duration;
        this.stem = true;
        this.stem_up = true;
        this.beam = false;
        this.tuplet = false;
        this.color = Color;
        this.voice = voice;
        this.clef = clef;
    }

    public FM_Note(FM_ScoreBase Score, @FM_NoteValue int Note, int Octave, @FM_ClefValue int clef, @FM_Accidental int Accidental, @FM_DurationValue int Duration, int voice, boolean StemUp) {
        super(FM_NoteType.NOTE, Score);
        this.note = Note;
        this.accidental = Accidental;
        this.octave = Octave;
        this.duration = Duration;
        this.stem = true;
        this.stem_up = StemUp;
        this.beam = false;
        this.tuplet = false;
        this.voice = voice;
        this.clef = clef;
    }

    public FM_Note(FM_Score Score, @FM_NoteValue int Note, int Octave, @FM_ClefValue int clef, @FM_Accidental int Accidental, @FM_DurationValue int Duration, int voice, boolean StemUp) {
        super(FM_NoteType.NOTE, Score.ScoreBase);
        this.note = Note;
        this.accidental = Accidental;
        this.octave = Octave;
        this.duration = Duration;
        this.stem = true;
        this.stem_up = StemUp;
        this.beam = false;
        this.tuplet = false;
        this.voice = voice;
        this.clef = clef;
    }

    public FM_Note(FM_ScoreBase Score, @FM_NoteValue int Note, int Octave, @FM_ClefValue int clef, @FM_Accidental int Accidental, @FM_DurationValue int Duration, int voice, boolean StemUp, int Color) {
        super(FM_NoteType.NOTE, Score);
        this.note = Note;
        this.accidental = Accidental;
        this.octave = Octave;
        this.duration = Duration;
        this.stem = true;
        this.stem_up = StemUp;
        this.beam = false;
        this.tuplet = false;
        this.color = Color;
        this.voice = voice;
        this.clef = clef;
    }

    public FM_Note(FM_Score Score, @FM_NoteValue int Note, int Octave, @FM_ClefValue int clef, @FM_Accidental int Accidental, @FM_DurationValue int Duration, int voice, boolean StemUp, int Color) {
        super(FM_NoteType.NOTE, Score.ScoreBase);
        this.note = Note;
        this.accidental = Accidental;
        this.octave = Octave;
        this.duration = Duration;
        this.stem = true;
        this.stem_up = StemUp;
        this.beam = false;
        this.tuplet = false;
        this.color = Color;
        this.voice = voice;
        this.clef = clef;
    }

    public int getOctave() {
        return octave;
    }
    void setOctave(int o) {
        octave = o;
    }

    float getDisplacement() {
        float offset = 0.0f;
        if (note.equals(FM_NoteValue.DO)) offset = 0.0f;
        if (note.equals(FM_NoteValue.RE)) offset = -0.5f;
        if (note.equals(FM_NoteValue.MI)) offset = -1.0f;
        if (note.equals(FM_NoteValue.FA)) offset = -1.5f;
        if (note.equals(FM_NoteValue.SOL)) offset = -2.0f;
        if (note.equals(FM_NoteValue.LA)) offset = -2.5f;
        if (note.equals(FM_NoteValue.SI)) offset = -3.0f;

        if (clef == FM_ClefValue.TREBLE) {
            offset = offset + 5.0f + 3.5f * (4 - octave);
            return offset;
        }
        if (clef == FM_ClefValue.BASS) {
            offset = offset + 6.0f + 3.5f * (2 - octave);
            return offset;
        }
        return 0.0f;
    }

    String asStringAccidental(){
        String s1 = "";
        String b = "";
        String e = "";
        int accidental = this.accidental;
        if (accidental > 100) {
            b = FM_Const.ParenthesisLeft;
            e = FM_Const.ParenthesisRight;
            accidental = accidental - 100;
        }
        if (accidental == FM_Accidental.Natural) s1 = b + FM_Const.Natural + e + " ";
        if (accidental == FM_Accidental.Flat) s1 = b + FM_Const.Flat + e + " ";
        if (accidental == FM_Accidental.Sharp) s1 = b + FM_Const.Sharp + e + " ";
        if (accidental == FM_Accidental.DoubleSharp) s1 = b + FM_Const.DoubleSharp + e + " ";
        if (accidental == FM_Accidental.DoubleFlat) s1 = b + FM_Const.DoubleFlat + e + " ";
        if (accidental == FM_Accidental.TripleSharp) s1 = b + FM_Const.Sharp+" "+FM_Const.DoubleSharp + e + " ";
        if (accidental == FM_Accidental.TripleFlat) s1 = b + FM_Const.TripleFlat + e + " ";
        return s1;
    }

    String asStringDot(){
        String s2 = "";
        if (duration>50) s2 = " " + FM_Const.Dot;
        return s2;
    }

    String asStringNote() {
        return asStringNote(stem);
    }

    String asStringNote(boolean stem){
        if (duration == 1 || duration == 51) return FM_Const._1Note;
        if (stem && !beam) {
            if (stem_up) {
                if (duration == 2 || duration == 52) return   FM_Const._2Note_up;
                if (duration == 4 || duration == 54) return   FM_Const._4Note_up;
                if (duration == 8 || duration == 58) return   FM_Const._8Note_up;
                if (duration == 16 || duration == 66) return FM_Const._16Note_up;
                if (duration == 32 || duration == 82) return FM_Const._32Note_up;
            } else {
                if (duration == 2 || duration == 52) return   FM_Const._2Note_down;
                if (duration == 4 || duration == 54) return   FM_Const._4Note_down;
                if (duration == 8 || duration == 58) return   FM_Const._8Note_down;
                if (duration == 16 || duration == 66) return FM_Const._16Note_down;
                if (duration == 32 || duration == 82) return FM_Const._32Note_down;
            }
        } else {
            if (duration == 2 || duration == 52) return FM_Const.EmptyNote;
            return FM_Const.FillNote;
        }
        return "";
    }

    String asString() {
        return asStringAccidental() + asStringNote() + asStringDot();
    }


    protected float WidthAccidental() {
        if (score.score == null) return 0;
        FM_Const.AdjustFont(score.score, FM_Const.Sharp, 2);
        return score.score.Font.measureText(asStringAccidental());
    }
    protected float WidthNoteNoStem() {
        if (score.score == null) return 0;
        FM_Const.AdjustFont(score.score, asStringNote(false), 1);
        return score.score.Font.measureText(asStringNote(false));
    }
    protected float WidthNote() {
        if (score.score == null) return 0;
        FM_Const.AdjustFont(score.score, asStringNote(false), 1);
        return score.score.Font.measureText(asStringNote(stem));
    }
    protected float WidthDot() {
        if (score.score == null) return 0;
        FM_Const.AdjustFont(score.score, FM_Const.Sharp, 2);
        return score.score.Font.measureText(asStringDot());
    }

    float Height(boolean all) {
        if (score.score == null) return 0;
        FM_Const.AdjustFont(score.score, asStringNote(false), 1);
        boolean tmp_beam = beam;
        if (all) beam = false;
        Rect bounds = new Rect();
        String s = asStringNote();
        score.score.Font.getTextBounds(s, 0, s.length(), bounds);
        beam = tmp_beam;
        return bounds.height();
    }

    void DrawNote(Canvas canvas) {
        if (score.score == null) return;
        if (!isVisible()) return;
        super.DrawNote(canvas);

        score.score.Font.setColor(score.score.getStaveLineColor());
        ys = StartY1;
        startX = StartX;
        float ly;
        float dy;
        boolean l = false;
        float offset = getDisplacement();
        if (offset >= 5.0f || offset < 0.0f) l = true;
        dy = StartY1 + offset * score.score.getDistanceBetweenStaveLines();

        float width_accidental = WidthAccidental();
        float widthNoteNoStem = WidthNoteNoStem();
        if (l) {
            if (offset >= 5.0f)
                for (int i = 5; i <= offset; i++) {
                    ly = StartY1 + i * score.score.getDistanceBetweenStaveLines();
                    float tX = StartX + paddingLeft + width_accidental + paddingNote - score.score.getDistanceBetweenStaveLines() / 3;
                    float tXe = StartX + paddingLeft + width_accidental + paddingNote + widthNoteNoStem + score.score.getDistanceBetweenStaveLines() / 3;
                    float tY = ly - FM_Const.dpTOpx(score.score.getContext(),0.25f);
                    float tYe = ly + FM_Const.dpTOpx(score.score.getContext(),0.25f);
                    canvas.drawRect(tX, tY,tXe, tYe, score.score.Font);
                }
            if (offset < 0.0f)
                for (int i = -1; i >= offset; i--) {
                    ly = StartY1 + i * score.score.getDistanceBetweenStaveLines();
                    float tX = StartX + paddingLeft + width_accidental + paddingNote - score.score.getDistanceBetweenStaveLines() / 3;
                    float tXe = StartX + paddingLeft + width_accidental + paddingNote + widthNoteNoStem + score.score.getDistanceBetweenStaveLines() / 3;
                    float tY = ly - FM_Const.dpTOpx(score.score.getContext(),0.25f);
                    float tYe = ly + FM_Const.dpTOpx(score.score.getContext(),0.25f);
                    canvas.drawRect(tX, tY,tXe, tYe, score.score.Font);
                }
        }
        score.score.Font.setColor(score.score.getColor());
        score.score.Font.setColor(color);

        FM_Const.AdjustFont(score.score, FM_Const.Sharp, 2);
        canvas.drawText(asStringAccidental(), StartX + paddingLeft, dy, score.score.Font);
        FM_Const.AdjustFont(score.score, FM_Const.EmptyNote, 1);
        canvas.drawText(asStringNote(), StartX + paddingLeft + width_accidental + paddingNote, dy, score.score.Font);

        if (duration>50) {
            float adjustDotY = 0;
            if (Math.abs(offset) - Math.floor(Math.abs(offset)) < 0.1) {
                if (stem_up) adjustDotY = -score.score.getDistanceBetweenStaveLines() * 0.2f;
                else adjustDotY = +score.score.getDistanceBetweenStaveLines() * 0.2f;
            }
            canvas.drawText(asStringDot(), StartX + paddingLeft + width_accidental + paddingNote + widthNoteNoStem + paddingDot, dy + adjustDotY, score.score.Font);
        }
        score.score.Font.setColor(score.score.getColor());
    }

    float Left(){
        return  StartX + paddingLeft;
    }

    float Bottom() {
        if (score.score == null) return 0;
        float offset = getDisplacement();
        float dy = StartY1 + offset * score.score.getDistanceBetweenStaveLines();
        if (stem_up) {
            return dy + 0.5f * score.score.getDistanceBetweenStaveLines();
        } else {
            return dy - 0.5f * score.score.getDistanceBetweenStaveLines();
        }
    }
    float Right() {
        if (score.score == null) return 0;
        float w1 = StartX + paddingLeft + WidthAccidental() + paddingNote + WidthNoteNoStem() + paddingDot + WidthDot();
        float w2 = StartX + paddingLeft + WidthAccidental() + paddingNote + WidthNote();
        return Math.max(w1, w2);
    }
    float Top(){
        if (score.score == null) return 0;
        float by = Bottom();
        if (stem_up) {
            return by - Height(stem);
        } else {
            return by + Height(stem);
        }
    }
    @FM_NoteValue
    public int getNote() {
        return note;
    }
}
