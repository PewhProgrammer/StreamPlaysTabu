package common.database;

import common.*;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.neo4j.driver.v1.Values.parameters;

import org.neo4j.driver.v1.types.Node;

/**
 * Created by Thinh-Laptop on 08.04.2017.
 */
public class Neo4jWrapper {
    private final Config config;
    private final int DEL_TRESHHOLD = 2; //
    private final int CATEGORY_TRESHOLD = 3; //word qualifies as category if more equal than 3 incoming arcs are available
    private final int VALIDATE_THRESHOLD = 8 ; //only need a validation score of eight
    private final String label;
    private final Driver driver;
    private final Random randomizer;
    Session session;
    private boolean needValidation;
    private StringBuilder neo4jbindAddr = new StringBuilder();
    private String userLabel = "userNode";

    public Neo4jWrapper(boolean simulation, String neo4jbind, int seed) {
        config = Config.build().withEncryptionLevel(Config.EncryptionLevel.REQUIRED).toConfig();//baut enkryptische Verbindung, um uns gegen "man-in-the-middle" attacken zu schützen
        neo4jbindAddr.append(neo4jbind);
        randomizer = new Random(seed);
        randomizer.setSeed(seed);
        driver = acquireDriver("bolt://" + neo4jbindAddr,
                AuthTokens.basic("neo4j", "streamplaystabu"), config);
        this.session = driver.session();

        //non-legacy nodes are used experimentally
        label = "Node";
        userLabel = "userNode";
        needValidation = !simulation;
    }

    /**
     * This method is mainly called to build our ontology
     * FROM node1 TO node2 : node1 -rel-> node2
     *
     * @param node1
     * @param node2
     * @param relationship
     * @return
     */
    public boolean insertNodesAndRelationshipIntoOntology(String node1, String node2, Boolean node2Explain, String relationship, Boolean reliableFlag) {
        boolean isNode1Explain = false;

        try {
            createNode(node1, isNode1Explain);
        } catch (ServiceUnavailableException | common.database.DatabaseException e) {
            Log.debug(e.getLocalizedMessage());
        }

        String type = "";
        try {
            type = fetchNodePropertiesFromDatabase(node1, "type");
        } catch (common.database.DatabaseException i) {
            Log.debug(i.getLocalizedMessage());
        }

        if (type.equals("explain")) isNode1Explain = true;

        try {
            createNode(node2, node2Explain);
        } catch (ServiceUnavailableException | common.database.DatabaseException e) {
            //If the start node is from type explain, update end node to potential category
            Log.debug(e.getLocalizedMessage());
        }
        if (isNode1Explain && node2Explain) setExplainWordToCategory(node2);
        return createRelationship(node1, node2, relationship, reliableFlag);
    }

    public void createUser(String str, String ch) {
        if (str.equals("")) {
            Thread.dumpStack();
            return;
        }
        try {
            generateUserNodeInDatabase(str);
            Log.db("Created Node: \"" + str + "\" userNode");
        } catch (common.database.DatabaseException e) {
            Log.error(e.getMessage());
        }

        createStreamNode(ch);
        createRelationshipStreamer(str, ch, "plays in");
    }

    private void createStreamNode(String ch) {
        try {
            generateNodeInDatabase(new customNode(ch, "streamNode", true, ch));
            Log.db("Created streamNode: \"" + ch + "\"");
        } catch (common.database.DatabaseException e) {
            Log.db(e.getMessage());
        }
    }

    public Set<String> getTabooWords(String explain, String forbiddenWord, int querySize) {
        Set<String> result = fetchTabooWords(Util.reduceStringToMinimum(explain), forbiddenWord, querySize);
        if (result.size() < querySize)
            Log.db("Could not retrieve enough taboo words. Missing -> Expected " + querySize + "; Actual " + result.size());
        return result;
    }

    /**
     * FORCES TABOO WORDS
     *
     * @param explain   explain word to retrieve taboo words from
     * @param querySize describes how many taboo words to retrieve
     * @return
     */
    public HashMap<String, Set<String>> getTabooWordsForValidation(String explain, int querySize) {
        String category = "none";
        HashMap<String, Set<String>> result = new HashMap<>();
        if (explain == null) explain = getRandomExplainWord(querySize);

        Set<String> taboo = fetchTabooWords(Util.reduceStringToMinimum(explain), category, querySize);
        if (taboo.size() > 0) {
            result.put(explain, taboo);
        } else {
            explain = getRandomExplainWord(querySize);
            taboo = fetchTabooWords(Util.reduceStringToMinimum(explain), category, querySize);
        }
        return result;
    }

    private String getRandomExplainWord(int querySize) {
        String explain = "";
        Set<String> cat = fetchFilteredCategoryFromDatabase(querySize);
        for (String str : cat) {
            if (randomizer.nextBoolean() || explain.equals("")) {
                explain = str;
            }
        }
        return explain;
    }

