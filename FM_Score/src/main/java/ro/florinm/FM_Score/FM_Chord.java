package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class FM_Chord extends FM_BaseNote {
    List<FM_Note> Notes;
    float ys, startX;

    public FM_Chord(FM_Score Stave) {
        super(FM_NoteType.CHORD, Stave);
        this.Notes = new ArrayList<>();
    }

    public void addNote(FM_Note note){
        Notes.add(note);
    }

    public void Compute(Paint font) {
        //Sort them by clef
        FM_Note tmp;
        for (int i = 0; i < Notes.size() - 1; i++)
            for (int j = i + 1; j < Notes.size(); j++) {
                if (Notes.get(i).clef >= Notes.get(j).clef) {
                    tmp = Notes.get(i);
                    Notes.set(i, Notes.get(j));
                    Notes.set(j, tmp);
                }
            }
        //Sort them by displacement
        for (int i = 0; i < Notes.size() - 1; i++)
            for (int j = i + 1; j < Notes.size(); j++) {
                if (Notes.get(i).clef.equals(Notes.get(j).clef) && Notes.get(i).getDisplacement() <= Notes.get(j).getDisplacement()) {
                    tmp = Notes.get(i);
                    Notes.set(i, Notes.get(j));
                    Notes.set(j, tmp);
                }
            }
        //if the distance between the notes is 0, remove accidental from second note
        for (int i = 0; i < Notes.size(); i++)
            for (int j = i + 1; j < Notes.size(); j++) {
                int distance = Math.abs(FM_Const.distanceBetweenNotes(Notes.get(i), Notes.get(j)));
                if (distance == 0) {
                    Notes.get(j).setAccidental(FM_Accidental.None);      //remove accidental
                }
            }
        //Get the maximum width without the DOT
        float maxW = -10000;
        float w = 0;
        for (int i = 0; i < Notes.size(); i++) {
            w = Notes.get(i).WidthAllNoDot(font);
            if (w > maxW) maxW = w;
        }
        //Pad the notes to have the same width, without the DOT (aligning them)
        for (int i = 0; i < Notes.size(); i++) {
            w = Notes.get(i).WidthAllNoDot(font);
            Notes.get(i).setPadding(maxW - w);
        }
        //if the distance between the notes is 0, and notes
        for (int i = 0; i < Notes.size(); i++)
            for (int j = i + 1; j < Notes.size(); j++) {
                int distance = Math.abs(FM_Const.distanceBetweenNotes(Notes.get(i), Notes.get(j)));
                if (distance == 0)
                if ((Notes.get(i).duration == FM_DurationValue.NOTE_WHOLE || Notes.get(i).duration == FM_DurationValue.NOTE_WHOLE_D) || (Notes.get(i).duration!=Notes.get(j).duration)) {
                        float all_width = (Notes.get(i).WidthAll(font) - Notes.get(i).WidthAccidental(font) + Notes.get(j).WidthAll(font)) / 2f;
                        Notes.get(i).setPadding(Notes.get(i).getPadding() - all_width * 0.5f);      //pad the dot on first note
                        Notes.get(j).setPadding(Notes.get(j).getPadding() + all_width * 0.5f);    //pad the note on the second note
                        if (Notes.get(i).stem_up == Notes.get(j).stem_up) Notes.get(j).stem = false;
                    }
            }
        //if the distance between the notes is 1, displace one of the notes
        for (int i = 0; i < Notes.size(); i++)
            for (int j = i + 1; j < Notes.size(); j++) {
                int distance = Math.abs(FM_Const.distanceBetweenNotes(Notes.get(i), Notes.get(j)));
                if (distance == 1) {
                    Notes.get(i).setPaddingDot(Notes.get(i).paddingDot + Notes.get(j).WidthNote(font) * 0.8f);      //pad the dot on first note
                    Notes.get(j).setPaddingNote(Notes.get(j).paddingNote + Notes.get(i).WidthNote(font) * 0.8f);    //pad the note on the second note
                    if (Notes.get(i).stem_up == Notes.get(j).stem_up) Notes.get(j).stem = false;
                }
            }
        //pad the accidentals when the distance between notes is les or equal to 3
        for (int i = 0; i < Notes.size(); i++)
            for (int j = i + 1; j < Notes.size(); j++) {
                int distance = Math.abs(FM_Const.distanceBetweenNotes(Notes.get(i), Notes.get(j)));
                if (distance <= 3 && distance!= 0 && Notes.get(i).paddingNote == 0) {
                    Notes.get(j).setPadding(Notes.get(j).padding - Notes.get(i).paddingNote - Notes.get(i).WidthAccidental(font));
                    Notes.get(j).setPaddingNote(Notes.get(j).paddingNote + Notes.get(i).paddingNote + Notes.get(i).WidthAccidental(font));
                }
            }
        //if any of the notes has a negative padding, move all of them right
        float minP = 0;
        float p = 0;
        for (int i = 0; i < Notes.size(); i++) {
            p = Notes.get(i).padding;
            if (p < 0 && p < minP) minP = p;
        }
        if (minP < 0)
            for (int i = 0; i < Notes.size(); i++) {
                Notes.get(i).setPadding(Notes.get(i).padding - minP);
            }
    }

    public String toString() {
        return "";
    }

    @Override
    public float WidthAll(Paint font, boolean all) {
        return WidthAll(font);
    }

    public float WidthAll(Paint font) {
        int i = 0;
        float w = 0;
        float maxW = 0;
        while (i < Notes.size()) {
            w = Notes.get(i).padding + Notes.get(i).WidthAll(font);
            if (w > maxW) maxW = w;
            i++;
        }
        return maxW;
    }

    public float WidthAccidental(Paint font) {
        return 0f;
    }

    public float WidthNote(Paint font) {
        return 0f;
    }

    public float WidthAllNoDot(Paint font) {
        return 0f;
    }

    @Override
    public void SetDrawParameters(float x, float ys1, float ys2) {
        StartX = x;
        StartY1 = ys1;
        StartY2 = ys2;
        for (int i = 0; i< Notes.size(); i++) {
            if (Notes.get(i).clef == Stave.getFirstStaveClef())
                Notes.get(i).SetDrawParameters(StartX, ys1, ys2);
            if (Notes.get(i).clef == Stave.getSecondStaveClef())
                Notes.get(i).SetDrawParameters(StartX, ys2, ys2);
        }
    }

    public void DrawNote(Canvas canvas) {
        if (!isVisible()) return;
        super.DrawNote(canvas);

        for (int i = 0; i< Notes.size(); i++) {
            if (Notes.get(i).clef == Stave.getFirstStaveClef()) Notes.get(i).DrawNote(canvas);
            if (Notes.get(i).clef == Stave.getSecondStaveClef()) Notes.get(i).DrawNote(canvas);
        }
    }

    @Override
    public float getDisplacement() {
        return 0;
    }
    public float Left(){
        return 0;
    };
    public float Bottom() {
        return 0;
    }
    public float Right() {
        return 0;
    }
    public float Top(){
        return 0;
    }
}
