package com.example.daniel.calc;

import org.junit.Test;

import static com.example.daniel.calc.basicCalc.countOccurances;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2+2);
    }
    @Test
    public void checkCountOccurencesMethod()
    {
        basicCalc bc = new basicCalc();
        assertEquals(1,countOccurances("ABB","A"));
        assertEquals(2,countOccurances("ABBA","A"));
        assertEquals(2,countOccurances("ABBA","B"));
    }
}