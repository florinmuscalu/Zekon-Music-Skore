package tech.zekon.FM_Score;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

/**
 * Locks down {@link FM_ScorePlayer#keyToMidi}, the map from the keyboard's key index (1..88, in the
 * app's white-then-black draw order — NOT chromatic) to MIDI note numbers.
 *
 * <p>Pure arithmetic, so it runs on the local JVM. It guards against the regression where the synth
 * and MIDI export assumed a chromatic order ({@code key + 20}) and swapped neighbouring notes.
 */
public class FM_ScorePlayerTest {

    @Test
    public void namedKeysMapToCorrectPitch() {
        assertEquals(21, FM_ScorePlayer.keyToMidi(1));    // A0
        assertEquals(23, FM_ScorePlayer.keyToMidi(2));    // B0
        assertEquals(22, FM_ScorePlayer.keyToMidi(3));    // A#0
        assertEquals(24, FM_ScorePlayer.keyToMidi(4));    // C1
        assertEquals(25, FM_ScorePlayer.keyToMidi(6));    // C#1
        assertEquals(60, FM_ScorePlayer.keyToMidi(40));   // middle C (C4) — the highlighted yellow key
        assertEquals(64, FM_ScorePlayer.keyToMidi(43));   // E4
        assertEquals(63, FM_ScorePlayer.keyToMidi(44));   // D#4
        assertEquals(108, FM_ScorePlayer.keyToMidi(88));  // C8
    }

    @Test
    public void e4IsOneSemitoneAboveDSharp4() {
        // The exact symptom of the old key+20 bug: MI4 (E4) came out below RE4# (D#4).
        assertTrue(FM_ScorePlayer.keyToMidi(43) > FM_ScorePlayer.keyToMidi(44));
        assertEquals(1, FM_ScorePlayer.keyToMidi(43) - FM_ScorePlayer.keyToMidi(44));
    }

    @Test
    public void allKeysMapBijectivelyOntoMidi21To108() {
        Set<Integer> notes = new HashSet<>();
        for (int key = 1; key <= 88; key++) {
            int midi = FM_ScorePlayer.keyToMidi(key);
            assertTrue("key " + key + " -> " + midi + " is outside 21..108", midi >= 21 && midi <= 108);
            assertTrue("duplicate MIDI note " + midi + " at key " + key, notes.add(midi));
        }
        // 88 distinct values within 21..108 (an 88-wide range) => exactly the full A0..C8 set.
        assertEquals(88, notes.size());
    }
}
