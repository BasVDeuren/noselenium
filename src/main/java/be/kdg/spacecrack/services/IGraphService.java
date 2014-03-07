package be.kdg.spacecrack.services;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.util.List;

/**
 * Created by Janne on 7/03/14.
 */
public interface IGraphService {
    List<List<String>> calculateChordlessCyclesFromVertex(UndirectedGraph<String, DefaultEdge> graph, String baseVertex);
}
