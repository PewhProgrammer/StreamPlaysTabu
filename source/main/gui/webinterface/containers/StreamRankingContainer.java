package gui.webinterface.containers;


import common.database.Neo4jWrapper;

import java.util.Iterator;
import java.util.List;

public class StreamRankingContainer {

    private String stream1 = "";
    private String stream2 = "";
    private String stream3 = "";

    private int points1 = 0;
    private int points2 = 0;
    private int points3 = 0;

    private String[] contributors1 = new String[3];
    private String[] contributors2 = new String[3];
    private String[] contributors3 = new String[3];

    private int[] userPoints1 = new int[3];
    private int[] userPoints2 = new int[3];
    private int[] userPoints3 = new int[3];

    public StreamRankingContainer(List<Neo4jWrapper.StreamerHighscore> streamerHighscore) {
        Iterator<Neo4jWrapper.StreamerHighscore> it = streamerHighscore.iterator();

        Neo4jWrapper.StreamerHighscore sh = it.next();
        stream1 = sh.getStream();
        points1 = sh.getStreamPoints();

        Iterator<Neo4jWrapper.Pair> itP = sh.getUserList().iterator();

        int idx = 0;

        while (itP.hasNext()) {
            Neo4jWrapper.Pair p = itP.next();
            contributors1[idx] = (String) p.getFirst();
            userPoints1[idx] = (Integer) p.getSecond();
        }

        sh = it.next();
        stream2 = sh.getStream();
        points2 = sh.getStreamPoints();

        sh = it.next();
        stream3 = sh.getStream();
        points3 = sh.getStreamPoints();

    }

    public String getStream1() {
        return stream1;
    }

    public String getStream2() {
        return stream2;
    }

    public String getStream3() {
        return stream3;
    }

    public int getPoints1() {
        return points1;
    }

    public int getPoints2() {
        return points2;
    }

    public int getPoints3() {
        return points3;
    }

    public String[] getContributors1() {
        return contributors1;
    }

    public String[] getContributors12() {
        return contributors2;
    }

    public String[] getContributors13() {
        return contributors3;
    }

    public int[] getUserPoints1() {
        return userPoints1;
    }

    public int[] getUserPoints2() {
        return userPoints2;
    }

    public int[] getUserPoints3() {
        return userPoints3;
    }
}
