package common;

import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.*;

import java.io.File;
import java.lang.reflect.Array;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.neo4j.driver.v1.Values.parameters;
import org.neo4j.driver.v1.types.Node;

/**
 * Created by Thinh-Laptop on 08.04.2017.
 */
public class Neo4jWrapper {

    private final Config config ;
    //default -> localhost:7687
    //You're using the wrong port number. 7474 is by default used for http
    // whereas 7687 is the default for binary bolt protocol.
    private StringBuilder neo4jbindAddr = new StringBuilder();
    private final boolean isDeletable ;
    private String userLabel = "userNode";
    private final int DEL_TRESHHOLD = 2; //
    private final int CATEGORY_TRESHOLD = 3; //word qualifies as category if more equal than 3 incoming arcs are available
    private final String label;
    private final Driver driver;
    private final Random randomizer;



    public Neo4jWrapper(boolean simulation, String neo4jbind,int seed){

        //baut enkryptische Verbindung, um uns gegen "man-in-the-middle" attacken zu schützen
        config = Config.build().withEncryptionLevel( Config.EncryptionLevel.REQUIRED ).toConfig() ;
        neo4jbindAddr.append(neo4jbind);
        randomizer = new Random(seed);
        randomizer.setSeed(seed);

        driver = acquireDriver("bolt://" + neo4jbindAddr,
                AuthTokens.basic( "neo4j", "streamplaystabu" ),config);

        //non-legacy nodes are used experimentally
        label = "Node";
        userLabel = "userNode";
        isDeletable = simulation ;

    }

    /**
     * This method is mainly called to build our ontology
     * FROM node1 TO node2 : node1 -rel-> node2
     * @param node1
     * @param node2
     * @param relationship
     * @return
     */
    public boolean insertNodesAndRelationshipIntoOntology(String node1, String node2,Boolean node2Explain, String relationship,Boolean reliableFlag){
        boolean isNode1Explain = false ;

        //create two nodes if not already
        try {
            createNode(node1,isNode1Explain);

        }catch(ServiceUnavailableException | DatabaseException e){
            //if it exists, check upon explain
            Log.debug(e.getLocalizedMessage());
        }

        String type = "";
        try {
            type = fetchNodePropertiesFromDatabase(node1, "type");
        }catch(DatabaseException i){
            Log.debug(i.getLocalizedMessage());
        }
        if(type.equals("explain")) isNode1Explain = true;

        try {
            createNode(node2,node2Explain);
        }catch(ServiceUnavailableException | DatabaseException e){
            if(isNode1Explain && node2Explain){ //If the start node is from type explain, update end node to potential category
                setExplainWordToCategory(node2);
            }
            Log.debug(e.getLocalizedMessage());
        }


        //creates a binding connection between node1 and node2
        return
                createRelationship(node1,node2,relationship,reliableFlag);
    }

    public int updateUserPoints(String user, int i,String ch) {
        try {
            return updateUserPropertiesFromDatabase(user,"points",i);
        }catch(DatabaseException e){
            Log.debug(e.getMessage());
            createUser(user,ch);
        }
        return 0; //if new user is created
    }

    public String updateUserVoteKicked(String user, String ch) {
        try {
            return ""+updateUserPropertiesFromDatabaseAdd(user,"votekicked",1);
        }catch(DatabaseException e){
            Log.debug(e.getMessage());
            createUser(user,ch);
        }
        return "0"; //if new user is created
    }


    /**
     * if no user was found, then a user will be created
     * @param user
     * @return Points of user as in database
     * @throws Neo4jException
     */
    public int getUserPoints(String user,String ch) throws Neo4jException{
        try {
            return Integer.parseInt(fetchUserPropertiesFromDatabase(user,"points"));
        }catch(DatabaseException e){
            Log.debug(e.getMessage());
            createUser(user,ch);
        }
        return 0; //if new user is created
    }

    /**
     *
     * @param cap how many players
     * @param ch channel name
     * @return
     */
    public LinkedHashMap<String, Integer> getHighScoreList(int cap,String ch){
        return fetchUserWithHighestPoints(cap,ch);
    }

