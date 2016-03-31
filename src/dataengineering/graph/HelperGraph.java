package dataengineering.graph;

import dataengineering.Options;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Helper class for the HashTagGraph class that performs necessary checks. 
 * Every hashtag is saved into a TreeMap along with its creation time. Hashtags that created exactly the same time are saved under the same key. 
 * Note we keep all of them.
 *
 * @author Ioannis Gasparis
 */
public class HelperGraph {

    private final HashTagGraph graph;
    private Long latestDate;
    private Long dummyDate;
    private Long earliestDate;
    private final NavigableMap<Long, List<List<String>>> datesMap; 
    private final long windowSize;

    /**
     * Constructor 
     * 
     * @param graph HashTagGraph instance
     */
    public HelperGraph(HashTagGraph graph) {
        this.graph = graph;
        this.latestDate = null;
        this.dummyDate = Long.MIN_VALUE;
        this.datesMap = new TreeMap<>();
        this.windowSize = Options.getInstance().getWindowSize() * 1000;
    }

    /**
     * This function checks if the hashtags should be inserted in the graph. 
     * First if it is the first time that this function is called we initialize the appropriate values and we return true.
     * Otherwise we check if the creation time is inside the current window. If it is not we return false.
     * If the creation time is later than the latest creation time then we update the appropriate value.
     * If it is earlier than the earliest creation time we update the appropriate value as well.
     * We save the hashtags in the map here and we call a function in order to remove any potential edges that reside outside of the window.
     * We return true as long as the size of hashtags is bigger than 1.
     * 
     * @param date Creation time
     * @param hashtags Hashtags
     * @return True if the hashtags should be added in the graph and false otherwise.
     */
    public boolean check(Date date, List<String> hashtags) {
        long dateL = date.getTime();

        if (latestDate == null) {
            latestDate = dateL;
            earliestDate = dateL;

            List<List<String>> list = datesMap.get(dateL);
            if (list == null) {
                list = new ArrayList<>();
                datesMap.put(dateL, list);
            }
            list.add(hashtags);

            return hashtags.size() > 1;
        }

        long ms = latestDate - dateL;

        if (ms > windowSize) {
            return false;
        } else if (ms < 0) {
            latestDate = dateL;
        } else if (dateL < earliestDate) {
            earliestDate = dateL;
        }

        List<List<String>> list = datesMap.get(dateL);
        if (list == null) {
            list = new ArrayList<>();
            datesMap.put(dateL, list);
        }
        list.add(hashtags);
        
        removePotentialEdges();

        return hashtags.size() > 1;
    }

    /**
     * Removes potential edges that reside outside of the window. 
     * First it checks if the (latestData - 60 seconds) = dummyDate is later than the earliest date.
     * If that is the case then it means that edges should be removed with creation time between the earliestDate and the dummyDate (exclusive). 
     * The HashTagGraph is updated accordingly as well as the local map.
     */
    private void removePotentialEdges() {
        long ms = (latestDate - windowSize);
        dummyDate = ms;

        if (dummyDate > earliestDate) {
            NavigableMap<Long, List<List<String>>> remMap = datesMap.subMap(earliestDate, true, dummyDate, false);

            Iterator<Entry<Long, List<List<String>>>> iter;
            Entry<Long, List<List<String>>> entry;
            for (iter = remMap.entrySet().iterator(); iter.hasNext();) {
                entry = iter.next();
                for (List<String> list : entry.getValue()) {
                    graph.removeEdges(list);
                }
                iter.remove();
            }
        }
    }
}