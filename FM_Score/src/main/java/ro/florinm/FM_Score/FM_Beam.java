package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

public class FM_Beam {
    int index;
    List<FM_Note> n;
    FM_Score score;
    public FM_Beam(FM_Score score, int index) {
        n = new ArrayList<>();
        this.index = index;
        this.score = score;
    }

    public void AddNote(FM_Note n) {
        this.n.add(n);
    }

    public void Draw(Canvas canvas) {
        float x, xe, y, ye;
        int EndIndex = n.size() - 1;
        float StemLength = 2.5f;
        if (n.get(0).stem_up) {
            x = n.get(0).startX +
                    n.get(0).paddingLeft +
                    n.get(0).WidthAccidental() +
                    n.get(0).paddingNote +
                    n.get(0).WidthNoteNoStem();

            xe = n.get(EndIndex).startX +
                    n.get(EndIndex).paddingLeft +
                    n.get(EndIndex).WidthAccidental() +
                    n.get(EndIndex).paddingNote +
                    n.get(EndIndex).WidthNoteNoStem();

            y =  n.get(0).ys +          (n.get(0).getDisplacement() - StemLength) *        score.getDistanceBetweenStaveLines();
            ye = n.get(EndIndex).ys +   (n.get(EndIndex).getDisplacement() - StemLength) * score.getDistanceBetweenStaveLines();

            if (ye>y) {
                float slope = FM_Const.slope(x, y, xe, ye);
                ye = FM_Const.getY2(slope, x, y, xe);
            }
            else {
                float slope = FM_Const.slope(xe, ye, x, y);
                y = FM_Const.getY2(slope, xe, ye, x);
            }
            float yMiddleMin = n.get(1).ys + (n.get(1).getDisplacement() - StemLength) * score.getDistanceBetweenStaveLines();

            for (int i = 2; i < n.size() - 1; i++) {
                float yMiddle = n.get(i).ys + (n.get(i).getDisplacement() - StemLength) * score.getDistanceBetweenStaveLines();
                if (yMiddle < yMiddleMin) yMiddleMin = yMiddle;
            }
            if ((y + ye) / 2 > yMiddleMin) {
                float diff = (y + ye) / 2 - yMiddleMin;
                y = y - diff;
                ye = ye - diff;
            }
        } else {
            x = n.get(0).startX +
                    n.get(0).paddingLeft +
                    n.get(0).WidthAccidental() +
                    n.get(0).paddingNote;

            xe = n.get(EndIndex).startX +
                    n.get(EndIndex).paddingLeft +
                    n.get(EndIndex).WidthAccidental() +
                    n.get(EndIndex).paddingNote;

            y = n.get(0).ys +           (n.get(0).getDisplacement() + StemLength + 1) * score.getDistanceBetweenStaveLines();
            ye = n.get(EndIndex).ys +   (n.get(EndIndex).getDisplacement() + StemLength + 1) * score.getDistanceBetweenStaveLines();

            if (ye<y) {
                float slope = FM_Const.slope(x, y, xe, ye);
                ye = FM_Const.getY2(slope, x, y, xe);
            }
            else {
                float slope = FM_Const.slope(xe, ye, x, y);
                y = FM_Const.getY2(slope, xe, ye, x);
            }
            float yMiddleMin = n.get(1).ys + (n.get(1).getDisplacement() + StemLength + 1) * score.getDistanceBetweenStaveLines();

            for (int i = 2; i < n.size() - 1; i++) {
                float yMiddle = n.get(i).ys + (n.get(i).getDisplacement() + StemLength + 1) * score.getDistanceBetweenStaveLines();
                if (yMiddle > yMiddleMin) yMiddleMin = yMiddle;
            }
            if ((y + ye) / 2 < yMiddleMin) {
                float diff = (y + ye) / 2 - yMiddleMin;
                y = y - diff;
                ye = ye - diff;
            }
        }

        //below is the code for stems
        float slope = FM_Const.slope(x, y, xe, ye);
        if (n.get(0).stem_up) {
            for (int i = 0; i < n.size(); i++) {
                float tmpX = n.get(i).startX + n.get(i).paddingLeft + n.get(i).WidthAccidental() + n.get(i).paddingNote + n.get(i).WidthNoteNoStem();
                float tmpY = n.get(i).ys + n.get(i).getDisplacement() * score.getDistanceBetweenStaveLines();
                float tmpY2 = FM_Const.getY2(slope, x, y, tmpX);

                canvas.drawRect(tmpX,
                        tmpY - FM_Const.dpTOpx(score.getContext(), 1),
                        tmpX - FM_Const.dpTOpx(score.getContext(), 1),
                        tmpY2 - score.getDistanceBetweenStaveLines() / 2 + FM_Const.dpTOpx(score.getContext(), 1),
                        score.Font);
            }
        } else {
            for (int i = 0; i < n.size(); i++) {
                float tmpX = n.get(i).startX + n.get(i).paddingLeft + n.get(i).WidthAccidental() + n.get(i).paddingNote;
                float tmpY = n.get(i).ys + n.get(i).getDisplacement() * score.getDistanceBetweenStaveLines();
                float tmpY2 = FM_Const.getY2(slope, x, y, tmpX);

                canvas.drawRect(tmpX,
                        tmpY + FM_Const.dpTOpx(score.getContext(), 1),
                        tmpX + FM_Const.dpTOpx(score.getContext(), 1),
                        tmpY2 - score.getDistanceBetweenStaveLines() / 2 - FM_Const.dpTOpx(score.getContext(), 1),
                        score.Font);
            }
        }

        //below is the core for the principal beam
        Path topPath = new Path();
        if (!n.get(0).stem_up) {
            topPath.reset();
            topPath.moveTo(x, y - score.getDistanceBetweenStaveLines() / 2);
            topPath.lineTo(xe + FM_Const.dpTOpx(score.getContext(), 1), ye - score.getDistanceBetweenStaveLines() / 2);
            topPath.lineTo(xe + FM_Const.dpTOpx(score.getContext(), 1), ye - score.getDistanceBetweenStaveLines());
            topPath.lineTo(x, y - score.getDistanceBetweenStaveLines());
            topPath.lineTo(x, y);
        } else {
            topPath.reset();
            topPath.moveTo(x - FM_Const.dpTOpx(score.getContext(), 1), y);
            topPath.lineTo(xe, ye);
            topPath.lineTo(xe, ye - score.getDistanceBetweenStaveLines() / 2);
            topPath.lineTo(x - FM_Const.dpTOpx(score.getContext(), 1), y - score.getDistanceBetweenStaveLines() / 2);
            topPath.lineTo(x - FM_Const.dpTOpx(score.getContext(), 1), y);
        }
        canvas.drawPath(topPath, score.Font);

        //below is the code for beams for 1/16 note
        for (int i = 0; i < n.size() - 1; i++) {
            boolean drawBegin = true;
            boolean drawEnd = true;
            if (n.get(i).duration != FM_DurationValue.NOTE_SIXTEENTH && n.get(i).duration != FM_DurationValue.NOTE_SIXTEENTH_D)
                drawBegin = false;
            if (n.get(i + 1).duration != FM_DurationValue.NOTE_SIXTEENTH && n.get(i + 1).duration != FM_DurationValue.NOTE_SIXTEENTH_D)
                drawEnd = false;

            if (drawBegin && !drawEnd && i > 0)
                if (n.get(i - 1).duration == FM_DurationValue.NOTE_SIXTEENTH || n.get(i - 1).duration == FM_DurationValue.NOTE_SIXTEENTH_D)
                    drawBegin = false;
            if (!drawBegin && drawEnd && i < n.size() - 2)
                if (n.get(i + 2).duration == FM_DurationValue.NOTE_SIXTEENTH || n.get(i + 2).duration == FM_DurationValue.NOTE_SIXTEENTH_D)
                    drawEnd = false;

            if (!drawBegin && !drawEnd) {
                continue;
            }
            float tmpX1 = 0;
            float tmpX2 = 0;
            if (drawBegin && drawEnd) {
                tmpX1 = n.get(i).startX + n.get(i).paddingLeft + n.get(i).WidthAccidental() + n.get(i).paddingNote;
                if (n.get(0).stem_up) tmpX1 = tmpX1 + n.get(i).WidthNoteNoStem() - FM_Const.dpTOpx(score.getContext(), 1);
                tmpX2 = n.get(i + 1).startX + n.get(i + 1).paddingLeft + n.get(i + 1).WidthAccidental() + n.get(i + 1).paddingNote;
                if (n.get(0).stem_up) tmpX2 = tmpX2 + n.get(i+1).WidthNoteNoStem();
                else tmpX2 = tmpX2 + FM_Const.dpTOpx(score.getContext(), 1);
            }
            if (drawBegin && !drawEnd) {
                tmpX1 = n.get(i).startX + n.get(i).paddingLeft + n.get(i).WidthAccidental() + n.get(i).paddingNote;
                if (n.get(0).stem_up) tmpX1 = tmpX1 + n.get(i).WidthNoteNoStem() - FM_Const.dpTOpx(score.getContext(), 1);
                tmpX2 = tmpX1 + score.getDistanceBetweenStaveLines() * 2 / 3;
            }
            if (!drawBegin && drawEnd) {
                tmpX2 = n.get(i + 1).startX + n.get(i + 1).paddingLeft + n.get(i + 1).WidthAccidental() + n.get(i + 1).paddingNote;
                if (n.get(0).stem_up) tmpX2 = tmpX2 + n.get(i + 1).WidthNoteNoStem() - FM_Const.dpTOpx(score.getContext(), 1);
                tmpX1 = tmpX2 - score.getDistanceBetweenStaveLines() * 2 / 3;
            }
            float tmpY1 = FM_Const.getY2(slope, x, y, tmpX1);
            float tmpY2 = FM_Const.getY2(slope, x, y, tmpX2);
            if (n.get(0).stem_up) {
                tmpY1 = tmpY1 + score.getDistanceBetweenStaveLines() * 2 / 3;
                tmpY2 = tmpY2 + score.getDistanceBetweenStaveLines() * 2 / 3;
            } else {
                tmpY1 = tmpY1 - score.getDistanceBetweenStaveLines() * 6 / 5;
                tmpY2 = tmpY2 - score.getDistanceBetweenStaveLines() * 6 / 5;
            }
            Path bottomPath = new Path();
            bottomPath.reset();
            bottomPath.moveTo(tmpX1, tmpY1);
            bottomPath.lineTo(tmpX2, tmpY2);
            bottomPath.lineTo(tmpX2, tmpY2 - score.getDistanceBetweenStaveLines() / 2);
            bottomPath.lineTo(tmpX1, tmpY1 - score.getDistanceBetweenStaveLines() / 2);
            bottomPath.lineTo(tmpX1, tmpY1);
            canvas.drawPath(bottomPath, score.Font);
        }
    }
}
