package couch25k.utils;

public class NumberUtils {
    /** Formats seconds as a duration description. */
    public static String secToDuration(int n) {
        if (n <= 90) {
            return n + " seconds";
        }
        int min = n / 60;
        int sec = n % 60;
        if (sec == 0) {
          return min + " minutes";
        }
        // Spare seconds in Couch-to-5k intervals all round nicely
        return (min + (float)sec/60) + " minutes";
    }

    /** Formats seconds as a time in MM:SS format. */
    public static String secToTime(int n) {
        int minutes = n / 60;
        int seconds = n % 60;
        return pad(minutes) + ":" + pad(seconds);
    }

    /** Pads a number with a leading zero if necessary. */
    public static String pad(int n) {
        if (n < 10) {
            return "0" + n;
        }
        return "" + n;
    }
}
