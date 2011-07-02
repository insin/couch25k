package couch25k.utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class StringUtils {
    /** Formats a String with $1, $2 style placeholders. */
    public static String format(String template, String[] values) {
        StringBuffer sb = new StringBuffer();
        int nextIndex = 0;
        for (int i = 0; i < values.length; i++) {
            int placeholderIndex = template.indexOf("$" + (i + 1), nextIndex);
            sb.append(template.substring(nextIndex, placeholderIndex));
            sb.append(values[i]);
            // Advance to the index beyond the placeholder
            nextIndex = (placeholderIndex + 2 + ((i + 1) / 10));
        }
        if (nextIndex <= template.length() - 1) {
            sb.append(template.substring(nextIndex));
        }
        return sb.toString();
    }

    /** Splits a String by a delimiter. */
    public static String[] split(String text, String delim) {
        Vector parts = new Vector();
        int nextIndex = 0;
        while (nextIndex < text.length()) {
            int nextMatch = text.indexOf(delim, nextIndex);
            if (nextMatch == -1) {
                parts.addElement(text.substring(nextIndex));
                break;
            }
            parts.addElement(text.substring(nextIndex, nextMatch));
            nextIndex = nextMatch + delim.length();
        }
        String[] results = new String[parts.size()];
        parts.copyInto(results);
        return results;
    }

    public static final String[] DAYS = new String[] {
        "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"
    };

    public static final String[] MONTHS = new String[] {
        "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct",
        "Nov", "Dec"
    };

    public static final String[] ORDINALS = new String[] {
        "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"
    };

    /**
     * Formats a Date in "00:10, Sat 2nd June 2011" format, only including the
     * year if it's not the current year.
     */
    public static String formatDate(Date date) {
        Calendar cal = Calendar.getInstance();
        int currentYear = cal.get(Calendar.YEAR);
        cal.setTime(date);
        StringBuffer sb = new StringBuffer();
        sb.append(NumberUtils.pad(cal.get(Calendar.HOUR_OF_DAY))).append(":")
          .append(NumberUtils.pad(cal.get(Calendar.MINUTE))).append(", ")
          .append(DAYS[cal.get(Calendar.DAY_OF_WEEK) - 1]).append(" ")
          .append(cal.get(Calendar.DAY_OF_MONTH))
          .append(ORDINALS[cal.get(Calendar.DAY_OF_MONTH) % 10]).append(" ")
          .append(MONTHS[cal.get(Calendar.MONTH)]);
        if (cal.get(Calendar.YEAR)!= currentYear ) {
            sb.append(" ").append(cal.get(Calendar.YEAR));
        }
        return sb.toString();
    }
}