    /**
     * Increase the error any user made by one
     * @param user
     * @return updated count of mistakes the user now have
     * @throws Neo4jException
     */
    public int increaseUserError(String user,String ch) throws Neo4jException{
        try {
            return updateUserPropertiesFromDatabase(user,"mistakes",1);
        }catch(DatabaseException e){
            Log.debug(e.getMessage());
            createUser(user,ch);
        }
        return 0; //if new user is created
    }

    public int getUserError(String user,String ch) throws Neo4jException{
        try {
            return Integer.parseInt(fetchUserPropertiesFromDatabase(user,"mistakes"));
        }catch(DatabaseException e){
            Log.debug(e.getMessage());
            createUser(user,ch);
        }
        return 0; //if new user is created
    }

    /**
     * retrieves categories for pre-voting
     * @param i capacity of categories
     * @return
     */
    public Set<String> getCategories(int i){
        return fetchFilteredCategoryFromDatabase(i);
    }

    /**
     * randomly fetch high-validated words connected to the category
     * @param category from which the explain word inherits
     * @param usedWords already skipped words
     * @return
     */
    public String getExplainWord(String category,Set<String> usedWords) throws DatabaseException
    {
        return fetchConnectedWordFromDatabase(category,usedWords);
    }


    /**
     * UserNode will be created in Database, if not already existing
     * @param str
     */
    public void createUser(String str,String ch){

        try {
            generateUserNodeInDatabase(str,ch);
            Log.trace("Created Node: \""+str+"\" userNode");
        }catch(DatabaseException e){
            Log.trace(e.getMessage());
        }

        createStreamNode(ch);
        createRelationshipCase(str,ch,"plays in");
    }

    public void createStreamNode(String ch){
        try {
            generateNodeInDatabase(ch,"streamNode");
            Log.trace("Created streamNode: \""+ch+"\"");
        }catch(DatabaseException e){
            Log.trace(e.getMessage());
        }
    }

    /**
     *
     * @param explain explain word to retrieve taboo words from
     * @param i describes how many taboo words to retrieve
     * @return
     */
    public Set<String> getTabooWords(String explain, int i){
        //could happen that we have too less connected words in database!!

        Set<String> result = fetchConnectedWordsFromDatabase(Util.reduceStringToMinimum(explain),i);
        if(result.size() < i)
            Log.trace("Could not retrieve enough taboo words. Missing -> Expected " + i  + "; Actual " + result.size());
        return result;
    }

    /**
     * FORCES TABOO WORDS
     * @param explain explain word to retrieve taboo words from
     * @param i describes how many taboo words to retrieve
     * @return
     */
    public HashMap<String,Set<String>> getTabooWordsForValidation(String explain, int i){
        //could happen that we have too less connected words in database!!

        HashMap<String,Set<String>> result = new HashMap<>();

       if(explain != null) {
           Set<String> taboo = fetchConnectedWordsFromDatabase(Util.reduceStringToMinimum(explain), i);
           if (taboo.size() > 0) {
               //Log.trace("Retieved Taboo Words: " + taboo.toString());
               result = new HashMap<>();
               result.put(explain, taboo);
               return result;
           }
           explain = "";
       }
       else explain = "";
        //Force it!
        Set<String> cat = fetchFilteredCategoryFromDatabase(5);
        for(String str: cat){
            if(randomizer.nextBoolean() || explain.equals("")){
                explain = str ;
            }
        }

        result.put(explain,fetchConnectedWordsFromDatabase(Util.reduceStringToMinimum(explain),i));
        return result;
    }

    public ArrayList<ArrayList<String>> getTabooWordsForValidationForGiver(){
        //could happen that we have too less connected words in database!!

        ArrayList<ArrayList<String>> result = new ArrayList<>();

        //Force it!
        Set<String> cat = fetchFilteredCategoryFromDatabase(10);
        int count = 0 ;
        for(String str: cat){
            if(randomizer.nextBoolean() || (count < 3)){
                count++;
                ArrayList<String> temp = new ArrayList<>();
                temp.add(str);
                Set<String> i = fetchConnectedWordsFromDatabase(Util.reduceStringToMinimum(str),1) ;
                temp.add(i.iterator().next());
                result.add(temp);
            }
        }

        return result;
    }