    public ArrayList<Pair> getValidationForGiver() {
        ArrayList<Pair> result = new ArrayList<>();
        //get explain
        result.add(new Pair(getExplainForValidation(), ""));
        //get taboo - explain
        result.add(getTabooExplainForValidation());
        //get explain - category
        result.add(getExplainCategoryForValidation());

        Log.db("Returned Validation For Giver: \n"
                + result.get(0) + ", " + result.get(1) + ", " + result.get(2));
        return result;
    }

    public String getExplainForValidation() {
        String result;
        LinkedList<String> results = new LinkedList<>();
        StringBuilder query = new StringBuilder();
        query.append("MATCH (s:Node) ")
                .append("WHERE s.validationLock <> true AND s.needValidation = true AND s.type = 'explain' ")
                .append("RETURN s");
        Transaction transX = session.beginTransaction();
        try {
            StatementResult sResult = transX.run(query.toString());
            while (sResult.hasNext()) {
                result = sResult.next().values().get(0).
                        asNode().get("name").toString().replaceAll("\"", "");
                results.add(result);
            }
            transX.success();
        } finally {
            transX.close();
        }

        Collections.shuffle(results, randomizer);
        try {
            return results.getFirst();
        } catch (NoSuchElementException e) {
            return "EMPTY";
        }
    }

    public Pair getTabooExplainForValidation() {
        LinkedList<Pair> results = new LinkedList<>();
        StringBuilder query = new StringBuilder();
        query.append("MATCH (s:Node)-[rel]->(t:Node) ")
                .append("WHERE s.needValidation = false AND s.type = 'explain' ")
                .append("AND rel.needValidationTaboo = true AND rel.validationLock <> true ")
                .append("RETURN s,t");
        Transaction transX = session.beginTransaction();
        try {
            StatementResult sResult = transX.run(query.toString());
            while (sResult.hasNext()) {
                Record records = sResult.next();
                String explain = records.values().get(0).
                        asNode().get("name").toString().replaceAll("\"", "");
                String taboo = records.values().get(1).
                        asNode().get("name").toString().replaceAll("\"", "");
                results.add(new Pair(explain, taboo));
            }
            transX.success();
        } finally {
            transX.close();
        }

        Collections.shuffle(results, randomizer);
        try {
            return results.getFirst();
        } catch (NoSuchElementException e) {
            return new Pair("EMPTY", "EMPTY");
        }
    }

    public Pair getExplainCategoryForValidation() {
        LinkedList<Pair> results = new LinkedList<>();
        StringBuilder query = new StringBuilder();
        query.append("MATCH (s:Node)-[rel]->(t:Node) ")
                .append("WHERE s.needValidation = false AND s.type = 'explain' ")
                .append("AND t.type <> 'basic' ")
                .append("AND rel.needValidationCategory = true AND rel.validationLock <> true ")
                .append("RETURN s,t");
        Transaction transX = session.beginTransaction();
        try {
            StatementResult sResult = transX.run(query.toString());
            while (sResult.hasNext()) {
                Record records = sResult.next();
                String explain = records.values().get(0).
                        asNode().get("name").toString().replaceAll("\"", "");
                String taboo = records.values().get(1).
                        asNode().get("name").toString().replaceAll("\"", "");
                results.add(new Pair(explain, taboo));
            }
            transX.success();
        } finally {
            transX.close();
        }

        Collections.shuffle(results, randomizer);
        try {
            return results.getFirst();
        } catch (NoSuchElementException e) {
            return new Pair("EMPTY", "EMPTY");
        }
    }

    /**
     * increases all connection between source and target node
     *
     * @param source target
     * @param target   source
     */
    public void validateConnectionTaboo(String source, String target, int i) {
        source = Util.reduceStringToMinimum(source);
        target = Util.reduceStringToMinimum(target);
        StringBuilder query = new StringBuilder();
        query.append("MATCH (s)-[rel]->(t) ")
                .append("WHERE s.name = {n1} AND t.name = {n2} ")
                .append("SET rel.validateRatingTaboo = rel.validateRatingTaboo+").append(i).append(" ")
                .append(", rel.validateFrequencyTaboo = rel.validateFrequencyTaboo+").append(1).append(" ")
                .append("WITH rel, ")
                .append("(CASE WHEN rel.validateRatingTaboo > 8 THEN false ELSE " + needValidation + " END) AS flag ")
                .append("SET rel.needValidationTaboo = flag");

        Transaction transX = session.beginTransaction();
        try { transX.run(query.toString(),
                    parameters("n1", source, "n2", target));
            transX.success();
            Log.db("VALIDATED taboo connection " + source + "-> "+ target +" with " + i + " score");
        } finally { transX.close(); }
    }

