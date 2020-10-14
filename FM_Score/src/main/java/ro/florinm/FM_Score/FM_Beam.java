package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

public class FM_Beam {
    int index;
    List<FM_Note> n;

    public FM_Beam(int index) {
        n = new ArrayList<>();
        this.index = index;
    }

    public void AddNote(FM_Note n) {
        this.n.add(n);
    }

    public void Draw(FM_Score stave, Canvas canvas) {
        float x, xe, y, ye;
        if (n.get(0).stem_up) {
            x = n.get(0).startX +
                    n.get(0).padding +
                    n.get(0).WidthAccidental(stave.StaveFont) +
                    n.get(0).paddingNote +
                    n.get(0).WidthNote(stave.StaveFont);

            xe = n.get(n.size() - 1).startX +
                    n.get(n.size() - 1).padding +
                    n.get(n.size() - 1).WidthAccidental(stave.StaveFont) +
                    n.get(n.size() - 1).paddingNote +
                    n.get(n.size() - 1).WidthNote(stave.StaveFont);

            y = n.get(0).ys +
                    n.get(0).getDisplacement() * stave.getDistanceBetweenStaveLines() -
                    n.get(0).Height(stave.StaveFont, true) * 2 / 3;
            ye = n.get(n.size() - 1).ys +
                    n.get(n.size() - 1).getDisplacement() * stave.getDistanceBetweenStaveLines() -
                    n.get(n.size() - 1).Height(stave.StaveFont, true) * 2 / 3;

            float yMiddleMin = n.get(1).ys +
                    n.get(1).getDisplacement() * stave.getDistanceBetweenStaveLines() -
                    n.get(1).Height(stave.StaveFont, true) * 2 / 3;

            for (int i = 2; i < n.size() - 1; i++) {
                float yMiddle = n.get(i).ys +
                        n.get(i).getDisplacement() * stave.getDistanceBetweenStaveLines() -
                        n.get(i).Height(stave.StaveFont, true) * 2 / 3;
                if (yMiddle < yMiddleMin) yMiddleMin = yMiddle;
            }
            if ((y + ye) / 2 > yMiddleMin) {
                float diff = (y + ye) / 2 - yMiddleMin;
                y = y - diff;
                ye = ye - diff;
            }
        } else {
            x = n.get(0).startX +
                    n.get(0).padding +
                    n.get(0).WidthAccidental(stave.StaveFont) +
                    n.get(0).paddingNote;

            xe = n.get(n.size() - 1).startX +
                    n.get(n.size() - 1).padding +
                    n.get(n.size() - 1).WidthAccidental(stave.StaveFont) +
                    n.get(n.size() - 1).paddingNote;

            y = n.get(0).ys +
                    n.get(0).getDisplacement() * stave.getDistanceBetweenStaveLines() +
                    n.get(0).Height(stave.StaveFont, true) * 2 / 3 +
                    n.get(0).Height(stave.StaveFont, false) - 0.5f * stave.getDistanceBetweenStaveLines();
            ye = n.get(n.size() - 1).ys +
                    n.get(n.size() - 1).getDisplacement() * stave.getDistanceBetweenStaveLines() +
                    n.get(n.size() - 1).Height(stave.StaveFont, true) * 2 / 3 +
                    n.get(n.size() - 1).Height(stave.StaveFont, false) - 0.5f * stave.getDistanceBetweenStaveLines();

            float yMiddleMin = n.get(1).ys +
                    n.get(1).getDisplacement() * stave.getDistanceBetweenStaveLines() +
                    n.get(1).Height(stave.StaveFont, true) * 2 / 3  +
                    n.get(1).Height(stave.StaveFont, false) - 0.5f * stave.getDistanceBetweenStaveLines();

            for (int i = 2; i < n.size() - 1; i++) {
                float yMiddle = n.get(i).ys +
                        n.get(i).getDisplacement() * stave.getDistanceBetweenStaveLines() +
                        n.get(i).Height(stave.StaveFont, true) * 2 / 3 +
                        n.get(i).Height(stave.StaveFont, false) - 0.5f * stave.getDistanceBetweenStaveLines();
                if (yMiddle > yMiddleMin) yMiddleMin = yMiddle;
            }
            if ((y + ye) / 2 < yMiddleMin) {
                float diff = (y + ye) / 2 - yMiddleMin;
                y = y - diff;
                ye = ye - diff;
            }
        }

        float slope = FM_Const.slope(x, y, xe, ye);

        if (n.get(0).stem_up) {
            for (int i = 0; i < n.size(); i++) {
                float tmpX = n.get(i).startX + n.get(i).padding + n.get(i).WidthAccidental(stave.StaveFont) + n.get(i).paddingNote + n.get(i).WidthNote(stave.StaveFont);
                float tmpY = n.get(i).ys + n.get(i).getDisplacement() * stave.getDistanceBetweenStaveLines();
                float tmpY2 = FM_Const.getY2(slope, x, y, tmpX);

                canvas.drawRect(tmpX, tmpY, tmpX - FM_Const.dpTOpx(stave.getContext(), 1), tmpY2 - stave.getDistanceBetweenStaveLines() / 2, stave.StaveFont);
            }
        } else {
            for (int i = 0; i < n.size(); i++) {
                float tmpX = n.get(i).startX + n.get(i).padding + n.get(i).WidthAccidental(stave.StaveFont) + n.get(i).paddingNote;
                float tmpY = n.get(i).ys + n.get(i).getDisplacement() * stave.getDistanceBetweenStaveLines();
                float tmpY2 = FM_Const.getY2(slope, x, y, tmpX);

                canvas.drawRect(tmpX, tmpY, tmpX + FM_Const.dpTOpx(stave.getContext(), 1), tmpY2 - stave.getDistanceBetweenStaveLines() / 2, stave.StaveFont);
            }
        }


        if (!n.get(0).stem_up) {
            xe = xe + 3;
        }
        Path topPath = new Path();
        topPath.reset();
        topPath.moveTo(x, y);
        topPath.lineTo(xe, ye);
        topPath.lineTo(xe, ye - stave.getDistanceBetweenStaveLines() / 2);
        topPath.lineTo(x, y - stave.getDistanceBetweenStaveLines() / 2);
        topPath.lineTo(x, y);

        canvas.drawPath(topPath, stave.StaveFont);

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
                tmpX1 = n.get(i).startX + n.get(i).padding + n.get(i).WidthAccidental(stave.StaveFont) + n.get(i).paddingNote;
                if (n.get(0).stem_up) tmpX1 = tmpX1 + n.get(i).WidthNote(stave.StaveFont);
                tmpX2 = n.get(i + 1).startX + n.get(i + 1).padding + n.get(i + 1).WidthAccidental(stave.StaveFont) + n.get(i + 1).paddingNote;
                if (n.get(0).stem_up) tmpX2 = tmpX2 + n.get(i+1).WidthNote(stave.StaveFont);
            }
            if (drawBegin && !drawEnd) {
                tmpX1 = n.get(i).startX + n.get(i).padding + n.get(i).WidthAccidental(stave.StaveFont) + n.get(i).paddingNote;
                if (n.get(0).stem_up) tmpX1 = tmpX1 + n.get(i).WidthNote(stave.StaveFont);
                tmpX2 = tmpX1 + stave.getDistanceBetweenStaveLines() * 2 / 3;
            }
            if (!drawBegin && drawEnd) {
                tmpX2 = n.get(i + 1).startX + n.get(i + 1).padding + n.get(i + 1).WidthAccidental(stave.StaveFont) + n.get(i + 1).paddingNote;
                if (n.get(0).stem_up) tmpX2 = tmpX2 + n.get(i + 1).WidthNote(stave.StaveFont);
                tmpX1 = tmpX2 - stave.getDistanceBetweenStaveLines() * 2 / 3;
            }