    /**
     * increases all connection between source and target node
     * @param explain target
     * @param taboo source
     */
    public void validateExplainAndTaboo(String explain,String taboo,int i){

        updateExplainTabooRelationship(explain,taboo,i);
        return;
    }


    /**
     * increases all connection between source and target node
     * @param explain target
     * @param taboo source
     */
    public void validateExplainAndTabooForGiver(String explain,String taboo,int i){
        updateExplainTabooRelationship(explain,taboo,i);
        return;
    }

    public void setUserErrorTimeStamp(String user,Date d){
        StringBuilder builder = new StringBuilder();
        Instant k = d.toInstant();
        builder.append(d.getDate()).append(".").append(d.getHours()).append(".").append(d.getMinutes());
        try {
            updateUserPropertiesFromDatabase(user, "cheat_occurence",builder.toString());
        }catch(DatabaseException e){
            Log.info(e.getLocalizedMessage());
        }
    }

    public String[] getUserErrorTimeStamp(String user){
        try {
            String callback = fetchUserPropertiesFromDatabase(user,"cheat_occurence");
            String[] result = callback.split("\\.");
            return result;
        }catch(DatabaseException e){
            Log.info(e.getLocalizedMessage());
        }

        return null;
    }


    //** TODO: Missing Logging system for user's activity


    /**************************** INTERN CYPHER MANAGEMENT METHOD ****************************/

    /**
     * creates a Node in our ontology. Dependent on legacy or not.
     * @param nodeName
     * @return
     * @throws ServiceUnavailableException
     */
    public void createNode(String nodeName,Boolean explain) throws ServiceUnavailableException,DatabaseException{
        String type ;
        if(explain) type = "explain";
        else type = "basic";

        String nodeNameNoWhitespace = Util.reduceStringToMinimumWithoutWhitespaces(nodeName);
        nodeName = Util.reduceStringToMinimum(nodeName);

        try ( Session session = driver.session() )
        {

            try ( Transaction tx = session.beginTransaction() )
            {
                if(lookUpNode(nodeName,"Node")){
                    throw new DatabaseException("Node: " + nodeName + " is already in the database!");
                }
                tx.run( "CREATE (a: "+label+" {name: {name}, type: {type}, deletable:{deletable} })",
                        parameters( "name", nodeName,"type",type,"deletable",isDeletable ) );
                tx.success();
            }

        }

        Log.trace("Created Node: " + nodeName +" "+label);
        return;
    }

    /**
     * look up a node in our ontology
     * @param nodeName
     * @return
     */
    public boolean lookUpNode(String nodeName, String label){
        if(label.equals("Node"))
            nodeName = Util.reduceStringToMinimumWithoutWhitespaces(nodeName);

        StringBuilder builder = new StringBuilder();
        try ( Session session = driver.session() ) {
            try (Transaction tx = session.beginTransaction()) {

                //neglects whitespaces
                StatementResult result = tx.run("MATCH (n:"+label+") WHERE " +
                                "replace(n.name,\" \",\"\") = {name} " +
                                "RETURN n",
                        parameters("name", nodeName));
                if(!result.hasNext()) return false;
                while (result.hasNext()) {
                    Record record = result.next();
                    List<Value> val = record.values();
                    Value name = val.get(0).asNode().get("name");

                    val.get(0).asNode().labels().forEach(t -> {
                        builder.append("Found Node: " + String.format("%s %s ", name,
                                t.toString()));
                    });

                }

            }
        }
        Log.trace(builder.toString());

        return true;
    }

