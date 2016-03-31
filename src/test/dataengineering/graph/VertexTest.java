package dataengineering.graph;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ioannis Gasparis
 */
public class VertexTest {
    
    public VertexTest() {
    }
    

    /**
     * Test of equals method, of class Vertex.
     */
    @Test
    public void testEquals() {
        System.out.println("Testing Vertex equals");
        Vertex vertex0 = new Vertex("Test");
        Vertex vertex1 = new Vertex("Test");
        boolean expResult = true;
        boolean result = vertex0.equals(vertex1);
        assertEquals(expResult, result);        
    }
    
    /**
     * Test of not equals method, of class Vertex.
     */
    @Test
    public void testNotEquals() {
        System.out.println("Testing Vertex not equals");
        Vertex vertex0 = new Vertex("Test");
        Vertex vertex1 = new Vertex("TesT");
        boolean expResult = false;
        boolean result = vertex0.equals(vertex1);
        assertEquals(expResult, result);        
    }

    /**
     * Test of increaseDegree method, of class Vertex.
     */
    @Test
    public void testIncreaseDegree() {
        System.out.println("Testing Vertex increaseDegree");
        Vertex instance = new Vertex("Test");
        double degree = instance.getDegree();
        instance.increaseDegree();
        double newDegree = instance.getDegree();
        double diff = newDegree - degree;
        double real = 1.0;
        assertEquals(diff, real, 0.0001);
    }

    /**
     * Test of decreaseDegree method, of class Vertex.
     */
    @Test
    public void testDecreaseDegree() {
        System.out.println("Testing Vertex decreaseDegree");
        Vertex instance = new Vertex("Test");
        instance.increaseDegree();
        instance.decreaseDegree();
        assertEquals(0.0, instance.getDegree(), 0.0001);
    }
    
}
