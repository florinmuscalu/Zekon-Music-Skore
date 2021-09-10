package tech.zekon.FM_Score;

import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Path;

import java.util.ArrayList;
import java.util.List;

class FM_Tuplet {
    int index;
    List<FM_BaseNote> n;
    int size;
    int position;       //0 - above, 1 - below
    FM_Score score;

    FM_Tuplet(FM_Score score, int size, int index, int position) {
        n = new ArrayList<>();
        this.size = size;
        this.index = index;
        this.position = position;
        this.score = score;
    }

    void AddNote(FM_BaseNote n) {
        this.n.add(n);
    }

    void Draw(Canvas canvas) {
        if (!n.get(0).blurred && !n.get(0).visible) return;
        if (n.get(0).blurred)
            score.Font.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
        else score.Font.setMaskFilter(null);
        float x, xe, y, ye;
        float StaveLineHalfWidth = FM_Const.dpTOpx(score.getContext(), 0.25f);
        int EndIndex = n.size() - 1;
        x = n.get(0).Left() + n.get(0).WidthAccidental() - 0.5f * score.getDistanceBetweenStaveLines();
        xe = n.get(EndIndex).Right() + 0.5f * score.getDistanceBetweenStaveLines();

        boolean beam = false;
        if ((n.get(0) instanceof FM_Note && ((FM_Note)n.get(0)).beam) && (n.get(EndIndex) instanceof FM_Note && ((FM_Note)n.get(EndIndex)).beam)) beam = true;
        if (n.get(0) instanceof FM_Pause) beam = false;
        if (n.get(EndIndex) instanceof FM_Pause) beam = false;

        if (position == 0) {
            if (n.get(0).stem_up) x = x + score.getDistanceBetweenStaveLines();
            y =  n.get(0).Top();
            if (n.get(0).Bottom() < y) y =n.get(0).Bottom();
            ye = n.get(EndIndex).Top();
            if (n.get(EndIndex).Bottom() < ye) ye =n.get(EndIndex).Bottom();
            y = y + 0.5f * score.getDistanceBetweenStaveLines();
            ye = ye + 0.5f * score.getDistanceBetweenStaveLines();

            if (!beam) {
                if (ye > y) {
                    float slope = FM_Const.slope(0, x, y, xe, ye);
                    ye = FM_Const.getY2(slope, x, y, xe);
                } else {
                    float slope = FM_Const.slope(0, xe, ye, x, y);
                    y = FM_Const.getY2(slope, xe, ye, x);
                }
                float yMiddleMin = n.get(1).Top();
                if (n.get(1).Bottom() < yMiddleMin) yMiddleMin =n.get(1).Bottom();

                for (int i = 2; i < n.size() - 1; i++) {
                    float yMiddle = n.get(i).Top();
                    if (n.get(i).Bottom() < yMiddle) yMiddle =n.get(i).Bottom();
                    if (yMiddle < yMiddleMin) yMiddleMin = yMiddle;
                }
                if ((y + ye) / 2 > yMiddleMin) {
                    float diff = (y + ye) / 2 - yMiddleMin;
                    y = y - diff;
                    ye = ye - diff;
                }
                y = y - 0.5f * score.getDistanceBetweenStaveLines();
                ye = ye - 0.5f * score.getDistanceBetweenStaveLines();
            }
            else{
                int i = 0;
                while (n.get(i) instanceof FM_Pause) i +=1;
                y = ((FM_Note) n.get(i)).StemTopY;
                i = n.size()-1;
                while (n.get(i) instanceof FM_Pause) i -=1;
                ye = ((FM_Note) n.get(i)).StemTopY;
            }
        } else {
            if (!n.get(EndIndex).stem_up) xe = xe - score.getDistanceBetweenStaveLines();

            y =  n.get(0).Top();
            if (n.get(0).Bottom() > y) y =n.get(0).Bottom();
            ye = n.get(EndIndex).Top();
            if (n.get(EndIndex).Bottom() > ye) ye =n.get(EndIndex).Bottom();
            y = y - 0.5f * score.getDistanceBetweenStaveLines();
            ye = ye - 0.5f * score.getDistanceBetweenStaveLines();

            if (!beam) {
                if (ye < y) {
                    float slope = FM_Const.slope(0, x, y, xe, ye);
                    ye = FM_Const.getY2(slope, x, y, xe);
                } else {
                    float slope = FM_Const.slope(0, xe, ye, x, y);
                    y = FM_Const.getY2(slope, xe, ye, x);
                }

                float yMiddleMin = n.get(1).Bottom();
                if (n.get(1).Top() > yMiddleMin) yMiddleMin =n.get(1).Top();

                for (int i = 2; i < n.size() - 1; i++) {
                    float yMiddle = n.get(i).Bottom();
                    if (n.get(i).Top() > yMiddle) yMiddle =n.get(i).Top();
                    if (yMiddle > yMiddleMin) yMiddleMin = yMiddle;
                }
                if ((y + ye) / 2 < yMiddleMin) {
                    float diff = (y + ye) / 2 - yMiddleMin;
                    y = y - diff;
                    ye = ye - diff;
                }
                y = y + 0.5f * score.getDistanceBetweenStaveLines();
                ye = ye + 0.5f * score.getDistanceBetweenStaveLines();
            } else {
                int i = 0;
                while (n.get(i) instanceof FM_Pause) i +=1;
                if (((FM_Note)n.get(i)).StemTopY != 0) y = ((FM_Note)n.get(i)).StemTopY;
                i = n.size()-1;
                while (n.get(i) instanceof FM_Pause) i -=1;
                if (((FM_Note)n.get(i)).StemTopY != 0) ye = ((FM_Note)n.get(i)).StemTopY;
                y = y - 0.6f * score.getDistanceBetweenStaveLines();
                ye = ye - 0.6f * score.getDistanceBetweenStaveLines();
            }
        }
        String text = FM_Const._3;
        if (size == 7) text = FM_Const._7;
        if (size == 6) text = FM_Const._6;
        if (size == 5) text = FM_Const._5;
        if (size == 4) text = FM_Const._4;
        if (size == 2) text = FM_Const._2;

        Path topPath;
        if (!beam) {
            if (position == 0) {
                canvas.drawRect(x - StaveLineHalfWidth, y, x + StaveLineHalfWidth, y - score.getDistanceBetweenStaveLines(), score.Font);
                canvas.drawRect(xe - StaveLineHalfWidth, ye, xe + StaveLineHalfWidth, ye - score.getDistanceBetweenStaveLines(), score.Font);
            } else {
                canvas.drawRect(x - StaveLineHalfWidth, y, x + StaveLineHalfWidth, y + score.getDistanceBetweenStaveLines(), score.Font);
                canvas.drawRect(xe - StaveLineHalfWidth, ye, xe + StaveLineHalfWidth, ye + score.getDistanceBetweenStaveLines(), score.Font);
            }
        }
        FM_Const.AdjustFont(score, text, 1);
        float w = score.Font.measureText(text);
        float middle1 = (x + xe) / 2 - w / 2 - score.getDistanceBetweenStaveLines() / 2;
        float middle2 = (x + xe) / 2 + w / 2 + score.getDistanceBetweenStaveLines() / 2;
        float slope = FM_Const.slope(0, x, y - score.getDistanceBetweenStaveLines(), xe, ye - score.getDistanceBetweenStaveLines());

        if (!beam) {
            if (position == 0) {
                topPath = new Path();
                topPath.reset();
                topPath.moveTo(x, y - score.getDistanceBetweenStaveLines() + 2 * StaveLineHalfWidth);
                topPath.lineTo(middle1, slope * (middle1 - x) + y - score.getDistanceBetweenStaveLines() + 2 * StaveLineHalfWidth);
                topPath.lineTo(middle1, slope * (middle1 - x) + y - score.getDistanceBetweenStaveLines());
                topPath.lineTo(x, y - score.getDistanceBetweenStaveLines());
                topPath.lineTo(x, y - score.getDistanceBetweenStaveLines() + 2 * StaveLineHalfWidth);
                canvas.drawPath(topPath, score.Font);

                topPath = new Path();
                topPath.reset();
                topPath.moveTo(middle2, ye - score.getDistanceBetweenStaveLines() - slope * (xe - middle2) + 2 * StaveLineHalfWidth);
                topPath.lineTo(xe, ye - score.getDistanceBetweenStaveLines() + 2 * StaveLineHalfWidth);
                topPath.lineTo(xe, ye - score.getDistanceBetweenStaveLines());
                topPath.lineTo(middle2, ye - score.getDistanceBetweenStaveLines() - slope * (xe - middle2));
                topPath.lineTo(middle2, ye - score.getDistanceBetweenStaveLines() - slope * (xe - middle2) + 2 * StaveLineHalfWidth);
                canvas.drawPath(topPath, score.Font);
            } else {
                topPath = new Path();
                topPath.reset();
                topPath.moveTo(x, y + score.getDistanceBetweenStaveLines() - 2 * StaveLineHalfWidth);
                topPath.lineTo(middle1, slope * (middle1 - x) + y + score.getDistanceBetweenStaveLines() - 2 * StaveLineHalfWidth);
                topPath.lineTo(middle1, slope * (middle1 - x) + y + score.getDistanceBetweenStaveLines());
                topPath.lineTo(x, y + score.getDistanceBetweenStaveLines());
                topPath.lineTo(x, y + score.getDistanceBetweenStaveLines() - 2 * StaveLineHalfWidth);
                canvas.drawPath(topPath, score.Font);
                topPath = new Path();
                topPath.reset();
                topPath.moveTo(middle2, ye + score.getDistanceBetweenStaveLines() - slope * (xe - middle2) - 2 * StaveLineHalfWidth);
                topPath.lineTo(xe, ye + score.getDistanceBetweenStaveLines() - 2 * StaveLineHalfWidth);
                topPath.lineTo(xe, ye + score.getDistanceBetweenStaveLines());
                topPath.lineTo(middle2, ye + score.getDistanceBetweenStaveLines() - slope * (xe - middle2));
                topPath.lineTo(middle2, ye + score.getDistanceBetweenStaveLines() - slope * (xe - middle2) - 2 * StaveLineHalfWidth);
                canvas.drawPath(topPath, score.Font);
            }
        }
        //canvas.drawLine(x, y - stave.getDistanceBetweenStaveLines(), xe,ye - stave.getDistanceBetweenStaveLines() , stave.StaveFont);
        if (position == 0) canvas.drawText(text, (x + xe) / 2 - w / 2, (y + ye) / 2 - 1.2f * score.getDistanceBetweenStaveLines(), score.Font);
        else canvas.drawText(text, (x + xe) / 2 - w / 2, (y + ye) / 2 + 0.8f * score.getDistanceBetweenStaveLines(), score.Font);
        score.Font.setMaskFilter(null);
    }
}