    //TODO
    public List<Node> getAllNodes(){
        List<Node> nodes = new ArrayList<>();
        try ( Session session = driver.session() ) {
            try (Transaction tx = session.beginTransaction()) {

                StatementResult result = tx.run("MATCH (n) RETURN n");
                while (result.hasNext()) {
                    Record record = result.next();
                    System.out.println(String.format("%s %s", record.get("title").asString(), record.get("name").asString()));
                    Object test = record.asMap();
                }
            }
        }

        return nodes;

    }

    /**
     * Creates a connection between two nodes while also incrementing the rating by 1
     * DONT USE THIS BESIDE IN TESTS
     * @param node1
     * @param node2
     * @param relationship
     * @return
     */
    private boolean createRelationship(String node1, String node2, String relationship, Boolean reliableFlag){


        node1 = Util.reduceStringToMinimum(node1);
        node2 = Util.reduceStringToMinimum(node2);
        relationship = Util.reduceStringToMinimum(relationship);
        try ( Session session = driver.session() )
        {

            try ( Transaction tx = session.beginTransaction() )
            {
                int count = 1;
                StatementResult result = tx.run( "MATCH (s)-[rel]->(t)" +
                        "WHERE s.name = {n1} AND t.name = {n2} AND type(rel) = {rel}" +
                        "" +
                        "RETURN rel",parameters("n1",node1,"n2",node2,"rel",relationship));
                if (result.hasNext()) {
                    Record record = result.next();
                    count = record.get("rel").asRelationship().get("rating").asInt() +1;
                    if(count >= 3) reliableFlag = true;
                    if(!record.get("rel").asRelationship().get("reliableFlag").asBoolean() &&
                            reliableFlag) {
                        tx.run("MATCH (s)-[rel]->(t)" +
                                "WHERE s.name = {n1} AND t.name = {n2}" +
                                "SET rel.rating = {c}" +
                                "SET rel.reliableFlag = {flag}" +
                                "RETURN rel", parameters("n1", node1, "n2", node2, "c", count,
                                "flag",reliableFlag));
                    }
                    else tx.run( "MATCH (s)-[rel]->(t)" +
                            "WHERE s.name = {n1} AND t.name = {n2}" +
                            "SET rel.rating = {c}" +
                            "RETURN rel",parameters("n1",node1,"n2",node2,"c",count));
                }
                else {
                    tx.run("MATCH (ee) WHERE ee.name =  \"" + node1 + "\" " +
                            "MATCH (js) WHERE js.name = \"" + node2 + "\" " +
                            "CREATE UNIQUE (ee)-[rel:`" + relationship + "` {rating: " + count + "," +
                            "deletable: " + isDeletable + ", reliableFlag: " + reliableFlag + "} " +
                            "]->(js)");
                }

                /*tx.run( "MATCH (ee) WHERE ee.name =  \""+node1+"\" "+
                                "MATCH (js) WHERE ee.name = \"" + node2 + "\"  " +
                        "CREATE (ee)-[rel:"+relationship+" {rating: "+ ++count +"," +
                                "legacy: "+!legacy+"} " +
                                "]->(js)"
                        , parameters( "name1",node1,"name2",node2 )); //*/

                tx.success();
                Log.trace("Created Relationship: "+node1 + " -> " + node2);
                return true;
            }

        }

    }

    /**
     * Creates a connection between two nodes while also incrementing the rating by 1
     * DONT USE THIS BESIDE IN TESTS
     * @param node1
     * @param node2
     * @param relationship
     * @return
     */
    private boolean createRelationshipCase(String node1, String node2, String relationship){


        try ( Session session = driver.session() )
        {

            try ( Transaction tx = session.beginTransaction() )
            {

                tx.run("MATCH (ee) WHERE ee.name =  \"" + node1 + "\" " +
                            "MATCH (js) WHERE js.name = \"" + node2 + "\" " +
                            "CREATE UNIQUE (ee)-[rel:`" + relationship + "` {"+
                            "deletable: " + isDeletable +"} " +
                            "]->(js)");
                tx.success();
                Log.trace("Created Relationship: "+node1 + " -> " + node2);
                return true;
            }

        }

    }

