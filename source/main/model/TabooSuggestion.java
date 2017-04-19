package model;

/**
 * Created by Tim on 19.04.2017.
 */
public class TabooSuggestion implements Comparable<TabooSuggestion> {

    private int occurrences;
    private String content;
    private String explanation;

    public TabooSuggestion(String content, String explanation) {
        occurrences = 1;
        this.content = content;
        this.explanation = explanation;
    }

    public void occurred() {
        occurrences++;
    }

    public String getContent() {
        return content;
    }

    public String getExplanation() {
        return explanation;
    }

    public int getOccurrences() {
        return occurrences;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        }
        TabooSuggestion ts = (TabooSuggestion) o;
        return ts.getContent().equals(content);
    }

    @Override
    public int compareTo(TabooSuggestion ts) {
        if (this.occurrences > ts.occurrences) {
            return -1;
        }

        if (this.occurrences < ts.occurrences) {
            return 1;
        }

        return 0;
    }
}
