package couch25k.utils;

import java.util.Calendar;

import jmunit.framework.cldc11.TestCase;
import couch25k.utils.IntervalParser;
import couch25k.utils.NumberUtils;
import couch25k.utils.StringUtils;

public class UtilsTests extends TestCase {
    public UtilsTests() {
        super(7, "UtilTests");
    }

    // NumberUtils tests -------------------------------------------------------

    public void secToDurationTest() {
        assertEquals("Before seconds boundary", "89 seconds", NumberUtils.secToDuration(89));
        assertEquals("Seconds boundary", "90 seconds", NumberUtils.secToDuration(90));
        assertEquals("Whole minutes", "2 minutes", NumberUtils.secToDuration(120));
        assertEquals("Fractional minutes", "2.5 minutes", NumberUtils.secToDuration(150));
    }

    public void secToTimeTest() {
        assertEquals("Zero", "00:00", NumberUtils.secToTime(0));
        assertEquals("00:05", NumberUtils.secToTime(5));
        assertEquals("Before minute boundary", "00:59", NumberUtils.secToTime(59));
        assertEquals("Whole minute", "01:00", NumberUtils.secToTime(60));
        assertEquals("After minute boundary", "01:01", NumberUtils.secToTime(61));
    }

    public void padTest() {
        assertEquals("02", NumberUtils.pad(2));
        assertEquals("20", NumberUtils.pad(20));
    }

    // StringUtils tests -------------------------------------------------------

    public void formatDateTest() {
        testDate("Date format", "23:59, Wed 30th June 2010", 23, 59, 30, Calendar.JUNE, 2010);
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        if (currentYear == 2011) {
            testDate("Year should not display for current year", "23:59, Thu 30th June", 23, 59, 30, Calendar.JUNE, 2011);
        }
    }

    private void testDate(String test, String expected, int hour, int minute, int day, int month, int year) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.YEAR, year);
        assertEquals(test, expected, StringUtils.formatDate(cal.getTime()));
    }

    public void formatTest() {
        assertEquals("Placeholder only",
            "Test", StringUtils.format("$1", new String[] { "Test" }));
        assertEquals("Placeholder at start",
            "TestFormat", StringUtils.format("$1Format", new String[] { "Test" }));
        assertEquals("Placeholder at end",
            "FormatTest", StringUtils.format("Format$1", new String[] { "Test" }));
        assertEquals("Placeholders only",
                     "123", StringUtils.format("$1$2$3", new String[] { "1", "2", "3" }));
        assertEquals("More than 10 placeholders can be used",
                     "1A2B3C4D5E6F7G8H9I10J11K",
                     StringUtils.format(
                         "$1A$2B$3C$4D$5E$6F$7G$8H$9I$10J$11K",
                         new String[] {
                             "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"
                         }
                     ));
    }

    public void splitTest() {
        String[] parts = StringUtils.split("0,1,2,3,4", ",");
        assertEquals("Correct number of parts", 5, parts.length);
        for (int i = 0; i < 5; i++) {
            assertEquals("Correct part values", ""+i, parts[i]);
        }
    }

    // IntervalParser tests ----------------------------------------------------

    public void intervalParserTest() {
        IntervalParser parser = new IntervalParser();
        int[] intervals = parser.parseLine("0,1,2,3,4");
        assertEquals("Correct number of intervals", 5, intervals.length);
        for (int i = 0; i < 5; i++) {
            assertEquals("Correct interval values", i, intervals[i]);
        }

        int[] repeatedIntervals = parser.parseLine("R");
        assertEquals("Previous intervals returned again", intervals, repeatedIntervals);
    }

    public void test(int testNumber) throws Throwable {
        switch (testNumber) {
        case 0:
            secToDurationTest();
            break;
        case 1:
            secToTimeTest();
            break;
        case 2:
            padTest();
            break;
        case 3:
            formatDateTest();
            break;
        case 4:
            formatTest();
            break;
        case 5:
            splitTest();
            break;
        case 6:
            intervalParserTest();
            break;
        }
    }
}