    /**
     * clear all connections that have too less of a rating
     * @return true if succesfully delete the relationship
     */
    public boolean clearFailedRelationships(){

        try ( Session session = driver.session() )
        {

            try ( Transaction tx = session.beginTransaction() )
            {
                tx.run( "MATCH (n)-[rel]->(r) \n" +
                                "WHERE rel.rating < "+DEL_TRESHHOLD+"\n" +
                                "DELETE rel");

                tx.success();
                return true;
            }

        }
    }

    /**
     * Resets all connections to the pre-existing legacy edges.
     * Anything besides of legacy = true, will be cleared
     * @return
     */
    public boolean resetRelationships(){

        try ( Session session = driver.session() )
        {

            try ( Transaction tx = session.beginTransaction() )
            {
                tx.run( "MATCH (n)-[rel]->(r) \n" +
                        "WHERE NOT EXISTS (rel.deletable) OR NOT (rel.deletable = false) " +
                        "DELETE rel");

                tx.success();
                return true;
            }
        }
    }

    /**
     * Does only delete normal nodes but not legacy nodes
     * @return suceed if nodes are deleted
     */
    public boolean resetDatabase(){
        resetRelationships();
        try ( Session session = driver.session() )
        {

            try ( Transaction tx = session.beginTransaction() )
            {
                tx.run("MATCH (n)\n" +
                        "DETACH DELETE n"
                ); // , parameters( "name", nodeName )

                tx.success();
            }

        }
        return true;
    }

    /**
     * Cypher request for all categories with high validation
     * @return
     */
    public Set<String> fetchFilteredCategoryFromDatabase(int cap){
        Set<String> result = new HashSet<>(cap);
        StringBuilder builder = new StringBuilder();

        try ( Session session = driver.session() )
        {
            try ( Transaction tx = session.beginTransaction() )
            {

                StatementResult sResult = tx.run(
                        "MATCH (s:Node) WHERE s.type = {cat} " +
                                "RETURN s",parameters("cat","category"));

                List<Record> list = sResult.list();
                list = list.stream().limit(cap).collect(Collectors.toList());

                builder.append("Fetched Categories: [");
                for(Record s: list){
                    String name = s.get("s").asNode().get("name").toString().replaceAll("\"", "");
                    result.add(name);
                    builder.append(name+", ");
                }
                builder.append("]");
                tx.success();
            }

        }

        Log.trace(builder.toString());
        return result ;
    }

    private String fetchUserPropertiesFromDatabase(String user,String property) throws DatabaseException{
        String result = "" ;
        StringBuilder builder = new StringBuilder();

        try ( Session session = driver.session() )
        {
            try ( Transaction tx = session.beginTransaction() )
            {
                StatementResult sResult = tx.run("MATCH (n:"+userLabel+") WHERE n.name = {name} " +
                                "RETURN n",
                        parameters("name",user));
                if(!sResult.hasNext()) throw new DatabaseException("No User "+user+" found!");
                while (sResult.hasNext()) {
                    Record record = sResult.next();
                    List<Value> val = record.values();
                    Value name = val.get(0).asNode().get(property);
                    result = name.toString();

                    builder.append("Fetched "+property+": " + String.format("%s %s ", user,
                            result));
                }
            }
        }
        Log.trace(builder.toString());
        return result ;
    }

    private LinkedHashMap<String,Integer> fetchUserWithHighestPoints(int cap, String channel){
        LinkedHashMap<String,Integer> ranking = new LinkedHashMap<>();
        StringBuilder builder = new StringBuilder();

        try ( Session session = driver.session() )
        {
            try ( Transaction tx = session.beginTransaction() )
            {
                StatementResult sResult = tx.run("MATCH (n:"+userLabel+")-[rel]->(t:streamNode)" +
                        "WHERE t.name = {channel}" +
                                "RETURN n " +
                                "ORDER BY n.points DESC " +
                                "LIMIT " + cap,parameters("channel",channel));
                while (sResult.hasNext()) {
                    Record record = sResult.next();
                    List<Value> val = record.values();
                    Value name = val.get(0).asNode().get("points");
                    int points = name.asInt();
                    name = val.get(0).asNode().get("name");
                    String user = name.toString().replaceAll("\"", "");
                    ranking.put(user,points);

                }
            }
        }
        Log.trace("Ranking: "+ ranking.toString());
        return ranking ;
    }

