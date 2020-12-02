package ro.florinm.FM_ScorePlayer;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

class FM_Helper {
    static String do_a = "";
    static String re_a = "";
    static String mi_a = "";
    static String fa_a = "";
    static String sol_a = "";
    static String la_a = "";
    static String si_a = "";

    static void StartMeasure() {
        do_a = "DO";
        re_a = "RE";
        mi_a = "MI";
        fa_a = "FA";
        sol_a = "SOL";
        la_a = "LA";
        si_a = "SI";
    }

    static String ApplyTonality(String tonality, String key){
        //send it only the note, without the octave!!
        if (key.contains("n") || key.contains("#") || key.contains("##") || key.contains("b") || key.contains("bb") || key.equals("B")) return key;
        String k = key.toUpperCase();
        if (tonality.equals("SOL") || tonality.equals("MIm")){
            if (k.equals("FA")) k = k+"#";
        }
        if (tonality.equals("RE") || tonality.equals("SIm")){
            if (k.equals("FA") || k.equals("DO")) k = k + "#";
        }
        if (tonality.equals("LA") || tonality.equals("FA#m")){
            if (k.equals("FA") || k.equals("DO")|| k.equals("SOL")) k = k + "#";
        }
        if (tonality.equals("MI") || tonality.equals("DO#m")){
            if (k.equals("FA") || k.equals("DO") || k.equals("SOL") || k.equals("RE")) k = k + "#";
        }
        if (tonality.equals("SI") || tonality.equals("SOL#m")){
            if (k.equals("FA") || k.equals("DO") || k.equals("SOL") || k.equals("RE") || k.equals("LA")) k = k + "#";
        }
        if (tonality.equals("FA") || tonality.equals("REm")){
            if (k.equals("SI")) k = k+ "b";
        }
        if (tonality.equals("SIb") || tonality.equals("SOLm")){
            if (k.equals("SI") || k.equals("MI")) k = k + "b";
        }
        if (tonality.equals("MIb") || tonality.equals("DOm")){
            if (k.equals("SI") || k.equals("MI")|| k.equals("LA")) k = k + "b";
        }
        if (tonality.equals("LAb") || tonality.equals("FAm")){
            if (k.equals("SI") || k.equals("MI") || k.equals("LA") || k.equals("RE")) k = k + "b";
        }
        if (tonality.equals("REb") || tonality.equals("SIbm")){
            if (k.equals("SI") || k.equals("MI") || k.equals("LA") || k.equals("RE") || k.equals("SOL")) k = k + "b";
        }
        return k;
    }

    static String[] computeNote(String tonality, String[] n) {
        String[] ret = new String[n.length];
        for (int i = 0; i < n.length; i++) {
            String o = n[i].substring(n[i].length() - 2);
            String note = n[i].substring(0, n[i].length() - 2);
            String initial_note = note;
            String tmp = ApplyTonality(tonality, note);
            if (note.startsWith("DO")) {
                if (!note.equals("DO")) do_a = note;
                else note = do_a;
            }
            if (note.startsWith("RE")) {
                if (!note.equals("RE")) re_a = note;
                else note = re_a;
            }
            if (note.startsWith("MI")) {
                if (!note.equals("MI")) mi_a = note;
                else note = mi_a;
            }
            if (note.startsWith("FA")) {
                if (!note.equals("FA")) fa_a = note;
                else note = fa_a;
            }
            if (note.startsWith("SOL")) {
                if (!note.equals("SOL")) sol_a = note;
                else note = sol_a;
            }
            if (note.startsWith("LA")) {
                if (!note.equals("LA")) la_a = note;
                else note = la_a;
            }
            if (note.startsWith("SI")) {
                if (!note.equals("SI")) si_a = note;
                else note = si_a;
            }
            if (initial_note.equals(note)) note = tmp;
            ret[i] = note + o;
        }
        return ret;
    }

    static FM_Audio_Song generateHarmonicSong(String keysignature, JSONArray keys) {
        FM_Audio_Song song = new FM_Audio_Song();
        song.keysignature = keysignature;
        JSONArray a = new JSONArray();
        try {
            boolean in_legato = false;
            while (keys.length() > 0) {
                JSONArray b = (JSONArray) keys.get(0);
                if (b.getString(0).startsWith("BAR")) {
                    JSONArray value = new JSONArray();
                    value.put("BAR");
                    a.put(value);
                    keys.remove(0);
                    continue;
                }
                boolean still_to_go = true;
                int current_group = 0;
                while (still_to_go) {
                    int i = 0;
                    current_group = current_group + 1;
                    JSONArray value = new JSONArray();
                    while (true) {
                        if (keys.length() == 0) {
                            still_to_go = false;
                            break;
                        }
                        if (i > keys.length() - 1) {
                            break;
                        }
                        JSONArray tmp = (JSONArray) keys.get(i);
                        if (tmp.getString(0).startsWith("BAR")) {
                            if (i == 0) still_to_go = false;
                            break;
                        }
//                        int pg = tmp.getInt(8);
//                        if (current_group == pg) {
//                            value.put(tmp.getString(0));
//                            value.put(tmp.getString(1));
//                            keys.remove(i);
//                            i = i - 1;
//                        }
//                        i = i + 1;

                        int pg = tmp.getInt(8);
                        if (current_group == pg) {
                            value.put(tmp.getString(0));
                            String triolet = "";
                            if (!tmp.getString(5).equals("")) triolet = "t";
                            value.put(tmp.getString(1) + triolet);
                            if (!tmp.getString(3).equals("")) {
                                if (!in_legato) value.put("legato_start");
                                else value.put("legato_end");
                                in_legato = !in_legato;
                            } else value.put("");
                            keys.remove(i);
                            i = i - 1;
                        }
                        i = i + 1;
                    }
                    a.put(value);
                }
            }

            FM_Audio_Measure m = new FM_Audio_Measure();
            song.measures.add(m);
            for (int i = 0; i < a.length(); i++) {
                JSONArray b = (JSONArray) a.get(i);
                if (b.length() == 1 && b.getString(0).startsWith("BAR")) {
                    m = new FM_Audio_Measure();
                    song.measures.add(m);
                } else {
                    FM_Audio_Note n = new FM_Audio_Note();
                    String note = "";
                    String duration = "";
                    for (int j = 0; j < b.length(); j = j + 3) {
                        note = note + "," + b.getString(j);
                        duration = duration + "," + b.getString(j + 1);
                    }
                    n.note = note.substring(1);
                    n.duration = duration.substring(1);
                    n.playDuration = duration.substring(1);
                    n.pauseDuration = duration.substring(1);
                    m.notes.add(n);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return song;
    }
}