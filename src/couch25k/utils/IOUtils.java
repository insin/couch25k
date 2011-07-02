package couch25k.utils;

import java.io.IOException;
import java.io.InputStreamReader;

public class IOUtils {
    /** Reads a line from a reader. */
    public static String readLine(InputStreamReader reader) throws IOException {
        int c = reader.read();
        if (c == -1) {
            return null;
        }
        StringBuffer line = new StringBuffer();
        while (c != -1 && c != '\n') {
            if (c != '\r') {
                line.append((char)c);
            }
            c = reader.read();
        }
        return line.toString();
    }
}
