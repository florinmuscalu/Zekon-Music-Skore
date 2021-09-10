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

import static android.os.SystemClock.sleep;

import tech.zekon.FM_Score.FM_Align;
import tech.zekon.FM_Score.FM_ClefValue;
import tech.zekon.FM_Score.FM_KeySignatureValue;
import tech.zekon.FM_Score.FM_Score;
import tech.zekon.FM_Score.FM_ScoreBase;
import tech.zekon.FM_Score.FM_ScorePlayer;
import tech.zekon.FM_Score.FM_TimeSignatureValue;

public class MainActivity extends AppCompatActivity {
    FM_ScorePlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FM_Score s = findViewById(R.id.stave);
        s.setBackgroundColor(Color.argb(255, 224, 211, 175));
        s.setColor(Color.argb(255, 26, 28, 33));
        s.setDistanceBetweenStaveLines(7f);
        s.setDistanceBetweenStaves(10);
        s.setDistanceBetweenRows(6);
        s.setPaddingE(1);
        s.setPaddingS(4);
        s.setFirstStaveClef(FM_ClefValue.TREBLE);
        s.setSecondStaveClef(FM_ClefValue.BASS);
        s.setTimeSignature(FM_TimeSignatureValue._4, FM_TimeSignatureValue._4);
        s.setKeySignature(FM_KeySignatureValue.DOb);
        s.setCenterVertical(false);
        s.setStartBar(true);
        s.setEndBar(true);
        s.setMultiRow(true);
        s.setAllowZoomPan(true);
        s.setShowBrace(true);
        s.setTrimLastRow(true);
        s.setNotesAlign(FM_Align.ALIGN_LEFT_LAST_MEASURE);
        s.clearStaveNotes();
        s.setVisibility(View.VISIBLE);
        s.setNoteSpacing(10);

//        List<FM_BaseNote> chord = new ArrayList<>();
//        List<Integer> clefs = new ArrayList<>();
//
        //FM_Note n = new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_HALF, 0, true);
//        chord.add(n);
//        n= new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHTH, 1, false);
//        chord.add(n);
//
//        s.AddToTie("1", n);
//
//        clefs.add(FM_ClefValue.TREBLE);
//        clefs.add(FM_ClefValue.TREBLE);
//        s.addChord(chord, clefs);
//
//        s.addStaveNote(new FM_BarNote(s));
//
//        n= new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER_D, 1, false);
//        s.AddToTie("1", n);
//        s.addStaveNote(n);

//        player = FM_ScorePlayer.getInstance(getApplicationContext());
//        player.LoadFromScore(s, 70);
//        player.setShowProgress(true);

        player = FM_ScorePlayer.getInstance(getApplicationContext());
        LoadJson();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (player.AssetsLoaded() != 100) {
//                    sleep (100);
//                }
//                FM_ScoreBase b = new FM_ScoreBase(null);
//
//                StringBuilder jingle = new StringBuilder();
//                BufferedReader reader = null;
//                index = index + 1;
//                if (index > 1) index = 0;
//                try {
//                    reader = new BufferedReader(new InputStreamReader(getAssets().open("test.json")));
//                    String mLine;
//                    while ((mLine = reader.readLine()) != null) {
//                        jingle.append(mLine);
//                    }
//                } catch (IOException e) {
//                    //log the exception
//                } finally {
//                    if (reader != null) {
//                        try {
//                            reader.close();
//                        } catch (IOException e) {
//                            //log the exception
//                        }
//                    }
//                }
//                JSONObject obj = null;
//                try {
//                    obj = new JSONObject(jingle.toString());
//                } catch (Exception ignored) {}
//
//                b.LoadFromJson(obj);
//                player.LoadFromScore(b, 80);
//                player.Play();
//
//            }
//        }).start();
//
    }

