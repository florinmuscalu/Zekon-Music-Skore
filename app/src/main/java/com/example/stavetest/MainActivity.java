package com.example.stavetest;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ro.florinm.FM_Score.FM_Accidental;
import ro.florinm.FM_Score.FM_Align;
import ro.florinm.FM_Score.FM_BarNote;
import ro.florinm.FM_Score.FM_BaseNote;
import ro.florinm.FM_Score.FM_ClefValue;
import ro.florinm.FM_Score.FM_DurationValue;
import ro.florinm.FM_Score.FM_KeySignatureValue;
import ro.florinm.FM_Score.FM_Note;
import ro.florinm.FM_Score.FM_NoteValue;
import ro.florinm.FM_Score.FM_Pause;
import ro.florinm.FM_Score.FM_Score;
import ro.florinm.FM_Score.FM_TimeSignature;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FM_Score s = findViewById(R.id.stave);
        s.setBackgroundColor(Color.argb(255, 255, 255, 255));
        s.setColor(Color.argb(255, 0, 0, 0));
        s.setFirstStaveClef(FM_ClefValue.TREBLE);
        s.setSecondStaveClef(FM_ClefValue.BASS);
        s.setTimeSignature(FM_TimeSignature._4_4);
        s.setKeySignature(FM_KeySignatureValue.DOb);
        s.setDistanceBetweenStaveLines(8);
        s.setPaddingS(2);
        s.setPaddingE(2);
        s.setPaddingT(5);
        s.setNoteSpacing(0);
        s.setTimeSignature(FM_TimeSignature._2_4);
        s.setKeySignature(FM_KeySignatureValue.DOsharp);
        s.setAlign(FM_Align.ALIGN_CENTER_NOTES);
        s.setPaddingT(5);
        s.setCenterVertical(true);
        s.setMultiLine(true);
        s.setShowBrace(true);
        s.setAllowZoomPan(true);
        s.setDrawBoundigBox(false);
        s.setTrimLastLine(true);

        String furelise = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open("furelise.json")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                furelise += mLine;
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        JSONObject obj = null;
        try {
            obj = new JSONObject(furelise);
        } catch (Exception ignored) {}

        s.LoadFromJson(obj);
            //addRandom();
        //addTestAll();
        // addSimpleMelodic();