    private String fetchNodePropertiesFromDatabase(String nodeName,String property) throws DatabaseException{
        String result = "";
        StringBuilder builder = new StringBuilder();
        nodeName = Util.reduceStringToMinimum(nodeName);

        try ( Session session = driver.session() )
        {
            try ( Transaction tx = session.beginTransaction() )
            {
                StatementResult sResult = tx.run("MATCH (n:"+label+") WHERE n.name = {name} " +
                                "RETURN n",
                        parameters("name",nodeName));
                if(!sResult.hasNext()) throw new DatabaseException("No Node "+nodeName+" found!");
                while (sResult.hasNext()) {
                    Record record = sResult.next();
                    List<Value> val = record.values();
                    Value name = val.get(0).asNode().get(property);
                    result = name.toString();

                    builder.append("Fetched "+property+": " + String.format("%s %s ", nodeName,
                            result));
                }
            }
        }
        Log.trace(builder.toString());
        return result.replaceAll("\"", "");
    }

    private int updateUserPropertiesFromDatabase(String user,String property,int i) throws DatabaseException{
        String oldPoints = fetchUserPropertiesFromDatabase(user,property) ;
        int result =  i ;
        StringBuilder builder = new StringBuilder();

        try ( Session session = driver.session() )
        {
            try ( Transaction tx = session.beginTransaction() )
            {
                tx.run("MATCH (n:"+userLabel+") WHERE n.name = {name} " +
                                "SET n."+property+"={propertyvalue}" +
                                "",
                        parameters("name",user,"propertyvalue",result));
                tx.success();


                builder.append("Updated "+property+": " + String.format("%s -> %s %s", oldPoints,
                            result ,user));
            }
        }
        Log.trace(builder.toString());
        return result ;
    }

    private int updateUserPropertiesFromDatabaseAdd(String user,String property,int i) throws DatabaseException{
        //String oldValue = ""+fetchUserPropertiesFromDatabase(user,property) ;
        String oldValue = "not_specified";
        int result =  i ;
        StringBuilder builder = new StringBuilder();

        try ( Session session = driver.session() )
        {
            try ( Transaction tx = session.beginTransaction() )
            {
                tx.run("MATCH (n:"+userLabel+") WHERE n.name = {name} " +
                                "SET n."+property+"= n."+property+"+{propertyvalue}" +
                                "",
                        parameters("name",user,"propertyvalue",result));
                tx.success();


                builder.append("Updated "+property+": " + String.format("%s -> %s %s", oldValue,
                        result ,user));
            }
        }
        Log.trace(builder.toString());
        return result ;
    }

    private String updateUserPropertiesFromDatabase(String user,String property,String i) throws DatabaseException{
        //String oldValue = ""+fetchUserPropertiesFromDatabase(user,property) ;
        String oldValue = "not_specified";
        String result =  i ;
        StringBuilder builder = new StringBuilder();

        try ( Session session = driver.session() )
        {
            try ( Transaction tx = session.beginTransaction() )
            {
                tx.run("MATCH (n:"+userLabel+") WHERE n.name = {name} " +
                                "SET n."+property+"={propertyvalue}" +
                                "",
                        parameters("name",user,"propertyvalue",result));
                tx.success();


                builder.append("Updated "+property+": " + String.format("%s -> %s %s", oldValue,
                        result ,user));
            }
        }
        Log.trace(builder.toString());
        return result ;
    }

