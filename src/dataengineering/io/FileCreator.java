package dataengineering.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * Class that writes the average degree values to a file
 * 
 * @author Ioannis Gasparis
 */
public class FileCreator {
    
    /**
     * 
     * @param outputFile Output file name
     * @return a new FileCreator object
     * @throws IOException 
     */
    public static FileCreator build(String outputFile) throws IOException {
        return new FileCreator(outputFile);
    }
    
    private final PrintWriter writer;    
    
    /**
     * Constructor
     * @param outputFile Output file name
     * @throws IOException 
     */
    private FileCreator(String outputFile) throws IOException {
        this.writer = new PrintWriter(new BufferedWriter(new FileWriter(outputFile)));
    }
    
    /**
     * Appends the double value to the file and flushes the stream
     * @param value Double value
     */
    public void append(double value) {
        this.writer.printf("%.2f\n", value);
        this.writer.flush();
    }   
    
    /**
     * Closes the stream.
     */
    public void close() {
        this.writer.close();
    }
}