            float tmpY1 = FM_Const.getY2(slope, x, y, tmpX1);
            float tmpY2 = FM_Const.getY2(slope, x, y, tmpX2);
            if (n.get(0).stem_up) {
                tmpY1 = tmpY1 + stave.getDistanceBetweenStaveLines() * 2 / 3;
                tmpY2 = tmpY2 + stave.getDistanceBetweenStaveLines() * 2 / 3;
            } else {
                tmpY1 = tmpY1 - stave.getDistanceBetweenStaveLines() * 2 / 3;
                tmpY2 = tmpY2 - stave.getDistanceBetweenStaveLines() * 2 / 3;
            }

            //float tmpY = n.get(i).ys + n.get(i).getDisplacement() * stave.getDistanceBetweenStaveLines();
            //float tmpY2 = FM_Const.getY2(slope, x, y, tmpX);

            Path bottomPath = new Path();
            bottomPath.reset();
            bottomPath.moveTo(tmpX1, tmpY1);
            bottomPath.lineTo(tmpX2, tmpY2);
            bottomPath.lineTo(tmpX2, tmpY2 - stave.getDistanceBetweenStaveLines() / 2);
            bottomPath.lineTo(tmpX1, tmpY1 - stave.getDistanceBetweenStaveLines() / 2);
            bottomPath.lineTo(tmpX1, tmpY1);
            canvas.drawPath(bottomPath, stave.StaveFont);
        }
    }
}
