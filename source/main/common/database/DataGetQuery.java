package common.database;

/**
 * Created by Thinh-Laptop on 30.05.2017.
 */
public class DataGetQuery {

    private final String target;
    private final int querySize;
    private String queryMode ;

    public DataGetQuery(String target, int querySize){
        this.target=target; this.querySize = querySize;
    }

    public DataGetQuery(int querySize){
        this.target = ""; this.querySize = querySize;
    }

    public DataGetQuery(String user, String ch){
        this.target = ""; this.querySize = 0;
    }

    public void setModeTabooWords(){
        queryMode = "taboo";
    }

    public void setModeTabooWordsForced(){
        queryMode = "taboo_forced";
    }

    public void setModeRandomExplainWord(){
        queryMode = "explain_random";
    }

    public void setModeUserPoints(){
        queryMode = "user_points";
    }

    public void setModeUserError(){
        queryMode = "user_error";
    }

    public void setModeUserErrorTimestamp(){
        queryMode = "user_error_timestamp";
    }

    public void setModeHighscore(){
        queryMode = "highscore";
    }

    public void setModeHighscoreStream(){
        queryMode = "highscore_stream";
    }

    public void setModeCategories(){
        queryMode = "categories";
    }

    public void setModeExplainWord(){
        queryMode = "explain";
    }




}
