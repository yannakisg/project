package dataengineering.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ioannis Gasparis
 */
public class HashTagGraphTest {

    String[][] arrayHashtags = {{"Spark", "Apache"}, {"Apache", "Hadoop", "Storm"}, {"Apache"}, {"Flink", "Spark"}, {"Spark", "HBase"}, {"Hadoop", "Apache"}};
   
    public HashTagGraphTest() {
    }

    /**
     * Test of addHashTags method, of class HashTagGraph.
     */
    @Test
    public void testAddHashTags() {
        System.out.println("Testing HashTagGraph addHashTags");
        HashTagGraph graph = HashTagGraph.build();
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(arrayHashtags[0]));

        graph.addHashTags(list);
        assertEquals(2, graph.getSize());
    }

    /**
     * Test of removeEdges method, of class HashTagGraph.
     */
    @Test
    public void testRemoveEdges() {
        System.out.println("Testing HashTagGraph removeEdges");

        HashTagGraph graph = HashTagGraph.build();
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(arrayHashtags[0]));

        graph.addHashTags(list);
        graph.removeEdges(list);
        assertEquals(0, graph.getSize());

    }

    /**
     * Test of computeAverageDegree method, of class HashTagGraph.
     */
    @Test
    public void testComputeAverageDegreeOne() {
        System.out.println("Testing HashTagGraph computeAverageDegree");

        HashTagGraph graph = HashTagGraph.build();
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(arrayHashtags[0]));

        graph.addHashTags(list);
        assertEquals(1.0, graph.computeAverageDegree(), 0.001);

    }

    /**
     * Test of computeAverageDegree method, of class HashTagGraph.
     */
    @Test
    public void testComputeAverageDegreeTwo() {
        System.out.println("Testing HashTagGraph computeAverageDegree");

        HashTagGraph graph = HashTagGraph.build();

        for (int i = 0; i < 2; i++) {
            List<String> list = new ArrayList<>();
            list.addAll(Arrays.asList(arrayHashtags[i]));

            graph.addHashTags(list);

        }
        assertEquals(2.0, graph.computeAverageDegree(), 0.001);
    }

    /**
     * Test of computeAverageDegree method, of class HashTagGraph.
     */
    @Test
    public void testComputeAverageDegreeAll() {
        System.out.println("Testing HashTagGraph computeAverageDegree");

        HashTagGraph graph = HashTagGraph.build();

        for (int i = 0; i < 5; i++) {
            List<String> list = new ArrayList<>();
            list.addAll(Arrays.asList(arrayHashtags[i]));

            graph.addHashTags(list);

        }

        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(arrayHashtags[0]));
        graph.removeEdges(list);
        list.clear();

        list.addAll(Arrays.asList(arrayHashtags[5]));

        graph.addHashTags(list);
        assertEquals(1.66, graph.computeAverageDegree(), 0.001);
    }
}
