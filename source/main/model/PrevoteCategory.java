package model;

import logic.commands.Prevote;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Lenovo on 18.04.2017.
 */
public class PrevoteCategory implements Comparable<PrevoteCategory> {

    private int score;
    private String category;
    private Set<String> votingUsers;

    public PrevoteCategory(String category) {
        this.category = category;
        this.votingUsers = new HashSet<String>();
    }

    public int getScore() {
        return this.score;
    }

    public boolean increaseScore(String user) {
        if (!votingUsers.contains(user)) {
            votingUsers.add(user);
            score++;
            return true;
        }
        return false;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public int compareTo(PrevoteCategory pc) {
        if (score < pc.getScore()) {
            return 1;
        }

        if (score > pc.getScore()) {
            return -1;
        }

        return 0;
    }

    @Override
    public boolean equals(Object o) {

        if (!o.getClass().equals(this.getClass())) {
            return false;
        }

        PrevoteCategory pc = (PrevoteCategory) o;

        return this.category.equals(pc.getCategory());
    }
}
