package gui.webinterface.containers;

import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by Lenovo on 04.05.2017.
 */
public class RankingContainer {

    private String firstName = "";
    private String secondName = "";
    private String thirdName = "";
    private String fourthName = "";
    private String fifthName = "";
    private String sixthName = "";
    private String seventhName = "";
    private String eighthName = "";
    private String ninthName = "";
    private String tenthName = "";

    private int firstPoints = 0;
    private int secondPoints = 0;
    private int thirdPoints = 0;
    private int fourthPoints = 0;
    private int fifthPoints = 0;
    private int sixthPoints = 0;
    private int seventhPoints = 0;
    private int eighthPoints = 0;
    private int ninthPoints = 0;
    private int tenthPoints = 0;


    public RankingContainer(LinkedHashMap<String, Integer> score) {
        int idx = 0;
        for (String s : score.keySet()) {
            idx++;
            if (idx == 1) {
                firstName = s;
                firstPoints = score.get(s);
                continue;
            }
            if (idx == 2) {
                secondName = s;
                secondPoints= score.get(s);
                continue;
            }
            if (idx == 3) {
                thirdName = s;
                thirdPoints = score.get(s);
                continue;
            }
            if (idx == 4) {
                fourthName = s;
                fourthPoints = score.get(s);
                continue;
            }
            if (idx == 5) {
                fifthName = s;
                fifthPoints = score.get(s);
                continue;
            }
            if (idx == 6) {
                sixthName = s;
                sixthPoints = score.get(s);
                continue;
            }
            if (idx == 7) {
                seventhName = s;
                seventhPoints = score.get(s);
                continue;
            }
            if (idx == 8) {
                eighthName = s;
                eighthPoints = score.get(s);
                continue;
            }
            if (idx == 9) {
                ninthName = s;
                ninthPoints = score.get(s);
                continue;
            }
            if (idx == 10) {
                tenthName = s;
                tenthPoints = score.get(s);
                continue;
            }
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getThirdName() {
        return thirdName;
    }

    public String getFourthName() {
        return fourthName;
    }

    public String getFifthName() {
        return fifthName;
    }

    public String getSixthName() {
        return sixthName;
    }

    public String getSeventhName() {
        return seventhName;
    }

    public String getEighthName() {
        return eighthName;
    }

    public String getNinthName() {
        return ninthName;
    }

    public String getTenthName() {
        return tenthName;
    }

    public int getFirstPoints() {
        return firstPoints;
    }

    public int getSecondPoints() {
        return secondPoints;
    }

    public int getThirdPoints() {
        return thirdPoints;
    }

    public int getFourthPoints() {
        return fourthPoints;
    }

    public int getFifthPoints() {
        return fifthPoints;
    }

    public int getSixthPoints() {
        return sixthPoints;
    }

    public int getSeventhPoints() {
        return seventhPoints;
    }

    public int getEighthPoints() {
        return eighthPoints;
    }

    public int getNinthPoints() {
        return ninthPoints;
    }

    public int getTenthPoints() {
        return tenthPoints;
    }
}
