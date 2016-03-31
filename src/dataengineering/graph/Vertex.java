package dataengineering.graph;

import java.util.Objects;

/**
 * Class that represents a vertex of the HashTagGraph. 
 * Each Vertex has a Label as well as a degree
 * 
 * @author Ioannis Gasparis
 */
public class Vertex {
    private final String label;
    private long degree;      
    
    /**
     * Constructor
     * @param label HashTag
     */
    public Vertex(String label) {
        this.label = label;
        this.degree = 0;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.label);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vertex other = (Vertex) obj;
        if (!Objects.equals(this.label, other.label)) {
            return false;
        }
        return true;
    }
    
    /**
     * 
     * @return Degree of Vertex
     */
    public double getDegree() {
        return degree;
    }
    
    /**
     * 
     * @return Label of Vertex
     */
    public String getLabel() {
        return label;
    }
    
    /**
     * Increases the degree by one (one new edge was added).
     */
    public void increaseDegree() {
        this.degree += 1;
    }
    
    /**
     * Decreases the degree by one (one edge was deleted).
     */
    public void decreaseDegree() {
        if (degree != 0) {
            this.degree -= 1;
        }
    }
}