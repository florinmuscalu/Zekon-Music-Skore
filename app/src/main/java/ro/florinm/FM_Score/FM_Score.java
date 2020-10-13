package ro.florinm.FM_Score;

//todo add key signatures

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class FM_Score extends View {
    private int ID;
    @FM_TimeSignature private int TimeSignature;
    @FM_KeySignatureValue private int KeySignature;
    @FM_StaffCount
    private int StaffCount;
    private int VoiceCount;
    private int Color;
    private boolean ShowBrace;
    private float NoteSpacing;
    int width, height;
    protected Paint StaveFont;
    FM_KeySignature FirstStaveKey, SecondStaveKey;
    private float DistanceBetweenStaveLines;
    private float DistanceBetweenStaves;
    private float DistanceBetweenLines;
    protected Paint StaveLineColor;
    private float PaddingTop;
    float PaddingStart;
    float PaddingEnd;
    private boolean StartBar;
    private boolean EndBar;
    @FM_ClefValue private int FirstStaveClef, SecondStaveClef;
    private int Lines;
    private final Context context;
    @FM_Align private int Align;


    private List<FM_BaseNote> StaveNotes = new ArrayList<>();
    private List<FM_Tie> Ties = new ArrayList<>();
    private List<FM_Tuple> Tuples = new ArrayList<>();
    private List<FM_Beam> Beams = new ArrayList<>();

    public FM_Score(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        ID = 0;
        VoiceCount = 1;
        NoteSpacing = 0;
        ShowBrace = false;
        Color = 0;
        StaveLineColor = new Paint();
        StaveLineColor.setColor(Color);
        StaveLineColor.setAntiAlias(true);
        Typeface bravura = Typeface.createFromAsset(context.getAssets(), "bravura.otf");
        StaveFont = new Paint();
        StaveFont.setAntiAlias(true);
        StaveFont.setTypeface(bravura);
        StaveFont.setColor(StaveLineColor.getColor());
        Lines = 1;
        setDistanceBetweenStaveLines(10);
        setDistanceBetweenStaves(50);
        setDistanceBetweenLines(50);
        setPaddingTop(40);
        setPaddingStart(15);
        setPaddingEnd(10);
        setStartBar(true);
        setEndBar(true);
        StaffCount = FM_StaffCount._1;
        setFirstStaveClef(FM_ClefValue.TREBLE);
        setSecondStaveClef(FM_ClefValue.BASS);
        setTimeSignature(FM_TimeSignature.None);
        setKeySignature(FM_KeySignatureValue.DO);
        setAlign(FM_Align.ALIGN_LEFT_MEASURES);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTimeSignature() {
        return TimeSignature;
    }

    public void setTimeSignature(@FM_TimeSignature int timeSignature ) {
        TimeSignature = timeSignature;
        invalidate();
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
        invalidate();
        requestLayout();
    }

    public boolean isShowBrace() {
        return ShowBrace;
    }

    public void setShowBrace(boolean showBrace) {
        ShowBrace = showBrace;
        invalidate();
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        try {
            int MaxHeight = (int) (PaddingTop + (Lines * (4 * DistanceBetweenStaveLines + DistanceBetweenLines)));
            if (StaffCount == FM_StaffCount._2)
                MaxHeight = MaxHeight + Lines * (int)(DistanceBetweenStaves + 4 * DistanceBetweenStaveLines);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(MaxHeight, MeasureSpec.AT_MOST);
            getLayoutParams().height = MaxHeight;
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
        ComputeLines();
        invalidate();
        requestLayout();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        float ys1 = PaddingTop;
        float ys2 = PaddingTop;
        float BarYs = 0;
        float BarYe = 0;
        for (int l = 0; l < Lines; l++) {
            ys2 = ys1;
            BarYs = ys1;
            BarYe = ys1 + 4 * DistanceBetweenStaveLines;
            //draw stave lines
            for (int i = 0; i < 5; i++) canvas.drawLine(PaddingStart, ys1 + i * DistanceBetweenStaveLines, width - PaddingEnd, ys1 + i * DistanceBetweenStaveLines, StaveLineColor);
            //draw clef
            if (FirstStaveClef == FM_ClefValue.TREBLE) DrawTrebleClef(canvas, ys1);
            else DrawBassClef(canvas, ys1);
            //draw keySignature
            FirstStaveKey.SetDrawParameters(PaddingStart + getClefWidth(), ys1, ys1);
            FirstStaveKey.DrawNote(canvas);
            //draw timeSignature
            if (l == 0) DrawTimeSignature(canvas, ys1);

            if (StaffCount == FM_StaffCount._2) {
                ys2 = ys1 + (DistanceBetweenStaves + 4 * DistanceBetweenStaveLines);
                BarYe = ys2 + 4 * DistanceBetweenStaveLines;
                for (int i = 0; i < 5; i++) canvas.drawLine(PaddingStart, ys2 + i * DistanceBetweenStaveLines, width - PaddingEnd, ys2 + i * DistanceBetweenStaveLines, StaveLineColor);
                if (SecondStaveClef == FM_ClefValue.TREBLE) DrawTrebleClef(canvas, ys2);
                else DrawBassClef(canvas, ys2);
                SecondStaveKey.SetDrawParameters(PaddingStart + getClefWidth(), ys2, ys2);
                SecondStaveKey.DrawNote(canvas);
                if (l == 0) DrawTimeSignature(canvas, ys2);

                //Draw Bracket
                if (ShowBrace) {
                    Paint f = new Paint();
                    f.setAntiAlias(true);
                    f.setTypeface(StaveFont.getTypeface());
                    Rect bounds = new Rect();
                    f.getTextBounds(FM_Const.Bracket, 0, 1, bounds);
                    int height = bounds.height();
                    f.setTextSize(f.getTextSize() * (BarYe - BarYs) / height);
                    f.setColor(StaveFont.getColor());
                    canvas.drawText(FM_Const.Bracket, PaddingStart - f.measureText(FM_Const.Bracket) - 2, ys2 + 4 * DistanceBetweenStaveLines, f);
                }
                //End Draw Bracket
            }

            if (StartBar) canvas.drawLine(PaddingStart, BarYs, PaddingStart, BarYe, StaveLineColor);
            if (EndBar) canvas.drawLine(width - PaddingEnd, BarYs, width - PaddingEnd, BarYe, StaveLineColor);
            ys1 = ys2 + (DistanceBetweenLines + 4 * DistanceBetweenStaveLines);
        }

        for (int i = 0; i < StaveNotes.size(); i++) StaveNotes.get(i).DrawNote(canvas);
        for (int j = 0; j < Ties.size(); j++) Ties.get(j).Draw(this, canvas);
        for (int j = 0; j < Tuples.size(); j++) Tuples.get(j).Draw(this, canvas);
        for (int j = 0; j < Beams.size(); j++) Beams.get(j).Draw(this, canvas);

        if (EndBar) {
            canvas.drawLine(width - PaddingEnd - 1, BarYs, width - PaddingEnd - 1, BarYe, StaveLineColor);
            canvas.drawLine(width - PaddingEnd - 2, BarYs, width - PaddingEnd - 2, BarYe, StaveLineColor);
            canvas.drawLine(width - PaddingEnd - 3, BarYs, width - PaddingEnd - 3, BarYe, StaveLineColor);
            canvas.drawLine(width - PaddingEnd - 4, BarYs, width - PaddingEnd - 4, BarYe, StaveLineColor);
            canvas.drawLine(width - PaddingEnd - 5, BarYs, width - PaddingEnd - 5, BarYe, StaveLineColor);
            canvas.drawLine(width - PaddingEnd - 10, BarYs, width - PaddingEnd - 10, BarYe, StaveLineColor);
            canvas.drawLine(width - PaddingEnd - 11, BarYs, width - PaddingEnd - 11, BarYe, StaveLineColor);
        }
        invalidate();
        requestLayout();
    }

    public int getColor() {
        return Color;
    }

    public boolean getStartBar() {
        return StartBar;
    }

    public void setStartBar(boolean b) {
        StartBar = b;
        invalidate();
        requestLayout();
    }

    public int getLines() {
        return Lines;
    }

    public boolean getEndBar() {
        return EndBar;
    }

    public void setEndBar(boolean b) {
        EndBar = b;
        invalidate();
        requestLayout();
    }

    public int getPaddingStart() {
        return (int)PaddingStart;
    }

    public void setPaddingStart(int p) {
        PaddingStart = FM_Const.dpTOpx(context, p);
        invalidate();
        requestLayout();
    }

    public int getPaddingTop() {
        return (int)PaddingTop;
    }

    public void setPaddingTop(int p) {
        PaddingTop = FM_Const.dpTOpx(context, p);
        invalidate();
        requestLayout();
    }

    public int getPaddingEnd() {
        return (int)PaddingEnd;
    }

    public void setPaddingEnd(int p) {
        PaddingEnd = FM_Const.dpTOpx(context, p);
        invalidate();
        requestLayout();
    }

    public float getDistanceBetweenStaveLines() {
        return DistanceBetweenStaveLines;
    }

    public void setDistanceBetweenStaveLines(float d) {
        DistanceBetweenStaveLines = FM_Const.dpTOpx(context, d);
        StaveFont.setTextSize(FM_Const.spTOpx(context, 5 * d));
        invalidate();
        requestLayout();
    }

    public float getDistanceBetweenLines() {
        return DistanceBetweenLines;
    }

    public void setDistanceBetweenLines(float d) {
        DistanceBetweenLines = FM_Const.dpTOpx(context, d);
        invalidate();
        requestLayout();
    }

    public float getDistanceBetweenStaves() {
        return DistanceBetweenStaves;
    }

    public void setDistanceBetweenStaves(float d) {
        DistanceBetweenStaves = FM_Const.dpTOpx(context, d);
        invalidate();
        requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                return true;

            case MotionEvent.ACTION_UP:
                performClick();
                return true;
        }
        return false;
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
        StaveLineColor.setColor(color);
        FirstStaveKey.setColor(color);
        SecondStaveKey.setColor(color);
        invalidate();
        requestLayout();
    }

    public Integer getKeySignature() {
        return KeySignature;
    }

    public void setKeySignature(Integer keySignature) {
        KeySignature = keySignature;
        FirstStaveKey = new FM_KeySignature(context, FirstStaveClef, keySignature, Color, this);
        SecondStaveKey = new FM_KeySignature(context, SecondStaveClef, keySignature, Color, this);
        invalidate();
        requestLayout();
    }

    protected float getClefWidth(){
        float w = StaveFont.measureText(FM_Const.TrebleClef) + 2 * FM_Const.spTOpx(context, 4);
        float w1 = StaveFont.measureText(FM_Const.BassClef) + 2 * FM_Const.spTOpx(context, 4);
        return Math.max(w, w1);
    }

    private void DrawTrebleClef(Canvas canvas, float y){
        StaveFont.setColor(StaveLineColor.getColor());
        canvas.drawText(FM_Const.TrebleClef, PaddingStart+FM_Const.spTOpx(context, 3), y + 3 * DistanceBetweenStaveLines, StaveFont);
    }

    private void DrawBassClef(Canvas canvas, float y){
        StaveFont.setColor(StaveLineColor.getColor());
        canvas.drawText(FM_Const.BassClef, PaddingStart+FM_Const.spTOpx(context, 3), y + 1 * DistanceBetweenStaveLines, StaveFont);
    }

    protected float getTimeSignatureWidth(){
        float w = 0;
        if (TimeSignature != FM_TimeSignature.None) w = StaveFont.measureText(FM_Const._4) + 2 * FM_Const.spTOpx(context,4);
        return w;
    }

    private void DrawTimeSignature(Canvas canvas, float y){
        StaveFont.setColor(StaveLineColor.getColor());
        if (TimeSignature == FM_TimeSignature._4_4) {
            canvas.drawText(FM_Const._4, PaddingStart + getClefWidth() + FirstStaveKey.WidthAll(StaveFont), y + 1 * DistanceBetweenStaveLines, StaveFont);
            canvas.drawText(FM_Const._4, PaddingStart + getClefWidth() + FirstStaveKey.WidthAll(StaveFont), y + 3 * DistanceBetweenStaveLines, StaveFont);
        }
        if (TimeSignature == FM_TimeSignature._2_4) {
            canvas.drawText(FM_Const._2, PaddingStart + getClefWidth() + FirstStaveKey.WidthAll(StaveFont), y + 1 * DistanceBetweenStaveLines, StaveFont);
            canvas.drawText(FM_Const._4, PaddingStart + getClefWidth() + FirstStaveKey.WidthAll(StaveFont), y + 3 * DistanceBetweenStaveLines, StaveFont);
        }
        if (TimeSignature == FM_TimeSignature._3_4) {
            canvas.drawText(FM_Const._3, PaddingStart + getClefWidth() + FirstStaveKey.WidthAll(StaveFont), y + 1 * DistanceBetweenStaveLines, StaveFont);
            canvas.drawText(FM_Const._4, PaddingStart + getClefWidth() + FirstStaveKey.WidthAll(StaveFont), y + 3 * DistanceBetweenStaveLines, StaveFont);
        }
        if (TimeSignature == FM_TimeSignature._3_2) {
            canvas.drawText(FM_Const._3, PaddingStart + getClefWidth() + FirstStaveKey.WidthAll(StaveFont), y + 1 * DistanceBetweenStaveLines, StaveFont);
            canvas.drawText(FM_Const._2, PaddingStart + getClefWidth() + FirstStaveKey.WidthAll(StaveFont), y + 3 * DistanceBetweenStaveLines, StaveFont);
        }
    }

    public void addStaffNote(FM_BaseNote n){
        addStaffNote(n, FirstStaveClef,false, false, false);
    }

    public void addStaffNote(FM_BaseNote n, @FM_ClefValue int clef){
        addStaffNote(n, clef,false, false, false);
    }

    public void addStaffNote(FM_BaseNote n, boolean addToBeam) {
        addStaffNote(n, FirstStaveClef, addToBeam, false, false);
    }

    public void addStaffNote(FM_BaseNote n, @FM_ClefValue int clef, boolean addToBeam) {
        addStaffNote(n, clef, addToBeam, false, false);
    }

    public void addStaffNote(FM_BaseNote n, boolean addToBeam, boolean addToTuple) {
        addStaffNote(n, FirstStaveClef, addToBeam, addToTuple, false);
    }

    public void addStaffNote(FM_BaseNote n, @FM_ClefValue int clef, boolean addToBeam, boolean addToTuple) {
        addStaffNote(n, clef, addToBeam, addToTuple, false);
    }

    public void addStaffNote(FM_BaseNote n, boolean addToBeam, boolean addToTuple, boolean addToTie) {
        addStaffNote(n, FirstStaveClef, addToBeam, addToTuple, addToTie);
    }

    public void addStaffNote(FM_BaseNote n, @FM_ClefValue int clef, boolean addToBeam, boolean addToTuple, boolean addToTie){
        if (n instanceof FM_BarNote) clef = FirstStaveClef;
        if (clef == SecondStaveClef) StaffCount = FM_StaffCount._2;
        n.setClef(clef);
        n.setColor(Color);
        n.setContext(context);
        StaveNotes.add(n);
        if (n instanceof FM_Note && addToBeam) AddToBeam((FM_Note)n);
        if (n instanceof FM_Note && addToTuple) AddToTuple((FM_Note)n);
        if (n instanceof FM_Note && addToTie) AddToTie((FM_Note)n);
    }

    public void addChord(List<FM_Note> n, @FM_ClefValue List<Integer> clef) {
        FM_Chord C = new FM_Chord(this);
        for (int i = 0; i < n.size(); i++) {
            if (clef.get(i) == SecondStaveClef) StaffCount = FM_StaffCount._2;
            n.get(i).setClef(clef.get(i));
            n.get(i).setColor(Color);
            n.get(i).setContext(context);
            C.addNote(n.get(i));
        }
        C.Compute(StaveFont);
        StaveNotes.add(C);
    }

    private void ComputeLines() {
        if (StaveNotes.size() == 0) return;
        int l = 1;
        float startX = PaddingStart + getClefWidth() + getTimeSignatureWidth() + SecondStaveKey.WidthAll(StaveFont);
        float endX = width - PaddingEnd - 15;
        float ys1 = PaddingTop;
        float ys2 = PaddingTop;
        if (StaffCount == FM_StaffCount._2)
            ys2 = ys1 + (DistanceBetweenStaves + 4 * DistanceBetweenStaveLines);

        if (Align == FM_Align.ALIGN_LEFT_NOTES) {
            float X = startX;
            FM_BaseNote last_note = null;
            for (int i = 0; i < StaveNotes.size(); i++) {
                float w = StaveNotes.get(i).WidthAll(StaveFont) + NoteSpacing;
                if (X + w > endX) {
                    if (last_note instanceof FM_BarNote) last_note.setVisible(false);
                    l++;
                    X = startX;
                    ys1 = ys2 + (DistanceBetweenLines + 4 * DistanceBetweenStaveLines);
                    ys2 = ys1;
                }
                if (StaffCount == FM_StaffCount._2)
                    ys2 = ys1 + (DistanceBetweenStaves + 4 * DistanceBetweenStaveLines);
                if (StaveNotes.get(i).clef == FirstStaveClef)
                    StaveNotes.get(i).SetDrawParameters(X, ys1, ys2);
                if (StaveNotes.get(i).clef == SecondStaveClef)
                    StaveNotes.get(i).SetDrawParameters(X, ys2, ys2);
                StaveNotes.get(i).line = l;
                X = X + w;
                last_note = StaveNotes.get(i);
            }
            //If last note is a bar, hide it
            if (StaveNotes.get(StaveNotes.size() - 1) instanceof FM_BarNote)
                StaveNotes.get(StaveNotes.size() - 1).setVisible(false);
        }
        if (Align == FM_Align.ALIGN_LEFT_MEASURES || Align == FM_Align.CENTER) {
            float X = startX;
            int last_bar = 0;
            int bar_cnt = 0;
            for (int i = 0; i < StaveNotes.size(); i++) {
                float w = StaveNotes.get(i).WidthAll(StaveFont) + NoteSpacing;
                if (StaveNotes.get(i) instanceof FM_BarNote) {
                    last_bar = i;
                    bar_cnt++;
                }
                else if (X + w > endX) {
                    l++;
                    X = startX;
                    ys1 = ys2 + (DistanceBetweenLines + 4 * DistanceBetweenStaveLines);
                    ys2 = ys1;
                    if (bar_cnt>0) {
                        bar_cnt = 0;
                        i = last_bar;
                        StaveNotes.get(last_bar).setVisible(false);
                        continue;
                    }
                }
                if (StaffCount == FM_StaffCount._2)
                    ys2 = ys1 + (DistanceBetweenStaves + 4 * DistanceBetweenStaveLines);
                if (StaveNotes.get(i).clef == FirstStaveClef)
                    StaveNotes.get(i).SetDrawParameters(X, ys1, ys2);
                if (StaveNotes.get(i).clef == SecondStaveClef)
                    StaveNotes.get(i).SetDrawParameters(X, ys2, ys2);
                StaveNotes.get(i).line = l;
                X = X + w;
            }
            //If last note is a bar, hide it
            if (StaveNotes.get(StaveNotes.size() - 1) instanceof FM_BarNote)
                StaveNotes.get(StaveNotes.size() - 1).setVisible(false);
        }
        Lines = l;
        if (Align == FM_Align.CENTER) {
            for (int i = 1; i <= Lines; i++) {
                float X = startX;
                int cnt = 0;
                float diff = 0;
                for (int j = 0; j < StaveNotes.size(); j++)
                    if (StaveNotes.get(j).line == i) {
                        float w = StaveNotes.get(j).WidthAll(StaveFont);
                        X = X + w;
                        cnt++;
                    }
                diff = (endX - X)/(cnt + 1);
                X = startX;
                for (int j = 0; j < StaveNotes.size(); j++)
                    if (StaveNotes.get(j).line == i) {
                        float w = StaveNotes.get(j).WidthAll(StaveFont);
                        StaveNotes.get(j).SetDrawParameters(X + diff, StaveNotes.get(j).StartY1, StaveNotes.get(j).StartY2);
                        X = X + w + diff;
                    }
            }
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

    public void EndTie(){
        inTie = false;
        if (TieNotes.size() != 2) return;
        if (!TieNotes.get(0).clef.equals(TieNotes.get(1).clef) || TieNotes.get(0).Octave != TieNotes.get(1).Octave || !TieNotes.get(0).Note.equals(TieNotes.get(1).Note)
        || !TieNotes.get(0).toStringAccidental().equals(TieNotes.get(1).toStringAccidental())) return;
        FM_Tie t = new FM_Tie(currentTie);
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
        FM_Tuple t = new FM_Tuple(inTuple_size, currentTuple);
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

    public void EndBeam() {
        inBeam = false;
        if (BeamNotes.size() == 0) return;
        for (int i = 0; i < BeamNotes.size(); i++)
            if (!(BeamNotes.get(i) instanceof FM_Note)) return;
        int clef = BeamNotes.get(0).clef;
        for (int i = 0; i < BeamNotes.size(); i++) {
            int d = ((FM_Note) BeamNotes.get(i)).duration;
            if (d == FM_DurationValue.NOTE_WHOLE) return;
            if (d == FM_DurationValue.NOTE_WHOLE_D) return;
            if (d == FM_DurationValue.NOTE_HALF) return;
            if (d == FM_DurationValue.NOTE_HALF_D) return;
            if (d == FM_DurationValue.NOTE_QUARTER) return;
            if (d == FM_DurationValue.NOTE_QUARTER_D) return;
        }
        FM_Beam t = new FM_Beam(currentBeam);
        currentBeam++;
        for (int i = 0; i < BeamNotes.size(); i++) {
            ((FM_Note)BeamNotes.get(i)).beam = true;
            t.AddNote((FM_Note) BeamNotes.get(i));
        }
        Beams.add(t);
    }

    public void AddToTie(FM_Note n){
        if (inTie) TieNotes.add(n);
    }
    public void AddToTuple(FM_Note n){
        if (inTuple) TupleNotes.add(n);
    }
    public void AddToBeam(FM_Note n){
        if (inBeam) BeamNotes.add(n);
    }

    public void setNoteSpacing(float noteSpacing) {
        NoteSpacing = FM_Const.dpTOpx(context, noteSpacing);
    }
}