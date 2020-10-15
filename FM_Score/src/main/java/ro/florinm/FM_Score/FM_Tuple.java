package ro.florinm.FM_Score;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class FM_Tuple {
    int index;
    List<FM_Note> n;
    int size;
    FM_Score score;

    public FM_Tuple(FM_Score score, int size, int index) {
        n = new ArrayList<>();
        this.size = size;
        this.index = index;
        this.score = score;
    }

    public void AddNote(FM_Note n) {
        this.n.add(n);
    }

    public void Draw(Canvas canvas) {
        float x, xe, y, ye;
        if (n.get(0).stem_up) {
            x = 0.5f * score.getDistanceBetweenStaveLines() + n.get(0).startX + n.get(0).paddingLeft + n.get(0).WidthAccidental() + n.get(0).paddingNote;
            xe = 0.5f * score.getDistanceBetweenStaveLines() + n.get(n.size() - 1).startX + n.get(n.size() - 1).paddingLeft + n.get(n.size() - 1).WidthAccidental() + n.get(n.size() - 1).paddingNote + n.get(n.size() - 1).WidthNote();
            y = n.get(0).ys + (n.get(0).getDisplacement() + 0.5f) * score.getDistanceBetweenStaveLines() - n.get(0).Height(true) + score.getDistanceBetweenStaveLines() / 2;
            ye = n.get(n.size() - 1).ys + (n.get(n.size() - 1).getDisplacement() + 0.5f) * score.getDistanceBetweenStaveLines() - n.get(n.size() - 1).Height(true) + score.getDistanceBetweenStaveLines() / 2;

            float yMiddleMin = n.get(1).ys + (n.get(1).getDisplacement() + 0.5f) * score.getDistanceBetweenStaveLines() - n.get(1).Height(true) + score.getDistanceBetweenStaveLines() / 2;

            for (int i = 2; i < n.size() - 1; i++) {
                float yMiddle = n.get(i).ys + (n.get(i).getDisplacement() + 0.5f) * score.getDistanceBetweenStaveLines() - n.get(i).Height(true) + score.getDistanceBetweenStaveLines() / 2;
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
                    n.get(0).paddingNote -
                    0.5f * score.getDistanceBetweenStaveLines() ;
            xe = n.get(n.size() - 1).startX +
                    n.get(n.size() - 1).paddingLeft +
                    n.get(n.size() - 1).WidthAccidental() +
                    n.get(n.size() - 1).WidthNote() +
                    n.get(n.size() - 1).paddingNote -
                    0.5f * score.getDistanceBetweenStaveLines();

            y = n.get(0).ys +
                    (n.get(0).getDisplacement() - 0.5f) * score.getDistanceBetweenStaveLines() +
                    n.get(0).Height(true) -
                    score.getDistanceBetweenStaveLines() / 2;
            ye = n.get(n.size() - 1).ys +
                    (n.get(n.size() - 1).getDisplacement() - 0.5f) * score.getDistanceBetweenStaveLines() +
                    n.get(n.size() - 1).Height(true) -
                    score.getDistanceBetweenStaveLines() / 2;

            float yMiddleMin = n.get(1).ys + (n.get(1).getDisplacement()) * score.getDistanceBetweenStaveLines() + n.get(1).Height(true) - score.getDistanceBetweenStaveLines() / 2;

            for (int i = 2; i < n.size() - 1; i++) {
                float yMiddle = n.get(i).ys + (n.get(i).getDisplacement()) * score.getDistanceBetweenStaveLines() + n.get(i).Height(true) - score.getDistanceBetweenStaveLines() / 2;
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
                canvas.drawLine(x, y, x, y - score.getDistanceBetweenStaveLines(), score.Font);
                canvas.drawLine(xe, ye, xe, ye - score.getDistanceBetweenStaveLines(), score.Font);
            } else {
                canvas.drawLine(x, y, x, y + score.getDistanceBetweenStaveLines(), score.Font);
                canvas.drawLine(xe, ye, xe, ye + score.getDistanceBetweenStaveLines(), score.Font);
            }
        }
        FM_Const.AdjustFont(score, text, 1);
        float w = score.Font.measureText(text);
        float middle1 = (x + xe) / 2 - w / 2 - score.getDistanceBetweenStaveLines() / 2;
        float middle2 = (x + xe) / 2 + w / 2 + score.getDistanceBetweenStaveLines() / 2;
        float slope = FM_Const.slope(x, y - score.getDistanceBetweenStaveLines(), xe, ye - score.getDistanceBetweenStaveLines());

        if (!n.get(0).beam) {
            if (n.get(0).stem_up) {
                canvas.drawLine(x,
                        y - score.getDistanceBetweenStaveLines(),
                        middle1,
                        slope * (middle1 - x) + y - score.getDistanceBetweenStaveLines(), score.Font);
                canvas.drawLine(middle2,
                        ye - score.getDistanceBetweenStaveLines() - slope * (xe - middle2),
                        xe,
                        ye - score.getDistanceBetweenStaveLines(), score.Font);
            } else {
                canvas.drawLine(x,
                        y + score.getDistanceBetweenStaveLines(),
                        middle1,
                        slope * (middle1 - x) + y + score.getDistanceBetweenStaveLines(), score.Font);
                canvas.drawLine(middle2,
                        ye + score.getDistanceBetweenStaveLines() - slope * (xe - middle2),
                        xe,
                        ye + score.getDistanceBetweenStaveLines(), score.Font);
            }
        }
        //canvas.drawLine(x, y - stave.getDistanceBetweenStaveLines(), xe,ye - stave.getDistanceBetweenStaveLines() , stave.StaveFont);
        if (n.get(0).stem_up) canvas.drawText(text, (x + xe) / 2 - w / 2, (y + ye) / 2 - score.getDistanceBetweenStaveLines(), score.Font);
        else canvas.drawText(text, (x + xe) / 2 - w / 2, (y + ye) / 2 + score.getDistanceBetweenStaveLines(), score.Font);
    }
}
