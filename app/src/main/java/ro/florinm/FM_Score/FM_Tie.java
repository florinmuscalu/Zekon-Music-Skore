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
        //C.setStyle(Paint.Style.STROKE);
        //C.setStrokeWidth(FM_Const.dpTOpx(stave.getContext(), 2));
        C.setAntiAlias(true);

        if (x > xe && ye > y) {
            RectF oval;
            float e = this.s.WidthNote(stave.StaveFont) * 2;
            if (s.stem_up)
                oval = new RectF(x, y - stave.getDistanceBetweenStaveLines() / 2, x + e, y + stave.getDistanceBetweenStaveLines() / 2);
            else
                oval = new RectF(x, y - stave.getDistanceBetweenStaveLines() * 3 / 2, x + e, y - stave.getDistanceBetweenStaveLines() / 2);
            if (s.stem_up)
                canvas.drawArc(oval, 90f, 90f, false, C);
            else
                canvas.drawArc(oval, 180f, 90f, false, C);


            RectF oval1;
            float e1 =  this.e.WidthNote(stave.StaveFont) * 2;
            if (s.stem_up)
                oval = new RectF(xe - e1, ye - stave.getDistanceBetweenStaveLines() / 2, xe, ye + stave.getDistanceBetweenStaveLines() / 2);
            else
                oval = new RectF(xe - e1, ye - stave.getDistanceBetweenStaveLines() * 3 / 2, xe, ye - stave.getDistanceBetweenStaveLines() / 2);
            if (s.stem_up)
                canvas.drawArc(oval, 0f, 90f, false, C);
            else
                canvas.drawArc(oval, 270f, 90f, false, C);
        } else {
            Path p = new Path();
            if (s.stem_up) {
                p = new Path();
                p.reset();
                p.moveTo(x, y);
                p.cubicTo(x, y, (xe + x) / 2, y + stave.getDistanceBetweenStaveLines() * 2 / 3, xe, ye);
                p.cubicTo(xe, ye, (xe + x) / 2, y + stave.getDistanceBetweenStaveLines() / 2, x, y);
            } else {
                p = new Path();
                y = y - stave.getDistanceBetweenStaveLines();
                ye = ye - stave.getDistanceBetweenStaveLines();
                p.reset();
                p.moveTo(x, y);
                p.cubicTo(x, y, (xe + x) / 2, y - stave.getDistanceBetweenStaveLines() * 2 / 3, xe, ye);
                p.cubicTo(xe, ye, (xe + x) / 2, y - stave.getDistanceBetweenStaveLines() / 2, x, y);
            }
            canvas.drawPath(p, C);
        }
    }
}