    private void generateUserNodeInDatabase(String user,String channel) throws DatabaseException{
        try ( Session session = driver.session() )
        {
            try ( Transaction tx = session.beginTransaction() )
            {
                if(lookUpNode(user,userLabel)){
                    throw new DatabaseException("User "+ user + " is already in the database!");
                }
                tx.run( "CREATE (a: "+userLabel+" {name: {name}," +
                                "points: 0,mistakes: 0,cheat_occurence:{v1},votekicked: 0 })",
                        parameters( "name", user,"points",0,"v1","none" ) );
                tx.success();
            }
        }
        return ;
    }

    private void generateNodeInDatabase(String name,String label) throws DatabaseException{
        try ( Session session = driver.session() )
        {
            try ( Transaction tx = session.beginTransaction() )
            {
                if(lookUpNode(name,label)){
                    throw new DatabaseException(label + " "+ name + " is already in the database!");
                }
                tx.run( "CREATE (a: "+label+" {name: {name}})",
                        parameters( "name", name ) );
                tx.success();
            }
        }
        return ;
    }

    /**
     * Take the best ten arc from an explain word to a category and choose arbitrarily
     * @param category
     * @param usedWords Words already skipped
     * @return
     */
    private String fetchConnectedWordFromDatabase(String category,Set<String> usedWords) throws DatabaseException{
        StringBuilder builder = new StringBuilder();
        String result = "";

        if(category.equals("simulation")){
            try ( Session session = driver.session() )
            {
                try ( Transaction tx = session.beginTransaction() )
                {

                    StatementResult sResult = tx.run( "MATCH (n:Node) RETURN n");

                    if(!sResult.hasNext())
                        throw new DatabaseException("No Explain Word available!");

                    for(Record r: sResult.list()){ //Iterate over all possible word connected to category
                        Node node = r.values().get(0).asNode();
                        String type = node.get("type").toString().replaceAll("\"", "");
                        String name = node.get("name").toString().replaceAll("\"", "");
                        if(!type.equals("basic") && !usedWords.contains(name)){ //if its an explain word and not in usedWord
                            if(result.equals("") || randomizer.nextBoolean()){
                                result = name ;
                            }
                        }
                    }

                    builder.append("Fetched ExplainWord: " + String.format("[%s]", result));
                    tx.success();
                }

            }
        }
        else{
            try ( Session session = driver.session() )
            {
                try ( Transaction tx = session.beginTransaction() )
                {

                    StatementResult sResult = tx.run( "MATCH (s:Node)-[rel]->(t:Node) WHERE t.name ={category} RETURN s",
                            parameters("category",category));

                    if(!sResult.hasNext())
                        throw new DatabaseException("No Explain Word available!");

                    builder.append("Fetched ExplainWord: [");

                    for(Record r: sResult.list()){ //Iterate over all possible word connected to category
                        Node node = r.values().get(0).asNode();
                        String type = node.get("type").toString().replaceAll("\"", "");
                        String name = node.get("name").toString().replaceAll("\"", "");
                        if(!type.equals("basic") && !usedWords.contains(name)){ //if its an explain word and not in usedWord
                            builder.append(","+name);
                            if(result.equals("") || randomizer.nextBoolean()){
                                result = name ;
                            }
                        }
                    }
                    builder.append("] -> " + result );

                    tx.success();
                }

            }
        }

        if(result.equals("")) throw new DatabaseException("No more explain words available!");
        Log.info(builder.toString());
        return result;
    }

    /**
     * Used for fetching taboo words
     * @param explainWord
     * @param count
     * @return
     */
    private Set<String> fetchConnectedWordsFromDatabase(String explainWord,int count){
        StringBuilder builder = new StringBuilder();
        Set<String> result = new HashSet<>(count);

            try ( Session session = driver.session() )
            {
                try ( Transaction tx = session.beginTransaction() )
                {

                    StatementResult sResult = tx.run(
                                    "MATCH (s)-[rel]->(t) WHERE t.name = {name} " +
                                    "AND rel.reliableFlag = true " +
                                    "RETURN rel,s"
                    ,parameters("name",explainWord));

                    List<Record> list = sResult.list();
                    list = list.stream().sorted((o2,o1) -> ((Integer)o1.get("rel").asRelationship().get("rating").asInt())
                    .compareTo((Integer)o2.get("rel").asRelationship().get("rating").asInt())).
                            limit(count).collect(Collectors.toList());


                    /*List<Value> val = record.values();
                    Value name = val.get(0).asNode().get("name");
                    result = name.toString();
                    result = result.replaceAll("\"", "");*/

                    builder.append("Fetched Taboo Words: [");
                    if(list.size() < 1) builder.append("EMPTY");
                    for(Record s: list){
                        String name = s.get("s").asNode().get("name").toString().replaceAll("\"", "") ;
                        result.add(name);
                        builder.append(name+", ");
                    }
                    builder.append("]");
                    tx.success();
                }

            }

        Log.info(builder.toString());
        return result;
    }

