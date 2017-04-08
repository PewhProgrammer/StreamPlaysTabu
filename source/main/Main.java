import common.Log;
import gui.GuiAnchor;
import logic.GameControl;
import logic.commands.Answer;
import model.GameModel;
import model.Language;
import org.apache.commons.cli.*;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by Thinh-Laptop on 26.03.2017.
 */
public class Main {

    private static final int TRACE = 1, DEBUG = 2, INFO = 3;

    private final Options mOptions = new Options();
    private String neo4jbindAddr = "";
    private int seed = (short) new Random().nextInt(Integer.MAX_VALUE);
    private int players = 0;
    private Language language ;

    private int mVerbosity = INFO;
    private boolean guiSimulation = true;

    private boolean defaultDatbase = true;

    /**
     * Normal Main Method started by java -jar
     *
     * @param args
     *          CommandLine args
     */
    public static void main(String[] args){new Main().parseCommandLine(args);}

    public void doSemantics(){

        switch(mVerbosity) {
            case TRACE: Log.setLevel(Log.Level.TRACE);
                break;
            case DEBUG: Log.setLevel(Log.Level.DEBUG);
                break;
            case INFO: Log.setLevel(Log.Level.INFO);
                break;
            default:
                Log.error("Unknown verbosity level");
        }

        Log.info("Following Command Lines have been processed: \n" +
                "neo4j bind address: " + neo4jbindAddr + "\n" +
                "neo4j default database: " + defaultDatbase + "\n"+
                "language mode is " + language + "\n" +
                "minimum players is " + players + "\n" +
                "gui prototype: "+ guiSimulation + "\n" +
                "verbosity level is " + Log.getLevel());


        Log.info("Connecting to neo4j default database("+defaultDatbase+")");

        Log.info("Launching Server...");
        new GameControl((short)2,Language.Ger).waitingForPlayers();


        String[] param = {"lol"};
        new GuiAnchor().main(param);
        Log.info("Launching GUI...");
        // ....
    }

    public void parseCommandLine(String[] args){
        // create the command line parser
        CommandLineParser parser = new GnuParser();

        mOptions.addOption(
                OptionBuilder.withLongOpt( "neo4jserver" )
                .withDescription( "aquires database driver" )
                .hasArg()
                .withType(Number.class)
                .withArgName("HOST:PORT")
                .create() );
        mOptions.addOption( OptionBuilder
                .withDescription( "Tell the program if it should use the default data base" )
                .hasArg()
                .withType(Boolean.TYPE)
                .withArgName("BOOLEAN")
                .create("defaultdata") );
        mOptions.addOption(
                OptionBuilder.withLongOpt( "lang" )
                        .withDescription( "set ups language mode for either ger or eng" )
                        .hasArg()
                        .withType(Number.class)
                        .withArgName("LANGUAGE")
                        .create() );
        mOptions.addOption( OptionBuilder
                .withDescription( "rng seed <server only>" )
                .hasArg()
                .withType(Number.class)
                .withArgName("SEED")
                .create('s') );
        mOptions.addOption( OptionBuilder
                .withDescription( "verbosity level: DEBUG(2), TRACE(1), INFO(3)" )
                .hasArg()
                .withType(Number.class)
                .withArgName("VERBOSITY")
                .create('v') );
        mOptions.addOption( OptionBuilder
                .withDescription( "if not used, will default to prototype gui" )
                .hasOptionalArg()
                .withType(Number.class)
                .withArgName("")
                .create("gui") );
        mOptions.addOption( OptionBuilder
                .withDescription( "number of min. players" )
                .hasArg()
                .withType(Number.class)
                .withArgName("PLAYERS")
                .create('p') );

        CommandLine line;
        try {
            // parse the command line arguments
            line = parser.parse( mOptions, args );

            // Syntactical check and parsing
            switch (args[0]) {
                case "--neo4jserver":
                    neo4jbindAddr = line.getOptionValue("neo4jserver");
                    if (line.hasOption("s"))
                        seed = ((Number)line.getParsedOptionValue("s")).intValue();
                    if (line.hasOption("defaultdata"))
                        defaultDatbase = Boolean.valueOf(line.getOptionValue("defaultdata"));
                    else
                        throw new ParseException("-defaultdata not specified!");
                    if (line.hasOption('p'))
                        players = ((Number)line.getParsedOptionValue("p")).intValue();
                    else
                        throw new ParseException("-p not specified!");
                    if(line.hasOption("lang")) {
                        if (line.getOptionValue("lang").equals("ger"))
                            language = Language.Ger;
                        else language = Language.Eng;
                    }
                    else throw new ParseException("-lang not specified!");
                    break;
                default:
                    abort("No launch mode specified");
            }
            if (line.hasOption("v"))
                mVerbosity = ((Number)line.getParsedOptionValue("v")).intValue();
        } catch (Exception e) {
            abort(e.getMessage());
        }
        doSemantics();
    }

    /**
     * check the address
     *
     * @param bindAddr
     *      address for checking
     * @return
     *      true if possible
     */
    private boolean checkBindAddrFormat(String bindAddr) {
        return bindAddr.equals("localhost")
                || Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,5}").matcher(bindAddr).matches();
    }

    /**
     * When something went wront call this method
     * It will show an additional help text
     *
     * @param message
     *          with failure description
     */
    private void abort(String message) {
        Log.error(message);
        System.err.println(
                "java <jarfile> --neo4jserver <host>:<port> [-s <seed>] -p <num min. player>\n" +
                        "               -gui --lang <language>\n" +
                        "               [-v (1-3)]");
        System.err.println();
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("StreamPlaysTabu", mOptions);

        System.exit(1);
    }
}
