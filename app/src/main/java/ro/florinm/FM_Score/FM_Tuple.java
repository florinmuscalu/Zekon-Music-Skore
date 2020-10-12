package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

public class FM_Tuple {
    int index;
    List<FM_Note> n;
    int size;

    public FM_Tuple(int size, int index) {
        n = new ArrayList<>();
        this.size = size;
        this.index = index;
    }

    public void AddNote(FM_Note n) {
        this.n.add(n);
    }

    public void Draw(FM_Score stave, Canvas canvas) {
        float x, xe, y, ye;
        if (n.get(0).stem_up) {
            x = 0.5f * stave.getDistanceBetweenStaveLines() + n.get(0).startX + n.get(0).padding + n.get(0).WidthAccidental(stave.StaveFont) + n.get(0).paddingNote;
            xe = 0.5f * stave.getDistanceBetweenStaveLines() + n.get(n.size() - 1).startX + n.get(n.size() - 1).padding + n.get(n.size() - 1).WidthAccidental(stave.StaveFont) + n.get(n.size() - 1).paddingNote + n.get(n.size() - 1).WidthNote(stave.StaveFont);
            y = n.get(0).ys + (n.get(0).getDisplacement() + 0.5f) * stave.getDistanceBetweenStaveLines() - n.get(0).Height(stave.StaveFont, true) + stave.getDistanceBetweenStaveLines() / 2;
            ye = n.get(n.size() - 1).ys + (n.get(n.size() - 1).getDisplacement() + 0.5f) * stave.getDistanceBetweenStaveLines() - n.get(n.size() - 1).Height(stave.StaveFont, true) + stave.getDistanceBetweenStaveLines() / 2;

            float yMiddleMin = n.get(1).ys + (n.get(1).getDisplacement() + 0.5f) * stave.getDistanceBetweenStaveLines() - n.get(1).Height(stave.StaveFont, true) + stave.getDistanceBetweenStaveLines() / 2;

            for (int i = 2; i < n.size() - 1; i++) {
                float yMiddle = n.get(i).ys + (n.get(i).getDisplacement() + 0.5f) * stave.getDistanceBetweenStaveLines() - n.get(i).Height(stave.StaveFont, true) + stave.getDistanceBetweenStaveLines() / 2;
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
                    n.get(0).paddingNote -
                    0.5f * stave.getDistanceBetweenStaveLines() ;
            xe = n.get(n.size() - 1).startX +
                    n.get(n.size() - 1).padding +
                    n.get(n.size() - 1).WidthAccidental(stave.StaveFont) +
                    n.get(n.size() - 1).WidthNote(stave.StaveFont) +
                    n.get(n.size() - 1).paddingNote -
                    0.5f * stave.getDistanceBetweenStaveLines();

            y = n.get(0).ys +
                    (n.get(0).getDisplacement() - 0.5f) * stave.getDistanceBetweenStaveLines() +
                    n.get(0).Height(stave.StaveFont, true) -
                    stave.getDistanceBetweenStaveLines() / 2;
            ye = n.get(n.size() - 1).ys +
                    (n.get(n.size() - 1).getDisplacement() - 0.5f) * stave.getDistanceBetweenStaveLines() +
                    n.get(n.size() - 1).Height(stave.StaveFont, true) -
                    stave.getDistanceBetweenStaveLines() / 2;

            float yMiddleMin = n.get(1).ys + (n.get(1).getDisplacement()) * stave.getDistanceBetweenStaveLines() + n.get(1).Height(stave.StaveFont, true) - stave.getDistanceBetweenStaveLines() / 2;

            for (int i = 2; i < n.size() - 1; i++) {
                float yMiddle = n.get(i).ys + (n.get(i).getDisplacement()) * stave.getDistanceBetweenStaveLines() + n.get(i).Height(stave.StaveFont, true) - stave.getDistanceBetweenStaveLines() / 2;
                if (yMiddle > yMiddleMin) yMiddleMin = yMiddle;
            }
            if ((y + ye) / 2 < yMiddleMin) {
                float diff = (y + ye) / 2 - yMiddleMin;
                y = y - diff;
                ye = ye - diff;
            }
        }

        String text = FM_Const._3;
        if (size == 5) text = FM_Const._5;

        if (!n.get(0).beam) {
            if (n.get(0).stem_up) {
                canvas.drawLine(x, y, x, y - stave.getDistanceBetweenStaveLines(), stave.StaveFont);
                canvas.drawLine(xe, ye, xe, ye - stave.getDistanceBetweenStaveLines(), stave.StaveFont);
            } else {
                canvas.drawLine(x, y, x, y + stave.getDistanceBetweenStaveLines(), stave.StaveFont);
                canvas.drawLine(xe, ye, xe, ye + stave.getDistanceBetweenStaveLines(), stave.StaveFont);
            }
        }
        float tmp = stave.StaveFont.getTextSize();
        stave.StaveFont.setTextSize(tmp / 2);
        float w = stave.StaveFont.measureText(text);
        float middle1 = (x + xe) / 2 - w / 2 - stave.getDistanceBetweenStaveLines() / 2;
        float middle2 = (x + xe) / 2 + w / 2 + stave.getDistanceBetweenStaveLines() / 2;
        float slope = FM_Const.slope(x, y - stave.getDistanceBetweenStaveLines(), xe, ye - stave.getDistanceBetweenStaveLines());

        if (!n.get(0).beam) {
            if (n.get(0).stem_up) {
                canvas.drawLine(x,
                        y - stave.getDistanceBetweenStaveLines(),
                        middle1,
                        slope * (middle1 - x) + y - stave.getDistanceBetweenStaveLines(), stave.StaveFont);
                canvas.drawLine(middle2,
                        ye - stave.getDistanceBetweenStaveLines() - slope * (xe - middle2),
                        xe,
                        ye - stave.getDistanceBetweenStaveLines(), stave.StaveFont);
            } else {
                canvas.drawLine(x,
                        y + stave.getDistanceBetweenStaveLines(),
                        middle1,
                        slope * (middle1 - x) + y + stave.getDistanceBetweenStaveLines(), stave.StaveFont);
                canvas.drawLine(middle2,
                        ye + stave.getDistanceBetweenStaveLines() - slope * (xe - middle2),
                        xe,
                        ye + stave.getDistanceBetweenStaveLines(), stave.StaveFont);
            }
        }
        //canvas.drawLine(x, y - stave.getDistanceBetweenStaveLines(), xe,ye - stave.getDistanceBetweenStaveLines() , stave.StaveFont);
        if (n.get(0).stem_up) {
            canvas.drawText(text, (x + xe) / 2 - w / 2, (y + ye) / 2 - stave.getDistanceBetweenStaveLines(), stave.StaveFont);
        } else {
            canvas.drawText(text, (x + xe) / 2 - w / 2, (y + ye) / 2 + stave.getDistanceBetweenStaveLines(), stave.StaveFont);
        }
        stave.StaveFont.setTextSize(tmp);
    }
}
