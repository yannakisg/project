package dataengineering.graph;

import dataengineering.Options;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ioannis Gasparis
 */
public class HelperGraphTest {

    SimpleDateFormat formatter;

    String[][] arrayHashtags = {{"Spark", "Apache"}, {"Apache", "Hadoop", "Storm"}, {"Apache"}, {"Flink", "Spark"}, {"Spark", "HBase"}, {"Hadoop", "Apache"}};
    String[] arrayDates = {"Thu Mar 24 17:51:10 +0000 2016", "Thu Mar 24 17:51:15 +0000 2016", "Thu Mar 24 17:51:30 +0000 2016", "Thu Mar 24 17:51:55 +0000 2016",
        "Thu Mar 24 17:51:58 +0000 2016", "Thu Mar 24 17:52:12 +0000 2016"};

    public HelperGraphTest() {
        formatter = new SimpleDateFormat(Options.getInstance().getDateFormat());
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of check method, of class HelperGraph.
     * @throws java.text.ParseException
     */
    @Test
    public void testCheck() throws ParseException {
        System.out.println("Testing HelperGraph check");

        HashTagGraph graph = HashTagGraph.build();
        HelperGraph helper = new HelperGraph(graph);
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(arrayHashtags[0]));

        boolean value = helper.check(formatter.parse(arrayDates[0]), list);
        assertEquals(value, true);
        list.clear();
        
        list.addAll(Arrays.asList(arrayHashtags[1]));

        value = helper.check(formatter.parse(arrayDates[1]), list);
        assertEquals(value, true);
        list.clear();
        
        list.addAll(Arrays.asList(arrayHashtags[2]));

        value = helper.check(formatter.parse(arrayDates[2]), list);
        assertEquals(value, false);
        list.clear();
    }

}
