package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.graphics.Path;

class FM_Tie {
    FM_Note s, e;
    int index;
    FM_Score score;
    FM_Tie(FM_Score score, int index) {
        s = null;
        e = null;
        this.score = score;
        this.index = index;
    }

    void AddStart(FM_Note s){
        this.s = s;
    }
    void AddEnd(FM_Note e){
        this.e = e;
    }

    void Draw(Canvas canvas) {
        if (!s.visible) return;
        float x = s.startX + s.paddingLeft + s.WidthAccidental() + s.paddingNote + s.WidthNote();
        float xe = e.startX + e.paddingLeft + e.WidthAccidental() + e.paddingNote;
        float y = s.ys + (s.getDisplacement() + 0.5f) * score.getDistanceBetweenStaveLines();
        float ye = e.ys + (e.getDisplacement() + 0.5f) * score.getDistanceBetweenStaveLines();

        Path p;
        if (x > xe && ye > y) {     //if the notes are on different lines
            float e = this.s.WidthNote() * 2;
            if (s.stem_up) {
                p = new Path();
                p.reset();
                p.moveTo(x, y);
                p.cubicTo(x, y, (2 * x + e) / 2, y + score.getDistanceBetweenStaveLines() / 2, x + e, y + score.getDistanceBetweenStaveLines() / 2);
                p.cubicTo(x + e, y + score.getDistanceBetweenStaveLines() / 2, (2 * x + e) / 2, y + score.getDistanceBetweenStaveLines(), x, y);
            } else {
                y = y - score.getDistanceBetweenStaveLines();
                p = new Path();
                p.reset();
                p.moveTo(x, y);
                p.cubicTo(x, y, (2 * x + e) / 2, y - score.getDistanceBetweenStaveLines() / 2, x + e, y - score.getDistanceBetweenStaveLines() / 2);
                p.cubicTo(x + e, y - score.getDistanceBetweenStaveLines() / 2, (2 * x + e) / 2, y - score.getDistanceBetweenStaveLines(), x, y);
            }
            canvas.drawPath(p, score.Font);

            float e1 =  this.e.WidthNote() * 2;
            if (s.stem_up) {
                p = new Path();
                p.reset();
                p.moveTo(xe, ye);
                p.cubicTo(xe, ye, (2 * xe - e1) / 2, ye + score.getDistanceBetweenStaveLines() / 2, xe - e1, ye + score.getDistanceBetweenStaveLines() / 2);
                p.cubicTo(xe - e1, ye + score.getDistanceBetweenStaveLines() / 2, (2 * xe - e1) / 2, ye + score.getDistanceBetweenStaveLines(), xe, ye);
            }
            else {
                ye = ye - score.getDistanceBetweenStaveLines();
                p = new Path();
                p.reset();
                p.moveTo(xe, ye);
                p.cubicTo(xe, ye, (2 * xe - e1) / 2, ye - score.getDistanceBetweenStaveLines() / 2, xe - e1, ye - score.getDistanceBetweenStaveLines() / 2);
                p.cubicTo(xe - e1, ye - score.getDistanceBetweenStaveLines() / 2, (2 * xe - e1) / 2, ye - score.getDistanceBetweenStaveLines(), xe, ye);
            }
        } else {        //if the notes are on the same line
            if (s.stem_up) {
                p = new Path();
                p.reset();
                p.moveTo(x, y);
                p.cubicTo(x, y, (xe + x) / 2, y + score.getDistanceBetweenStaveLines() / 2, xe, ye);
                p.cubicTo(xe, ye, (xe + x) / 2, y + score.getDistanceBetweenStaveLines(), x, y);
            } else {
                p = new Path();
                y = y - score.getDistanceBetweenStaveLines();
                ye = ye - score.getDistanceBetweenStaveLines();
                p.reset();
                p.moveTo(x, y);
                p.cubicTo(x, y, (xe + x) / 2, y - score.getDistanceBetweenStaveLines() / 2, xe, ye);
                p.cubicTo(xe, ye, (xe + x) / 2, y - score.getDistanceBetweenStaveLines(), x, y);
            }
        }
        canvas.drawPath(p, score.Font);
    }
}
