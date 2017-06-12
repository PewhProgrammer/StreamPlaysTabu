import common.Log;
import common.database.Neo4jWrapper;
import gui.webinterface.RunInterface;
import gui.webinterface.SiteController;
import logic.GameControl;
import model.GameModel;
import model.Language;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public class Main {

    private static final int TRACE = 1, DEBUG = 2, INFO = 3;

    private final Options mOptions = new Options();
    private String neo4jbindAddr = "";
    private String ext_bindAddr = "";
    private int seed;
    private int players = 0;
    private Language language;

    private int mVerbosity = INFO;
    private boolean guiSimulation = true;
    private boolean defaultDatbase = true;
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
                .append("- Neo4j Bind Address: " + neo4jbindAddr + "\n")
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

        sBuild.append("- Verbosity Level is set to " + Log.getLevel() + "\n");

        if (setSeed) sBuild.append("- Seed has been set to " + seed + "\n");
        else sBuild.append("- Seed has been randomized to " + seed + "\n");

        Log.info(sBuild.toString());

        defaultDatbase = false;

        if (defaultDatbase)
            Log.info("Connecting to neo4j default database with " + neo4jbindAddr);
        else
            Log.info("Connecting to neo4j legacy database with " + neo4jbindAddr); //Diese datenbank benutzten wir für unsere studie

        Neo4jWrapper neoWrapper = new Neo4jWrapper(defaultDatbase, neo4jbindAddr, seed);

        GameModel model = new GameModel(neoWrapper);

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

    public void parseCommandLine(String[] args) {
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
                .withDescription("Tell the program if it should use the default data base")
                .hasArg()
                .withType(Boolean.TYPE)
                .withArgName("BOOLEAN")
                .create("defaultdata"));
        mOptions.addOption(
                OptionBuilder.withLongOpt("lang")
                        .withDescription("Sets up Language Mode for either Ger or Eng")
                        .hasArg()
                        .withType(Number.class)
                        .withArgName("LANGUAGE")
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
                        if (line.hasOption("s")) {
                            seed = ((Number) line.getParsedOptionValue("s")).intValue();
                            setSeed = true;
                        }
                        if (line.hasOption("defaultdata"))
                            defaultDatbase = Boolean.valueOf(line.getOptionValue("defaultdata"));
                        else
                            throw new ParseException("-defaultdata not specified!");
                       /* if (line.hasOption("lang")) {
                            if (line.getOptionValue("lang").equals("ger"))
                                language = Language.Ger;
                            else language = Language.Eng;
                        } else throw new ParseException("-lang not specified!");*/
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
