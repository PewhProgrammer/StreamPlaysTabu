package common;

import java.util.Date;

/**
 * Created by Tim on 19.04.2017.
 */
public class Util {

    public static int getWordDist(String word1, String word2) {

        int x = word1.length();
        int y = word2.length();

        int[][] dp = new int[x + 1][y + 1];

        for (int i = 0; i <= x; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= y; j++) {
            dp[0][j] = j;
        }

        for (int i = 0; i < x; i++) {
            char c1 = word1.charAt(i);
            for (int j = 0; j < y; j++) {
                char c2 = word2.charAt(j);

                if (c1 == c2) {
                    dp[i + 1][j + 1] = dp[i][j];
                } else {
                    int replace = dp[i][j] + 1;
                    int insert = dp[i][j + 1] + 1;
                    int delete = dp[i + 1][j] + 1;

                    int min = replace > insert ? insert : replace;
                    min = delete > min ? min : delete;
                    dp[i + 1][j + 1] = min;
                }
            }
        }

        return dp[x][y];
    }

    public static double diffTimeStamp(Date set, Date current) {
        long diff = current.getTime() - set.getTime();
        double q = (double) diff / (1000.0);
        return q;
    }

    public static String[] parseTemplate(String tmp) {
        return null;
    }

    /**
     * Checks whether given string is equal to target
     * string ignoring sensitive,whitespace case
     *
     * @param origin
     * @param target
     * @return
     */
    public static boolean guessEquals(String origin, String target) {
        String regex = "[^a-zA-Z0-9]";
        origin = origin.replaceAll(regex, "");
        target = target.replaceAll(regex, "");
        if (origin.replaceAll("\\s", "").equalsIgnoreCase(
                target.replaceAll("\\s", "")
        )) {
            return true;
        }

        return false;
    }

    /**
     * Primarily used in Database. Reduced to lower case
     * and replaces multiple whitespace with one single
     * @param origin
     * @return
     */
    public static String reduceStringToMinimum(String origin) {
        origin = origin.trim().replaceAll(" +"," ");
        String regex = "[^a-zA-Z0-9]\\s";
        origin = origin.replaceAll(regex,"");

        return origin.toLowerCase();
    }

    public static String reduceStringToMinimumWithoutWhitespaces(String origin) {
        origin = origin.trim().replaceAll(" +","");
        String regex = "[^a-zA-Z0-9]\\s";
        origin = origin.replaceAll(regex,"");

        return origin.toLowerCase();
    }
}
