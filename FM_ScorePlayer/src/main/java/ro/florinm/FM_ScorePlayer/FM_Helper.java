package ro.florinm.FM_ScorePlayer;

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
}
