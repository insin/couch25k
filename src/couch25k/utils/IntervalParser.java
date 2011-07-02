package couch25k.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Parses workout interval definitions from  a text file. Expected format of the
 * file is:
 *
 * * One set of intervals per line.
 * * Intervals are commma-separated integers, no whitespace.
 * * A line consisting solely of the letter "R" indicates repitition of the last
 *   defined intervals.
 */
public class IntervalParser {
    /** Caches the last set of intervals which was parsed, for repitition. */
    int[] lastParsedLine;

    /** Parses interval definitions and repititions from a text file. */
    public int[][] parseIntervals(String fileName) {
        InputStream in = IntervalParser.class.getResourceAsStream("/" + fileName);
        InputStreamReader reader = new InputStreamReader(in);
        Vector intervals = new Vector();
        try {
            String line;
            while ((line = IOUtils.readLine(reader)) != null) {
                // Ignore empty lines
                if (!"".equals(line)) {
                    intervals.addElement(parseLine(line));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[][] results = new int[intervals.size()][];
        intervals.copyInto(results);
        return results;
    }

    /** Parses a single line of an interval definition file. */
    int[] parseLine(String line) {
        if (line.equals("R")) {
            return lastParsedLine;
        }
        String[] parts = StringUtils.split(line, ",");
        lastParsedLine = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            lastParsedLine[i] = Integer.parseInt(parts[i]);
        }
        return lastParsedLine;
    }
}
