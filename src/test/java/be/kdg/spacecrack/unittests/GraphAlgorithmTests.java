package be.kdg.spacecrack.unittests;

import be.kdg.spacecrack.services.GraphAlgorithm;
import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Created by Janne on 5/03/14.
 */
public class GraphAlgorithmTests extends BaseUnitTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    @Transactional
    public void calculateChordlessCyclesFromVertex_GraphWithCyclesFromVertex_ChordlessCyclesFound() {
        // Create graph to test
        UndirectedGraph<String, DefaultEdge> graph = createGraphWithCycles();

        // Calculate cycles
        // Returns a list of cycles
        // A cycles = an ordered list of vertices (but undirected)
        List<List<String>> cyclesFound = GraphAlgorithm.calculateChordlessCyclesFromVertex(graph, getBaseVertex());

        // Rotate to keep the order but synchronize the starting vertex
        for(List<String> cycle : cyclesFound) {
            int index = cycle.indexOf(getBaseVertex());
            Collections.rotate(cycle, -index); // Rotate the vertex to the index 0
        }

        // Expected output =
        /*
         * [B, A, D, E]
         * [B, A, D, G, H, I, F, C]
         */
        List<List<String>> cyclesExpected = new ArrayList<List<String>>();
        cyclesExpected.add(Arrays.asList("A", "D", "E", "B"));
        cyclesExpected.add(Arrays.asList("A", "D", "G", "H", "I", "F", "C", "B"));

        assertEquals("Chordless cycles should match", cyclesExpected, cyclesFound); // Test output
    }

    @Test
    @Transactional
    public void calculateChorlessCyclesFromVertex_GraphWithCyclesNotFromVertex_DifferentCyclesFound() {
        // Create graph to test
        UndirectedGraph<String, DefaultEdge> graph = createGraphWithCycles();

        // Calculate cycles
        // Returns a list of cycles
        // A cycles = an ordered list of vertices (but undirected)
        List<List<String>> cyclesFound = GraphAlgorithm.calculateChordlessCyclesFromVertex(graph, "B"); // A is the baseVertex, so take another one

        // Rotate to keep the order but synchronize the starting vertex
        for(List<String> cycle : cyclesFound) {
            int index = cycle.indexOf(getBaseVertex());
            Collections.rotate(cycle, -index); // Rotate the vertex to the index 0
        }

        // Expected output for baseVertex A (so they should not match) =
        /*
         * [B, A, D, E]
         * [B, A, D, G, H, I, F, C]
         */
        List<List<String>> cyclesExpected = new ArrayList<List<String>>();
        cyclesExpected.add(Arrays.asList("A", "D", "E", "B"));
        cyclesExpected.add(Arrays.asList("A", "D", "G", "H", "I", "F", "C", "B"));

        assertNotSame("Chordless cycles should not match for vertex A and vertex B", cyclesExpected, cyclesFound); // Test output
    }

    @Test
    @Transactional
    public void calculateChordlessCyclesFromVertex_GraphWithoutCycles_NoCyclesFound() {
        // Create graph without cycles to test
        UndirectedGraph<String, DefaultEdge> graph = createGraphWithoutCycles();

        // Get cycles
        List<List<String>> cyclesFound = GraphAlgorithm.calculateChordlessCyclesFromVertex(graph, "H"); // Test from H to test more than 1 node away

        assertTrue("List of cycles found should be empty", cyclesFound.isEmpty());
    }

    // Vertex to test if it is part of one or more cycles
    private String getBaseVertex() {
        return "A";
    }

    private UndirectedGraph<String, DefaultEdge> createGraphWithCycles() {
        UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

        String a = "A";
        String b = "B";
        String c = "C";
        String d = "D";
        String e = "E";
        String f = "F";
        String g = "G";
        String h = "H";
        String i = "I";

        Graphs.addEdgeWithVertices(graph, a, b);
        Graphs.addEdgeWithVertices(graph, b, c);
        Graphs.addEdgeWithVertices(graph, a, d);
        Graphs.addEdgeWithVertices(graph, b, e);
        Graphs.addEdgeWithVertices(graph, c, f);
        Graphs.addEdgeWithVertices(graph, d, e);
        Graphs.addEdgeWithVertices(graph, e, f);
        Graphs.addEdgeWithVertices(graph, d, g);
        Graphs.addEdgeWithVertices(graph, e, h);
        Graphs.addEdgeWithVertices(graph, f, i);
        Graphs.addEdgeWithVertices(graph, g, h);
        Graphs.addEdgeWithVertices(graph, h, i);

        return graph;
    }

    private UndirectedGraph<String, DefaultEdge> createGraphWithoutCycles() {
        UndirectedGraph<String, DefaultEdge> graph = new SimpleGraph<String, DefaultEdge>(DefaultEdge.class);

        String a = "A";
        String b = "B";
        String c = "C";
        String d = "D";
        String e = "E";
        String f = "F";
        String g = "G";
        String h = "H";
        String i = "I";

        Graphs.addEdgeWithVertices(graph, a,d);
        Graphs.addEdgeWithVertices(graph, b,e);
        Graphs.addEdgeWithVertices(graph, c,f);
        Graphs.addEdgeWithVertices(graph, d,g);
        Graphs.addEdgeWithVertices(graph, e,h);
        Graphs.addEdgeWithVertices(graph, f,i);
        Graphs.addEdgeWithVertices(graph, g,h);
        Graphs.addEdgeWithVertices(graph, h,i);

        return graph;
    }

}
