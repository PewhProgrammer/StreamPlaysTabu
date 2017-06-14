package common;

import model.GameModel;

import java.util.*;

/**
 * Created by Tim on 19.04.2017.
 */
public class Util {

    private static final String REGEX = "[-,'a-zA-Z0-9 ]*";
    private static final String[] whiteArray = {"the", "of", "a", "an"};
    private static final List<String> whiteList = Arrays.asList(whiteArray);


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
        String[] pair = {tmp ,userInput, userInput2, isword};
        return pair;
    }

    //The color of its skin is blue
    //getPair(has, skin, blue, false)
    //getPair(Kante, Knoten, Atrribut, Attribut)

    public static String[] parseTemplate(String tmp) {

        String newTmp = "";
        String[] userInput;
        String[] help;

        if (tmp.startsWith("The color of its ")){
            newTmp = "The color of its";
            help = tmp.split("The color of its ");
            userInput = help[1].split(" is ");
            if ((userInput[0].equals(" ")) || (userInput[1].equals(" "))){
                //ERROR
            }
            return getPair("has", userInput[0], userInput[1], "false");
        }

        if (tmp.startsWith("The size of its ")){
            newTmp = "The size of its";
            help = tmp.split("The size of its ");
            userInput = help[1].split(" is ");
            if ((userInput[0].equals(" ")) || (userInput[1].equals(" "))){
                //ERROR
            }
            return getPair("has", userInput[0], userInput[1], "false");
        }

        if (tmp.startsWith("The shape of its ")){
            newTmp = "The shape of its";
            help = tmp.split("The shape of its ");
            userInput = help[1].split(" is ");
            if ((userInput[0].equals(" ")) || (userInput[1].equals(" "))){
                //ERROR
            }
            return getPair("has", userInput[0], userInput[1], "false");
        }

        if (tmp.startsWith("It wears a ")){
            newTmp = "It wears";
            userInput =tmp.split("It wears a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It wears an ")){
            newTmp = "It wears";
            userInput =tmp.split("It wears an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It wears ")){
            newTmp = "It wears";
            userInput =tmp.split("It wears ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is used for a ")){
            newTmp = "It is used for";
            userInput =tmp.split("It is used for a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is used for an ")){
            newTmp = "It is used for";
            userInput =tmp.split("It is used for an ");
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

        if (tmp.startsWith("It enhances a ")){
            newTmp = "It enhances";
            userInput =tmp.split("It enhances a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It enhances an ")){
            newTmp = "It enhances";
            userInput =tmp.split("It enhances an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It enhances ")){
            newTmp = "It enhances";
            userInput =tmp.split("It enhances ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It appears in a ")){
            newTmp = "It appears in";
            userInput =tmp.split("It appears in a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It appears in an ")){
            newTmp = "It appears in";
            userInput =tmp.split("It appears in an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It appears in ")){
            newTmp = "It appears in";
            userInput =tmp.split("It appears in ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is a part of a ")){
            newTmp = "It is a part of";
            userInput =tmp.split("It is a part of a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is a part of an ")){
            newTmp = "It is a part of";
            userInput =tmp.split("It is a part of an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is a part of ")){
            newTmp = "It is a part of";
            userInput =tmp.split("It is a part of ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It requires a ")){
            newTmp = "It requires";
            userInput =tmp.split("It requires a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It requires an ")){
            newTmp = "It requires";
            userInput =tmp.split("It requires an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It requires ")){
            newTmp = "It requires";
            userInput =tmp.split("It requires ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It has the ability to ")){
            newTmp = "It has the ability to";
            userInput =tmp.split("It has the ability to ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is known for a ")){
            newTmp = "It is known for";
            userInput =tmp.split("It is known for a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is known for an ")){
            newTmp = "It is known for";
            userInput =tmp.split("It is known for an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is known for ")){
            newTmp = "It is known for";
            userInput =tmp.split("It is known for ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is known as a ")){
            newTmp = "It is known as";
            userInput =tmp.split("It is known as a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is known as an ")){
            newTmp = "It is known as";
            userInput =tmp.split("It is known as an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is known as ")){
            newTmp = "It is known as";
            userInput =tmp.split("It is known as ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It can be received from a ")){
            newTmp = "It can be received from";
            userInput =tmp.split("It can be received from a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It can be received from an ")){
            newTmp = "It can be received from";
            userInput =tmp.split("It can be received from an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It can be received from ")){
            newTmp = "It can be received from";
            userInput =tmp.split("It can be received from ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It works as a ")){
            newTmp = "It works as";
            userInput =tmp.split("It works as a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It works as an ")){
            newTmp = "It works as";
            userInput =tmp.split("It works as an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It works as ")){
            newTmp = "It works as";
            userInput =tmp.split("It works as ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("Its character is ")){
            newTmp = "Its character is";
            userInput =tmp.split("Its character is ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("Its genre is a ")){
            newTmp = "Its genre is";
            userInput =tmp.split("Its genre is a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("Its genre is an ")){
            newTmp = "Its genre is";
            userInput =tmp.split("Its genre is an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("Its genre is ")){
            newTmp = "Its genre is";
            userInput =tmp.split("Its genre is ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It sets in a ")){
            newTmp = "It sets in";
            userInput =tmp.split("It sets in a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It sets in an ")){
            newTmp = "It sets in";
            userInput =tmp.split("It sets in an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It sets in ")){
            newTmp = "It sets in";
            userInput =tmp.split("It sets in ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is published by a ")){
            newTmp = "It is published by";
            userInput =tmp.split("It is published by a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is published by an ")){
            newTmp = "It is published by";
            userInput =tmp.split("It is published by an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is published by ")){
            newTmp = "It is published by";
            userInput =tmp.split("It is published by ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It was released in a ")){
            newTmp = "It was released in";
            userInput =tmp.split("It was released in a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It was released in an ")){
            newTmp = "It was released in";
            userInput =tmp.split("It was released in an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It was released in ")){
            newTmp = "It was released in";
            userInput =tmp.split("It was released in ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is typically near a ")){
            newTmp = "It is typically near";
            userInput =tmp.split("It is typically near a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is typically near an ")){
            newTmp = "It is typically near";
            userInput =tmp.split("It is typically near an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is typically near ")){
            newTmp = "It is typically near";
            userInput =tmp.split("It is typically near ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is typically on a ")){
            newTmp = "It is typically on";
            userInput =tmp.split("It is typically on a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is typically on an ")){
            newTmp = "It is typically on";
            userInput =tmp.split("It is typically on an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is typically on ")){
            newTmp = "It is typically on";
            userInput =tmp.split("It is typically on ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is typically in a ")){
            newTmp = "It is typically in";
            userInput =tmp.split("It is typically in a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is typically in an ")){
            newTmp = "It is typically in";
            userInput =tmp.split("It is typically in an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is typically in ")){
            newTmp = "It is typically in";
            userInput =tmp.split("It is typically in ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is the opposite of a ")){
            newTmp = "It is the opposite of";
            userInput =tmp.split("It is the opposite of a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is the opposite of an ")){
            newTmp = "It is the opposite of";
            userInput =tmp.split("It is the opposite of an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is the opposite of ")){
            newTmp = "It is the opposite of";
            userInput =tmp.split("It is the opposite of ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is related to a ")){
            newTmp = "It is related to";
            userInput =tmp.split("It is related to a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is related to an ")){
            newTmp = "It is related to";
            userInput =tmp.split("It is related to an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is related to ")){
            newTmp = "It is related to";
            userInput =tmp.split("It is related to ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It has a ")){
            newTmp = "It has";
            userInput =tmp.split("It has a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It has an ")){
            newTmp = "It has";
            userInput =tmp.split("It has an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It has ")){
            newTmp = "It has";
            userInput =tmp.split("It has ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is a ")){
            newTmp = "It is";
            userInput = tmp.split("It is a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is an ")){
            newTmp = "It is";
            userInput = tmp.split("It is an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is ")){
            newTmp = "It is";
            userInput = tmp.split("It is ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("An example for it is a ")){
            newTmp = "An example for it is";
            userInput = tmp.split("An example for it is a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("An example for it is an ")){
            newTmp = "An example for it is";
            userInput = tmp.split("An example for it is an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("An example for it is ")){
            newTmp = "An example for it is";
            userInput = tmp.split("An example for it is ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is a kind of a ")){
            newTmp = "It is a kind of";
            userInput = tmp.split("It is a kind of a ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is a kind of an ")){
            newTmp = "It is a kind of";
            userInput = tmp.split("It is a kind of an ");
            return parseTemplate(newTmp, userInput[1], "");
        }

        if (tmp.startsWith("It is a kind of ")){
            newTmp = "It is a kind of";
            userInput = tmp.split("It is a kind of ");
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


        /* Appearance */


        // The color of its ___ is ___
                if (tmp.equals("The color of its")) {
                    return getPair("has", userInput, userInput2, "false");
                }
            // The size of its ___ is ___
                if (tmp.equals("The size of its")) {
                    return getPair("has", userInput, userInput2, "false");
                }
            // The shape of its ___ is ___
                if (tmp.equals("The shape of its")) {
                    return getPair("has", userInput, userInput2, "false");
                }

             // It wears ___
                 if (tmp.equals("It wears")) {
                     return getPair("wears", userInput, "", "true");
                 }

            // It has a ___
                if (tmp.equals("It has")) {
                    return getPair("has", userInput, "", "true");
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

            // It is kind ___
                if (tmp.equals("It is a kind of")) {
                    return getPair("is a kind of", userInput, "", "true");
                }

            // An example for it is ___
                if (tmp.equals("An example for it is")) {
                    return getPair("is example of", userInput, "", "true");
                }

            // It has ___
                if (tmp.equals("It has")) {
                    return getPair("has", userInput, "", "true");
                }
            // It appears in ___
                if (tmp.equals("It appears in")) {
                    return getPair("appears in", userInput, "", "true");
                }
            // It is a part of ___
                if (tmp.equals("It is a part of")) {
                    return getPair(tmp, userInput, "", "true");
                }

            // It requires ___
                if (tmp.equals("It requires")) {
                    return getPair("requires", userInput, "", "true");
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

    public static String checkSpelling(String input) {
        if (!input.matches(REGEX)) {
            String error = "Invalid character: " + input;
            System.out.println(error);
            return error;
        }

        return "";
    }

    public static String checkCheating(String input, GameModel gm) {

        //check invalid characters
        String rtn = checkSpelling(input);
        if (!rtn.equals("")) {
            return rtn;
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

            rtn = checkWordDist(lemma, tabooLemmas);

            if (!rtn.equals("")) {
               return rtn;
            }

            rtn = checkWordDist(lemma, explainLemmas);

            if (!rtn.equals("")) {
                return rtn;
            }
        }

        return "";
    }

    private static String checkWordDist(String input, List<String> lemmas) {
        for (String exp : lemmas) {
            if (Util.getWordDist(exp, input) < 1) {
                if (!whiteList.contains(input) && !whiteList.contains(exp)) {
                    String error = "Found an invalid word: " + exp + " was matched to " + input + ".";
                    Log.info(error);
                    return error;
                }
            }
        }
        return "";
    }
}
