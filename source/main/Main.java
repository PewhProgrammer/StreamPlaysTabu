import common.Log;
import common.database.Neo4jWrapper;
import common.dbExport;
import gui.webinterface.RunInterface;
import gui.webinterface.SiteController;
import logic.GameControl;
import model.GameModel;
import model.Language;
import org.apache.commons.cli.*;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */

@SpringBootApplication
public class Main {

    private static final int TRACE = 1, DEBUG = 2, INFO = 3;

    private final Options mOptions = new Options();
    private String neo4jbindAddr = "pewhgames.com:7687";
    private String ext_bindAddr = "http://m.schubhan.de:1337/";
    private String neo4jUsr = "neo4j";
    private String neo4jPassword = "streamplaystabu";
    private int seed = 222;
    private int min_players = 2;
    private int round_time = 105;
    private int bonus_time = 10;
    private Language language = Language.Eng;

    private int mVerbosity = INFO;
    private boolean guiSimulation = false;
    private boolean defaultDatbase = false;
    private boolean setSeed = false;


    Thread mTHREAD;

    /**
     * Normal Main Method started by java -jar
     *
     * @param args CommandLine args
     */
    public static void main(String[] args) {
        new Main().parseCommandLine(args);
    }

    public void doSemantics() {

        StringBuilder sBuild = new StringBuilder();

        sBuild.append("Following Command Lines have been processed: \n")
                .append("- Neo4j Bind Address: " + neo4jbindAddr + " with usr/pw: "+ neo4jUsr+"/"+neo4jPassword+"\n")
                .append("- External Webpage Bind Address: " + ext_bindAddr + "\n");

        if (defaultDatbase) sBuild.append("- Default Data Processing in Database\n");
        else sBuild.append("- Release Data Processing in Database\n");

        if (language == Language.Ger) sBuild.append("- Language Mode is set to German\n");
        else sBuild.append("- Language Mode is set to English\n");

        if (guiSimulation) sBuild.append("- Using Prototype Graphical User Interface\n");
        else sBuild.append("- Using Release Graphical User Interface\n");

        switch (mVerbosity) {
            case TRACE:
                Log.setLevel(Log.Level.TRACE);
                break;
            case DEBUG:
                Log.setLevel(Log.Level.DEBUG);
                break;
            case INFO:
                Log.setLevel(Log.Level.INFO);
                break;
            default:
                Log.error("Unknown verbosity level");
        }

        Log.init("console_output.txt");

        sBuild.append("- Verbosity Level is set to " + Log.getLevel() + "\n");

        if (setSeed) sBuild.append("- Seed has been set to " + seed + "\n");
        else sBuild.append("- Seed has been randomized to " + seed + "\n");

        sBuild.append("- Round time set to " + round_time + "\n");
        sBuild.append("- Bonus time set to " + bonus_time + "\n");

        Log.info(sBuild.toString());

        defaultDatbase = false;

        if (defaultDatbase)
            Log.info("Connecting to neo4j default database with " + neo4jbindAddr);
        else
            Log.info("Connecting to neo4j legacy database with " + neo4jbindAddr); //Diese datenbank benutzten wir für unsere studie

        Neo4jWrapper neoWrapper = new Neo4jWrapper(defaultDatbase, neo4jbindAddr, seed,neo4jUsr,neo4jPassword);

        GameModel model = new GameModel(neoWrapper);
        model.setMinNumPlayers(min_players);
        model.setROUND_TIME_STATIC(round_time);
        model.setBONUS_TIME_STATIC(bonus_time);

        mTHREAD = new Thread() {
            @Override
            public void run() {
                Log.info("Launching Server...");
                new GameControl(model, seed, ext_bindAddr).waitingForConfig();
            }

        };

        mTHREAD.start();

        Log.info("Launching webinterface ...");
        RunInterface.main(new String[]{});

        try {
            Runtime rt = Runtime.getRuntime();
            String url = "http://localhost:8080";
            rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            new SiteController(model, ext_bindAddr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doDatabaseExport(Neo4jWrapper db,boolean preset){
        //print out all explain nodes
        dbExport.init("explain_nodes.txt","Processing explain...");
        db.dbExportExplain(preset);
        dbExport.close();

        //print out all category nodes
        dbExport.init("category_nodes.txt","Processing category...");
        db.dbExportCategory(preset);
        dbExport.close();

        //print out all taboo nodes to different explain nodes
        dbExport.init("explain-taboo_nodes.txt","Processing taboo -> explain...");
        db.dbExportExplainTaboo(preset);
        dbExport.close();

        //print out all explain nodes to different category nodes
        dbExport.init("explain-category_nodes.txt","Processing explain -> category...");
        db.dbExportExplainCategory(preset);
        dbExport.close();

        dbExport.init("game_logs.txt","Processing game logs...");
        db.dbExportGameLogs();
        dbExport.close();


        Log.db("export completed");
    }

    public void parseCommandLine(String[] args) {


        if(args.length > 0 && args[0].equals("--dbexport")){
            Neo4jWrapper neo = new Neo4jWrapper(false,"pewhgames.com:7687",0,neo4jUsr,neo4jPassword);
            Log.db("init export process");
            boolean preset = true ;
            if(args.length > 1 && args[1] != null && args[1].equals("unique")){
                preset = false;
            }
            doDatabaseExport(neo,preset);
            System.exit(1);
        }

        //initiates seed
        Random rand = new Random();
        rand.setSeed(new Date().getTime());
        seed = rand.nextInt(100);

        // create the command line parser
        CommandLineParser parser = new GnuParser();

        mOptions.addOption(
                OptionBuilder.withLongOpt("neo4jserver")
                        .withDescription("Aquires Database Driver")
                        .hasArg()
                        .withType(Number.class)
                        .withArgName("HOST:PORT")
                        .create());
        mOptions.addOption(
                OptionBuilder.withLongOpt("webpageserver")
                        .withDescription("Aquires host:port information")
                        .hasArg()
                        .withType(Number.class)
                        .withArgName("HOST:PORT")
                        .create());
        mOptions.addOption(OptionBuilder
                .withDescription("Set deterministic RNG Seed <server only>")
                .hasArg()
                .withType(Number.class)
                .withArgName("SEED")
                .create('s'));
        mOptions.addOption(OptionBuilder
                .withDescription("Verbosity Level: DEBUG(2), TRACE(1), INFO(3)")
                .hasArg()
                .withType(Number.class)
                .withArgName("VERBOSITY")
                .create('v'));
        mOptions.addOption(OptionBuilder
                .withDescription("If not used, will default to Prototype GUI")
                .hasOptionalArg()
                .withType(Number.class)
                .withArgName("")
                .create("gui"));
        mOptions.addOption(OptionBuilder
                .withDescription("If not used, will default to 2")
                .hasOptionalArg()
                .withType(Number.class)
                .withArgName("MIN_PLAYERS")
                .create("p"));
        mOptions.addOption(OptionBuilder
                .withDescription("If not used, will default to 105")
                .hasOptionalArg()
                .withType(Number.class)
                .withArgName("ROUNDTIME")
                .create("roundtime"));
        mOptions.addOption(OptionBuilder
                .withDescription("If not used, will default to 10")
                .hasOptionalArg()
                .withType(Number.class)
                .withArgName("BONUSTIME")
                .create("bonustime"));
        mOptions.addOption(OptionBuilder
                .withDescription("If not used, will default to <anonymous>")
                .hasOptionalArg()
                .withType(Number.class)
                .withArgName("USER")
                .create("dbusr"));
        mOptions.addOption(OptionBuilder
                .withDescription("If not used, will default to <anonymous>")
                .hasOptionalArg()
                .withType(Number.class)
                .withArgName("PASSWORD")
                .create("dbpassword"));

        CommandLine line;
        for (String arg : args) {
            try {
                // parse the command line arguments
                line = parser.parse(mOptions, args);

                // Syntactical check and parsing

                switch (arg) {
                    case "--webpageserver": {
                        ext_bindAddr = line.getOptionValue("webpageserver");
                        //if (!checkBindAddrFormat(ext_bindAddr))
                        //    throw new ParseException(neo4jbindAddr + " malicious bind address format for the webpage!");
                        ext_bindAddr = "http://" + line.getOptionValue("webpageserver") + "/";
                    }
                    break;
                    case "--neo4jserver":
                        neo4jbindAddr = line.getOptionValue("neo4jserver");
                        //  if (!checkBindAddrFormat(neo4jbindAddr))
                        //      throw new ParseException(neo4jbindAddr + " malicious bind address format for neo4j!");
                        break;
                    case "-dbusr":
                        neo4jUsr = line.getOptionValue("dbusr");
                        break;
                    case "-dbpassword":
                        neo4jPassword = line.getOptionValue("dbpassword");
                        break;
                    case "-s":
                        seed = ((Number) line.getParsedOptionValue("s")).intValue();
                        setSeed = true;
                        break;
                    case "-p":
                        min_players = ((Number) line.getParsedOptionValue("p")).intValue();
                        break;
                    case "-roundtime":
                        round_time = ((Number) line.getParsedOptionValue("roundtime")).intValue();
                        break;
                    case "-bonustime":
                        bonus_time = ((Number) line.getParsedOptionValue("bonustime")).intValue();
                        break;
                    default:
                        //abort("No launch mode specified");
                }
                if (line.hasOption("v"))
                    mVerbosity = ((Number) line.getParsedOptionValue("v")).intValue();
            } catch (Exception e) {
                abort(e.getMessage());
            }
        }
        if (ext_bindAddr.equals("")) abort("Missing --webpageserver <host>:<port> option");
        doSemantics();
    }

    /**
     * check the address
     *
     * @param bindAddr address for checking
     * @return true if possible
     */
    private boolean checkBindAddrFormat(String bindAddr) {


        String[] parts = {};
        try {
            parts = bindAddr.split(":");
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
        String host = parts[0]; // host
        String port = parts[1]; // port
        boolean correctHost = host.equals("localhost") || host.equals("pewhgames.com")
                || Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,5}").
                matcher(host).matches();
        boolean correctPort = Pattern.compile("\\d+").
                matcher(port).matches();
        return correctHost && correctPort;
    }

    /**
     * When something went wront call this method
     * It will show an additional help text
     *
     * @param message with failure description
     */
    private void abort(String message) {
        Log.error(message);
        System.err.println(
                "[java <jarfile>] --webpageserver <host>:<port> --neo4jserver <host>:<port> -defaultdata <boolean> [-s <seed>] \n" +
                        "               -gui --lang <language>\n" +
                        "               [-v (1-3)]");
        System.err.println();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("StreamPlaysTabu", mOptions);

        System.exit(1);
    }
}
