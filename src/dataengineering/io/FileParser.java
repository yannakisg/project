package dataengineering.io;

import dataengineering.Options;
import dataengineering.threading.Worker;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * It parses the tweet file
 * 
 * @author Ioannis Gasparis
 */
public class FileParser {

    private final String fileName;
    private final SimpleDateFormat formatter;
    private final Worker worker;
    
    /**
     * 
     * @param fileName Input file name
     * @return A new FileParser object
     */
    public static FileParser build(String fileName) {
        return new FileParser(fileName);
    }

    /**
     * Constructor
     * @param fileName Input file name 
     */
    private FileParser(String fileName) {
        this.worker = new Worker();
        this.fileName = fileName;
        this.formatter = new SimpleDateFormat(Options.getInstance().getDateFormat());
    }

    /**
     * Starts the parsing. Every single line is further parsed. After the parsing finish, we inform the worker thread that this is the end. 
     * 
     * @throws FileNotFoundException
     * @throws InterruptedException 
     */
    public void parse() throws FileNotFoundException, InterruptedException {
        (new Thread(worker)).start();
        
        Scanner scanner = new Scanner(new File(fileName));
        String line;

        while (scanner.hasNextLine()) {
            line = scanner.nextLine();
            try {
                parseSingleLine(line);
            } catch (ParseException ex) {
                System.err.println(line + " - Invalid time");
            }
        }
        scanner.close();
        worker.stop();
    }

    /**
     * It parses the line from the tweets file and it finds the creation time as well as the hashtags. We care only about unique hashtags.
     * After that it informs the worker thread that a new tweet is available.
     * 
     * @param line A single line
     * @throws ParseException 
     */
    private void parseSingleLine(String line) throws ParseException {
        JSONObject obj = new JSONObject(line);
        
        try {
            Date date = formatter.parse(obj.getString("created_at"));
            List<String> hashTags = new LinkedList<>();
            Set<String> unique = new HashSet<>();
            
            JSONArray arr = obj.getJSONObject("entities").getJSONArray("hashtags");
            for (int i = 0; i < arr.length(); i++) {
                unique.add(arr.getJSONObject(i).getString("text"));
            }
                        
            hashTags.addAll(unique);
            
            try {
                worker.pushTask(date, hashTags);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ex);
            }
        } catch (JSONException ex) {
        } 
    }
}