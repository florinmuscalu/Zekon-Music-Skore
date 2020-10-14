package ro.florinm.FM_Score;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

public class FM_Tie {
    FM_Note s, e;
    int index;
    public FM_Tie(int index) {
        s = null;
        e = null;
        this.index = index;
    }

    public void AddStart(FM_Note s){
        this.s = s;
    }
    public void AddEnd(FM_Note e){
        this.e = e;
    }

    public void Draw(FM_Score stave, Canvas canvas) {
        float x = s.startX + s.padding + s.WidthAccidental(stave.StaveFont) + s.paddingNote + s.WidthNote(stave.StaveFont);
        float xe = e.startX + e.padding + e.WidthAccidental(stave.StaveFont) + e.paddingNote;
        float y = s.ys + (s.getDisplacement() + 0.5f) * stave.getDistanceBetweenStaveLines();
        float ye = e.ys + (e.getDisplacement() + 0.5f) * stave.getDistanceBetweenStaveLines();

        Paint C = new Paint();
        C.setColor(stave.StaveFont.getColor());
        C.setAntiAlias(true);
        Path p = new Path();

        if (x > xe && ye > y) {     //if the notes are on different lines
            RectF oval;
            float e = this.s.WidthNote(stave.StaveFont) * 2;
            if (s.stem_up) {
                p = new Path();
                p.reset();
                p.moveTo(x, y);
                p.cubicTo(x, y, (2 * x + e) / 2, y + stave.getDistanceBetweenStaveLines() / 2, x + e, y + stave.getDistanceBetweenStaveLines() / 2);
                p.cubicTo(x + e, y + stave.getDistanceBetweenStaveLines() / 2, (2 * x + e) / 2, y + stave.getDistanceBetweenStaveLines(), x, y);
                canvas.drawPath(p, C);
            } else {
                y = y - stave.getDistanceBetweenStaveLines();
                p = new Path();
                p.reset();
                p.moveTo(x, y);
                p.cubicTo(x, y, (2 * x + e) / 2, y - stave.getDistanceBetweenStaveLines() / 2, x + e, y - stave.getDistanceBetweenStaveLines() / 2);
                p.cubicTo(x + e, y - stave.getDistanceBetweenStaveLines() / 2, (2 * x + e) / 2, y - stave.getDistanceBetweenStaveLines(), x, y);
                canvas.drawPath(p, C);
            }

            RectF oval1;
            float e1 =  this.e.WidthNote(stave.StaveFont) * 2;
            if (s.stem_up) {
                p = new Path();
                p.reset();
                p.moveTo(xe, ye);
                p.cubicTo(xe, ye, (2 * xe - e1) / 2, ye + stave.getDistanceBetweenStaveLines() / 2, xe - e1, ye + stave.getDistanceBetweenStaveLines() / 2);
                p.cubicTo(xe - e1, ye + stave.getDistanceBetweenStaveLines() / 2, (2 * xe - e1) / 2, ye + stave.getDistanceBetweenStaveLines(), xe, ye);
                canvas.drawPath(p, C);
            }
            else {
                ye = ye - stave.getDistanceBetweenStaveLines();
                p = new Path();
                p.reset();
                p.moveTo(xe, ye);
                p.cubicTo(xe, ye, (2 * xe - e1) / 2, ye - stave.getDistanceBetweenStaveLines() / 2, xe - e1, ye - stave.getDistanceBetweenStaveLines() / 2);
                p.cubicTo(xe - e1, ye - stave.getDistanceBetweenStaveLines() / 2, (2 * xe - e1) / 2, ye - stave.getDistanceBetweenStaveLines(), xe, ye);
                canvas.drawPath(p, C);
            }
        } else {        //if the notes are on the same line
            if (s.stem_up) {
                p = new Path();
                p.reset();
                p.moveTo(x, y);
                p.cubicTo(x, y, (xe + x) / 2, y + stave.getDistanceBetweenStaveLines() / 2, xe, ye);
                p.cubicTo(xe, ye, (xe + x) / 2, y + stave.getDistanceBetweenStaveLines(), x, y);
            } else {
                p = new Path();
                y = y - stave.getDistanceBetweenStaveLines();
                ye = ye - stave.getDistanceBetweenStaveLines();
                p.reset();
                p.moveTo(x, y);
                p.cubicTo(x, y, (xe + x) / 2, y - stave.getDistanceBetweenStaveLines() / 2, xe, ye);
                p.cubicTo(xe, ye, (xe + x) / 2, y - stave.getDistanceBetweenStaveLines(), x, y);
            }
            canvas.drawPath(p, C);
        }
    }
}