    public void validateConnectionCategory(String source, String target, int i) {
        source = Util.reduceStringToMinimum(source);
        target = Util.reduceStringToMinimum(target);
        StringBuilder query = new StringBuilder();
        query.append("MATCH (s)-[rel]->(t) ")
                .append("WHERE s.name = {n1} AND t.name = {n2} ")
                .append("SET rel.validateRatingCategory = rel.validateRatingCategory+").append(i).append(" ")
                .append(", rel.validateFrequencyCategory = rel.validateFrequencyCategory+").append(1).append(" ")
                .append("WITH rel, ")
                .append("(CASE WHEN rel.validateRatingCategory > 8 THEN false ELSE " + needValidation + " END) AS flag ")
                .append("SET rel.needValidationCategory = flag");

        Transaction transX = session.beginTransaction();
        try { transX.run(query.toString(),
                parameters("n1", source, "n2", target));
            transX.success();
            Log.db("VALIDATED category connection " + source + "-> "+ target +" with " + i + " score");
        } finally { transX.close(); }
    }

    public void validateNode(String source, int i) {
        source = Util.reduceStringToMinimum(source);
        StringBuilder query = new StringBuilder();
        query.append("MATCH (s) ")
                .append("WHERE s.name = {n1} ")
                .append("SET s.validateRating = s.validateRating+").append(i).append(" ")
                .append(", s.validateFrequency = s.validateFrequency+").append(1).append(" ")
                .append("WITH s, ")
                .append("(CASE WHEN s.validateRating > 8 THEN false ELSE " + needValidation + " END) AS flag ")
                .append("SET s.needValidation = flag");

        Transaction transX = session.beginTransaction();
        try { transX.run(query.toString(),
                    parameters("n1", source));
            transX.success();
            Log.db("VALIDATED " + source + " with " + i + " score");
        } finally { transX.close(); }
    }

    public void setUserErrorTimeStamp(String user, Date d) {
        StringBuilder builder = new StringBuilder();
        Instant k = d.toInstant();
        String[] parts = k.toString().split("\\-");
        builder.append(parts[0]).append(".").append(d.getDate()).append(".").append(d.getHours()).append(".").append(d.getMinutes());
        try {
            updateUserProperties(user, "cheat_occurence", builder.toString());
        } catch (common.database.DatabaseException e) {
            Log.error(e.getLocalizedMessage());
        }
    }

    public String[] getUserErrorTimeStamp(String user) {
        try {
            String callback = fetchUserPropertiesFromDatabase(user, "cheat_occurence");
            String[] result = callback.split("\\.");
            return result;
        } catch (common.database.DatabaseException e) {
            Log.error(e.getLocalizedMessage());
        }
        return null;
    }


    public LinkedHashMap<String, Integer> getHighScoreList(int querySize, String ch) {
        return fetchUserWithHighestPoints(querySize, ch);
    }

    public LinkedList<StreamerHighscore> getStreamHighScore() {
        return fetchStreamWithHighestScores();
    }

    public String updateUserVoteKicked(String user, String ch) {
        try {
            return "" + updateUserProperties(user, "votekicked", "1");
        } catch (common.database.DatabaseException e) {
            Log.debug(e.getMessage());
            createUser(user, ch);
        }
        return "0"; //default value
    }

    public int getUserPoints(String user, String ch) throws Neo4jException {
        try {
            return Integer.parseInt(fetchRelationshipProperties(user, "points", ch));
        } catch (common.database.DatabaseException e) {
            Log.debug(e.getMessage());
            createUser(user, ch);
        }

        return 0; //default value
    }

    public int updateUserPoints(String user, int i, String ch) {
        try {
            return Integer.parseInt(updateRelationshipProperties(new customRelationship(user, ch, "points", "" + i)));
        } catch (common.database.DatabaseException e) {
            Log.debug(e.getMessage());
            createUser(user, ch);
        }
        return 0; //default value
    }

    /**
     * Increase the error any user made by one
     *
     * @param user
     * @return updated count of mistakes the user now have
     * @throws Neo4jException
     */
    public int increaseUserError(String user, String ch) throws Neo4jException {
        try {
            return Integer.parseInt(updateUserProperties(user, "mistakes", "" + 1));
        } catch (common.database.DatabaseException e) {
            Log.debug(e.getMessage());
            createUser(user, ch);
        }
        return 0; //default value
    }

    public int getUserError(String user, String ch) throws Neo4jException {
        try {
            return Integer.parseInt(fetchUserPropertiesFromDatabase(user, "mistakes"));
        } catch (common.database.DatabaseException e) {
            Log.debug(e.getMessage());
            createUser(user, ch);
        }

        return 0; //default value
    }

