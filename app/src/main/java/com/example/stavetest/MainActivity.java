package com.example.stavetest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import ro.florinm.FM_Score.FM_Accidental;
import ro.florinm.FM_Score.FM_BarNote;
import ro.florinm.FM_Score.FM_BaseNote;
import ro.florinm.FM_Score.FM_ClefValue;
import ro.florinm.FM_Score.FM_DurationValue;
import ro.florinm.FM_Score.FM_KeySignatureValue;
import ro.florinm.FM_Score.FM_Note;
import ro.florinm.FM_Score.FM_NoteValue;
import ro.florinm.FM_Score.FM_Pause;
import ro.florinm.FM_Score.FM_Score;
import ro.florinm.FM_Score.FM_StaffCount;
import ro.florinm.FM_Score.FM_TimeSignature;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FM_Score s = findViewById(R.id.stave);
        s.setColor(Color.argb(255, 0, 0, 0));
        s.setFirstStaveClef(FM_ClefValue.TREBLE);
        s.setSecondStaveClef(FM_ClefValue.BASS);
        s.setTimeSignature(FM_TimeSignature._4_4);
        s.setKeySignature(FM_KeySignatureValue.DOb);
        s.setDistanceBetweenStaveLines(10);
        s.setNoteSpacing(0);
        addSimpleMelodic();
        addRandom();
    }

    public void addRandom(){
        FM_Score s = findViewById(R.id.stave);
        s.addStaffNote(new FM_Pause(s, FM_DurationValue.NOTE_EIGHT), FM_ClefValue.TREBLE);


        s.BeginTie();
        List<FM_Note> chord = new ArrayList<>();
        List<Integer> clefs = new ArrayList<>();
        FM_Note n = new FM_Note(s, FM_NoteValue.SI, 3, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true);
        s.AddToTie(n);
        chord.add(n);
        chord.add(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.DoubleSharp, FM_DurationValue.NOTE_WHOLE_D, true, true));
        chord.add(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.Sharp, FM_DurationValue.NOTE_WHOLE_D, true, false));
        clefs.add(FM_ClefValue.TREBLE);
        clefs.add(FM_ClefValue.TREBLE);
        clefs.add(FM_ClefValue.TREBLE);
        s.addChord(chord, clefs);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 3, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH, true, true), FM_ClefValue.TREBLE, false, false, true);
        s.EndTie();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHT, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        new FM_BarNote(s, FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        new FM_BarNote(s, FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        s.BeginTie();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE, false, false, true);
        new FM_BarNote(s, FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH, true, true), FM_ClefValue.TREBLE, false, false, true);
        s.EndTie();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.Sharp, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.LA, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        new FM_BarNote(s, FM_ClefValue.TREBLE);
        s.BeginTuple(5);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, false), FM_ClefValue.TREBLE, false, true, false);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, false), FM_ClefValue.TREBLE, false, true, false);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, false), FM_ClefValue.TREBLE, false, true, false);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, false), FM_ClefValue.TREBLE, false, true, false);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, false), FM_ClefValue.TREBLE, false, true, false);
        s.EndTuple();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        new FM_BarNote(s, FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.TripleSharp, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);

        s.BeginBeam();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH, true, false), FM_ClefValue.TREBLE, true);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.TripleFlat, FM_DurationValue.NOTE_EIGHT, true, true), FM_ClefValue.TREBLE, true);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH, true, true), FM_ClefValue.TREBLE, true);
        s.EndBeam();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        new FM_BarNote(s, FM_ClefValue.TREBLE);
        s.BeginBeam();
        s.BeginTuple(3);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHT, true, true), FM_ClefValue.TREBLE, true, true);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.Natural, FM_DurationValue.NOTE_EIGHT, true, true), FM_ClefValue.TREBLE, true, true);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_EIGHT, true, true), FM_ClefValue.TREBLE, true, true);
        s.EndTuple();
        s.EndBeam();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        new FM_BarNote(s, FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true, true), FM_ClefValue.TREBLE);
    }

    public void addSimpleMelodic(){
        FM_Score s = findViewById(R.id.stave);
        s.setTimeSignature(FM_TimeSignature._2_4);
        s.setKeySignature(FM_KeySignatureValue.DO);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s, FM_ClefValue.TREBLE), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s, FM_ClefValue.TREBLE), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s, FM_ClefValue.TREBLE), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s, FM_ClefValue.TREBLE), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s, FM_ClefValue.TREBLE), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s, FM_ClefValue.TREBLE), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s, FM_ClefValue.TREBLE), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s, FM_ClefValue.TREBLE), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s, FM_ClefValue.TREBLE), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s, FM_ClefValue.TREBLE), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s, FM_ClefValue.TREBLE), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s, FM_ClefValue.TREBLE), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF, true, true), FM_ClefValue.TREBLE);
    }
}