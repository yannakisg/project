package dataengineering.threading;

import dataengineering.Options;
import dataengineering.graph.HashTagGraph;
import dataengineering.io.FileCreator;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Worker thread that accepts tasks from the FileParser and it further processes them by the help of HashTagGraph class
 *
 * @author Ioannis Gasparis
 */
public class Worker implements Runnable {    
    
    private final BlockingQueue<Task> queue;
    
    /**
     * Constructor. 
     */
    public Worker() {
        this.queue = new LinkedBlockingQueue<>();
    }
    
    /**
     * Pushes the new task to the Blocking Queue
     * 
     * @param date creation time
     * @param hashTags hashtags
     * @throws InterruptedException 
     */
    public void pushTask(Date date, List<String> hashTags) throws InterruptedException {
        this.queue.put(new Task(date, hashTags));
    }
    
    /**
     * Inserts to the Blocking Queue an empty task indicating that the worker thread should stop executing.
     * @throws InterruptedException 
     */
    public void stop() throws InterruptedException {
      this.queue.put(new Task());
    }
    
    /**
     * Worker thread. It creates the output file and as long as there are tasks from the Blocking Queue it processes 
     * them with the help of the HashTagGraph. After that it appends the average degree value of the graph to the output file.
     */
    @Override
    public void run() {
        HashTagGraph graph = HashTagGraph.build();
        FileCreator creator;
        try {
            creator = FileCreator.build(Options.getInstance().getOutputFile());
        } catch (IOException ex) {
            System.err.println("Error while creating output file: " + ex.getMessage());
            System.err.println("Exiting...");
            return;
        }
        
        while (true) {
            try {
                Task task = queue.take();
                if (task.isEmpty()) {
                    break;
                }
                graph.handle(task.date, task.hashTags);
                creator.append(graph.computeAverageDegree());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(ex);
            }
        }
        
        creator.close();
    }    
    
    /**
     * Task class
     */
    protected class Task {
        private final Date date;
        private final List<String> hashTags;
        
        protected Task(Date date, List<String> hashTags) {
            this.date = date;
            this.hashTags = hashTags;
        }
        
        protected Task() {
            this.date = null;
            this.hashTags = null;
        }
        
        protected boolean isEmpty() {
            return date == null && hashTags == null;
        }
    }
}