    /**
     * retrieves categories for pre-voting
     *
     * @param querySize capacity of categories
     * @return
     */
    public Set<String> getCategories(int querySize) {
        return fetchFilteredCategoryFromDatabase(querySize);
    }

    public String getExplainWord(String category, Set<String> skippedWords) throws common.database.DatabaseException {
        return fetchConnectedWordFromDatabase(category, skippedWords);
    }

    //** TODO: Missing Logging system for user's activity


    /**************************** INTERN CYPHER MANAGEMENT METHOD ****************************/


    /**
     * creates a Node in our ontology. Dependent on legacy or not.
     *
     * @param nodeName
     * @return
     * @throws ServiceUnavailableException
     */
    public void createNode(String nodeName, Boolean explain) throws ServiceUnavailableException, common.database.DatabaseException {
        if (nodeName.equals("")) throw new common.database.DatabaseException("Node does not have a string literal!");
        String type = explain ? "explain" : "basic";
        boolean validate = false;
        if (type.equals("explain") && needValidation) validate = true;
        nodeName = Util.reduceStringToMinimum(nodeName);

        if (lookUpNode(nodeName, "Node", "")) {
            throw new common.database.DatabaseException("Node: " + nodeName + " is already in the database!");
        }
        Transaction transX = session.beginTransaction();
        try {
            transX.run("CREATE (a: " + label + " {name: {name}," +
                            " type: {type}, needValidation: {validateBoolean} , validateRating: 0, validationLock: "
                    +!needValidation+" })",
                    parameters("name", nodeName, "type", type, "validateBoolean", validate));
            transX.success();
        } finally {
            transX.close();
        }
        Log.db("Created Node: " + nodeName + " " + label);
        return;
    }

    public boolean lookUpNode(String nodeName, String label, String ch) {
        nodeName = label.equals("Node") ? Util.reduceStringToMinimumWithoutWhitespaces(nodeName) : nodeName;
        StringBuilder builder = new StringBuilder();
        StringBuilder query = new StringBuilder();
        query.append("MATCH (s:" + label + ") WHERE " +
                "replace(s.name,\" \",\"\") = {name}");
        if (!ch.equals("")) {
            //query.append("AND t.name = {channel}");
        }
        query.append(" RETURN s");

        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult result = tx.run(query.toString(), parameters("name", nodeName));
                if (!result.hasNext()) return false;
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

        Log.db(builder.toString());
        return true;
    }

    /**
     * Creates a connection between two nodes while also incrementing the rating by 1
     *
     * @param node1
     * @param node2
     * @param relationship
     * @return
     */
    private boolean createRelationship(String node1, String node2, String relationship, Boolean reliableFlag) {
        node1 = Util.reduceStringToMinimum(node1);
        node2 = Util.reduceStringToMinimum(node2);
        relationship = Util.reduceStringToMinimum(relationship);

        StringBuilder query = new StringBuilder();
        int i = 0;
        query.append("MATCH (s),(t) WHERE s.name = {n1} AND t.name = {n2} ")
                .append("MERGE (s)-[rel:`" + relationship + "`]->(t) ")
                .append("ON MATCH SET rel.frequency = rel.frequency + 1 ")
                .append("ON CREATE SET rel.frequency = 0 ")
                .append(", rel.validateRatingTaboo = 0 ")
                .append(", rel.validateFrequencyTaboo = 0 ")
                .append(", rel.validateRatingCategory = 0 ")
                .append(", rel.validateFrequencyCategory = 0 ")
                .append(", rel.needValidationCategory = " + needValidation + " ")
                .append(", rel.validationLock = " + !needValidation + " ")
                .append("WITH rel," +
                        "(CASE WHEN rel.frequency > 1 THEN false ELSE " + needValidation + " END) AS flag ")
                .append("SET rel.needValidationTaboo = flag ");

        Transaction transX = session.beginTransaction();
        try { transX.run(query.toString(),
                    parameters("n1", node1, "n2", node2, "rel", relationship));
            transX.success();
        } finally { transX.close(); }

        Log.db("Created Relationship: " + node1 + " -> " + node2);
        return true;
    }

