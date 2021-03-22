package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.graphics.Path;

class FM_Tie {
    FM_Note s, e;
    FM_Score score;
    int up = 0;     //if 0, then follow the stems. If 1, then down. If 2, then up.
    FM_Tie(FM_Score score) {
        s = null;
        e = null;
        this.score = score;
        this.up = 0;
    }
    FM_Tie(FM_Score score, boolean up) {
        s = null;
        e = null;
        this.score = score;
        if (up) this.up = 2;
        else this.up = 1;
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

        boolean draw_up = s.stem_up;
        if (up == 1) draw_up = true;
        if (up == 2) draw_up = false;

        Path p;
        if (x > xe && ye > y) {     //if the notes are on different lines
            float e = this.s.WidthNote() * 2;
            if (draw_up) {
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
            if (draw_up) {
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
            if (draw_up) {
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
