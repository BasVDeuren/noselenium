package be.kdg.spacecrack.services;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.NeighborIndex;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Janne on 5/03/14.
 */

public class GraphAlgorithm {

    public GraphAlgorithm() {}

    public static List<List<String>> calculateChordlessCyclesFromVertex(UndirectedGraph<String, DefaultEdge> graph, String baseVertex) {
        List<List<String>> cycles = new ArrayList<List<String>>(); // Cycles to output
        NeighborIndex<String, DefaultEdge> neighborIndex = new NeighborIndex<String, DefaultEdge>(graph); // Create a NeighbourIndex for the graph
        Set<DefaultEdge> edgesSet = graph.edgesOf(baseVertex); // Get all the edges of the base vertex we want to test
        // Double for loop evaluates the edges in pairs
        DefaultEdge[] edges = edgesSet.toArray(new DefaultEdge[edgesSet.size()]); // Convert edges to array to make it workable
        for(int i = 0; i < edges.length-1; i++) {
            for(int j = i + 1; j < edges.length; j++) {
                // Is the first vertex in the pair directly connected to the second vertex in the pair?
                DefaultEdge firstEdgeInPair = edges[i];
                DefaultEdge secondEdgeInPair = edges[j];
                String adjacentVertexA = Graphs.getOppositeVertex(graph, firstEdgeInPair, baseVertex);
                String adjacentVertexB = Graphs.getOppositeVertex(graph, secondEdgeInPair, baseVertex);
                // Test if the adjacent nodes are also connected to each other
                if(graph.containsEdge(adjacentVertexA, adjacentVertexB)) {
                    // There is a direct simple cycle, add it to the list
                    List<String> cycle = new ArrayList<String>();
                    cycle.add(adjacentVertexB);
                    cycle.add(baseVertex);
                    cycle.add(adjacentVertexA);
                    cycles.add(cycle);
                    System.out.println("Cycle found: " + cycle);
                } else {
                    // If they are not directly connected, look further into the graph
                    // Create the base vector to test on
                    List<String> baseVector = new ArrayList<String>();
                    baseVector.add(adjacentVertexA);
                    baseVector.add(baseVertex);
                    baseVector.add(adjacentVertexB);

                    evaluateVector(baseVector, cycles, neighborIndex);
                }
            }
        }
        return cycles;
    }

    private static List<List<String>> evaluateVector(List<String> vector, List<List<String>> cycles, NeighborIndex<String, DefaultEdge> neighborIndex) {
        String lastNode = vector.get(vector.size() - 1); // Last node in the vertex (the node we will be working with)
        String secondToLastNode = vector.get(vector.size() - 2); // The node before that
        String firstNode = vector.get(0); // The first node (which we are trying to circle back to)

        boolean cycleFound = false;

        List<String> neighbors = neighborIndex.neighborListOf(lastNode);
        for(String neighbor : neighbors) {
            // If the last node is connected to the first there is a cycle
            if(neighbor.equals(firstNode)) {
                // Cyles found, but check all the neighbours to be sure it's chordless!
                cycleFound = true;
            }

            // Is the last node connected to any internal node except adjacentVertexA or the previous node in the vector?
            // In other words, if it is connected to an internal node, discard the vector
            for(String node : vector) {
                if(neighbor.equals(node) && !node.equals(firstNode) && !node.equals(secondToLastNode)) {
                    return cycles;
                }
            }
        }

        // If we found a cycles AND we are sure it is chordless (in other words we didn't return yet after checking all the neighbours), add it to the list
        if(cycleFound) {
            // Output cycle
            cycles.add(vector);
            return cycles;
        }

        // We didn't find a cycle, but there is still a possibility one exists
        // For each neighbour create a new vector and evaluate it
        for(String neighbor : neighbors) {
            if(!neighbor.equals(secondToLastNode)) {
                List<String> candidateVector = new ArrayList<String>(vector);
                candidateVector.add(neighbor);
                cycles = evaluateVector(candidateVector, cycles, neighborIndex);
            }
        }
        return cycles;
    }
}
