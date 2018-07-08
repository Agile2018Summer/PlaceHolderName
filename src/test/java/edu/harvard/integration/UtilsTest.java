package edu.harvard.integration;

import org.junit.Test;

import java.time.Year;
import java.util.Calendar;

import static junit.framework.TestCase.*;

public class UtilsTest {
    @Test
    public void testValidInteger() {
        assertTrue(Utils.legalInt("12"));
        assertTrue(!Utils.legalInt("A"));
    }

    @Test
    public void testDateNoYear() {
        Calendar date = Utils.getDate("7/8");
        assertNotNull(date);
        assertEquals(8, date.get(Calendar.DATE));
        assertEquals(6, date.get(Calendar.MONTH));
        assertEquals(Year.now().getValue(), date.get(Calendar.YEAR));
    }

    @Test
    public void testDateWithYear() {
        Calendar date = Utils.getDate("7/8/2018");
        assertNotNull(date);
        assertEquals(8, date.get(Calendar.DATE));
        assertEquals(6, date.get(Calendar.MONTH));
        assertEquals(2018, date.get(Calendar.YEAR));
    }

    @Test
    public void testDateFormatNoYear() {
        Calendar c = new Calendar.Builder().setDate(2018, 6, 8).build();
        assertEquals("7/8", Utils.formattedDate(c, false));
    }

    @Test
    public void testDateFormatWithYear() {
        Calendar c = new Calendar.Builder().setDate(2018, 6, 8).build();
        assertEquals("7/8/2018", Utils.formattedDate(c, true));
    }
}