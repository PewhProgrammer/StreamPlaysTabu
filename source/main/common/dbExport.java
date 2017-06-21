package common;

import java.io.*;
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
 * <p>
 * TRACE
 * DEBUG
 * INFO
 * DB
 */
public class dbExport {
    static Path file;
    static BufferedWriter writer = null;
    static FileWriter fw = null;

    public static void init(String filename, String process) {
        file = Paths.get(filename);
        try {
            new FileWriter(file.toFile(), false);
            fw = new FileWriter(file.toFile(), true);
            writer = new BufferedWriter(fw);
            //Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            Log.error(e.getStackTrace().toString());
        }

        write("Latest polling: " + new SimpleDateFormat("dd.MM HH:mm:ss").format(new Date()));
        try {
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(process);
    }

    public static void write(String msg) {
        print(msg);
    }

    public static BufferedWriter getWriter(){return writer;}

    public static void printLine(){
        try {
            dbExport.getWriter().newLine();
        }catch (IOException e){e.printStackTrace();}
    }

    private static void print(String msg) {
        try {
            writer.write(msg);
            writer.newLine();
            //Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    public static void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
