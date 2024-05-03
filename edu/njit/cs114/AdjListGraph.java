package edu.njit.cs114;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: Ravi Varadarajan
 * Date created: 4/21/2024
 */
public class AdjListGraph extends AbstractGraph {

    private List<Edge>[] adjLists;

    public AdjListGraph(int n, boolean directed) {
        super(n, directed);
        init(n);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void init(int n) {
        adjLists = (List<Edge>[]) Array.newInstance(List.class, n);
        for (int i = 0; i < n; i++) {
            adjLists[i] = new LinkedList<>();
        }
    }

    @Override
    public void addGraphEdge(int u, int v, int weight) throws GraphException {
        if (v < 0 || v >= adjLists.length) {
            throw new GraphException("Invalid vertex " + v);
        }

        Edge matchingEdge = getEdge(u, v);
        if (matchingEdge != null) {
            throw new GraphException("Edge (u,v) already exists");
        }

        // Create edge and add to adjList
        Edge newEdge = new Edge(u, v, weight);
        adjLists[u].add(newEdge);

    }

    @Override
    public Iterator<Edge> getOutgoingEdges(int v) {
        if (v < 0 || v >= adjLists.length) {
            throw new IllegalArgumentException("Invalid vertex " + v);
        }
        return adjLists[v].iterator();
    }

    @Override
    public Edge delGraphEdge(int u, int v) {
        Iterator<Edge> iter = getOutgoingEdges(u);
        while (iter.hasNext()) {
            Edge edge = iter.next();
            if (edge.to == v) {
                iter.remove();
                return edge;
            }
        }

        return null;
    }

    /**
     * Get edge from u to v if it exists else null
     * 
     * @param u
     * @param v
     * @return
     */
    public Edge getEdge(int u, int v) {
        Iterator<Edge> iter = getOutgoingEdges(u);
        while (iter.hasNext()) {
            Edge edge = iter.next();
            if (edge.to == v) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public boolean isEdge(int u, int v) {
        return (getEdge(u, v) != null);
    }

    @Override
    public int weight(int u, int v) throws GraphException {
        Edge edge = getEdge(u, v);
        if (edge == null) {
            throw new GraphException("No edge from " + u + " to " + v + " exists");
        }
        return edge.weight;
    }

}
