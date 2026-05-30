package tech.zekon.FM_Score;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 * Pure-logic unit tests for the notation helpers in {@link FM_Const}.
 *
 * <p>These run on the local JVM (no Android runtime required) because the methods
 * under test only do String parsing. They lock in the parsing behaviour so future
 * refactors can't silently change how note names are interpreted.
 */
public class FM_ConstTest {

    @Test
    public void keyToOctave_parsesTrailingDigit() {
        assertEquals(4, FM_Const.keyToOctave("c4"));
        assertEquals(5, FM_Const.keyToOctave("sol5"));
        assertEquals(0, FM_Const.keyToOctave("r")); // a rest has no octave
    }

    @Test
    public void keyToOctave_isCaseInsensitive() {
        assertEquals(4, FM_Const.keyToOctave("C4"));
        assertEquals(FM_Const.keyToOctave("do3"), FM_Const.keyToOctave("DO3"));
    }

    @Test
    public void keyToNote_treatsSolfegeAndLetterNamesAsEqual() {
        assertEquals(FM_Const.keyToNote("do4"), FM_Const.keyToNote("c4"));
        assertEquals(FM_Const.keyToNote("la3"), FM_Const.keyToNote("a3"));
        assertEquals(FM_Const.keyToNote("sol2"), FM_Const.keyToNote("g2"));
    }

    @Test
    public void keyToNote_distinguishesDifferentNotes() {
        assertNotEquals(FM_Const.keyToNote("c4"), FM_Const.keyToNote("d4"));
        assertNotEquals(FM_Const.keyToNote("e4"), FM_Const.keyToNote("f4"));
    }
}
