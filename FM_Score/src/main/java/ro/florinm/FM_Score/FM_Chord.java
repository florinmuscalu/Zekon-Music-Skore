package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class FM_Chord extends FM_BaseNote {
    List<FM_BaseNote> Notes;

    public FM_Chord(FM_Score Score) {
        super(FM_NoteType.CHORD, Score);
        this.Notes = new ArrayList<>();
        duration = 0;
    }

    public void addNote(FM_BaseNote note){
        Notes.add(note);
        if (FM_Const.getDurationMs(note.duration) > FM_Const.getDurationMs(duration))
            duration = note.duration;
    }

    public void Compute() {
        FM_BaseNote tmp;
        for (int i = 0; i < Notes.size() - 1; i++)
            for (int j = i + 1; j < Notes.size(); j++) {
                //Step 1
                //Fist step is to sort the notes in the chord by staff. If they belong to the same staff, sort them by Displacement (by note, basically)
                if ((Notes.get(i).staff > Notes.get(j).staff) || (Notes.get(i).staff == Notes.get(j).staff && Notes.get(i).getDisplacement() <= Notes.get(j).getDisplacement())) {
                    tmp = Notes.get(i);
                    Notes.set(i, Notes.get(j));
                    Notes.set(j, tmp);
                }
            }

        //Step 2
        //if the distance between any two notes is 0, remove accidental and/or dot from the second note
        for (int i = 0; i < Notes.size(); i++)
            for (int j = i + 1; j < Notes.size(); j++) {
                int distance = Math.abs(FM_Const.distanceBetweenNotes(Notes.get(i), Notes.get(j)));
                if (distance == 0) {
                    Notes.get(j).setAccidental(FM_Accidental.None);      //remove accidental
                    if (Notes.get(i).duration == Notes.get(j).duration && Notes.get(i).duration > 50) {
                        Notes.get(j).duration = Notes.get(j).duration - 50;
                    }
                }
            }

        //Step 3
        //Get the maximum width without the DOT
        float maxW = 0;
        float w = 0;
        for (int i = 0; i < Notes.size(); i++) {
            w = Notes.get(i).WidthNoDotNoStem() - Notes.get(i).WidthNoteNoStem() / 2f;
            if (w > maxW) maxW = w;
        }

        //Step 4
        //Pad the notes to have the same width, without the DOT (aligning them)
        for (int i = 0; i < Notes.size(); i++) {
            w = Notes.get(i).WidthNoDotNoStem() - Notes.get(i).WidthNoteNoStem() / 2f;
            Notes.get(i).setPaddingLeft(maxW - w);
        }

        for (int i = 0; i < Notes.size(); i++)
            for (int j = i + 1; j < Notes.size(); j++) {
                int distance = Math.abs(FM_Const.distanceBetweenNotes(Notes.get(i), Notes.get(j)));

                //Step 5
                //if the distance between the notes is 0, line them up
                if (distance == 0) {
                    int ni = 0;
                    int nj = 0;
                    if (Notes.get(i).duration == FM_DurationValue.NOTE_WHOLE || Notes.get(i).duration == FM_DurationValue.NOTE_WHOLE_D) ni = 1;
                    if (Notes.get(i).duration == FM_DurationValue.NOTE_HALF || Notes.get(i).duration == FM_DurationValue.NOTE_HALF_D) ni = 2;

                    if (Notes.get(j).duration == FM_DurationValue.NOTE_WHOLE || Notes.get(j).duration == FM_DurationValue.NOTE_WHOLE_D) nj = 1;
                    if (Notes.get(j).duration == FM_DurationValue.NOTE_HALF || Notes.get(j).duration == FM_DurationValue.NOTE_HALF_D) nj = 2;

                    if (ni == 1 || ni != nj) {
                        //float all_width = (Notes.get(i).Width() - Notes.get(i).WidthAccidental() + Notes.get(j).Width()) / 2f;
                        float all_width = (Notes.get(i).WidthNote() + Notes.get(j).WidthNote()) / 2f;
                        Notes.get(i).setPaddingLeft(Notes.get(i).getPaddingLeft() - all_width * 0.5f);      //pad the dot on first note
                        Notes.get(j).setPaddingLeft(Notes.get(j).getPaddingLeft() + all_width * 0.5f);    //pad the note on the second note
                        if (Notes.get(i).stem_up == Notes.get(j).stem_up) Notes.get(j).stem_up = !Notes.get(j).stem_up;
                    }
                }

                //Step 6
                //if the distance between the notes is 1, displace one of the notes
                if (distance == 1) {
                    Notes.get(i).setPaddingDot(Notes.get(i).paddingDot + Notes.get(j).WidthNote() * 0.8f);      //pad the dot on first note
                    Notes.get(j).setPaddingNote(Notes.get(j).paddingNote + Notes.get(j).WidthNote() * 0.8f);    //pad the note on the second note
                    if (Notes.get(i).stem_up == Notes.get(j).stem_up) Notes.get(j).stem_up = !Notes.get(j).stem_up;
                }

                //Step 7
                //pad the accidentals when the distance between notes is les or equal to 3
                if (distance <= 3 && distance!= 0 && Notes.get(i).paddingNote == 0) {
                    Notes.get(j).setPaddingLeft(Notes.get(j).paddingLeft - Notes.get(i).paddingNote - Notes.get(i).WidthAccidental());
                    Notes.get(j).setPaddingNote(Notes.get(j).paddingNote + Notes.get(i).paddingNote + Notes.get(i).WidthAccidental());
                }
            }

        //Step 8
        //if any of the notes has a negative padding, move all of them right
        float minP = 0;
        float p = 0;
        for (int i = 0; i < Notes.size(); i++) {
            p = Notes.get(i).paddingLeft;
            if (p < 0 && p < minP) minP = p;
        }
        if (minP < 0)
            for (int i = 0; i < Notes.size(); i++) {
                Notes.get(i).setPaddingLeft(Notes.get(i).paddingLeft - minP);
            }
    }

    public String asString() {
        return "";
    }

    @Override
    public float Width() {
        int i = 0;
        float w = 0;
        float maxW = 0;
        while (i < Notes.size()) {
            w = Notes.get(i).Width();
            if (w > maxW) maxW = w;
            i++;
        }
        return maxW;
    }
    public float WidthAccidental() {
        return 0f;
    }

    public float WidthNote() {
        return 0f;
    }

    public float WidthAllNoDot() {
        return 0f;
    }
    protected float WidthNoteNoStem(){
        return 0;
    }
    protected float WidthDot(){
        return 0;
    }

    @Override
    public void SetDrawParameters(float x, float ys1, float ys2) {
        StartX = x;
        StartY1 = ys1;
        StartY2 = ys2;
        for (int i = 0; i< Notes.size(); i++) {
            if (Notes.get(i).staff == 0) Notes.get(i).SetDrawParameters(StartX, ys1, ys2);
            if (Notes.get(i).staff == 1) Notes.get(i).SetDrawParameters(StartX, ys2, ys2);
        }
    }

    public void DrawNote(Canvas canvas) {
        if (!isVisible()) return;
        for (int i = 0; i< Notes.size(); i++) {
            if (Notes.get(i).staff == 0) Notes.get(i).DrawNote(canvas);
            if (Notes.get(i).staff == 1) Notes.get(i).DrawNote(canvas);
        }
        super.DrawNote(canvas);
    }

    @Override
    public float getDisplacement() {
        return 0;
    }
    public float Left(){
        float left = Notes.get(0).Left();
        for (int i = 0; i< Notes.size(); i++) if (Notes.get(i).Left() < left) left = Notes.get(i).Left();
        return left;
    };
    public float Bottom() {
        float bottom = Notes.get(0).Bottom();
        for (int i = 0; i< Notes.size(); i++) if (Notes.get(i).Bottom() < bottom) bottom = Notes.get(i).Bottom();
        for (int i = 0; i< Notes.size(); i++) if (Notes.get(i).Top() < bottom) bottom = Notes.get(i).Top();
        return bottom;
    }
    public float Right() {
        float right = Notes.get(0).Right();
        for (int i = 0; i< Notes.size(); i++) if (Notes.get(i).Right() > right) right = Notes.get(i).Right();
        return right;
    }
    public float Top(){
        float top = Notes.get(0).Top();
        for (int i = 0; i< Notes.size(); i++) if (Notes.get(i).Top() > top) top = Notes.get(i).Top();
        for (int i = 0; i< Notes.size(); i++) if (Notes.get(i).Bottom() > top) top = Notes.get(i).Bottom();
        return top;
    }

    public void setColor(int color) {
        this.color = color;
        for (int i = 0; i< Notes.size(); i++) Notes.get(i).setColor(color);
    }

    public void setVisible(boolean visible) {
        //if (!visible) line = -1;
        //else line = 1;
        this.visible = visible;
        for (int i = 0; i< Notes.size(); i++) Notes.get(i).setVisible(visible);
    }
}
