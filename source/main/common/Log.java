package common;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A Simple Log Class with different Levels
 *
 * TRACE
 * DEBUG
 * INFO
 * DB
 */
public class Log {
    static Path file = Paths.get("Logging.txt");

    private static Level mLevel = Level.INFO;

    public static void debug(String msg) {
        if (mLevel.level <= Level.DEBUG.level)
            print(Level.DEBUG.toString().charAt(0) + " >> " + msg, System.out);
    }

    public static void info(String msg) {
        if (mLevel.level <= Level.INFO.level)
            print(Level.INFO.toString().charAt(0) + " >> " + msg, System.out);
    }

    public static void trace(String msg) {
        if (mLevel.level <= Level.TRACE.level)
            print(Level.TRACE.toString().charAt(0) + " >> " + msg, System.out);
    }

    public static void db(String msg) {
        if (mLevel.level <= Level.DB.level)
            print(Level.DB.toString() + " >> " + msg, System.out);
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
        out.println(new SimpleDateFormat("HH:mm:ss").format(new Date()) + ". " +
            msg);

        List<String> lines = Arrays.asList(new SimpleDateFormat("dd.MM HH:mm:ss").format(new Date()) + ". " +
                msg);

        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        }catch(IOException e){

        }
    }

    /**
     * ENUM-Class for Levels
     */
    public enum Level {
        TRACE(1),
        DEBUG(2),
        INFO(3),
        DB(4);

        private final int level;

        Level(int i) {
            level = i;
        }
    }
}
