package common;

import model.GameModel;

import java.util.*;

/**
 * Created by Tim on 19.04.2017.
 */
public class Util {

    public static final String REGEX = "[-,'a-zA-Z0-9 ]*";

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

    public static String[] getPair(String tmp, String userInput, String userInput2, String isword) {
        String[] pair = {tmp+userInput2, userInput, isword};
        return pair;
    }

    public static String[] parseTemplate(String tmp) {

        String newTmp = "";
        String[] userInput;



        if (tmp.startsWith("The color of its ")){
            newTmp = "The color of its";
            userInput =tmp.split(" ");
            return parseTemplate(newTmp, userInput[4], " "+userInput[6]);
        }

        if (tmp.startsWith("The size of its ")){
            newTmp = "The size of its";
            userInput =tmp.split(" ");
            return parseTemplate(newTmp, userInput[4], " "+userInput[6]);
        }

        if (tmp.startsWith("The shape of its ")){
            newTmp = "The shape of its";
            userInput =tmp.split(" ");
            return parseTemplate(newTmp, userInput[4], " "+userInput[6]);
        }

        if (tmp.startsWith("It wears a ")){
            newTmp = "It wears a";
            userInput =tmp.split("It wears a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It wears an ")){
            newTmp = "It wears an";
            userInput =tmp.split("It wears an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is used for ")){
            newTmp = "It is used for";
            userInput =tmp.split("It is used for ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is used to ")){
            newTmp = "It is used to";
            userInput =tmp.split("It is used to ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It enhances ")){
            newTmp = "It enhances";
            userInput =tmp.split("It enhances ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It appears in ")){
            newTmp = "It appears in";
            userInput =tmp.split("It appears in ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is a part of a ")){
            newTmp = "It is a part of a";
            userInput =tmp.split("It is a part of a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is a part of an ")){
            newTmp = "It is a part of an";
            userInput =tmp.split("It is a part of an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It requires a ")){
            newTmp = "It requires a";
            userInput =tmp.split("It requires a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It requires an ")){
            newTmp = "It requires an";
            userInput =tmp.split("It requires ann ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It has the ability to ")){
            newTmp = "It has the ability to";
            userInput =tmp.split("It has the ability to ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is known for ")){
            newTmp = "It is known for";
            userInput =tmp.split("It is known for ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is known as ")){
            newTmp = "It is known as";
            userInput =tmp.split("It is known as ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It can be received from ")){
            newTmp = "It can be received from";
            userInput =tmp.split("It can be received from ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It works as a ")){
            newTmp = "It works as a";
            userInput =tmp.split("It works as a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It works as an ")){
            newTmp = "It works as an";
            userInput =tmp.split("It works as an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("Its character is ")){
            newTmp = "Its character is";
            userInput =tmp.split("Its character is ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("Its genre is ")){
            newTmp = "Its genre is";
            userInput =tmp.split("Its genre is ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It sets in ")){
            newTmp = "It sets in";
            userInput =tmp.split("It sets in ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is published by ")){
            newTmp = "It is published by";
            userInput =tmp.split("It is published by ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It was released in ")){
            newTmp = "It was released in";
            userInput =tmp.split("It was released in ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is typically near ")){
            newTmp = "It is typically near";
            userInput =tmp.split("It is typically near ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is typically on ")){
            newTmp = "It is typically on";
            userInput =tmp.split("It is typically on ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is typically in ")){
            newTmp = "It is typically in";
            userInput =tmp.split("It is typically in ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is the opposite of ")){
            newTmp = "It is the opposite of";
            userInput =tmp.split("It is the opposite of ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is related to ")){
            newTmp = "It is related to";
            userInput =tmp.split("It is related to ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It has a ")){
            newTmp = "It has a";
            userInput =tmp.split("It has a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It has an ")){
            newTmp = "It has an";
            userInput =tmp.split("It has an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is ")){
            newTmp = "It is";
            userInput = tmp.split("It is ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It has ")){
            newTmp = "It has";
            userInput = tmp.split("It has ");
            return parseTemplate(newTmp, userInput[1], "");
        }


        return null;
    }

    /**
     * Extracts the meaning of a template. Example: tmp = "It is used for killing monsters" returns new String {"tool to kill", "monsters"s}
     *
     * @param tmp template to parse
     * @return String array holding the meaning of the template and the target
     */
    public static String[] parseTemplate(String tmp, String userInput, String userInput2) {


        /* YES NO */

        if (tmp.equals("It is true")) {
            return getPair(userInput, userInput2, "It is true", "false");
        }
        if (tmp.equals("It is not true")) {
            return getPair(userInput, userInput2, "It is not true", "false");
        }
        /* Appearance */


        // The color of its ___ is ___
                if (tmp.equals("The color of its")) {
                    return getPair("its color is", userInput, userInput2, "false");
                }
            // The size of its ___ is ___
                if (tmp.equals("The size of its")) {
                    return getPair("its size is", userInput, userInput2, "false");
                }
            // The shape of its ___ is ___
                if (tmp.equals("The shape of its")) {
                    return getPair("its shape is", userInput, userInput2, "false");
                }
            // It wears a ___
                if (tmp.equals("It wears a")) {
                    return getPair("wears a", userInput, "", "true");
                }
            // It wears an ___
                if (tmp.equals("It wears an")) {
                    return getPair("wears an", userInput, "", "true");
                }

            // It has a ___
                if (tmp.equals("It has a")) {
                    return getPair("has a", userInput, "", "true");
                }

            // It has an ___
                if (tmp.equals("It has an")) {
                    return getPair("has an", userInput, "", "true");
                }

        /* Purpose */

            // It is used for ___
                if (tmp.equals("It is used for")) {
                    return getPair("is used for", userInput, "", "true");
                }

            // It is used for ___
                if (tmp.equals("It is used to")) {
                    return getPair("is used to", userInput, "", "true");
                }

            // It enhances ___
                if (tmp.equals("It enhances")) {
                    return getPair("enhances", userInput, "", "true");
                }

        /* Characteristics */

            // It is typically near ___
                if (tmp.equals("It is typically near")) {
                    return getPair("is typically near", userInput, "", "true");
                }

            // It is typically in ___
                if (tmp.equals("It is typically in")) {
                    return getPair("is typically in", userInput, "", "true");
                }

            // It is typically on ___
                if (tmp.equals("It is typically on")) {
                    return getPair("is typically on", userInput, "", "true");
                }

            // It is the opposite of ___
                if (tmp.equals("It is the opposite of")) {
                    return getPair("is the opposite of", userInput, "", "true");
                }

            // It is related to ___
                if (tmp.equals("It is related to")) {
                    return getPair("is related to", userInput, "", "true");
                }

            // It is ___
                if (tmp.equals("It is")) {
                    return getPair("is", userInput, "", "true");
                }
            // It has ___
                if (tmp.equals("It has")) {
                    return getPair("has", userInput, "", "true");
                }
            // It appears in ___
                if (tmp.equals("It appears in")) {
                    return getPair("appears in", userInput, "", "true");
                }
            // It is a part of a ___
                if (tmp.equals("It is a part of a")) {
                    return getPair(tmp, userInput, "", "true");
                }

            // It is a part of an ___
                if (tmp.equals("It is a part of an")) {
                    return getPair(tmp, userInput, "", "true");
                }

            // It requires a ___
                if (tmp.equals("It requires a")) {
                    return getPair("requires a", userInput, "", "true");
                }

            // It requires an ___
                if (tmp.equals("It requires an")) {
                    return getPair("requires an", userInput, "", "true");
                }

            // It has the ability to ___
                if (tmp.equals("It has the ability to")) {
                    return getPair("has the ability to", userInput, "", "true");
                }
            // It is known for ___
                if (tmp.equals("It is known for")) {
                    return getPair("is known for", userInput, "", "true");
                }
            // It is known as ___
                if (tmp.equals("It is known as")) {
                    return getPair("is known as", userInput, "", "true");
                }
            // It can be received from ___
                if (tmp.equals("It can be received from")) {
                    return getPair("can be received from", userInput, "", "true");
                }
            // It works as a ___
                if (tmp.equals("It works as a")) {
                    return getPair("works as a", userInput, "", "true");
                }

            // It works as an ___
                if (tmp.equals("It works as an")) {
                    return getPair("works as an", userInput, "", "true");
                }

            // Its character is ____
                if (tmp.equals("Its character is")) {
                    return getPair("character is", userInput, "", "true");
                }

        /* Game specific */

            // Its genre is ___
                if (tmp.equals("Its genre is")) {
                    return getPair("genre is", userInput, "", "true");
                }
            // It is set in ___
                if (tmp.equals("It sets in")) {
                    return getPair("is set in", userInput, "", "true");
                }
            // It is published by ___
                if (tmp.equals("It is published by")) {
                    return getPair("is published by", userInput, "", "true");
                }
            // It was released in ___
                if (tmp.equals("It was released in")) {
                    return getPair("was released in", userInput, "", "false");
                }
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

    public static boolean checkCheating(String input, GameModel gm) {

        //check invalid characters
        if (!input.matches(REGEX)) {
            return false;
        }

        //lemmatize
        List<String> lemmas = gm.lemmatize(input);
        Set<String> tW = gm.getTabooWords();
        Iterator<String> it = tW.iterator();
        String taboo = "";
        while (it.hasNext()) {
            taboo = taboo.concat(" ".concat(it.next()));
        }

        List<String> tabooLemmas = gm.lemmatize(taboo);
        List<String> explainLemmas = gm.lemmatize(gm.getExplainWord());

        //check word dist of lemmas
        for (String lemma : lemmas) {

            //check explain word
            for (String exp : explainLemmas) {
                if (Util.getWordDist(exp, lemma) <= 1) {
                    System.out.println("Found an invalid word: " + exp + " was matched to " + lemma + ".");
                    return false;
                }
            }

            //check taboo words
            for (String exp : tabooLemmas) {
                if (Util.getWordDist(exp, lemma) <= 1) {
                    System.out.println("Found an invalid word: " + exp + " was matched to " + lemma + ".");
                    return false;
                }
            }
        }
        return true;
    }
}