    /**
     * redundant method considering case sensitive between streamer and users.
     *
     * @param node1
     * @param node2
     * @param relationship
     * @return
     */
    private boolean createRelationshipStreamer(String node1, String node2, String relationship) {
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                tx.run("MATCH (ee:userNode) WHERE ee.name =  \"" + node1 + "\" " +
                        "MATCH (js:streamNode) WHERE js.name = \"" + node2 + "\" " +
                        "CREATE UNIQUE (ee)-[rel:`" + relationship + "` {" +
                        "points: 0} " +
                        "]->(js)");
                tx.success();
            }
        }
        Log.db("Created Relationship: " + node1 + " -> " + node2);
        return true;
    }

    /**
     * Resets all connections to the pre-existing legacy edges.
     * Anything besides of legacy = true, will be cleared
     *
     * @return
     */
    public boolean resetRelationships() {

        try (Session session = driver.session()) {

            try (Transaction tx = session.beginTransaction()) {
                tx.run("MATCH (n)-[rel]->(r) \n" +
                        "WHERE NOT EXISTS (rel.deletable) OR NOT (rel.deletable = false) " +
                        "DELETE rel");

                tx.success();
                return true;
            }
        }
    }

    /**
     * Does everything
     *
     * @return suceed if nodes are deleted
     */
    public boolean resetDatabase() {
        resetRelationships();
        try (Session session = driver.session()) {

            try (Transaction tx = session.beginTransaction()) {
                tx.run("MATCH (n)\n" +
                        "DETACH DELETE n"
                ); // , parameters( "name", nodeName )

                tx.success();
            }

        }
        return true;
    }

    public void createQuestion(String src, String relation, String target){

        src = Util.reduceStringToMinimum(src);
        target = Util.reduceStringToMinimum(target);
        relation = Util.reduceStringToMinimum(relation);

        StringBuilder query = new StringBuilder();
        query.append("MATCH (s) WHERE s.name = {n1} ")
                .append("MERGE (s)-[rel:`" + relation + "`]->(t{name:{n2}}) ")
                .append("ON MATCH SET rel.frequency = rel.frequency + 1 ")
                .append("ON CREATE ")
                .append("SET rel.frequency = 0")
                .append(", t: Logging");

        Transaction transX = session.beginTransaction();
        try { transX.run(query.toString(),
                parameters("n1", src, "n2", target, "rel", relation));
            transX.success();
        } finally { transX.close(); }

        Log.db("Created Relationship: " + src + " -> " + target);
    }

    /**
     * Cypher request for all categories with high validation
     *
     * @return
     */
    public Set<String> fetchFilteredCategoryFromDatabase(int cap) {
        Set<String> result = new HashSet<>(cap);
        StringBuilder builder = new StringBuilder();

        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {

                StatementResult sResult = tx.run(
                        "MATCH (s:Node) WHERE s.type = {cat} " +
                                "RETURN s", parameters("cat", "category"));

                List<Record> list = sResult.list();
                list = list.stream().limit(cap).collect(Collectors.toList());

                builder.append("Fetched Categories: [");
                for (Record s : list) {
                    String name = s.get("s").asNode().get("name").toString().replaceAll("\"", "");
                    result.add(name);
                    builder.append(name + ", ");
                }
                builder.append("]");
                tx.success();
            }

        }

        Log.db(builder.toString());
        return result;
    }

    private String fetchUserPropertiesFromDatabase(String user, String property) throws common.database.DatabaseException {
        String result = "";
        StringBuilder builder = new StringBuilder();

        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult sResult = tx.run("MATCH (n:" + userLabel + ") WHERE n.name = {name} " +
                                "RETURN n",
                        parameters("name", user));
                if (!sResult.hasNext()) throw new common.database.DatabaseException("No User " + user + " found!");
                while (sResult.hasNext()) {
                    Record record = sResult.next();
                    List<Value> val = record.values();
                    Value name = val.get(0).asNode().get(property);
                    result = name.toString().replaceAll("\"", "");

                    builder.append("Fetched " + property + ": " + String.format("%s %s ", user,
                            result));
                }
            }
        }
        Log.db(builder.toString());
        return result;
    }

    private String fetchRelationshipProperties(String user, String property, String ch) throws common.database.DatabaseException {
        String result = "";
        StringBuilder builder = new StringBuilder();

        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult sResult = tx.run("MATCH (n:" + userLabel + ")-[rel]->(t) WHERE n.name = {name}" +
                                "AND t.name = {channel} " +
                                "RETURN rel",
                        parameters("name", user, "channel", ch));
                if (!sResult.hasNext())
                    throw new common.database.DatabaseException("No Relationship " + user + " found!");
                while (sResult.hasNext()) {
                    Record record = sResult.next();
                    List<Value> val = record.values();
                    Value name = val.get(0).asRelationship().get(property);
                    result = name.toString().replaceAll("\"", "");

                    builder.append("Fetched " + property + ": " + String.format("%s %s ", user,
                            result));
                }
            }
        }
        Log.db(builder.toString());
        return result;
    }

    private LinkedHashMap<String, Integer> fetchUserWithHighestPoints(int cap, String channel) {
        LinkedHashMap<String, Integer> ranking = new LinkedHashMap<>();
        StringBuilder builder = new StringBuilder();

        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult sResult = tx.run("MATCH (n:" + userLabel + ")-[rel]->(t:streamNode)" +
                        "WHERE t.name = {channel}" +
                        "RETURN rel,n " +
                        "ORDER BY rel.points DESC " +
                        "LIMIT " + cap, parameters("channel", channel));
                while (sResult.hasNext()) {
                    Record record = sResult.next();
                    List<Value> val = record.values();
                    Value name = val.get(0).asRelationship().get("points");
                    int points = Integer.parseInt(name.toString().replaceAll("\"", ""));
                    name = val.get(1).asNode().get("name");
                    String user = name.toString().replaceAll("\"", "");
                    ranking.put(user, points);

                }
            }
        }
        Log.db("Ranking: " + ranking.toString());
        return ranking;
    }

    private LinkedList<StreamerHighscore> fetchStreamWithHighestScores() {
        LinkedList<StreamerHighscore> resultEnd = new LinkedList<>();

        StringBuilder builder = new StringBuilder();

        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {

                StatementResult sResultTNode = tx.run("MATCH (t:streamNode)" +
                        "RETURN t ORDER BY t.totalPoints DESC");

                while (sResultTNode.hasNext()) {
                    StreamerHighscore result = new StreamerHighscore();
                    Record recordTNode = sResultTNode.next();
                    List<Value> valTNode = recordTNode.values();
                    int totalPoints = valTNode.get(0).asNode().get("totalPoints").asInt();
                    String streamName = valTNode.get(0).asNode().get("name").toString().replaceAll("\"", "");
                    result.setStream(streamName);
                    result.setStreamPoints(totalPoints);

                    StatementResult sResult = tx.run("MATCH (n:" + userLabel + ")-[rel]->(t:streamNode)" +
                            "WHERE t.name = {streamName}" +
                            "RETURN n,rel " +
                            "ORDER BY t.totalPoints DESC", parameters("streamName", streamName));

                    while (sResult.hasNext()) {
                        Record record = sResult.next();
                        List<Value> val = record.values();
                        String userName = val.get(0).asNode().get("name").toString().replaceAll("\"", "");
                        int userPoints = Integer.parseInt(val.get(1).asRelationship().get("points").toString().replaceAll("\"", ""));
                        result.addUserPointsPair(new Pair(userName, userPoints));
                    }

                    resultEnd.push(result);
                }

            }
        }
        return resultEnd;
    }

    private String fetchNodePropertiesFromDatabase(String nodeName, String property) throws common.database.DatabaseException {
        String result = "";
        StringBuilder builder = new StringBuilder();
        nodeName = Util.reduceStringToMinimum(nodeName);

        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult sResult = tx.run("MATCH (n:" + label + ") WHERE n.name = {name} " +
                                "RETURN n",
                        parameters("name", nodeName));
                if (!sResult.hasNext()) throw new common.database.DatabaseException("No Node " + nodeName + " found!");
                while (sResult.hasNext()) {
                    Record record = sResult.next();
                    List<Value> val = record.values();
                    Value name = val.get(0).asNode().get(property);
                    result = name.toString();

                    builder.append("Fetched " + property + ": " + String.format("%s %s ", nodeName,
                            result));
                }
            }
        }
        Log.db(builder.toString());
        return result.replaceAll("\"", "");
    }

    private String updateRelationshipProperties(customRelationship rel) throws common.database.DatabaseException {
        String oldPoints = rel.property.equals("points") ? "new points" : "not_specified";
        String result = rel.value;
        int resultInt = 0;
        int diff = 0;
        StringBuilder builder = new StringBuilder();
        StringBuilder query = new StringBuilder();
        query.append("MATCH (n:" + userLabel + ")-[rel]->(t) WHERE n.name = {name} AND t.name = {target} ");
        if (rel.property.equals("points")) {
            int oldvalue = getUserPoints(rel.source, rel.target);
            resultInt = Integer.parseInt(result);
            diff = resultInt - oldvalue;
            query.append("SET rel." + rel.property + "={propertyvalueInt} ");
            query.append(", t.totalPoints = t.totalPoints+{diffInt} ");
        } else {
            query.append("SET rel." + rel.property + "={propertyvalue} ");
        }
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                tx.run(query.toString(),
                        parameters("name", rel.source, "propertyvalue", result, "propertyvalueInt", resultInt, "target", rel.target, "diffInt", diff));
                tx.success();
                builder.append("Updated " + rel.property + ": " + String.format("%s -> %s %s", oldPoints,
                        result, rel.source));
            }
        }
        Log.db(builder.toString());
        return result;
    }

    private String updateUserProperties(String user, String property, String i) throws common.database.DatabaseException {
        String oldPoints = property.equals("points") ? fetchUserPropertiesFromDatabase(user, property) : "not_specified";
        String result = i;
        StringBuilder builder = new StringBuilder();

        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                tx.run("MATCH (n:" + userLabel + ") WHERE n.name = {name} " +
                                "SET n." + property + "={propertyvalue} ",
                        parameters("name", user, "propertyvalue", result));
                tx.success();


                builder.append("Updated " + property + ": " + String.format("%s -> %s %s", oldPoints,
                        result, user));
            }
        }
        Log.db(builder.toString());
        return result;
    }

    private void generateUserNodeInDatabase(String user) throws common.database.DatabaseException {
        StringBuilder query = new StringBuilder();
        query
                .append("MERGE (s: "+ userLabel+ " {name: {name}}) ")
                .append("ON CREATE ")
                .append("SET s.name = {name} ")
                .append(", s.mistakes = 0 ")
                .append(", s.cheatFrequency = 0 ")
                .append(", s.voteKicked = 0 ");

        Transaction transX = session.beginTransaction();
        try {
            transX.run(query.toString(),
                    parameters("name", user, "points", 0, "v1", "none"));
            transX.success();
        } finally { transX.close(); }
    }

    private void generateNodeInDatabase(customNode data) throws common.database.DatabaseException {
        String property = data.isStream ? ",totalPoints: 0" : "";
        String stream = data.isStream ? data.ch : "";

        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                if (lookUpNode(data.name, data.label, stream)) {
                    throw new common.database.DatabaseException(label + " " + data.name + " is already in the database!");
                }
                tx.run("CREATE (a: " + data.label + " {name: {name}" + property + " })",
                        parameters("name", data.name));
                tx.success();
            }
        }
        return;
    }

    /**
     * Take the best ten arc from an explain word to a category and choose arbitrarily
     *
     * @param category
     * @param usedWords Words already skipped
     * @return
     */
    private String fetchConnectedWordFromDatabase(String category, Set<String> usedWords) throws common.database.DatabaseException {
        StringBuilder builder = new StringBuilder();
        String result = "";

        if (category.equals("simulation")) {
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {

                    StatementResult sResult = tx.run("MATCH (n:Node) RETURN n");

                    if (!sResult.hasNext())
                        throw new common.database.DatabaseException("No Explain Word available!");

                    for (Record r : sResult.list()) { //Iterate over all possible word connected to category
                        Node node = r.values().get(0).asNode();
                        String type = node.get("type").toString().replaceAll("\"", "");
                        String name = node.get("name").toString().replaceAll("\"", "");
                        if (!type.equals("basic") && !usedWords.contains(name)) { //if its an explain word and not in usedWord
                            if (result.equals("") || randomizer.nextBoolean()) {
                                result = name;
                            }
                        }
                    }

                    builder.append("Fetched ExplainWord: " + String.format("[%s]", result));
                    tx.success();
                }

            }
        } else {
            try (Session session = driver.session()) {
                try (Transaction tx = session.beginTransaction()) {

                    StatementResult sResult = tx.run("MATCH (s:Node)-[rel]->(t:Node) WHERE t.name ={category} RETURN s",
                            parameters("category", category));

                    if (!sResult.hasNext())
                        throw new common.database.DatabaseException("No Explain Word available!");

                    builder.append("Fetched ExplainWord: [");

                    for (Record r : sResult.list()) { //Iterate over all possible word connected to category
                        Node node = r.values().get(0).asNode();
                        String type = node.get("type").toString().replaceAll("\"", "");
                        String name = node.get("name").toString().replaceAll("\"", "");
                        if (!type.equals("basic") && !usedWords.contains(name)) { //if its an explain word and not in usedWord
                            builder.append("," + name);
                            if (result.equals("") || randomizer.nextBoolean()) {
                                result = name;
                            }
                        }
                    }
                    builder.append("] -> " + result);

                    tx.success();
                }

            }
        }

        if (result.equals("")) throw new common.database.DatabaseException("No more explain words available!");
        Log.info(builder.toString());
        return result;
    }

    /**
     * Used for fetching taboo words
     *
     * @param explainWord
     * @param count
     * @return
     */
    private Set<String> fetchTabooWords(String explainWord, String forbiddenWord, int count) {
        StringBuilder builder = new StringBuilder();
        Set<String> result = new HashSet<>(count);

        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                StatementResult sResult = tx.run(
                        "MATCH (s)-[rel]->(t) WHERE t.name = {name} " +
                                "AND rel.needValidationTaboo = false RETURN rel,s", parameters("name", explainWord));
                List<Record> list = sResult.list();
                list = list.stream().sorted(
                        (o2, o1) -> ((Integer) (o1.get("rel").asRelationship().get("validateRatingTaboo").asInt()
                         + (Integer) o1.get("rel").asRelationship().get("frequency").asInt() ) )
                        .compareTo((Integer) o2.get("rel").asRelationship().get("validateRatingTaboo").asInt()
                        + (Integer) o2.get("rel").asRelationship().get("frequency").asInt()
                        )).limit(count).collect(Collectors.toList());

                builder.append("Fetched Taboo Words: [");
                if (list.size() < 1) builder.append("EMPTY");
                for (Record s : list) {
                    String name = s.get("s").asNode().get("name").toString().replaceAll("\"", "");
                    if (!name.equals(forbiddenWord)) {
                        result.add(name);
                        builder.append(name + ", ");
                    }
                }
                builder.append("]");
                tx.success();
            }
        }

        Log.info(builder.toString());
        return result;
    }

    /**
     * We know that start node was an explain word
     * and check if relationship suits for a category
     *
     * @param nodeName
     */
    private void setExplainWordToCategory(String nodeName) {
        nodeName = Util.reduceStringToMinimum(nodeName);

        StringBuilder builder = new StringBuilder();
        int count = 1;
            try (Transaction tx = session.beginTransaction()) {

                StatementResult sResult = tx.run("MATCH (s)-[rel]->(t) WHERE t.name = {name} " +
                                "AND rel.needValidationCategory = false " +
                                "RETURN s",
                        parameters("name", nodeName));

                for (Record r : sResult.list()) { //Iterate over all possible word connected to category
                    Node node = r.get("s").asNode();
                    String type = node.get("type").toString().replaceAll("\"", "");
                    String name = node.get("name").toString().replaceAll("\"", "");
                    if (type.toString().equals("explain")) { //if its an explain word
                        count++;
                    }
                }
                if (count >= CATEGORY_TRESHOLD) {
                    tx.run("MATCH (s:Node)" +
                            "WHERE s.name = {n1} " +
                            "AND s.type = {c}" +
                            "RETURN s", parameters("n1", nodeName, "c", "category"));

                    builder.append(nodeName + " did qualifiy as category with " + count + " incoming edges");
                } else builder.append(nodeName + " did not qualifiy as category with " + count + " incoming edges");
                tx.success();
            }
        Log.db(builder.toString());
    }

    /**
     * virtual method to set Category
     *
     * @param nodeName
     */
    public void setCategory(String nodeName) {
        nodeName = Util.reduceStringToMinimum(nodeName);
        StringBuilder builder = new StringBuilder();

        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {

                StatementResult sResult =
                        tx.run("MATCH (s:Node)" +
                                "WHERE s.name = {n1}" +
                                "SET s.type = {c}" +
                                "RETURN s", parameters("n1", nodeName, "c", "category"));

                builder.append("Set type of " + nodeName + " to Category");
                tx.success();
            }

        }
    }

    /********************************** TRIVIAL METHOD **************************************/

    /**
     * clear all connections that have too less of a rating
     *
     * @return true if succesfully delete the relationship
     */
    public boolean clearFailedRelationships() {
        try (Session session = driver.session()) {
            try (Transaction tx = session.beginTransaction()) {
                tx.run("MATCH (n)-[rel]->(r) \n" +
                        "WHERE rel.rating < " + DEL_TRESHHOLD + "\n" +
                        "DELETE rel");
                tx.success();
                return true;
            }
        }
    }

    public void setSimulation(boolean b) {
        this.needValidation = !b;
    }

    private Driver acquireDriver(String uri, AuthToken authToken, Config config) {

        try {
            return GraphDatabase.driver(uri, authToken, config);
        } catch (ServiceUnavailableException ex) {
            Log.debug("No valid database URI found");
            System.exit(0);
        }
        return null;

    }

    private class customNode {
        String name = "";
        String label = "";
        boolean isStream = false;
        String ch;

        public customNode(String name, String label, boolean isStream, String ch) {
            this.isStream = isStream;
            this.name = name;
            this.label = label;
            this.ch = ch;
        }
    }

    private class customRelationship {
        String source, target, property, value, label;

        public customRelationship(String source, String target, String property, String value) {
            this.source = source;
            this.target = target;
            this.property = property;
            this.value = value;
        }

        public void setLabel(String l) {
            this.label = l;
        }

    }

    public class StreamerHighscore {
        private String stream;
        private int totalPoints;
        private LinkedList<Pair> users;

        public StreamerHighscore() {
            users = new LinkedList<>();
        }

        public void addUserPointsPair(Pair p) {
            users.push(p);
        }

        public String getStream() {
            return stream;
        }

        public void setStream(String s) {
            stream = s;
        }

        public int getStreamPoints() {
            return totalPoints;
        }

        public void setStreamPoints(int i) {
            totalPoints = i;
        }

        public LinkedList<Pair> getUserList() {
            return users;
        }
    }

    public class Pair {
        Object first, second;

        public Pair(Object i, Object k) {
            first = i;
            second = k;
        }

        public Object getFirst() {
            return first;
        }

        public Object getSecond() {
            return second;
        }

        @Override
        public String toString() {
            return "Pair:{" + first + "," + second + "}";
        }
    }


}