    public void Testing(){
//        FM_Score s = findViewById(R.id.stave);
//
//        List<FM_BaseNote> chord = new ArrayList<>();
//        List<Integer> clefs = new ArrayList<>();
//
//        s.BeginTie();
//
//        chord.add(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true));
//        s.AddToTie((FM_Note)chord.get(0));
//        chord.add(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, Color.argb(255, 255, 0, 0)));
//        chord.add(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, false, Color.argb(255, 0, 0, 255)));
//
//        clefs.add(FM_ClefValue.TREBLE);
//        clefs.add(FM_ClefValue.TREBLE);
//        clefs.add(FM_ClefValue.TREBLE);
//        s.addChord(chord, clefs);
//
//        s.addStaveNote(new FM_BarNote(s));
//
//        chord = new ArrayList<>();
//        clefs = new ArrayList<>();
//        chord.add(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true, Color.argb(255, 255, 0, 0)));
//        s.AddToTie((FM_Note)chord.get(0));
//        chord.add(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF, true));
//        //chord.add(new FM_Note(s, FM_NoteValue.SI, 3, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, false));
//
//        clefs.add(FM_ClefValue.TREBLE);
//        clefs.add(FM_ClefValue.TREBLE);
//        clefs.add(FM_ClefValue.TREBLE);
//        s.addChord(chord, clefs);
//        s.EndTie();
    }

    String[] files = {"test.json", "furelise.json"};
    int index = 1;

    public void LoadJson(){
        FM_Score s = findViewById(R.id.stave);
        StringBuilder furelise = new StringBuilder();
        BufferedReader reader = null;
        index = index + 1;
        if (index > 1) index = 0;
        try {
            reader = new BufferedReader(new InputStreamReader(getAssets().open(files[index])));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                furelise.append(mLine);
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
            obj = new JSONObject(furelise.toString());
        } catch (Exception ignored) {}

        s.LoadFromJson(obj);
        player.LoadFromScore(s, 80);
        player.setShowProgress(true);
    }

    public void addRandom(){
//        FM_Score s = findViewById(R.id.stave);
//        s.addStaveNote(new FM_Pause(s, FM_DurationValue.NOTE_EIGHTH, 0), FM_ClefValue.TREBLE);
//
//
//        s.BeginTie();
//        s.BeginBeam();
//        List<FM_BaseNote> chord = new ArrayList<>();
//        List<Integer> clefs = new ArrayList<>();
//        FM_Note n = new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.Flat, FM_DurationValue.NOTE_EIGHTH,  false);
//        s.AddToTie(n);
//        s.AddToBeam(n);
//        chord.add(n);
//        chord.add(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.DoubleSharp, FM_DurationValue.NOTE_WHOLE,  true));
//        chord.add(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.Sharp, FM_DurationValue.NOTE_WHOLE_D, false));
//        clefs.add(FM_ClefValue.TREBLE);
//        clefs.add(FM_ClefValue.TREBLE);
//        clefs.add(FM_ClefValue.TREBLE);
//        s.addChord(chord, clefs);
//
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.Flat, FM_DurationValue.NOTE_EIGHTH, true), FM_ClefValue.TREBLE);
//        s.AddToTie((FM_Note) s.getLastNote());
//        s.AddToBeam((FM_Note) s.getLastNote());
//        s.EndBeam();
//        s.EndTie();
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHTH,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        new FM_BarNote(s);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        new FM_BarNote(s);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        s.BeginTie();
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        s.AddToTie((FM_Note) s.getLastNote());
//        new FM_BarNote(s);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  true), FM_ClefValue.TREBLE);
//        s.AddToTie((FM_Note) s.getLastNote());
//        s.EndTie();
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.Sharp, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.LA, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SI, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        new FM_BarNote(s);
//        s.BeginTuplet("a");
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, false), FM_ClefValue.TREBLE);
//        s.AddToBeam((FM_Note) s.getLastNote());
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  false), FM_ClefValue.TREBLE);
//        s.AddToBeam((FM_Note) s.getLastNote());
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  false), FM_ClefValue.TREBLE);
//        s.AddToBeam((FM_Note) s.getLastNote());
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  false), FM_ClefValue.TREBLE);
//        s.AddToBeam((FM_Note) s.getLastNote());
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  false), FM_ClefValue.TREBLE);
//        s.AddToBeam((FM_Note) s.getLastNote());
//        s.EndTuplet();
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        new FM_BarNote(s);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.TripleSharp, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//
//        s.BeginBeam();
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  false), FM_ClefValue.TREBLE);
//        s.AddToBeam((FM_Note) s.getLastNote());
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.TripleFlat, FM_DurationValue.NOTE_EIGHTH,  true), FM_ClefValue.TREBLE);
//        s.AddToBeam((FM_Note) s.getLastNote());
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  true), FM_ClefValue.TREBLE);
//        s.AddToBeam((FM_Note) s.getLastNote());
//        s.EndBeam();
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true), FM_ClefValue.TREBLE);
//        new FM_BarNote(s);
//        s.BeginBeam();
//        s.BeginTuplet("a");
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHTH, true), FM_ClefValue.TREBLE);
//        s.AddToBeam((FM_Note) s.getLastNote());
//        s.AddToTuplet((FM_Note) s.getLastNote());
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.Natural, FM_DurationValue.NOTE_EIGHTH, true), FM_ClefValue.TREBLE);
//        s.AddToBeam((FM_Note) s.getLastNote());
//        s.AddToTuplet((FM_Note) s.getLastNote());
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_EIGHTH,  true), FM_ClefValue.TREBLE);
//        s.AddToBeam((FM_Note) s.getLastNote());
//        s.AddToTuplet((FM_Note) s.getLastNote());
//        s.EndTuplet();
//        s.EndBeam();
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        new FM_BarNote(s);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE, true), FM_ClefValue.TREBLE);
    }

