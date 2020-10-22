package ro.florinm.FM_Score;

//todo add key signatures

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class FM_Score extends View {
    @FM_TimeSignature private int TimeSignature;
    @FM_KeySignatureValue private int KeySignature;
    @FM_StaffCount
    private int StaffCount;
    private int VoiceCount;
    private int Color;
    private int StaveLineColor;
    private boolean ShowBrace;
    private float NoteSpacing;
    int width, height;
    protected Paint Font;
    FM_KeySignature FirstStaveKey, SecondStaveKey;
    private float _DistanceBetweenStaveLines;
    private float DistanceBetweenStaves_cnt;                //Distance between Staves as number of distances between lines
    private float DistanceBetweenRows_cnt;                 //Distance between Rows of staves as number of distances between lines
    private float PaddingV_cnt, PaddingS_p, PaddingE_p;     //padding Start and padding End are set as percentange of the total width
    private float PaddingS, PaddingE;                       //padding Vertical is set as number of Distances between Lines.
    private boolean StartBar;
    private boolean EndBar;
    @FM_ClefValue private int FirstStaveClef, SecondStaveClef;
    private int Lines;
    private final Context context;
    @FM_Align private int Align;

    private boolean CenterVertical = true;
    private boolean MultiLine = false;
    private boolean AllowZoomPan = false;

    private final List<FM_BaseNote> StaveNotes = new ArrayList<>();
    private final List<FM_Tie> Ties = new ArrayList<>();
    private final List<FM_Tuple> Tuples = new ArrayList<>();
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

    private boolean DrawBoundingBox;

    public FM_Score(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        Color = android.graphics.Color.argb(255, 0,0,0);
        setStaveLineColor(android.graphics.Color.argb(255, 100, 100, 100));
        Typeface bravura = Typeface.createFromAsset(context.getAssets(), "bravura.otf");
        Font = new Paint();
        Font.setAntiAlias(true);
        Font.setTypeface(bravura);
        Font.setColor(Color);
        Lines = 1;
        StaffCount = FM_StaffCount._1;
        setVoiceCount(1);
        setNoteSpacing(0);
        setShowBrace(false);
        setDistanceBetweenStaveLines(10);
        setDistanceBetweenStaves(5);
        setDistanceBetweenRows(10);
        setPaddingT(5);
        setPaddingS(5);
        setPaddingE(5);
        setStartBar(true);
        setEndBar(true);
        setMultiLine(false);
        setFirstStaveClef(FM_ClefValue.TREBLE);
        setSecondStaveClef(FM_ClefValue.BASS);
        setTimeSignature(FM_TimeSignature.None);
        setKeySignature(FM_KeySignatureValue.DO);
        setAlign(FM_Align.ALIGN_CENTER_MEASURES);
        setDrawBoundigBox(false);

    }

    public void setDrawBoundigBox(boolean on){
        DrawBoundingBox = on;
    }

    public boolean getDrawBoundingBox(){
        return DrawBoundingBox;
    }

    public boolean getCenterVertical() { return CenterVertical; }
    public void setCenterVertical(boolean center) {
        CenterVertical = center;
        CapTranslateVars();
        invalidate();
    }

    public int getTimeSignature() {
        return TimeSignature;
    }

    public void setTimeSignature(@FM_TimeSignature int timeSignature ) {
        TimeSignature = timeSignature;
        //invalidate();
        requestLayout();
    }

    public int getStaffCount() {
        return StaffCount;
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

    private float getDrawHeight(){
        float MaxHeight = Lines * (4 * getDistanceBetweenStaveLines()) + (Lines - 1) * + getDistanceBetweenRows() + 2 * PaddingV_cnt * getDistanceBetweenStaveLines();
        if (StaffCount == FM_StaffCount._2) MaxHeight = MaxHeight + Lines * (getDistanceBetweenStaves() + 4 * getDistanceBetweenStaveLines());
        //if (MaxHeight < height) return height;
        return  MaxHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            /*if (!CenterVertical) {
                int MaxHeight = (int) getDrawHeight();
                if (MaxHeight < height) MaxHeight = height;
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(MaxHeight, MeasureSpec.AT_MOST);
                getLayoutParams().height = MaxHeight;
            }*/
        } catch (Exception ignored) {
        } finally {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void onSizeChanged(int width, int height, int old_width, int old_height) {
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
            for (int i = 0; i < 5; i++) canvas.drawRect(PaddingS, ys1 + i * getDistanceBetweenStaveLines() - StaveLineHalfWidth, width - PaddingE, ys1 + i * getDistanceBetweenStaveLines() + StaveLineHalfWidth, Font);
            Font.setColor(Color);
            //draw clef
            if (FirstStaveClef == FM_ClefValue.TREBLE) DrawTrebleClef(canvas, ys1);
            else DrawBassClef(canvas, ys1);
            //draw keySignature
            FirstStaveKey.SetDrawParameters(PaddingS + getClefWidth(), ys1, ys1);
            FirstStaveKey.DrawNote(canvas);
            //draw timeSignature
            if (l == 0) DrawTimeSignature(canvas, ys1);

            if (StaffCount == FM_StaffCount._2) {
                ys2 = ys1 + (getDistanceBetweenStaves() + 4 * getDistanceBetweenStaveLines());
                BarYe = ys2 + 4 * getDistanceBetweenStaveLines();
                Font.setColor(StaveLineColor);
                for (int i = 0; i < 5; i++) canvas.drawRect(PaddingS, ys2 + i * getDistanceBetweenStaveLines() - StaveLineHalfWidth, width - PaddingE, ys2 + i * getDistanceBetweenStaveLines() + StaveLineHalfWidth, Font);
                Font.setColor(Color);
                if (SecondStaveClef == FM_ClefValue.TREBLE) DrawTrebleClef(canvas, ys2);
                else DrawBassClef(canvas, ys2);
                SecondStaveKey.SetDrawParameters(PaddingS + getClefWidth(), ys2, ys2);
                SecondStaveKey.DrawNote(canvas);
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
                    canvas.drawText(FM_Const.Bracket, PaddingS - f.measureText(FM_Const.Bracket) - FM_Const.dpTOpx(context,2), ys2 + 4 * getDistanceBetweenStaveLines(), f);
                }
                //End Draw Bracket
            }
            Font.setColor(Color);
            if (StartBar) canvas.drawRect(PaddingS - FM_Const.dpTOpx(context, 1), BarYs - StaveLineHalfWidth, PaddingS, BarYe + StaveLineHalfWidth, Font);
            if (EndBar) canvas.drawRect(width - PaddingE, BarYs - StaveLineHalfWidth, width - PaddingE + FM_Const.dpTOpx(context, 1), BarYe + StaveLineHalfWidth, Font);
            ys1 = ys2 + (getDistanceBetweenRows() + 4 * getDistanceBetweenStaveLines());
        }

        for (int i = 0; i < StaveNotes.size(); i++) StaveNotes.get(i).DrawNote(canvas);
        for (int j = 0; j < Ties.size(); j++) Ties.get(j).Draw(canvas);
        for (int j = 0; j < Tuples.size(); j++) Tuples.get(j).Draw(canvas);
        for (int j = 0; j < Beams.size(); j++) Beams.get(j).Draw(canvas);

        if (EndBar) {
            Font.setColor(Color);
            canvas.drawRect(width - PaddingE - FM_Const.dpTOpx(context,getDistanceBetweenStaveLines() / 7), BarYs, width - PaddingE, BarYe, Font);
            canvas.drawRect(width - PaddingE - FM_Const.dpTOpx(context,getDistanceBetweenStaveLines() * 2 / 7), BarYs, width - PaddingE - FM_Const.dpTOpx(context,getDistanceBetweenStaveLines() *17 / 70), BarYe, Font);
        }
        canvas.restore();
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

    public void setPaddingT(float count) {
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
        _DistanceBetweenStaveLines = FM_Const.dpTOpx(context, d);
        Font.setTextSize(FM_Const.dpTOpx(context, 5 * d));
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
        return DistanceBetweenStaves_cnt * getDistanceBetweenStaveLines();
    }

    public void setDistanceBetweenStaves(float d) {
        DistanceBetweenStaves_cnt = d;
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
                    super.performClick();
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
        FirstStaveKey = new FM_KeySignature(context, FirstStaveClef, keySignature, this);
        SecondStaveKey = new FM_KeySignature(context, SecondStaveClef, keySignature, this);
        //invalidate();
        //requestLayout();
    }

    protected float getClefWidth(){
        FM_Const.AdjustFont(this, FM_Const._4, 2);
        float w = Font.measureText(FM_Const.TrebleClef) + 2 * FM_Const.dpTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING);
        float w1 = Font.measureText(FM_Const.BassClef) + 2 * FM_Const.dpTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING);
        return Math.max(w, w1);
    }

    private void DrawTrebleClef(Canvas canvas, float y){
        FM_Const.AdjustFont(this, FM_Const._4, 2);
        Font.setColor(Color);
        canvas.drawText(FM_Const.TrebleClef, PaddingS + FM_Const.dpTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING), y + 3 * getDistanceBetweenStaveLines(), Font);
    }

    private void DrawBassClef(Canvas canvas, float y){
        FM_Const.AdjustFont(this, FM_Const._4, 2);
        Font.setColor(Color);
        canvas.drawText(FM_Const.BassClef, PaddingS + FM_Const.dpTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING), y + 1 * getDistanceBetweenStaveLines(), Font);
    }

    protected float getTimeSignatureWidth(){
        float w = FM_Const.dpTOpx(context,FM_Const.DEFAULT_EXTRA_PADDING);
        FM_Const.AdjustFont(this, FM_Const._4, 2);
        if (TimeSignature != FM_TimeSignature.None) w = w + Font.measureText(FM_Const._4);
        return w;
    }

    private void DrawTimeSignature(Canvas canvas, float y){
        Font.setColor(Color);
        float pad = PaddingS + getClefWidth() + FirstStaveKey.WidthAll();
        FM_Const.AdjustFont(this, FM_Const._4, 2);
        if (TimeSignature == FM_TimeSignature._4_4) {
            canvas.drawText(FM_Const._4, pad, y + 1 * getDistanceBetweenStaveLines(), Font);
            canvas.drawText(FM_Const._4, pad, y + 3 * getDistanceBetweenStaveLines(), Font);
        }
        if (TimeSignature == FM_TimeSignature._2_4) {
            canvas.drawText(FM_Const._2, pad, y + 1 * getDistanceBetweenStaveLines(), Font);
            canvas.drawText(FM_Const._4, pad, y + 3 * getDistanceBetweenStaveLines(), Font);
        }
        if (TimeSignature == FM_TimeSignature._3_4) {
            canvas.drawText(FM_Const._3, pad, y + 1 * getDistanceBetweenStaveLines(), Font);
            canvas.drawText(FM_Const._4, pad, y + 3 * getDistanceBetweenStaveLines(), Font);
        }
        if (TimeSignature == FM_TimeSignature._3_2) {
            canvas.drawText(FM_Const._3, pad, y + 1 * getDistanceBetweenStaveLines(), Font);
            canvas.drawText(FM_Const._2, pad, y + 3 * getDistanceBetweenStaveLines(), Font);
        }
    }

    public void clearStaffNotes(){
        StaveNotes.clear();
        Tuples.clear();
        Beams.clear();
        Ties.clear();
        mPosX = 0;
        mPosY = 0;
        Lines = 1;
        StaffCount = FM_StaffCount._1;
        setVoiceCount(1);
        setFirstStaveClef(FM_ClefValue.TREBLE);
        setSecondStaveClef(FM_ClefValue.BASS);
        setTimeSignature(FM_TimeSignature.None);
        setKeySignature(FM_KeySignatureValue.DO);
        invalidate();
    }


    public void addStaffNote(FM_BaseNote n) {
        addStaffNote(n, FirstStaveClef);
    }

    public void addStaffNote(FM_BaseNote n, @FM_ClefValue int clef){
        if (n instanceof FM_BarNote) clef = FirstStaveClef;
        if (clef != FirstStaveClef && clef == SecondStaveClef) StaffCount = FM_StaffCount._2;
        n.setClef(clef);
        StaveNotes.add(n);
        ComputeLines();
    }

    public void addChord(List<FM_Note> n, @FM_ClefValue List<Integer> clef) {
        FM_Chord C = new FM_Chord(this);
        for (int i = 0; i < n.size(); i++) {
            if (clef.get(i) != FirstStaveClef && clef.get(i) == SecondStaveClef) StaffCount = FM_StaffCount._2;
            n.get(i).setClef(clef.get(i));
            C.addNote(n.get(i));
        }
        C.Compute();
        StaveNotes.add(C);
        ComputeLines();
    }

    private void ComputeLines() {
        if (StaveNotes.size() == 0) return;
        int l = 1;
        float startX = PaddingS + getClefWidth() + FirstStaveKey.WidthAll() + getTimeSignatureWidth() + 2 * FM_Const.dpTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING);
        float endX = width - PaddingE - 2 * FM_Const.dpTOpx(context, FM_Const.DEFAULT_EXTRA_PADDING);
        float ys1 = getPaddingVertical();
        float ys2 = getPaddingVertical();
        if (StaffCount == FM_StaffCount._2) ys2 = ys1 + (getDistanceBetweenStaves() + 4 * getDistanceBetweenStaveLines());
        for (int i = 0; i < StaveNotes.size(); i++) StaveNotes.get(i).setVisible(true);

        if (MultiLine && Align == FM_Align.ALIGN_LEFT_NOTES) {
            float X = startX;
            FM_BaseNote last_note = null;
            for (int i = 0; i < StaveNotes.size(); i++) {
                float w = StaveNotes.get(i).WidthAll(true) + NoteSpacing;
                if (X + w > endX) {
                    if (last_note instanceof FM_BarNote) last_note.setVisible(false);
                    l++;
                    X = startX;
                    ys1 = ys2 + (getDistanceBetweenRows() + 4 * getDistanceBetweenStaveLines());
                    ys2 = ys1;
                }
                if (StaffCount == FM_StaffCount._2)
                    ys2 = ys1 + (getDistanceBetweenStaves() + 4 * getDistanceBetweenStaveLines());
                if (StaveNotes.get(i).clef == FirstStaveClef)
                    StaveNotes.get(i).SetDrawParameters(X, ys1, ys2);
                if (StaveNotes.get(i).clef == SecondStaveClef)
                    StaveNotes.get(i).SetDrawParameters(X, ys2, ys2);
                StaveNotes.get(i).line = l;
                X = X + w;
                last_note = StaveNotes.get(i);
            }
            //If last note is a bar, hide it
            if (StaveNotes.get(StaveNotes.size() - 1) instanceof FM_BarNote) StaveNotes.get(StaveNotes.size() - 1).setVisible(false);
        }

        float scale = 1.15f;
        if (Align == FM_Align.ALIGN_CENTER_NOTES) scale = 1.0f;
        if (MultiLine && (Align == FM_Align.ALIGN_CENTER_MEASURES || Align == FM_Align.ALIGN_CENTER_NOTES || Align == FM_Align.ALIGN_LEFT_MEASURES)) {
            float X = startX;
            int last_bar = 0;
            int bar_cnt = 0;
            for (int i = 0; i < StaveNotes.size(); i++) {
                float w = StaveNotes.get(i).WidthAll(true) + NoteSpacing;
                if (StaveNotes.get(i) instanceof FM_BarNote) {
                    last_bar = i;
                    bar_cnt++;
                }
                if (bar_cnt > 0 && X + w > endX * scale) {
                    l++;
                    X = startX;
                    ys1 = ys2 + (getDistanceBetweenRows() + 4 * getDistanceBetweenStaveLines());
                    ys2 = ys1;
                    bar_cnt = 0;
                    i = last_bar;
                    StaveNotes.get(last_bar).setVisible(false);
                    continue;
                }
                if (StaffCount == FM_StaffCount._2)
                    ys2 = ys1 + (getDistanceBetweenStaves() + 4 * getDistanceBetweenStaveLines());
                if (StaveNotes.get(i).clef == FirstStaveClef)
                    StaveNotes.get(i).SetDrawParameters(X, ys1, ys2);
                if (StaveNotes.get(i).clef == SecondStaveClef)
                    StaveNotes.get(i).SetDrawParameters(X, ys2, ys2);
                StaveNotes.get(i).line = l;
                X = X + w;
            }
            //If last note is a bar, hide it
            if (StaveNotes.get(StaveNotes.size() - 1) instanceof FM_BarNote) StaveNotes.get(StaveNotes.size() - 1).setVisible(false);
        }

        if (!MultiLine){
            //If last note is a bar, hide it
            if (StaveNotes.get(StaveNotes.size() - 1) instanceof FM_BarNote) StaveNotes.get(StaveNotes.size() - 1).setVisible(false);
        }
        Lines = l;

        if (Align == FM_Align.ALIGN_CENTER_NOTES) {
            for (int i = 1; i <= Lines; i++) {
                float X = startX;
                int cnt = 0;
                float diff;
                for (int j = 0; j < StaveNotes.size(); j++)
                    if (StaveNotes.get(j).line == i) {
                        float w = StaveNotes.get(j).WidthAll(true);
                        X = X + w;
                        cnt++;

                    }
                diff = (endX - X) / (cnt + 1);
                X = startX;
                if (Align != FM_Align.ALIGN_LEFT_MEASURES || diff < 0)
                    for (int j = 0; j < StaveNotes.size(); j++)
                        if (StaveNotes.get(j).line == i) {
                            float w = StaveNotes.get(j).WidthAll(true);
                            StaveNotes.get(j).SetDrawParameters(X + diff, StaveNotes.get(j).StartY1, StaveNotes.get(j).StartY2);
                            X = X + w + diff;

                        }
            }
        }

        if (Align == FM_Align.ALIGN_CENTER_MEASURES || Align == FM_Align.ALIGN_LEFT_MEASURES) {
            for (int i = 1; i <= Lines; i++) {
                float X = startX;
                int cnt = 0;
                float diff, w1, we;
                w1 = -1;
                for (int j = 0; j < StaveNotes.size(); j++)
                    if (StaveNotes.get(j).line == i) {
                        if (w1 == -1) w1 = StaveNotes.get(j).WidthAll(true);
                        else {
                            float w = StaveNotes.get(j).WidthAll(true);
                            X = X + w;
                            cnt++;
                        }
                    }
                diff = (endX - X) / (cnt + 1);
                X = startX + w1;
                w1 = -1;
                if (Align != FM_Align.ALIGN_LEFT_MEASURES || diff < 0)
                    for (int j = 0; j < StaveNotes.size(); j++)
                        if (StaveNotes.get(j).line == i) {
                            if (w1 == -1) {
                                w1 = 0;
                                StaveNotes.get(j).SetDrawParameters(startX, StaveNotes.get(j).StartY1, StaveNotes.get(j).StartY2);
                            }
                            else {
                                float w = StaveNotes.get(j).WidthAll(true);
                                StaveNotes.get(j).SetDrawParameters(X + diff, StaveNotes.get(j).StartY1, StaveNotes.get(j).StartY2);
                                X = X + w + diff;
                            }
                        }
            }
        }

        ys1 = getPaddingVertical();
        ys2 = getPaddingVertical();
        for (int i = 1; i <= Lines; i++) {
            if (StaffCount == FM_StaffCount._2) ys2 = ys1 + (getDistanceBetweenStaves() + 4 * getDistanceBetweenStaveLines());
            for (int j = 0; j < StaveNotes.size(); j++) {
                if (StaveNotes.get(j).line == i) {
                    if (StaveNotes.get(j).clef == FirstStaveClef) StaveNotes.get(j).SetDrawParameters(StaveNotes.get(j).StartX, ys1, ys2);
                    if (StaveNotes.get(j).clef == SecondStaveClef) StaveNotes.get(j).SetDrawParameters(StaveNotes.get(j).StartX, ys2, ys2);
                }
            }
            ys1 = ys2 + (getDistanceBetweenRows() + 4 * getDistanceBetweenStaveLines());
            ys2 = ys1;
        }
    }

    public int getAlign() {
        return Align;
    }

    public void setAlign(@FM_Align int align) {
        Align = align;
        ComputeLines();
    }


    private boolean inTie = false;
    private int currentTie = 0;
    List <FM_Note> TieNotes;

    public void BeginTie(){
        inTie = true;
        TieNotes = new ArrayList<>();
    }

    public void AddToTie(FM_Note n){
        if (inTie) TieNotes.add(n);
    }

    public void EndTie(){
        inTie = false;
        if (TieNotes.size() != 2) return;
        if (!TieNotes.get(0).clef.equals(TieNotes.get(1).clef) || TieNotes.get(0).octave != TieNotes.get(1).octave || !TieNotes.get(0).note.equals(TieNotes.get(1).note)
        || !TieNotes.get(0).asStringAccidental().equals(TieNotes.get(1).asStringAccidental())) return;
        FM_Tie t = new FM_Tie(this, currentTie);
        currentTie++;
        t.AddStart(TieNotes.get(0));
        t.AddEnd(TieNotes.get(1));
        Ties.add(t);
    }

    private boolean inTuple = false;
    private int inTuple_size = 3;
    private int currentTuple = 0;
    List <FM_BaseNote> TupleNotes;

    public void BeginTuple(int size){
        inTuple = true;
        inTuple_size = size;
        TupleNotes = new ArrayList<>();
    }

    public void AddToTuple(FM_Note n){
        if (inTuple) TupleNotes.add(n);
    }

    public void EndTuple() {
        inTuple = false;
        if (TupleNotes.size() != inTuple_size) return;
        for (int i = 0; i < TupleNotes.size(); i++)
            if (!(TupleNotes.get(i) instanceof FM_Note)) return;
        int clef = TupleNotes.get(0).clef;
        int duration = ((FM_Note) TupleNotes.get(0)).duration;
        for (int i = 0; i < TupleNotes.size(); i++)
            if ((TupleNotes.get(i).clef != clef) || (((FM_Note) TupleNotes.get(i)).duration != duration))
                return;
        FM_Tuple t = new FM_Tuple(this, inTuple_size, currentTuple);
        currentTuple++;
        for (int i = 0; i < TupleNotes.size(); i++) {
            ((FM_Note)TupleNotes.get(i)).tuple = true;
            t.AddNote((FM_Note) TupleNotes.get(i));
        }
        Tuples.add(t);
    }

    private boolean inBeam = false;
    private int currentBeam = 0;
    List <FM_BaseNote> BeamNotes;

    public void BeginBeam(){
        inBeam = true;
        BeamNotes = new ArrayList<>();
    }

    public void AddToBeam(FM_Note n){
        if (inBeam) BeamNotes.add(n);
    }

    public void EndBeam() {
        inBeam = false;
        if (BeamNotes.size() == 0) return;
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
            ((FM_Note)BeamNotes.get(i)).beam = true;
            t.AddNote((FM_Note) BeamNotes.get(i));
        }
        Beams.add(t);
    }

    public void setNoteSpacing(float noteSpacing) {
        NoteSpacing = FM_Const.dpTOpx(context, noteSpacing);
    }

    public boolean isMultiLine() {
        return MultiLine;
    }

    public void setMultiLine(boolean multiLine) {
        MultiLine = multiLine;
        ComputeLines();
    }

    public boolean isAllowZoomPan() {
        return AllowZoomPan;
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

    private float getMeasuredHeight_FM(){
        return Math.max(getLayoutParams().height, getMeasuredHeight());
    }

    public int getNoteCount() {
        return StaveNotes.size();
    }

    public FM_BaseNote getNote(int index){
        if (index < 0) return null;
        if (index > StaveNotes.size() - 1) return null;
        return StaveNotes.get(index);
    }

    public FM_BaseNote getLastNote(){
        if (getNoteCount() == 0) return null;
        return StaveNotes.get(StaveNotes.size()-1);
    }
}