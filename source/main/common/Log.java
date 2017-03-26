package common;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A Simple Log Class with different Levels
 *
 * TRACE
 * DEBUG
 * INFO
 *
 */
public class Log {
    private static Level mLevel = Level.INFO;

    public static void debug(String msg) {
        if (mLevel.level <= Level.DEBUG.level)
            print(Level.DEBUG.toString().charAt(0) + ": " + msg, System.out);
    }

    public static void info(String msg) {
        if (mLevel.level <= Level.INFO.level)
            print(Level.INFO.toString().charAt(0) + ": " + msg, System.out);
    }

    public static void trace(String msg) {
        if (mLevel.level <= Level.TRACE.level)
            print(Level.TRACE.toString().charAt(0) + ": " + msg, System.out);
    }

    public static void error(String msg) {
        print("E: " + msg, System.err);
    }

    public static Level getLevel() {
        return mLevel;
    }

    public static void setLevel(Level l) {
        mLevel = l;
    }

    private static void print(String msg, PrintStream out) {
        out.println(new SimpleDateFormat("HH:mm:ss.SSS").format(new Date()) + ": " +
            msg);
    }

    /**
     * ENUM-Class for Levels
     */
    public enum Level {
        TRACE(1),
        DEBUG(2),
        INFO(3);

        private final int level;

        Level(int i) {
            level = i;
        }
    }
}