//    public void addTestAll() {
//        FM_Score s = findViewById(R.id.stave);
//        //Pauses
//        s.addStaveNote(new FM_Pause(s, FM_DurationValue.NOTE_WHOLE, 0), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Pause(s, FM_DurationValue.NOTE_HALF, 0), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Pause(s, FM_DurationValue.NOTE_QUARTER, 0), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Pause(s, FM_DurationValue.NOTE_EIGHTH, 0), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Pause(s, FM_DurationValue.NOTE_SIXTEENTH, 0), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Pause(s, FM_DurationValue.NOTE_THIRTY_SECOND, 0), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Pause(s, FM_DurationValue.NOTE_QUARTER, 0), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SI, 3, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE_D,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF_D,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER_D,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHTH,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHTH_D,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH_D,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_THIRTY_SECOND,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.FA, 5, FM_Accidental.None, FM_DurationValue.NOTE_THIRTY_SECOND_D,  true), FM_ClefValue.TREBLE);
//
//
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.LA, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHTH,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SI, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHTH_D,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 5, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH_D,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 5, FM_Accidental.None, FM_DurationValue.NOTE_THIRTY_SECOND,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.FA, 5, FM_Accidental.None, FM_DurationValue.NOTE_THIRTY_SECOND_D,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
//
//        //Pauses
//        s.addStaveNote(new FM_Pause(s, FM_DurationValue.NOTE_WHOLE, 0), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Pause(s, FM_DurationValue.NOTE_HALF, 0), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Pause(s, FM_DurationValue.NOTE_QUARTER, 0), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Pause(s, FM_DurationValue.NOTE_EIGHTH, 0), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Pause(s, FM_DurationValue.NOTE_SIXTEENTH, 0), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Pause(s, FM_DurationValue.NOTE_THIRTY_SECOND, 0), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SI, 2, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  true), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE_D,  true), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 3, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 3, FM_Accidental.None, FM_DurationValue.NOTE_HALF_D,  true), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.FA, 3, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SOL, 3, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER_D,  true), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.LA, 3, FM_Accidental.None, FM_DurationValue.NOTE_EIGHTH,  true), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SI, 3, FM_Accidental.None, FM_DurationValue.NOTE_EIGHTH_D,  true), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  true), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 3, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH_D,  true), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 3, FM_Accidental.None, FM_DurationValue.NOTE_THIRTY_SECOND,  true), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.FA, 3, FM_Accidental.None, FM_DurationValue.NOTE_THIRTY_SECOND_D,  true), FM_ClefValue.BASS);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.BASS);
//
////        s.BeginTie();
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_WHOLE,  false), FM_ClefValue.TREBLE);
////        s.AddToTie((FM_Note) s.getLastNote());
////        s.addStaveNote(new FM_BarNote(s));
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  false), FM_ClefValue.TREBLE);
////        s.AddToTie((FM_Note) s.getLastNote());
////        s.EndTie();
////
////        s.BeginTie();
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_EIGHTH,  false), FM_ClefValue.TREBLE);
////        s.AddToTie((FM_Note) s.getLastNote());
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 5, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  false), FM_ClefValue.TREBLE);
////        s.AddToTie((FM_Note) s.getLastNote());
////        s.EndTie();
////
////        s.addStaveNote(new FM_BarNote(s));
////
////        s.BeginBeam();
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHTH,  true), FM_ClefValue.TREBLE);
////        s.AddToBeam((FM_Note) s.getLastNote());
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  true), FM_ClefValue.TREBLE);
////        s.AddToBeam((FM_Note) s.getLastNote());
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  true), FM_ClefValue.TREBLE);
////        s.AddToBeam((FM_Note) s.getLastNote());
////        s.EndBeam();
////
////        s.BeginTuplet("b");
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.TREBLE);
////        s.AddToTuplet((FM_Note) s.getLastNote());
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.TREBLE);
////        s.AddToTuplet((FM_Note) s.getLastNote());
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.TREBLE);
////        s.AddToTuplet((FM_Note) s.getLastNote());
////        s.EndTuplet();
////
////        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.BASS);
////
////        s.BeginBeam();
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_EIGHTH,  false), FM_ClefValue.TREBLE);
////        s.AddToBeam((FM_Note) s.getLastNote());
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  false), FM_ClefValue.TREBLE);
////        s.AddToBeam((FM_Note) s.getLastNote());
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_SIXTEENTH,  false), FM_ClefValue.TREBLE);
////        s.AddToBeam((FM_Note) s.getLastNote());
////        s.EndBeam();
////
////        s.BeginTuplet("b");
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  false), FM_ClefValue.TREBLE);
////        s.AddToTuplet((FM_Note) s.getLastNote());
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  false), FM_ClefValue.TREBLE);
////        s.AddToTuplet((FM_Note) s.getLastNote());
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  false), FM_ClefValue.TREBLE);
////        s.AddToTuplet((FM_Note) s.getLastNote());
////        s.EndTuplet();
////
////        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.BASS);
////
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.Natural, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.Flat, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.Sharp, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.DoubleSharp, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);
////        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.BASS);
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.DoubleFlat, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.TripleSharp, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);
////        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 3, FM_Accidental.TripleFlat, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.BASS);
////
////        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.BASS);
////
////        List<FM_BaseNote> chord = new ArrayList<>();
////        List<Integer> clefs = new ArrayList<>();
////        chord.add(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.DoubleSharp, FM_DurationValue.NOTE_WHOLE_D,  true));
////        chord.add(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.DoubleSharp, FM_DurationValue.NOTE_WHOLE_D,  true));
////        chord.add(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.Sharp, FM_DurationValue.NOTE_WHOLE_D,  false));
////        clefs.add(FM_ClefValue.TREBLE);
////        clefs.add(FM_ClefValue.TREBLE);
////        clefs.add(FM_ClefValue.TREBLE);
////        s.addChord(chord, clefs);
////
////        chord = new ArrayList<>();
////        clefs = new ArrayList<>();
////        chord.add(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.DoubleSharp, FM_DurationValue.NOTE_HALF,  true));
////        chord.add(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.DoubleSharp, FM_DurationValue.NOTE_HALF,  false));
////        chord.add(new FM_Note(s, FM_NoteValue.MI, 3, FM_Accidental.Sharp, FM_DurationValue.NOTE_HALF,  false));
////        chord.add(new FM_Note(s, FM_NoteValue.SOL, 3, FM_Accidental.Sharp, FM_DurationValue.NOTE_HALF,  true));
////        clefs.add(FM_ClefValue.TREBLE);
////        clefs.add(FM_ClefValue.TREBLE);
////        clefs.add(FM_ClefValue.BASS);
////        clefs.add(FM_ClefValue.BASS);
////        s.addChord(chord, clefs);
////
////        chord = new ArrayList<>();
////        clefs = new ArrayList<>();
////        chord.add(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.Flat, FM_DurationValue.NOTE_HALF,  true));
////        chord.add(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  false));
////        chord.add(new FM_Note(s, FM_NoteValue.MI, 3, FM_Accidental.Sharp, FM_DurationValue.NOTE_HALF,  false));
////        chord.add(new FM_Note(s, FM_NoteValue.SOL, 3, FM_Accidental.Sharp, FM_DurationValue.NOTE_HALF,  true));
////        clefs.add(FM_ClefValue.TREBLE);
////        clefs.add(FM_ClefValue.TREBLE);
////        clefs.add(FM_ClefValue.BASS);
////        clefs.add(FM_ClefValue.BASS);
////        s.addChord(chord, clefs);
////
////        chord = new ArrayList<>();
////        clefs = new ArrayList<>();
////        chord.add(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.Flat, FM_DurationValue.NOTE_HALF,  true));
////        chord.add(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  false));
////        clefs.add(FM_ClefValue.TREBLE);
////        clefs.add(FM_ClefValue.TREBLE);
////        s.addChord(chord, clefs);
////
////        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.BASS);
////
////        chord = new ArrayList<>();
////        clefs = new ArrayList<>();
////        chord.add(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.Flat, FM_DurationValue.NOTE_HALF,  true));
////        chord.add(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true));
////
////        chord.add(new FM_Note(s, FM_NoteValue.MI, 3, FM_Accidental.Flat, FM_DurationValue.NOTE_HALF,  true));
////        chord.add(new FM_Note(s, FM_NoteValue.SOL, 3, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true));
////        clefs.add(FM_ClefValue.TREBLE);
////        clefs.add(FM_ClefValue.TREBLE);
////        clefs.add(FM_ClefValue.BASS);
////        clefs.add(FM_ClefValue.BASS);
////        s.addChord(chord, clefs);
//    }
//
//    public void addSimpleMelodic(){
//        FM_Score s = findViewById(R.id.stave);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
//
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.FA, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
//
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
//
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
//
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
//
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER, true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
//
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
//
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.SOL, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
//
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
//
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.RE, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.MI, 4, FM_Accidental.None, FM_DurationValue.NOTE_QUARTER,  true), FM_ClefValue.TREBLE);
//        s.addStaveNote(new FM_BarNote(s), FM_ClefValue.TREBLE);
//
//        s.addStaveNote(new FM_Note(s, FM_NoteValue.DO, 4, FM_Accidental.None, FM_DurationValue.NOTE_HALF,  true), FM_ClefValue.TREBLE);
//    }

    public void OnClick(View v){
        //LoadJson();
        player.Play();
        //player.Play(2,2);
    }

    public void Change(View v){
        LoadJson();
    }

    public void OnClickBBox(View v){
        FM_Score s = findViewById(R.id.stave);
        int i = s.getShowBoundingBoxes();
        i = i + 1;
        if (i > 2) i = 0;
        s.ShowBoundingBoxes(i);
        s.setAllowZoomControls(! s.isAllowZoomControls());
    }
    boolean show = false;
    public void OnClickVisible(View v){
        FM_Score s = findViewById(R.id.stave);
        if (show) {
            s.setVisibility(View.INVISIBLE);
            s.ShowScore(0, false);
        }
        else {
            s.setVisibility(View.VISIBLE);
            s.ShowScore(1, true);
        }
        show = !show;
    }
}