    private void updateExplainTabooRelationship(String node1,String node2,int i){
        node1 = Util.reduceStringToMinimum(node1);
        node2 = Util.reduceStringToMinimum(node2);

        StringBuilder builder = new StringBuilder();
        try ( Session session = driver.session() )
        {
            try ( Transaction tx = session.beginTransaction() )
            {
                int count = 1;
                StatementResult result = tx.run( "MATCH (s)-[rel]->(t)" +
                        "WHERE s.name = {n2} AND t.name = {n1}" +
                        "SET rel.rating = rel.rating+"+i+" " +
                        "RETURN rel.rating",parameters("n1",node1,"n2",node2));

                tx.success();
                builder
                        .append("Validated " + node2 + "->" + node1);
            }

        }

        Log.trace(builder.toString());
        return;

    }

    /**
     * We know that start node was an explain word and check if end word qualifies for category
     * @param nodeName
     */
    private void setExplainWordToCategory(String nodeName){
        nodeName = Util.reduceStringToMinimum(nodeName);

        StringBuilder builder = new StringBuilder();
        int count = 1 ;
            try ( Session session = driver.session() )
            {
                try ( Transaction tx = session.beginTransaction() )
                {

                    StatementResult sResult = tx.run( "MATCH (s)-[rel]->(t) WHERE t.name = {name} " +
                                    "AND rel.reliableFlag = true " +
                                    "RETURN s",
                            parameters("name",nodeName));

                    for(Record r: sResult.list()){ //Iterate over all possible word connected to category
                        Node node = r.get("s").asNode();
                        String type = node.get("type").toString().replaceAll("\"", "");
                        String name = node.get("name").toString().replaceAll("\"", "");
                        if(type.toString().equals("explain") ){ //if its an explain word
                            count++;
                        }
                    }
                    if(count >= CATEGORY_TRESHOLD){
                        tx.run("MATCH (s:Node)" +
                                "WHERE s.name = {n1}"+
                                "SET s.type = {c}" +
                                "RETURN s",parameters("n1",nodeName,"c","category"));

                        builder.append(nodeName + " did qualifiy as category with " + count + " incoming edges");
                    }
                    else builder.append(nodeName + " did not qualifiy as category with " + count + " incoming edges");
                    tx.success();
                }

            }

        Log.info(builder.toString());
    }

    /**
     * virtual method to set Category
     * @param nodeName
     */
    public void setCategory(String nodeName){
        nodeName = Util.reduceStringToMinimum(nodeName);
        StringBuilder builder = new StringBuilder();

        try ( Session session = driver.session() )
        {
            try ( Transaction tx = session.beginTransaction() )
            {

                StatementResult sResult =
                    tx.run("MATCH (s:Node)" +
                            "WHERE s.name = {n1}"+
                            "SET s.type = {c}" +
                            "RETURN s",parameters("n1",nodeName,"c","category"));

                builder.append("Set type of " +nodeName + " to Category");
                tx.success();
            }

        }
    }

    private Driver acquireDriver(String uri, AuthToken authToken, Config config)
    {

        try {
            return GraphDatabase.driver(uri, authToken, config);
        }
        catch (ServiceUnavailableException ex)
        {
            Log.debug("No valid database URI found");
            System.exit(0);
        }
        return null;

    }


}
