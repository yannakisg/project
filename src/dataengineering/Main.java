package dataengineering;

import dataengineering.io.FileParser;
import java.io.FileNotFoundException;
import java.text.ParseException;

/**
 * Main class with main function
 *
 * @author Ioannis Gasparis
 */
public class Main {    
    
    /**
     * Usage function
     */
    private static void usage() {
        System.err.println("Usage: java -jar \"DataEngineering.jar\" <input file> <output file>");
        System.exit(1);
    }
    
    /**
     * Main function that reads the arguments and saves them and starts the project
     * @param args <input file> <output file>
     * @throws ParseException
     * @throws FileNotFoundException
     * @throws InterruptedException 
     */
    public static void main(String args[]) throws ParseException, FileNotFoundException, InterruptedException {
        
        if (args == null || args.length != 2) {
            usage();
        }
                        
        Options.getInstance().setInputFile(args[0]);
        Options.getInstance().setOutputFile(args[1]);
        
        FileParser parser = FileParser.build(Options.getInstance().getInputFile());
        parser.parse();        
    }
}