//        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true), FM_ClefValue.TREBLE);
//        List<FM_Note> chord = new ArrayList<>();
//        List<Integer> clefs = new ArrayList<>();
//        chord.add(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true));
//        chord.add(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.TripleFlat, FM_DurationValue.NOTE_WHOLE, true, Color.argb(255, 255, 0, 0)));
//        chord.add(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.TripleFlat, FM_DurationValue.NOTE_WHOLE, false, Color.argb(255, 0, 0, 255)));
//        chord.add(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, false));
//
//        clefs.add(FM_ClefValue.TREBLE);
//        clefs.add(FM_ClefValue.TREBLE);
//        clefs.add(FM_ClefValue.TREBLE);
//        clefs.add(FM_ClefValue.TREBLE);
//        clefs.add(FM_ClefValue.TREBLE);
//        s.addChord(chord, clefs);
//
//        s.addStaffNote(new FM_BarNote(s));
//
//        s.BeginBeam();
//        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH, false), FM_ClefValue.TREBLE);
//        s.AddToBeam((FM_Note) s.getLastNote());
//        s.addStaffNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.TripleFlat, FM_DurationValue.NOTE_EIGHT, true), FM_ClefValue.TREBLE);
//        s.AddToBeam((FM_Note) s.getLastNote());
//        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH, true), FM_ClefValue.TREBLE);
//        s.AddToBeam((FM_Note) s.getLastNote());
//        s.EndBeam();
//
////        s.BeginBeam();
////        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH, true), FM_ClefValue.TREBLE);
////        s.AddToBeam((FM_Note) s.getLastNote());
////        s.addStaffNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.TripleFlat, FM_DurationValue.NOTE_EIGHT, true), FM_ClefValue.TREBLE);
////        s.AddToBeam((FM_Note) s.getLastNote());
////        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH, true), FM_ClefValue.TREBLE);
////        s.AddToBeam((FM_Note) s.getLastNote());
////        s.EndBeam();

    }

    public void addRandom(){
        FM_Score s = findViewById(R.id.stave);
        s.addStaffNote(new FM_Pause(s, FM_DurationValue.NOTE_EIGHT), FM_ClefValue.TREBLE);


        s.BeginTie();
        s.BeginBeam();
        List<FM_BaseNote> chord = new ArrayList<>();
        List<Integer> clefs = new ArrayList<>();
        FM_Note n = new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.Flat, FM_DurationValue.NOTE_EIGHT,  false);
        s.AddToTie(n);
        s.AddToBeam(n);
        chord.add(n);
        chord.add(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.DoubleSharp, FM_DurationValue.NOTE_WHOLE,  true));
        chord.add(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.Sharp, FM_DurationValue.NOTE_WHOLE_D, false));
        clefs.add(FM_ClefValue.TREBLE);
        clefs.add(FM_ClefValue.TREBLE);
        clefs.add(FM_ClefValue.TREBLE);
        s.addChord(chord, clefs);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.Flat, FM_DurationValue.NOTE_EIGHT, true), FM_ClefValue.TREBLE);
        s.AddToTie((FM_Note) s.getLastNote());
        s.AddToBeam((FM_Note) s.getLastNote());
        s.EndBeam();
        s.EndTie();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHT,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        new FM_BarNote(s);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        new FM_BarNote(s);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        s.BeginTie();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        s.AddToTie((FM_Note) s.getLastNote());
        new FM_BarNote(s);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  true), FM_ClefValue.TREBLE);
        s.AddToTie((FM_Note) s.getLastNote());
        s.EndTie();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.Sharp, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.LA, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        new FM_BarNote(s);
        s.BeginTuple(5);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, false), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  false), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  false), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  false), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  false), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.EndTuple();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        new FM_BarNote(s);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.TripleSharp, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);

        s.BeginBeam();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  false), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.TripleFlat, FM_DurationValue.NOTE_EIGHT,  true), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  true), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.EndBeam();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true), FM_ClefValue.TREBLE);
        new FM_BarNote(s);
        s.BeginBeam();
        s.BeginTuple(3);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHT, true), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.AddToTuple((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.Natural, FM_DurationValue.NOTE_EIGHT, true), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.AddToTuple((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_EIGHT,  true), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.AddToTuple((FM_Note) s.getLastNote());
        s.EndTuple();
        s.EndBeam();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        new FM_BarNote(s);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true), FM_ClefValue.TREBLE);
    }

    public void addTestAll() {
        FM_Score s = findViewById(R.id.stave);
        //Pauses
        s.addStaffNote(new FM_Pause(s, FM_DurationValue.NOTE_WHOLE), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Pause(s, FM_DurationValue.NOTE_HALF), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Pause(s, FM_DurationValue.NOTE_QUARTER), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Pause(s, FM_DurationValue.NOTE_EIGHT), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Pause(s, FM_DurationValue.NOTE_SIXTEENTH), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Pause(s, FM_DurationValue.NOTE_THIRTY_SECOND), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Pause(s, FM_DurationValue.NOTE_QUARTER), FM_ClefValue.BASS);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 3, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE_D,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF_D,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER_D,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHT,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHT_D,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH_D,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_THIRTY_SECOND,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 5, FM_Accidental.None, FM_DurationValue.NOTE_THIRTY_SECOND_D,  true), FM_ClefValue.TREBLE);


        s.addStaffNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHT,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHT_D,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH_D,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_THIRTY_SECOND,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 5, FM_Accidental.None, FM_DurationValue.NOTE_THIRTY_SECOND_D,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.TREBLE);

        //Pauses
        s.addStaffNote(new FM_Pause(s, FM_DurationValue.NOTE_WHOLE), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Pause(s, FM_DurationValue.NOTE_HALF), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Pause(s, FM_DurationValue.NOTE_QUARTER), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Pause(s, FM_DurationValue.NOTE_EIGHT), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Pause(s, FM_DurationValue.NOTE_SIXTEENTH), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Pause(s, FM_DurationValue.NOTE_THIRTY_SECOND), FM_ClefValue.BASS);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 2, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE_D,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 3, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 3, FM_Accidental.None, FM_DurationValue.NOTE_HALF_D,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 3, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 3, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER_D,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.LA, 3, FM_Accidental.None, FM_DurationValue.NOTE_EIGHT,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SI, 3, FM_Accidental.None, FM_DurationValue.NOTE_EIGHT_D,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 3, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH_D,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 3, FM_Accidental.None, FM_DurationValue.NOTE_THIRTY_SECOND,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 3, FM_Accidental.None, FM_DurationValue.NOTE_THIRTY_SECOND_D,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.BASS);

        s.BeginTie();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  false), FM_ClefValue.TREBLE);
        s.AddToTie((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_BarNote(s));
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  false), FM_ClefValue.TREBLE);
        s.AddToTie((FM_Note) s.getLastNote());
        s.EndTie();

        s.BeginTie();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_EIGHT,  false), FM_ClefValue.TREBLE);
        s.AddToTie((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  false), FM_ClefValue.TREBLE);
        s.AddToTie((FM_Note) s.getLastNote());
        s.EndTie();

        s.addStaffNote(new FM_BarNote(s));

        s.BeginBeam();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHT,  true), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  true), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  true), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.EndBeam();

        s.BeginTuple(3);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.TREBLE);
        s.AddToTuple((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.TREBLE);
        s.AddToTuple((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.TREBLE);
        s.AddToTuple((FM_Note) s.getLastNote());
        s.EndTuple();

        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.BASS);

        s.BeginBeam();
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHT,  false), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  false), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  false), FM_ClefValue.TREBLE);
        s.AddToBeam((FM_Note) s.getLastNote());
        s.EndBeam();

        s.BeginTuple(3);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  false), FM_ClefValue.TREBLE);
        s.AddToTuple((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  false), FM_ClefValue.TREBLE);
        s.AddToTuple((FM_Note) s.getLastNote());
        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  false), FM_ClefValue.TREBLE);
        s.AddToTuple((FM_Note) s.getLastNote());
        s.EndTuple();

        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.BASS);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.Natural, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.Flat, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.Sharp, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.DoubleSharp, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.DoubleFlat, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.TripleSharp, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.TripleFlat, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);

        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.BASS);

        List<FM_BaseNote> chord = new ArrayList<>();
        List<Integer> clefs = new ArrayList<>();
        chord.add(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.DoubleSharp, FM_DurationValue.NOTE_WHOLE_D,  true));
        chord.add(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.DoubleSharp, FM_DurationValue.NOTE_WHOLE_D,  true));
        chord.add(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.Sharp, FM_DurationValue.NOTE_WHOLE_D,  false));
        clefs.add(FM_ClefValue.TREBLE);
        clefs.add(FM_ClefValue.TREBLE);
        clefs.add(FM_ClefValue.TREBLE);
        s.addChord(chord, clefs);

        chord = new ArrayList<>();
        clefs = new ArrayList<>();
        chord.add(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.DoubleSharp, FM_DurationValue.NOTE_HALF,  true));
        chord.add(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.DoubleSharp, FM_DurationValue.NOTE_HALF,  false));
        chord.add(new FM_Note(s, FM_NoteValue.MI, 3, FM_Accidental.Sharp, FM_DurationValue.NOTE_HALF,  false));
        chord.add(new FM_Note(s, FM_NoteValue.SOL, 3, FM_Accidental.Sharp, FM_DurationValue.NOTE_HALF,  true));
        clefs.add(FM_ClefValue.TREBLE);
        clefs.add(FM_ClefValue.TREBLE);
        clefs.add(FM_ClefValue.BASS);
        clefs.add(FM_ClefValue.BASS);
        s.addChord(chord, clefs);

        chord = new ArrayList<>();
        clefs = new ArrayList<>();
        chord.add(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.Flat, FM_DurationValue.NOTE_HALF,  true));
        chord.add(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  false));
        chord.add(new FM_Note(s, FM_NoteValue.MI, 3, FM_Accidental.Sharp, FM_DurationValue.NOTE_HALF,  false));
        chord.add(new FM_Note(s, FM_NoteValue.SOL, 3, FM_Accidental.Sharp, FM_DurationValue.NOTE_HALF,  true));
        clefs.add(FM_ClefValue.TREBLE);
        clefs.add(FM_ClefValue.TREBLE);
        clefs.add(FM_ClefValue.BASS);
        clefs.add(FM_ClefValue.BASS);
        s.addChord(chord, clefs);

        chord = new ArrayList<>();
        clefs = new ArrayList<>();
        chord.add(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.Flat, FM_DurationValue.NOTE_HALF,  true));
        chord.add(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  false));
        clefs.add(FM_ClefValue.TREBLE);
        clefs.add(FM_ClefValue.TREBLE);
        s.addChord(chord, clefs);

        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.BASS);

        chord = new ArrayList<>();
        clefs = new ArrayList<>();
        chord.add(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.Flat, FM_DurationValue.NOTE_HALF,  true));
        chord.add(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true));

        chord.add(new FM_Note(s, FM_NoteValue.MI, 3, FM_Accidental.Flat, FM_DurationValue.NOTE_HALF,  true));
        chord.add(new FM_Note(s, FM_NoteValue.SOL, 3, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true));
        clefs.add(FM_ClefValue.TREBLE);
        clefs.add(FM_ClefValue.TREBLE);
        clefs.add(FM_ClefValue.BASS);
        clefs.add(FM_ClefValue.BASS);
        s.addChord(chord, clefs);
    }

    public void addSimpleMelodic(){
        FM_Score s = findViewById(R.id.stave);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
        s.addStaffNote(new FM_BarNote(s), FM_ClefValue.TREBLE);

        s.addStaffNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.TREBLE);
    }

    public void OnClick(View v){
        FM_Score s = findViewById(R.id.stave);
        //s.Prepare(44);
        //s.Play(44);
    }
}