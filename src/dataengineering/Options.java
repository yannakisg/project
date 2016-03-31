package dataengineering;

/**
 *
 * Helper class that contains key attributes (e.g. the window size) for the project.
 * 
 * @author Ioannis Gasparis
 */
public class Options {
    private static Options _instance = null;
    
    /**
     * 
     * @return The singleton object of this class
     */
    public static Options getInstance() {
        if (_instance == null) {
            _instance = new Options();
        }
        
        return _instance;
    }
    
    
    private String inputFile;
    private String outputFile;
    private long windowSize;
    private final String dateFormat;
    
    /**
     * Private constructor. Only one object per experiment.
     */
    private Options() {
        inputFile = "tweets.txt";
        outputFile = "output.txt";
        windowSize = 60;
        dateFormat = "EEE MMM dd kk:mm:ss Z yyyy";
    }
    
    /**
     * 
     * @return the date format
     */
    public String getDateFormat() {
        return this.dateFormat;
    }
    
    /**
     * 
     * @return The window size in seconds
     */
    public long getWindowSize() {
        return this.windowSize;
    }
    
    /**
     * 
     * @return the input file name
     */
    public String getInputFile() {
        return this.inputFile;
    }
    
    /**
     * 
     * @return the output file name
     */
    public String getOutputFile() {
        return this.outputFile;
    }
    
    /**
     * 
     * @param windowSize The window size in seconds
     */
    protected void setWindowSize(long windowSize) {
        this.windowSize = windowSize;
    }
    
    /**
     * 
     * @param inputFile The input file name
     */
    protected void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }
    
    /**
     * 
     * @param outputFile The output file name
     */
    protected void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }
}
