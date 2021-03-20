package ro.florinm.FM_Score;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class FM_Score extends View {
    @FM_TimeSignatureValue
    private int TimeSignature_n;    //numerator
    @FM_TimeSignatureValue
    private int TimeSignature_d;    //denominator
    @FM_KeySignatureValue
    private int KeySignature;
    @FM_StaveCount
    private int StaveCount;
    private int VoiceCount;
    private int Color;
    private int StaveLineColor;
    private boolean ShowBrace;
    private float NoteSpacing;
    int width, height;
    protected Paint Font;
    FM_KeySignature FirstStaveKey, SecondStaveKey;
    private float _DistanceBetweenStaveLines;
    private float DistanceBetweenStave_cnt;                //Distance between Staves as number of distances between lines
    private float DistanceBetweenRows_cnt;                 //Distance between Rows of staves as number of distances between lines
    private float PaddingV_cnt, PaddingS_p, PaddingE_p;     //padding Start and padding End are set as percentage of the total width
    private float PaddingS, PaddingE;                       //padding Vertical is set as number of Distances between Lines.
    private boolean StartBar;
    private boolean EndBar;
    @FM_ClefValue
    private int FirstStaveClef, SecondStaveClef;
    private int Lines;
    final Context context;
    @FM_Align
    private int Align;

    private boolean CenterVertical = true;
    private boolean MultiRow = false;
    private boolean AllowZoomPan = false;
    private boolean AllowZoomControls = false;
    private boolean tmpZoomControls = false;
    private final List<FM_BaseNote> StaveNotes = new ArrayList<>();
    public final List<FM_Tie> Ties = new ArrayList<>();
    private final List<FM_Tuplet> Tuplets = new ArrayList<>();
    private final List<FM_Beam> Beams = new ArrayList<>();

    private final ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private float mPosX = 0;
    private float mPosY = 0;
    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;

    float pivotPointX = 0f;
    float pivotPointY = 0f;

    private boolean TrimLastRow;
    private int progressBar;
    @FM_BoundingBoxType
    private int DrawBoundingBox;

    private CountDownLatch finishedDraw = null;

    public FM_Score(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setBackgroundColor(android.graphics.Color.argb(255, 224, 211, 175));
        Color = android.graphics.Color.argb(255, 26, 28, 33);
        setStaveLineColor(android.graphics.Color.argb(255, 50, 50, 50));
        Typeface bravura = Typeface.createFromAsset(context.getAssets(), "bravura.otf");
        Font = new Paint();
        Font.setAntiAlias(true);
        Font.setTypeface(bravura);
        Font.setColor(Color);
        Lines = 1;
        TrimLastRow = false;
        progressBar = -1;
        StaveCount = FM_StaveCount._1;
        setVoiceCount(1);
        setNoteSpacing(5);
        setShowBrace(false);
        setDistanceBetweenStaveLines(10);
        setDistanceBetweenStaves(5);
        setDistanceBetweenRows(10);
        setPaddingVertical(5);
        setPaddingS(5);
        setPaddingE(5);
        setStartBar(true);
        setEndBar(true);
        setMultiRow(false);
        setFirstStaveClef(FM_ClefValue.TREBLE);
        setSecondStaveClef(FM_ClefValue.BASS);
        setTimeSignature(FM_TimeSignatureValue.None, FM_TimeSignatureValue.None);
        setKeySignature(FM_KeySignatureValue.DO);
        setNotesAlign(FM_Align.ALIGN_CENTER_MEASURES);
        ShowBoundingBoxes(FM_BoundingBoxType.None);

    }

    public void ProgressReset() {
        progressBar = -1;
        invalidate();
    }

    public void ProgressSetStart(int measure) {
        if (measure == 1) {
            progressBar = -1;
        } else {
            for (int i = 0; i < StaveNotes.size(); i++) {
                if (StaveNotes.get(i).getType() == FM_NoteType.BAR) measure--;
                if (measure == 1) {
                    progressBar = i;
                    break;
                }
            }
        }
        invalidate();
    }

    public void ProgressAdvance() {
        progressBar += 1;
        if (StaveNotes.get(progressBar) instanceof FM_BarNote) progressBar += 1;
        if (StaveNotes.get(progressBar) instanceof FM_Clef) progressBar += 1;
        if (StaveNotes.get(progressBar) instanceof FM_KeySignature) progressBar += 1;
        invalidate();
    }

    public void setTrimLastRow(boolean trimLastRow) {
        TrimLastRow = trimLastRow;
        invalidate();
    }

    public boolean isTrimLastRow() {
        return TrimLastRow;
    }

    public void ShowBoundingBoxes(@FM_BoundingBoxType int show) {
        DrawBoundingBox = show;
        invalidate();
    }

    @FM_BoundingBoxType
    public int getShowBoundingBoxes() {
        return DrawBoundingBox;
    }

    public boolean getCenterVertical() {
        return CenterVertical;
    }

    public void setCenterVertical(boolean center) {
        CenterVertical = center;
        CapTranslateVars();
        invalidate();
    }

    /**
     * @return Return the numerator
     */
    public int getTimeSignature_n() {
        return TimeSignature_n;
    }

    /**
     * @return Return the denominator
     */
    public int getTimeSignature_d() {
        return TimeSignature_d;
    }

    public void setTimeSignature(@FM_TimeSignatureValue int numerator, @FM_TimeSignatureValue int denominator) {
        TimeSignature_n = numerator;
        TimeSignature_d = denominator;
        //invalidate();
        requestLayout();
    }

    public int getStaveCount() {
        return StaveCount;
    }

    public int getVoiceCount() {
        return VoiceCount;
    }

    public void setVoiceCount(int voiceCount) {
        VoiceCount = voiceCount;
        //invalidate();
        //requestLayout();
    }

    public boolean isShowBrace() {
        return ShowBrace;
    }

    public void setShowBrace(boolean showBrace) {
        ShowBrace = showBrace;
        //invalidate();
        // requestLayout();
    }

    private float getDrawHeight() {
        float MaxHeight = Lines * (4 * getDistanceBetweenStaveLines()) + (Lines - 1) * +getDistanceBetweenRows() + 2 * PaddingV_cnt * getDistanceBetweenStaveLines();
        if (StaveCount == FM_StaveCount._2)
            MaxHeight = MaxHeight + Lines * (getDistanceBetweenStaves() + 4 * getDistanceBetweenStaveLines());
        //if (MaxHeight < height) return height;
        return MaxHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int width, int height, int old_width, int old_height) {
        super.onSizeChanged(width, height, old_width, old_height);
        this.width = width;
        this.height = height;
        PaddingS = PaddingS_p * width / 100.f;
        PaddingE = PaddingE_p * width / 100.f;
        ComputeLines();
        invalidate();
        //requestLayout();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        Matrix matrix = new Matrix();
        matrix.postScale(mScaleFactor, mScaleFactor, 0, 0);
        float adjustY = 0;
        if (CenterVertical) adjustY = height / 2f - getDrawHeight() / 2f;
        matrix.postTranslate(mPosX, mPosY + adjustY);
        canvas.setMatrix(matrix);

        float StaveLineHalfWidth = FM_Const.dpTOpx(context, 0.25f);
        float ys1 = getPaddingVertical();
        float ys2;
        float BarYs = 0;
        float BarYe = 0;
        for (int l = 0; l < Lines; l++) {
            ys2 = ys1;
            BarYs = ys1;
            BarYe = ys1 + 4 * getDistanceBetweenStaveLines();
            //draw stave lines
            Font.setColor(StaveLineColor);
            if (!TrimLastRow || (l < Lines - 1))
                for (int i = 0; i < 5; i++)
                    canvas.drawRect(PaddingS, ys1 + i * getDistanceBetweenStaveLines() - StaveLineHalfWidth, width - PaddingE, ys1 + i * getDistanceBetweenStaveLines() + StaveLineHalfWidth, Font);
            else
                for (int i = 0; i < 5; i++)
                    canvas.drawRect(PaddingS, ys1 + i * getDistanceBetweenStaveLines() - StaveLineHalfWidth, getLineWidth(l + 1), ys1 + i * getDistanceBetweenStaveLines() + StaveLineHalfWidth, Font);

            Font.setColor(Color);
            //draw clef
            int clef = FirstStaveClef;
            for (int n = 0; n < StaveNotes.size(); n++)
                if (StaveNotes.get(n).line - 1 == l && StaveNotes.get(n).stave == 0) {
                    if (StaveNotes.get(n) instanceof FM_Clef) {
                        clef = ((FM_Clef) StaveNotes.get(n)).clef;
                        //StaveNotes.get(n).setVisible(false);
                    }
                    break;
                }
            if (clef == FM_ClefValue.TREBLE) DrawTrebleClef(canvas, ys1);
            else DrawBassClef(canvas, ys1);
            //draw keySignature
            FirstStaveKey.SetDrawParameters(PaddingS + getClefWidth(), ys1, ys1);
            FirstStaveKey.DrawNote(canvas, clef);
            //draw timeSignature
            if (l == 0) DrawTimeSignature(canvas, ys1);

            if (StaveCount == FM_StaveCount._2) {
                ys2 = ys1 + (getDistanceBetweenStaves() + 4 * getDistanceBetweenStaveLines());
                BarYe = ys2 + 4 * getDistanceBetweenStaveLines();
                Font.setColor(StaveLineColor);
                if (!TrimLastRow || (l < Lines - 1)) for (int i = 0; i < 5; i++)
                    canvas.drawRect(PaddingS, ys2 + i * getDistanceBetweenStaveLines() - StaveLineHalfWidth, width - PaddingE, ys2 + i * getDistanceBetweenStaveLines() + StaveLineHalfWidth, Font);
                else for (int i = 0; i < 5; i++)
                    canvas.drawRect(PaddingS, ys2 + i * getDistanceBetweenStaveLines() - StaveLineHalfWidth, getLineWidth(l + 1), ys2 + i * getDistanceBetweenStaveLines() + StaveLineHalfWidth, Font);
                Font.setColor(Color);
                //draw clef
                clef = SecondStaveClef;
                for (int n = 0; n < StaveNotes.size(); n++)
                    if (StaveNotes.get(n).line-1 == l && StaveNotes.get(n).stave == 1) {
                        if (StaveNotes.get(n) instanceof FM_Clef) {
                            clef = ((FM_Clef) StaveNotes.get(n)).clef;
                            //StaveNotes.get(n).setVisible(false);
                        }
                        break;
                    }
                if (clef == FM_ClefValue.TREBLE) DrawTrebleClef(canvas, ys2);
                else DrawBassClef(canvas, ys2);
                SecondStaveKey.SetDrawParameters(PaddingS + getClefWidth(), ys2, ys2);
                SecondStaveKey.DrawNote(canvas, clef);
                if (l == 0) DrawTimeSignature(canvas, ys2);

                //Draw Bracket
                if (ShowBrace) {
                    Paint f = new Paint();
                    f.setAntiAlias(true);
                    f.setTypeface(Font.getTypeface());
                    Rect bounds = new Rect();
                    f.getTextBounds(FM_Const.Bracket, 0, 1, bounds);
                    int height = bounds.height();
                    f.setTextSize(f.getTextSize() * (BarYe - BarYs) / height);
                    f.setColor(Font.getColor());
                    canvas.drawText(FM_Const.Bracket, PaddingS - f.measureText(FM_Const.Bracket) - FM_Const.dpTOpx(context, 2), ys2 + 4 * getDistanceBetweenStaveLines(), f);
                }
                //End Draw Bracket
            }
            Font.setColor(Color);
            if (StartBar)
                canvas.drawRect(PaddingS - FM_Const.dpTOpx(context, 1), BarYs - StaveLineHalfWidth, PaddingS, BarYe + StaveLineHalfWidth, Font);
            if (EndBar) {
                if (!TrimLastRow || (l < Lines - 1))
                    canvas.drawRect(width - PaddingE, BarYs - StaveLineHalfWidth, width - PaddingE + FM_Const.dpTOpx(context, 1), BarYe + StaveLineHalfWidth, Font);
                else
                    canvas.drawRect(getLineWidth(l + 1), BarYs - StaveLineHalfWidth, getLineWidth(l + 1) + FM_Const.dpTOpx(context, 1), BarYe + StaveLineHalfWidth, Font);
            }
            ys1 = ys2 + (getDistanceBetweenRows() + 4 * getDistanceBetweenStaveLines());
        }

        for (int i = 0; i < StaveNotes.size(); i++) StaveNotes.get(i).DrawNote(canvas);
        for (int j = 0; j < Ties.size(); j++) Ties.get(j).Draw(canvas);
        for (int j = 0; j < Beams.size(); j++) Beams.get(j).Draw(canvas);
        for (int j = 0; j < Tuplets.size(); j++) Tuplets.get(j).Draw(canvas);

        if (progressBar > -1) {
            int line = StaveNotes.get(progressBar).line;
            float x = StaveNotes.get(progressBar).StartX + StaveNotes.get(progressBar).Width() + FM_Const.dpTOpx(context, 3);
            Paint f = new Paint();
            f.setAntiAlias(true);
            f.setColor(android.graphics.Color.RED);
            float ys = getPaddingVertical();
            float ye = getPaddingVertical() + 4 * getDistanceBetweenStaveLines();
            float mul = (getDistanceBetweenRows() + 4 * getDistanceBetweenStaveLines());
            if (StaveCount == FM_StaveCount._2) {
                ye = ye + getDistanceBetweenStaves() + 4 * getDistanceBetweenStaveLines();
                mul = mul + getDistanceBetweenStaves() + 4 * getDistanceBetweenStaveLines();
            }
            ys = ys + (line - 1) * mul;
            ye = ye + (line - 1) * mul;
            canvas.drawRect(x, ys, x + FM_Const.dpTOpx(context, 1), ye, f);
        }

        if (EndBar) {
            Font.setColor(Color);
            if (TrimLastRow) {
                canvas.drawRect(getLineWidth(Lines) - FM_Const.dpTOpx(context, getDistanceBetweenStaveLines() / 7), BarYs - StaveLineHalfWidth, getLineWidth(Lines), BarYe + StaveLineHalfWidth, Font);
                canvas.drawRect(getLineWidth(Lines) - FM_Const.dpTOpx(context, getDistanceBetweenStaveLines() * 2 / 7), BarYs - StaveLineHalfWidth, getLineWidth(Lines) - FM_Const.dpTOpx(context, getDistanceBetweenStaveLines() * 17 / 70), BarYe + StaveLineHalfWidth, Font);
            } else {
                canvas.drawRect(width - PaddingE - FM_Const.dpTOpx(context, getDistanceBetweenStaveLines() / 7), BarYs - StaveLineHalfWidth, width - PaddingE, BarYe + StaveLineHalfWidth, Font);
                canvas.drawRect(width - PaddingE - FM_Const.dpTOpx(context, getDistanceBetweenStaveLines() * 2 / 7), BarYs - StaveLineHalfWidth, width - PaddingE - FM_Const.dpTOpx(context, getDistanceBetweenStaveLines() * 17 / 70), BarYe + StaveLineHalfWidth, Font);
            }
        }
        canvas.restore();

        if (AllowZoomControls) {
            Paint zoomBtnFnt = new Paint();
            zoomBtnFnt.setStrokeWidth(2);
            zoomBtnFnt.setStyle(Paint.Style.STROKE);
            zoomBtnFnt.setColor(android.graphics.Color.argb(150, 50, 50, 50));
            zoomBtnFnt.setAntiAlias(true);
            canvas.drawRoundRect(width - 110, height - 110, width - 10, height - 10, 10, 10, zoomBtnFnt);
            canvas.drawRoundRect(width - 220, height - 110, width - 120, height - 10, 10, 10,zoomBtnFnt);

            zoomBtnFnt.setColor(android.graphics.Color.argb(200, 0, 0, 0));
            zoomBtnFnt.setStrokeWidth(5);
            canvas.drawLine(width - 90, height - 60, width - 30, height - 60, zoomBtnFnt);
            canvas.drawLine(width - 60, height - 90, width - 60, height - 30, zoomBtnFnt);
            canvas.drawLine(width - 190, height - 60, width - 150, height - 60, zoomBtnFnt);
        }
        if (finishedDraw != null) finishedDraw.countDown();
    }

    public int getColor() {
        return Color;
    }

    public boolean getStartBar() {
        return StartBar;
    }

    public void setStartBar(boolean b) {
        StartBar = b;
        //invalidate();
        //requestLayout();
    }

    public int getLines() {
        return Lines;
    }

    public boolean getEndBar() {
        return EndBar;
    }

    public void setEndBar(boolean b) {
        EndBar = b;
        //invalidate();
        //requestLayout();
    }

    public float getPaddingS() {
        return PaddingS;
    }

    public void setPaddingS(float percent) {
        PaddingS_p = percent;
        if (width < 100) width = 100;
        PaddingS = percent * width / 100.f;
        invalidate();
    }

    public float getPaddingVertical() {
        return PaddingV_cnt * getDistanceBetweenStaveLines();
    }

    public void setPaddingVertical(float count) {
        PaddingV_cnt = count;
        invalidate();
    }

    public float getPaddingE() {
        return PaddingE;
    }

    public void setPaddingE(float percent) {
        PaddingE_p = percent;
        if (width < 100) width = 100;
        PaddingE = percent * width / 100.f;
        //invalidate();
        //requestLayout();
    }

    public float getDistanceBetweenStaveLines() {
        return _DistanceBetweenStaveLines;
    }

    public void setDistanceBetweenStaveLines(float d) {
        if (d < 5) d = 5;
        if (d > 20) d = 20;
        _DistanceBetweenStaveLines = FM_Const.dpTOpx(context, d);
        Font.setTextSize(FM_Const.dpTOpx(context, 5 * d));
        this.ComputeLines();
        invalidate();
        //requestLayout();
    }

    public float getDistanceBetweenRows() {
        return DistanceBetweenRows_cnt * getDistanceBetweenStaveLines();
    }

    public void setDistanceBetweenRows(float d) {
        DistanceBetweenRows_cnt = d;
        //invalidate();
        //requestLayout();
    }

    public float getDistanceBetweenStaves() {
        return DistanceBetweenStave_cnt * getDistanceBetweenStaveLines();
    }

    public void setDistanceBetweenStaves(float d) {
        DistanceBetweenStave_cnt = d;
        //invalidate();
        //requestLayout();
    }

    private float startY;
    private float startX;

    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        int CLICK_ACTION_THRESHOLD = 10;
        return !(differenceX > CLICK_ACTION_THRESHOLD || differenceY > CLICK_ACTION_THRESHOLD);
    }

    private void CapTranslateVars() {
        if (mPosX > 0) mPosX = 0;
        if (mPosX < width * (1 - mScaleFactor)) mPosX = width * (1 - mScaleFactor);

        float adjustY = 0;
        if (CenterVertical) {
            adjustY = height / 2f - getDrawHeight() / 2f;
            if (adjustY >= 0) {
                mPosY = -(mScaleFactor - 1) * getDrawHeight() / 2f;
                return;
            }
        }
        mPosY += adjustY;
        if (mPosY < (height - mScaleFactor * getDrawHeight())) {
            mPosY = (height - mScaleFactor * getDrawHeight());
        }
        if (mPosY > 0) mPosY = 0;
        mPosY -= adjustY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!AllowZoomPan) return super.onTouchEvent(event);
        mScaleDetector.onTouchEvent(event);
        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = event.getX();
                final float y = event.getY();
                startX = x;
                startY = y;
                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = event.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = event.findPointerIndex(mActivePointerId);
                float x = event.getX(pointerIndex);
                float y = event.getY(pointerIndex);

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!mScaleDetector.isInProgress()) {
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;
                    mPosX += dx;
                    mPosY += dy;
                    invalidate();
                }

                mLastTouchX = x;
                mLastTouchY = y;
                CapTranslateVars();
                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                if (isAClick(startX, event.getX(), startY, event.getY())) {
                    boolean save = false;
                    if (AllowZoomControls) {
                        if (event.getX() > width - 110 && event.getX() < width - 10 && event.getY() > height - 110 && event.getY() < height - 10) {
                            setDistanceBetweenStaveLines(FM_Const.pxTOdp(context, _DistanceBetweenStaveLines + 1f));
                            save = true;
                        }
                        if (event.getX() > width - 220 && event.getX() < width - 120 && event.getY() > height - 110 && event.getY() < height - 10) {
                            setDistanceBetweenStaveLines(FM_Const.pxTOdp(context, _DistanceBetweenStaveLines - 1f));
                            save = true;
                        }
                        if (save) {
                            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putFloat("zoom_level", FM_Const.pxTOdp(context, _DistanceBetweenStaveLines));
                            editor.apply();
                        }
                    }
                    if (!save) super.performClick();
                    return super.onTouchEvent(event);
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = event.getX(newPointerIndex);
                    mLastTouchY = event.getY(newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    public int getFirstStaveClef() {
        return FirstStaveClef;
    }

    public void setFirstStaveClef(@FM_ClefValue int firstStaveClef) {
        FirstStaveClef = firstStaveClef;
        setKeySignature(KeySignature);
    }

    public int getSecondStaveClef() {
        return SecondStaveClef;
    }

    public void setSecondStaveClef(@FM_ClefValue int secondStaveClef) {
        SecondStaveClef = secondStaveClef;
        setKeySignature(KeySignature);
    }

    /**
     * Set the default color for the stave elements (notes, pauses, clefs, etc).
     * If you don't set it yourself, the default is android.graphics.Color.argb(255, 26, 28, 33).
     * @param color the drawing color
     */
    public void setColor(int color) {
        Color = color;
        Font.setColor(color);
        FirstStaveKey.setColor(color);
        SecondStaveKey.setColor(color);
        //invalidate();
        //requestLayout();
    }

    public Integer getKeySignature() {
        return KeySignature;
    }

    public void setKeySignature(Integer keySignature) {
        KeySignature = keySignature;
        FirstStaveKey = new FM_KeySignature( FirstStaveClef, keySignature, this);
        SecondStaveKey = new FM_KeySignature(SecondStaveClef, keySignature, this);
        //invalidate();
        //requestLayout();
    }

    protected float getClefWidth() {
        FM_Const.AdjustFont(this, FM_Const._4, 2);
        float w = Font.measureText(FM_Const.TrebleClef) + 2 * FM_Const.dpTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING);
        float w1 = Font.measureText(FM_Const.BassClef) + 2 * FM_Const.dpTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING);
        return Math.max(w, w1);
    }

    private void DrawTrebleClef(Canvas canvas, float y) {
        FM_Const.AdjustFont(this, FM_Const._4, 2);
        Font.setColor(Color);
        canvas.drawText(FM_Const.TrebleClef, PaddingS + FM_Const.dpTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING), y + 3 * getDistanceBetweenStaveLines(), Font);
    }

    private void DrawBassClef(Canvas canvas, float y) {
        FM_Const.AdjustFont(this, FM_Const._4, 2);
        Font.setColor(Color);
        canvas.drawText(FM_Const.BassClef, PaddingS + FM_Const.dpTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING), y + 1 * getDistanceBetweenStaveLines(), Font);
    }

    protected float getTimeSignatureWidth() {
        if (TimeSignature_n == FM_TimeSignatureValue.None) return 0;
        float w = FM_Const.dpTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING);
        FM_Const.AdjustFont(this, FM_Const._4, 2);
        return w + Font.measureText(FM_Const._4);
    }

    private void DrawTimeSignature(Canvas canvas, float y) {
        Font.setColor(Color);
        float pad = PaddingS + getClefWidth() + FirstStaveKey.Width();
        FM_Const.AdjustFont(this, FM_Const._4, 2);
        if (TimeSignature_n == FM_TimeSignatureValue._2)
            canvas.drawText(FM_Const._2, pad, y + 1 * getDistanceBetweenStaveLines(), Font);
        if (TimeSignature_n == FM_TimeSignatureValue._3)
            canvas.drawText(FM_Const._3, pad, y + 1 * getDistanceBetweenStaveLines(), Font);
        if (TimeSignature_n == FM_TimeSignatureValue._4)
            canvas.drawText(FM_Const._4, pad, y + 1 * getDistanceBetweenStaveLines(), Font);
        if (TimeSignature_n == FM_TimeSignatureValue._5)
            canvas.drawText(FM_Const._5, pad, y + 1 * getDistanceBetweenStaveLines(), Font);
        if (TimeSignature_n == FM_TimeSignatureValue._6)
            canvas.drawText(FM_Const._6, pad, y + 1 * getDistanceBetweenStaveLines(), Font);
        if (TimeSignature_n == FM_TimeSignatureValue._7)
            canvas.drawText(FM_Const._7, pad, y + 1 * getDistanceBetweenStaveLines(), Font);
        if (TimeSignature_n == FM_TimeSignatureValue._8)
            canvas.drawText(FM_Const._8, pad, y + 1 * getDistanceBetweenStaveLines(), Font);
        if (TimeSignature_n == FM_TimeSignatureValue._9)
            canvas.drawText(FM_Const._9, pad, y + 1 * getDistanceBetweenStaveLines(), Font);

        if (TimeSignature_d == FM_TimeSignatureValue._2)
            canvas.drawText(FM_Const._2, pad, y + 3 * getDistanceBetweenStaveLines(), Font);
        if (TimeSignature_d == FM_TimeSignatureValue._3)
            canvas.drawText(FM_Const._3, pad, y + 3 * getDistanceBetweenStaveLines(), Font);
        if (TimeSignature_d == FM_TimeSignatureValue._4)
            canvas.drawText(FM_Const._4, pad, y + 3 * getDistanceBetweenStaveLines(), Font);
        if (TimeSignature_d == FM_TimeSignatureValue._5)
            canvas.drawText(FM_Const._5, pad, y + 3 * getDistanceBetweenStaveLines(), Font);
        if (TimeSignature_d == FM_TimeSignatureValue._6)
            canvas.drawText(FM_Const._6, pad, y + 3 * getDistanceBetweenStaveLines(), Font);
        if (TimeSignature_d == FM_TimeSignatureValue._7)
            canvas.drawText(FM_Const._7, pad, y + 3 * getDistanceBetweenStaveLines(), Font);
        if (TimeSignature_d == FM_TimeSignatureValue._8)
            canvas.drawText(FM_Const._8, pad, y + 3 * getDistanceBetweenStaveLines(), Font);
        if (TimeSignature_d == FM_TimeSignatureValue._9)
            canvas.drawText(FM_Const._9, pad, y + 3 * getDistanceBetweenStaveLines(), Font);
    }

    public void clearStaveNotes() {
        StaveNotes.clear();
        Tuplets.clear();
        Beams.clear();
        Ties.clear();
        mPosX = 0;
        mPosY = 0;
        Lines = 1;
        progressBar = -1;
        StaveCount = FM_StaveCount._1;
        setVoiceCount(1);
        setFirstStaveClef(FM_ClefValue.TREBLE);
        setSecondStaveClef(FM_ClefValue.BASS);
        setTimeSignature(FM_TimeSignatureValue.None, FM_TimeSignatureValue.None);
        setKeySignature(FM_KeySignatureValue.DO);
        invalidate();
    }


    public void addStaveNote(FM_BaseNote n) {
        addStaveNote(n, 0);
    }

    public void addStaveNote(FM_BaseNote n, int stave) {
        if (n instanceof FM_BarNote) stave = 0;
        if (stave == 1) StaveCount = FM_StaveCount._2;
        n.stave = stave;
        StaveNotes.add(n);
        ComputeLines();
    }

    public void addChord(List<FM_BaseNote> n, List<Integer> stave) {
        if (n.get(0) instanceof FM_Clef) {
            n.get(0).stave = stave.get(0);
            StaveNotes.add(n.get(0));
        } else {
        FM_Chord C = new FM_Chord(this);
        for (int i = 0; i < n.size(); i++) {
            if (stave.get(i) == 1) StaveCount = FM_StaveCount._2;
            n.get(i).stave = stave.get(i);
            C.addNote(n.get(i));
        }
        C.Compute();
        StaveNotes.add(C);
        }
        ComputeLines();
    }

    private float getStartX(int line) {
        if (line == 1)
            return PaddingS + getClefWidth() + FirstStaveKey.Width() + getTimeSignatureWidth() + FM_Const.dpTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING);
        else
            return PaddingS + getClefWidth() + FirstStaveKey.Width() + FM_Const.dpTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING);
    }

    private float getLineWidth(int line) {
        float X = 0;
        float w = 0;
        for (int j = 0; j < StaveNotes.size(); j++)
            if (StaveNotes.get(j).line == line) {
                if (X < StaveNotes.get(j).StartX) {
                    X = StaveNotes.get(j).StartX;
                    w = StaveNotes.get(j).Width();
                }
            }
        float ret = X + w + 4 * FM_Const.dpTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING);
        if (ret > width - PaddingE) ret = width - PaddingE;
        return ret;
    }

    private void ComputeLines() {
        if (StaveNotes.size() == 0) return;
        int l = 1;
        float endX = width - PaddingE - 2 * FM_Const.dpTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING) - FM_Const.dpTOpx(context, 10);
        float ys1 = getPaddingVertical();
        float ys2 = getPaddingVertical();
        if (StaveCount == FM_StaveCount._2)
            ys2 = ys1 + (getDistanceBetweenStaves() + 4 * getDistanceBetweenStaveLines());

        //set everything to be visible
        for (int i = 0; i < StaveNotes.size(); i++) {
            StaveNotes.get(i).setVisible(true);
            if (StaveNotes.get(i) instanceof FM_BarNote)
                ((FM_BarNote) StaveNotes.get(i)).lineEnd = false;
        }

        if (MultiRow && Align == FM_Align.ALIGN_LEFT_NOTES) {
            float X = getStartX(l);
            int noteIndex = 0;
            FM_BaseNote last_note = null;
            boolean already_a_clef = false;
            for (int i = 0; i < StaveNotes.size(); i++) {
                float w = StaveNotes.get(i).Width() + NoteSpacing;
                if (noteIndex == 0 && StaveNotes.get(i) instanceof FM_Clef) {
                    w = 0;
                    StaveNotes.get(i).setVisible(false);
                    already_a_clef = true;
                }
                if (already_a_clef && noteIndex != 0 && StaveNotes.get(i) instanceof FM_Clef) {
                    w = 0;
                    StaveNotes.get(i).setVisible(false);
                    already_a_clef = true;
                }
                noteIndex++;
                if (X + w > endX) {
                    if (last_note instanceof FM_BarNote) {
                        ((FM_BarNote) last_note).lineEnd = true;
                        last_note.setVisible(false);
                    }
                    l++;
                    noteIndex = 0;
                    X = getStartX(l);
                    ys1 = ys2 + (getDistanceBetweenRows() + 4 * getDistanceBetweenStaveLines());
                    ys2 = ys1;
                    already_a_clef = false;
                }
                if (StaveCount == FM_StaveCount._2) ys2 = ys1 + (getDistanceBetweenStaves() + 4 * getDistanceBetweenStaveLines());
                if (StaveNotes.get(i).stave == 0) StaveNotes.get(i).SetDrawParameters(X, ys1, ys2);
                if (StaveNotes.get(i).stave == 1) StaveNotes.get(i).SetDrawParameters(X, ys2, ys2);
                StaveNotes.get(i).line = l;
                X = X + w;
                last_note = StaveNotes.get(i);
            }
            //If last note is a bar, hide it
            if (StaveNotes.get(StaveNotes.size() - 1) instanceof FM_BarNote) {
                ((FM_BarNote) StaveNotes.get(StaveNotes.size() - 1)).lineEnd = true;
                StaveNotes.get(StaveNotes.size() - 1).setVisible(false);
            }
        }

        float scale = 1.15f;
        if (Align == FM_Align.ALIGN_LEFT_LAST_MEASURE) scale = 1f;
        if (Align == FM_Align.ALIGN_CENTER_NOTES) scale = 1.0f;
        if (MultiRow && (Align == FM_Align.ALIGN_CENTER_MEASURES || Align == FM_Align.ALIGN_CENTER_NOTES || Align == FM_Align.ALIGN_LEFT_MEASURES || Align == FM_Align.ALIGN_LEFT_LAST_MEASURE)) {
            float X = getStartX(l);
            int last_bar = 0;
            int bar_cnt = 0;
            int noteIndex = 0;
            int last_clef = getFirstStaveClef();
            for (int i = 0; i < StaveNotes.size(); i++) {
                noteIndex++;
                float widthIncrement = StaveNotes.get(i).Width() + NoteSpacing;
                if (StaveNotes.get(i) instanceof FM_Clef) {
                    if (noteIndex == 1) {
                        widthIncrement = 0;
                        StaveNotes.get(i).setVisible(false);
                    } else if (last_clef == ((FM_Clef) StaveNotes.get(i)).clef) {
                        widthIncrement = 0;
                        StaveNotes.get(i).setVisible(false);
                    }
                    last_clef = ((FM_Clef) StaveNotes.get(i)).clef;
                }
                if (StaveNotes.get(i) instanceof FM_BarNote) {
                    last_bar = i;
                    bar_cnt++;
                }
                if (bar_cnt > 0 && X + widthIncrement > endX * scale) {
                    StaveNotes.get(last_bar).line = l;
                    StaveNotes.get(last_bar).setVisible(false);
                    ((FM_BarNote) StaveNotes.get(last_bar)).lineEnd = true;
                    l++;
                    noteIndex = 0;
                    X = getStartX(l);
                    ys1 = ys2 + (getDistanceBetweenRows() + 4 * getDistanceBetweenStaveLines());
                    ys2 = ys1;
                    bar_cnt = 0;
                    i = last_bar;
                    continue;
                }
                if (StaveCount == FM_StaveCount._2)
                    ys2 = ys1 + (getDistanceBetweenStaves() + 4 * getDistanceBetweenStaveLines());
                if (StaveNotes.get(i).stave == 0) StaveNotes.get(i).SetDrawParameters(X, ys1, ys2);
                if (StaveNotes.get(i).stave == 1) StaveNotes.get(i).SetDrawParameters(X, ys2, ys2);
                StaveNotes.get(i).line = l;
                X = X + widthIncrement;
            }
            //If last note is a bar, hide it
            if (StaveNotes.get(StaveNotes.size() - 1) instanceof FM_BarNote) {
                ((FM_BarNote) StaveNotes.get(StaveNotes.size() - 1)).lineEnd = true;
                StaveNotes.get(StaveNotes.size() - 1).setVisible(false);
            }
        }

        if (!MultiRow) {
            //If last note is a bar, hide it
            if (StaveNotes.get(StaveNotes.size() - 1) instanceof FM_BarNote) {
                ((FM_BarNote) StaveNotes.get(StaveNotes.size() - 1)).lineEnd = true;
                StaveNotes.get(StaveNotes.size() - 1).setVisible(false);
            }
        }
        Lines = l;

        if (Align == FM_Align.ALIGN_CENTER_NOTES || Align == FM_Align.ALIGN_CENTER_NOTES_ALL) {
            for (int i = 1; i <= Lines; i++) {
                float X = getStartX(i);
                int cnt = 0;
                float diff;
                for (int j = 0; j < StaveNotes.size(); j++)
                    if (StaveNotes.get(j).line == i) {
                        float w = StaveNotes.get(j).Width();
                        if (!StaveNotes.get(j).isVisible()) w = 0;
                        X = X + w;
                        cnt++;

                    }
                float s = (endX - X) / (cnt + 1);
                diff = (endX - X) / (cnt - 1);
                X = getStartX(i);
                if (Align == FM_Align.ALIGN_CENTER_NOTES_ALL) {
                    for (int j = 0; j < StaveNotes.size(); j++)
                        if (StaveNotes.get(j).line == i) {
                            float w = StaveNotes.get(j).Width();
                            if (!StaveNotes.get(j).isVisible()) w = 0;
                            StaveNotes.get(j).SetDrawParameters(X, StaveNotes.get(j).StartY1, StaveNotes.get(j).StartY2);
                            X = X + w + diff;
                        }
                } else {
                    for (int j = 0; j < StaveNotes.size(); j++)
                        if (StaveNotes.get(j).line == i) {
                            float w = StaveNotes.get(j).Width();
                            if (!StaveNotes.get(j).isVisible()) w = 0;
                            StaveNotes.get(j).SetDrawParameters(X + s, StaveNotes.get(j).StartY1, StaveNotes.get(j).StartY2);
                            X = X + w + s;
                        }
                }
            }
        }

        if (Align == FM_Align.ALIGN_CENTER_MEASURES || Align == FM_Align.ALIGN_LEFT_MEASURES || Align == FM_Align.ALIGN_LEFT_LAST_MEASURE) {
            for (int i = 1; i <= Lines; i++) {
                float X = getStartX(i);
                int cnt = 0;
                float diff, w1;
                w1 = -1;
                for (int j = 0; j < StaveNotes.size(); j++)
                    if (StaveNotes.get(j).line == i && StaveNotes.get(j).isVisible()) {
                        if (w1 == -1) w1 = StaveNotes.get(j).Width();
                        else {
                            float w = StaveNotes.get(j).Width();
                            X = X + w;
                            cnt++;
                        }
                    }
                diff = (endX - X) / (cnt + 1);
                X = getStartX(i) + w1;
                w1 = -1;
                if ((Align != FM_Align.ALIGN_LEFT_MEASURES && (Align == FM_Align.ALIGN_LEFT_LAST_MEASURE && i != Lines)) || diff < 0)
                    for (int j = 0; j < StaveNotes.size(); j++)
                        if (StaveNotes.get(j).line == i && StaveNotes.get(j).isVisible()) {
                            if (w1 == -1) {
                                w1 = 0;
                                StaveNotes.get(j).SetDrawParameters(getStartX(i), StaveNotes.get(j).StartY1, StaveNotes.get(j).StartY2);
                            } else {
                                float w = StaveNotes.get(j).Width();
                                if (!StaveNotes.get(j).isVisible()) w = 0;
                                StaveNotes.get(j).SetDrawParameters(X + diff, StaveNotes.get(j).StartY1, StaveNotes.get(j).StartY2);
                                X = X + w + diff;
                            }
                        }
            }
        }

        ys1 = getPaddingVertical();
        ys2 = getPaddingVertical();
        for (int i = 1; i <= Lines; i++) {
            if (StaveCount == FM_StaveCount._2)
                ys2 = ys1 + (getDistanceBetweenStaves() + 4 * getDistanceBetweenStaveLines());
            for (int j = 0; j < StaveNotes.size(); j++) {
                if (StaveNotes.get(j).line == i) {
                    if (StaveNotes.get(j).stave == 0)
                        StaveNotes.get(j).SetDrawParameters(StaveNotes.get(j).StartX, ys1, ys2);
                    if (StaveNotes.get(j).stave == 1)
                        StaveNotes.get(j).SetDrawParameters(StaveNotes.get(j).StartX, ys2, ys2);
                }
            }
            ys1 = ys2 + (getDistanceBetweenRows() + 4 * getDistanceBetweenStaveLines());
            ys2 = ys1;
        }
    }

    public int getNotesAlign() {
        return Align;
    }

    public void setNotesAlign(@FM_Align int align) {
        Align = align;
        ComputeLines();
    }

    HashMap<String, List<FM_Note>> TieNotes = new HashMap<>();

    public void AddToTie(String tie, FM_Note n) {
        if (TieNotes.containsKey(tie)) {
            Objects.requireNonNull(TieNotes.get(tie)).add(n);
            EndTie(tie);
        } else {
            List <FM_Note> note_list = new ArrayList<>();
            note_list.add(n);
            TieNotes.put(tie, note_list);
        }
    }

    private void EndTie(String tie) {
        List <FM_Note> note_list = TieNotes.get(tie);
        assert note_list != null;
        if (note_list.size() != 2) return;
        if (note_list.get(0).stave != note_list.get(1).stave || note_list.get(0).octave != note_list.get(1).octave || !note_list.get(0).note.equals(note_list.get(1).note))
            return;
        FM_Tie t = new FM_Tie(this);
        t.AddStart(note_list.get(0));
        t.AddEnd(note_list.get(1));
        note_list.get(0).isTieStart = true;
        note_list.get(1).isTieEnd = true;
        Ties.add(t);
        TieNotes.remove(tie);
    }

    private boolean inTuplet = false;
    private int TupletPosition = 0;
    private int currentTuplet = 0;
    List<FM_BaseNote> TupletNotes;

    public void BeginTuplet(String s) {
        inTuplet = true;
        TupletNotes = new ArrayList<>();
        TupletPosition = 1;
        if (s.toLowerCase().contains("a")) TupletPosition = 0;
    }

    public void AddToTuplet(FM_BaseNote n) {
        if (inTuplet) TupletNotes.add(n);
    }

    public void EndTuplet() {
        inTuplet = false;
        for (int i = 0; i < TupletNotes.size(); i++)
            if (!(TupletNotes.get(i) instanceof FM_Note || TupletNotes.get(i) instanceof FM_Pause)) return;
        int stave = TupletNotes.get(0).stave;
        int minDuration =  6000;
        int allDuration = 0;
        int cnt = TupletNotes.size();
        for (int i = 0; i < cnt; i++) {
            int d = (int) (FM_Const.getDurationMs(TupletNotes.get(i).duration) * 1000);
            allDuration += d;
            if (d < minDuration) minDuration = d;
        }
        while (allDuration != cnt * minDuration) cnt++;

        for (int i = 0; i < TupletNotes.size(); i++)
            if ((TupletNotes.get(i).stave != stave) /*|| (TupletNotes.get(i)).duration != duration*/) return;
        FM_Tuplet t = new FM_Tuplet(this, cnt, currentTuplet, TupletPosition);
        currentTuplet++;
        for (int i = 0; i < TupletNotes.size(); i++) {
            TupletNotes.get(i).tuplet = true;
            TupletNotes.get(i).tupletSize = TupletNotes.size();
            t.AddNote(TupletNotes.get(i));
        }
        Tuplets.add(t);
    }

    private boolean inBeam = false;
    private int currentBeam = 0;
    List<FM_BaseNote> BeamNotes;

    public void BeginBeam() {
        inBeam = true;
        BeamNotes = new ArrayList<>();
    }

    public void AddToBeam(FM_Note n) {
        if (inBeam) BeamNotes.add(n);
    }

    public void EndBeam() {
        inBeam = false;
        if (BeamNotes.size() < 2) return;
        for (int i = 0; i < BeamNotes.size(); i++)
            if (!(BeamNotes.get(i) instanceof FM_Note)) return;
        for (int i = 0; i < BeamNotes.size(); i++) {
            int d = ((FM_Note) BeamNotes.get(i)).duration;
            if (d == FM_DurationValue.NOTE_WHOLE) return;
            if (d == FM_DurationValue.NOTE_WHOLE_D) return;
            if (d == FM_DurationValue.NOTE_HALF) return;
            if (d == FM_DurationValue.NOTE_HALF_D) return;
            if (d == FM_DurationValue.NOTE_QUARTER) return;
            if (d == FM_DurationValue.NOTE_QUARTER_D) return;
        }
        FM_Beam t = new FM_Beam(this, currentBeam);
        currentBeam++;
        for (int i = 0; i < BeamNotes.size(); i++) {
            ((FM_Note) BeamNotes.get(i)).beam = true;
            t.AddNote((FM_Note) BeamNotes.get(i));
        }
        Beams.add(t);
    }

    public void setNoteSpacing(float noteSpacing) {
        NoteSpacing = FM_Const.dpTOpx(context, noteSpacing);
    }

    public boolean isMultiRow() {
        return MultiRow;
    }

    public void setMultiRow(boolean multiRow) {
        MultiRow = multiRow;
        ComputeLines();
    }

    public boolean isAllowZoomPan() {
        return AllowZoomPan;
    }
    public boolean isAllowZoomControls() {
        return AllowZoomControls;
    }

    public void setAllowZoomControls(boolean allowZoomControls) {
        AllowZoomControls = allowZoomControls;
        tmpZoomControls = allowZoomControls;
        if (allowZoomControls) {
            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
            float d = settings.getFloat("zoom_level", FM_Const.pxTOdp(context, _DistanceBetweenStaveLines));
            setDistanceBetweenStaveLines(d);
        }
        invalidate();
    }
    public void setAllowZoomPan(boolean allowZoomPan) {
        AllowZoomPan = allowZoomPan;
    }

    public int getStaveLineColor() {
        return StaveLineColor;
    }

    public void setStaveLineColor(int staveLineColor) {
        StaveLineColor = staveLineColor;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float prevScale = mScaleFactor;
            mScaleFactor *= detector.getScaleFactor();
            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 5.0f));

            float adjustedScaleFactor = mScaleFactor / prevScale;
            pivotPointX = detector.getFocusX();
            pivotPointY = detector.getFocusY();
            float adjustY = 0;
            if (CenterVertical) adjustY = getMeasuredHeight_FM() / 2f - getDrawHeight() / 2f;
            pivotPointY -= adjustY;
            mPosX += (mPosX - pivotPointX) * (adjustedScaleFactor - 1);
            mPosY += (mPosY - pivotPointY) * (adjustedScaleFactor - 1);
            invalidate();
            return true;
        }
    }

    private float getMeasuredHeight_FM() {
        return Math.max(getLayoutParams().height, getMeasuredHeight());
    }

    public int getNoteCount() {
        return StaveNotes.size();
    }

    public FM_BaseNote getNote(int index) {
        if (index < 0) return null;
        if (index > StaveNotes.size() - 1) return null;
        return StaveNotes.get(index);
    }

    public FM_BaseNote getLastNote() {
        if (getNoteCount() == 0) return null;
        return StaveNotes.get(StaveNotes.size() - 1);
    }

    public int LoadFromJson(JSONObject obj) {
        List<String> key_list = new ArrayList<>();
        List<String> clef_list = new ArrayList<>();
        String keySignature;
        String timeSignature;
        try {
            JSONArray keys = obj.getJSONArray("keys");
            for (int k = 0; k < keys.length(); k++) key_list.add(keys.getJSONArray(k).toString());
            JSONArray clef = obj.getJSONArray("clef");
            for (int k = 0; k < clef.length(); k++) clef_list.add(clef.getString(k));
            timeSignature = obj.optString("timesignature", "4/4");
            keySignature = obj.optString("keysignature", "DO");
        } catch (JSONException e) {
            return -1;
        }

        setNotesAlign(FM_Align.ALIGN_LEFT_LAST_MEASURE);
        clearStaveNotes();
        setTimeSignature(FM_Const.getTimeSignature_n(timeSignature), FM_Const.getTimeSignature_d(timeSignature));
        setKeySignature(FM_Const.StringToKeySignature(keySignature));

        int firstStaveClef = FM_ClefValue.TREBLE;
        int secondStaveClef = FM_ClefValue.BASS;

        if (clef_list.size() >= 1) {
            if (clef_list.get(0).equals("treble")) {
                setFirstStaveClef(FM_ClefValue.TREBLE);
                firstStaveClef = FM_ClefValue.TREBLE;
            }
            else {
                setFirstStaveClef(FM_ClefValue.BASS);
                firstStaveClef = FM_ClefValue.BASS;
            }
        }
        if (clef_list.size() > 1) {
            if (clef_list.get(1).equals("treble")) {
                setSecondStaveClef(FM_ClefValue.TREBLE);
                secondStaveClef = FM_ClefValue.TREBLE;
            }
            else {
                setSecondStaveClef(FM_ClefValue.BASS);
                secondStaveClef = FM_ClefValue.BASS;
            }
        }
        int originalFirstStaveClef = firstStaveClef;
        int originalSecondStaveClef = secondStaveClef;

        int i = 0;
        String beam = "";
        String tie;
        String tuple = "";
        HashMap<Integer, List<FM_BaseNote>> Notes = new HashMap();
        HashMap<Integer, List<Integer>> Staves = new HashMap();
        while (i < key_list.size()) {
            FM_Key key = new FM_Key(key_list.get(i));
            if (key.type == FM_KeyType.Bar) {
                for (Integer k : Notes.keySet())
                    addChord(Objects.requireNonNull(Notes.get(k)), Staves.get(k));
                Notes.clear();
                Staves.clear();
                addStaveNote(new FM_BarNote(this));
                i++;
                continue;
            }
            if (key.type == FM_KeyType.Clef) {
                firstStaveClef = key.clef;
                FM_BaseNote c;
                if (firstStaveClef == FM_ClefValue.BASS) c = new FM_Clef(this, FM_ClefValue.BASS, 0);
                else c = new FM_Clef(this, FM_ClefValue.TREBLE, 0);
                List<FM_BaseNote> Note_List = new ArrayList<>();
                List<Integer> stave_List = new ArrayList<>();
                Note_List.add(c);
                stave_List.add(0);
                Notes.put(key.chord, Note_List);
                Staves.put(key.chord, stave_List);
                i++;
                continue;
            }

            if (!beam.equals("") && (key.beam.equals("") || !key.beam.equals(beam))) EndBeam();
            if (!tuple.equals("") && (key.tuple.equals("") || !key.tuple.equals(tuple))) EndTuplet();

            if (!key.beam.equals(beam) && !key.beam.equals("")) BeginBeam();
            if (!key.tuple.equals(tuple) && !key.tuple.equals("")) BeginTuplet(key.tuple);

            beam = key.beam;
            tie = key.tie;
            tuple = key.tuple;

            FM_BaseNote n;
            if (key.note == FM_NoteValue.REST) {
                n = new FM_Pause(this, key.duration, key.octave, key.voice);
            } else {
                int clef = firstStaveClef;
                if (key.stave == 1) clef = secondStaveClef;
                n = new FM_Note(this, key.note, key.octave, clef, key.accidental, key.duration, key.voice, key.stemUp);
                if (!beam.equals("")) AddToBeam((FM_Note) n);
                if (!tie.equals("")) AddToTie(tie, (FM_Note) n);
            }
            if (!tuple.equals("")) AddToTuplet(n);
            List<FM_BaseNote> Note_List = Notes.get(key.chord);
            List<Integer> stave_List = Staves.get(key.chord);
            if (Note_List == null) Note_List = new ArrayList();
            if (stave_List == null) stave_List = new ArrayList();
            Note_List.add(n);
            stave_List.add(key.stave);
            Notes.put(key.chord, Note_List);
            Staves.put(key.chord, stave_List);
            i++;
        }
        for (Integer k : Notes.keySet()) addChord(Objects.requireNonNull(Notes.get(k)), Staves.get(k));
        Notes.clear();
        Staves.clear();
        if (!beam.equals("")) EndBeam();
        if (!tuple.equals("")) EndTuplet();
        setFirstStaveClef(originalFirstStaveClef);
        setSecondStaveClef(originalSecondStaveClef);
        return 0;
    }

    public void ShowScore(int measures) {
        finishedDraw = new CountDownLatch(1);
        if (measures == 0) AllowZoomControls = tmpZoomControls;
        else AllowZoomControls = false;
        new Thread(() -> {
            for (int i = 0; i < StaveNotes.size(); i++) {
                StaveNotes.get(i).setVisible(true);
                if (StaveNotes.get(i) instanceof FM_BarNote && ((FM_BarNote) StaveNotes.get(i)).lineEnd)
                    StaveNotes.get(i).setVisible(false);
            }
            this.post(this::invalidate);
            try {
                finishedDraw.await();
                finishedDraw = null;
            } catch (Exception ignored) {
            }
            if (measures != 0) {
                int i = 0;
                int bars = 0;
                while (i < StaveNotes.size()) {
                    if (StaveNotes.get(i) instanceof FM_BarNote) bars++;
                    if (bars >= measures) StaveNotes.get(i).setVisible(false);
                    i++;
                }
            }
            this.post(this::invalidate);
        }).start();
    }
}