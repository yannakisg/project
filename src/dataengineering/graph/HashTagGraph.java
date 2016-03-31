package dataengineering.graph;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * HashTagGraph representation. The Graph is a Map between Vertices and a map of Vertices with the total number of edges. 
 * 
 *
 * Furthermore there is a helper map that points labels to Vertices. 
 * @author Ioannis Gasparis
 */
public class HashTagGraph {

    private final HelperGraph helper;
    private final Map<String, Vertex> labelMapping;
    private final Map<Vertex, Map<Vertex, Integer>> graph;
    private double avgDegree;
    private boolean toCompute;
    
    /**
     * 
     * @return A new HashTagGraph object
     */
    public static HashTagGraph build() {
        return new HashTagGraph();
    }

    private HashTagGraph() {
        this.graph = new HashMap<>();
        this.labelMapping = new HashMap<>();
        this.avgDegree = 0.0;
        this.toCompute = false;
        this.helper = new HelperGraph(this);
    }

    /**
     * This function first checks if the hashtags should be added. If this is indeed the case then it adds them to the graph. 
     * Otherwise if applicable the average degree value of the graph is updated.
     * 
     * @param date The created_at field from the JSON object
     * @param hashtags The hashtags fields from the JSON object
     */
    public void handle(Date date, List<String> hashtags) {
        if (helper.check(date, hashtags)) {
            addHashTags(hashtags);
        } else {
            if (toCompute) {
                computeAverageDegree();
            }
        }
        
    }

    /**
     * It adds the hashtags to the graph. First if applicable we update the average degree value (in case before a call to the removeEdges was made).
     * 
     * This function takes the combination of the hashtags and adds them to the graph, if applicable. 
     * 
     * @param hashtags New hashtags to be added to the graph
     */
    protected void addHashTags(List<String> hashtags) {
        if (toCompute) {
            computeAverageDegree();
        }
        toCompute = false;

        for (int i = 0; i < hashtags.size() - 1; i++) {
            Vertex v0 = findOrCreate(hashtags.get(i));

            for (int j = 1; j < hashtags.size(); j++) {
                Vertex v1 = findOrCreate(hashtags.get(j));
                addEdges(v0, v1);
            }
        }
    }

    /**
     * This function removes the edges of the hashtag list and if applicable decrease the degree of the node.
     * Furthermore removes Vertices without edges from the graph.
     * 
     * @param hashtags The edges of the hashtags that have to be removed
     */
    protected void removeEdges(List<String> hashtags) {
        toCompute = false;
        

        for (int i = 0; i < hashtags.size() - 1; i++) {
            Vertex v0 = labelMapping.get(hashtags.get(i));
            if (v0 == null) {
                continue;
            }

            Map<Vertex, Integer> v0Edges = graph.get(v0);
            
            for (int j = 1; j < hashtags.size(); j++) {
                Vertex v1 = labelMapping.get(hashtags.get(j));
                if (v1 == null) {
                    continue;
                }
                Map<Vertex, Integer> v1Edges = graph.get(v1);
                
                if (v0Edges.containsKey(v1)) {
                    Integer value = v0Edges.get(v1);
                    if (value == 1) {
                        v0Edges.remove(v1);
                        v0.decreaseDegree();
                        toCompute = true;
                    } else {
                        v0Edges.put(v1, value - 1);
                    }
                }
                
                if (v1Edges.containsKey(v0)) {
                    Integer value = v1Edges.get(v0);
                    if (value == 1) {
                        v1Edges.remove(v0);
                        v1.decreaseDegree();
                        toCompute = true;
                    } else {
                        v1Edges.put(v0, value - 1);
                    }
                }

                if (v1Edges.isEmpty()) {
                    graph.remove(v1);
                    labelMapping.remove(v1.getLabel());
                }
            }

            if (v0Edges.isEmpty()) {
                toCompute = true;
                graph.remove(v0);
                labelMapping.remove(v0.getLabel());
            }
        }
    }

    /**
     * It computes the average degree value, if it has changed, of the graph and returns it.
     * @return the average degree value
     */
    public double computeAverageDegree() {
        if (graph.isEmpty()) {
            this.avgDegree = 0.0;
            toCompute = false;
        } else if (toCompute) {
            double totalDegrees = 0;
            for (Vertex vertex : graph.keySet()) {
                totalDegrees += vertex.getDegree();
            }
            this.avgDegree = ((int) ((totalDegrees / graph.size()) * 100)) / 100.0;
            toCompute = false;
        }
        return avgDegree;
    }
    
    /**
     * 
     * @param label - Vertex Label (HashTag)
     * @return A newly created or an existing vertex with that label
     */
    private Vertex findOrCreate(String label) {
        Vertex vertex = labelMapping.get(label);

        if (vertex == null) {
            vertex = new Vertex(label);
            labelMapping.put(label, vertex);
            graph.put(vertex, new HashMap<>());
        }

        return vertex;
    }
    
    /**
     * It creates if applicable the edges between the first and the second vertex.
     * 
     * @param v0 - The first vertex
     * @param v1 - The second vertex
     */
    private void addEdges(Vertex v0, Vertex v1) {
        if (v0.equals(v1)) {
            return;
        }
        
        Map<Vertex, Integer> m0 = graph.get(v0);
        if (m0.containsKey(v1)) {
            m0.put(v1, m0.get(v1) + 1);
        } else {
            toCompute = true;
            v0.increaseDegree();
            m0.put(v1, 1);
        }
        
        Map<Vertex, Integer> m1 = graph.get(v1);
        if (m1.containsKey(v0)) {
            m1.put(v0, m1.get(v0) + 1);
        } else {
            toCompute = true;
            v1.increaseDegree();
            m1.put(v0, 1);
        }
    }
    
    /**
     * 
     * @return Graph size
     */
    public int getSize() {
        return graph.size();
    }
}