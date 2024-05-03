package edu.njit.cs114;

import java.util.*;

/**
 * Author: Ravi Varadarajan
 * Date created: 4/21/2024
 */
public class GraphSearch {

    public static final int VISITED = 1;
    public static final int UNVISITED = 0;

    public static void printDfsTreeEdges(Graph g, List<Graph.Edge> treeEdges) {
        int lastVertex = -1;
        for (Graph.Edge edge : treeEdges) {
            if (edge.from != lastVertex) {
                if (lastVertex >= 0) {
                    System.out.println();
                }
                System.out.print(edge.from + " ---> " + edge.to);
            } else {
                System.out.print(" ---> " + edge.to);
            }
            lastVertex = edge.to;
        }
        System.out.println();
    }

    public static void printBfsTreeEdges(Graph g, List<Graph.Edge> treeEdges) {
        int fromVertex = -1;
        for (Graph.Edge edge : treeEdges) {
            if (edge.from != fromVertex) {
                if (fromVertex >= 0) {
                    System.out.println();
                }
                System.out.print(edge.from + "(" + g.getMark(edge.from) +
                        ")" + " ---> " +
                        edge.to + "(" + g.getMark(edge.to) + ")");
            } else {
                System.out.print("," + edge.to + "(" + g.getMark(edge.to) + ")");
            }
            fromVertex = edge.from;
        }
        System.out.println();
    }

    public static void graphTraverseBFS(Graph g) {
        System.out.println("breadth-first search of graph..");
        for (int v = 0; v < g.numVertices(); v++) {
            g.setMark(v, UNVISITED);
        }
        for (int v = 0; v < g.numVertices(); v++) {
            if (g.getMark(v) == UNVISITED) {
                System.out.println("Start vertex : " + v);
                LinkedList<Graph.Edge> treeEdges = bfs(g, v, -1);
                printBfsTreeEdges(g, treeEdges);
            }
        }
    }

    public static void graphTraverseDFS(Graph g) {
        System.out.println("depth-first search of graph..");
        for (int v = 0; v < g.numVertices(); v++) {
            g.setMark(v, UNVISITED);
        }
        for (int v = 0; v < g.numVertices(); v++) {
            if (g.getMark(v) == UNVISITED) {
                System.out.println("Start vertex : " + v);
                LinkedList<Graph.Edge> treeEdges = dfs(g, v, -1);
                printDfsTreeEdges(g, treeEdges);
            }
        }
    }

    // do a DFS starting from vertex start and optionally ending at vertex end if
    // end >=0
    public static LinkedList<Graph.Edge> dfs(Graph g, int v, int end) {
        g.setMark(v, VISITED);
        Iterator<Graph.Edge> outEdgeIter = g.getOutgoingEdges(v);
        // search-tree edges rooted at v
        LinkedList<Graph.Edge> subTreeEdges = new LinkedList<>();
        while (outEdgeIter.hasNext()) {
            // (v, w)
            Graph.Edge edge = outEdgeIter.next();
            int w = edge.to;
            if (g.getMark(w) == UNVISITED) {
                subTreeEdges.add(edge);
                if (w == end) {
                    return subTreeEdges;
                }
                subTreeEdges.addAll(dfs(g, w, end));
            }
        }

        return subTreeEdges;
    }

    // do a BFS starting from vertex start and optionally ending at vertex end if
    // end >=0
    public static LinkedList<Graph.Edge> bfs(Graph g, int start, int end) {
        Queue<Integer> vertexQueue = new LinkedList<Integer>();
        vertexQueue.add(start);
        g.setMark(start, 1);
        LinkedList<Graph.Edge> treeEdges = new LinkedList<>();
        while (!vertexQueue.isEmpty()) {

            int u = (int) vertexQueue.poll();
            Iterator<Graph.Edge> outgoingEdges = g.getOutgoingEdges(u);

            while (outgoingEdges.hasNext()) {
                Graph.Edge edge = outgoingEdges.next();
                int v = edge.to;
                if (g.getMark(v) == UNVISITED) {
                    g.setMark(v, 1 + g.getMark(u));
                    vertexQueue.add(v);
                    treeEdges.add(edge);
                    if (v == end) {
                        return treeEdges;
                    }

                }
            }

        }
        return treeEdges;
    }

    /**
     * Find a path in the graph from fromVertex to toVertex using BFS or DFS
     * 
     * @param g
     * @param fromVertex
     * @param toVertex
     * @param isBfs      use BFS if set to true else use DFS
     * @return
     */
    public static List<Integer> graphPath(Graph g, int fromVertex, int toVertex,
            boolean isBfs) {
        for (int v = 0; v < g.numVertices(); v++) {
            g.setMark(v, UNVISITED);
        }
        LinkedList<Integer> pathVertices = new LinkedList<>();
        LinkedList<Graph.Edge> treeEdges = isBfs ? bfs(g, fromVertex, toVertex) : dfs(g, fromVertex, toVertex);
        // get edges in the path starting from toVertex
        Iterator<Graph.Edge> edgeIter = treeEdges.descendingIterator();
        int lastVertex = toVertex;

        while (edgeIter.hasNext()) {
            Graph.Edge edge = edgeIter.next();
            if (edge.to == lastVertex) {
                pathVertices.addFirst(edge.to);
                if (edge.from == fromVertex) {
                    pathVertices.addFirst(edge.from);
                }
                lastVertex = edge.from;
            }
        }

        return pathVertices;
    }

    /**
     * (complete it for homework assignment)
     * Returns true if a cycle exists in undirected graph
     * 
     * @param g undirected graph
     * @return
     */
    public static boolean cycleExists(Graph g) {

        // Mark all vertices unvisited
        for (int v = 0; v < g.numVertices(); v++) {
            g.setMark(v, UNVISITED);
        }

        // Run modified DFS on all unvisited vertifices
        for (int v = 0; v < g.numVertices(); v++) {
            if (g.getMark(v) == UNVISITED) {
                if (cycleExists(g, v, -1)) { // If cycleExists then immediately return true
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean cycleExists(Graph g, int v, int parent) {
        g.setMark(v, VISITED);
        // Get all outgoing edges of v
        Iterator<Graph.Edge> outEdgeIter = g.getOutgoingEdges(v);

        while (outEdgeIter.hasNext()) {
            Graph.Edge edge = outEdgeIter.next();
            // (v, w)
            int w = edge.to;
            // If w is unvisited, recursively call cycleExist on a new vertex
            if (g.getMark(w) == UNVISITED) {
                cycleExists(g, w, v);
            }
            // If vertex is visited and is not the parent, there is a cycle, and return true
            else if (g.getMark(w) != UNVISITED && w != parent) {
                return true;
            }
        }
        return false;
    }

    /**
     * (complete it for homework assignment)
     * Returns true if a simple cycle with odd number of edges exists in undirected
     * graph
     * 
     * @param g undirected graph
     * @return
     */
    public static boolean oddCycleExists(Graph g) {
        // Mark all vertices unvisited
        for (int v = 0; v < g.numVertices(); v++) {
            g.setMark(v, UNVISITED);
        }

        // Run modified BFS on all unvisited vertifices
        for (int v = 0; v < g.numVertices(); v++) {
            if (g.getMark(v) == UNVISITED) {
                if (oddCycleExists(g, v)) { // If cycleExists then immediately return true
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean oddCycleExists(Graph g, int startVertex) {
        Queue<Integer> vertexQueue = new LinkedList<Integer>();
        vertexQueue.add(startVertex);
        g.setMark(startVertex, 1);

        while (!vertexQueue.isEmpty()) {

            // Remove v from queue
            int v = (int) vertexQueue.poll();

            // Get all outgoing edges of v
            Iterator<Graph.Edge> outgoingEdges = g.getOutgoingEdges(v);

            while (outgoingEdges.hasNext()) {
                Graph.Edge edge = outgoingEdges.next();
                // (v, w)
                int w = edge.to;
                // If vertex is visited and is at same level, there is an odd cycle
                // so return true
                if (g.getMark(w) != UNVISITED && g.getMark(w) == g.getMark(v)) {
                    return true;
                }
                // If w is unvisited, set mark, and add to queue to continue BFS
                else if (g.getMark(w) == UNVISITED) {
                    g.setMark(w, 1 + g.getMark(v));
                    vertexQueue.add(w);
                }
            }

        }
        return false;
    }

    /**
     * Find the diameter (length of longest path) in the tree
     * 
     * @param tree (undirected graph which is a tree)
     * @return
     */
    public static int diameter(Graph tree) {
        // Mark all vertices unvisited
        for (int v = 0; v < tree.numVertices(); v++) {
            tree.setMark(v, UNVISITED);
        }

        // Call dfs from any vertex
        dfsWithLevels(tree, 0, 0);

        // Iterate through all verticies. Whichever one has the highest mark, save that
        // vertex
        int highestMark = -1;
        int highestMarkedVertex = -1;
        for (int v = 0; v < tree.numVertices(); v++) {
            if (tree.getMark(v) > highestMark) {
                highestMark = tree.getMark(v);
                highestMarkedVertex = v;
            }
        }

        // Mark all vertices unvisited
        for (int v = 0; v < tree.numVertices(); v++) {
            tree.setMark(v, UNVISITED);
        }

        // Call dfs from the saved vertex for the maxLevel
        int maxLevel = dfsWithLevels(tree, highestMarkedVertex, 0);

        return maxLevel - 1;
    }

    public static int dfsWithLevels(Graph tree, int v, int level) {
        tree.setMark(v, level);
        int height = 0; // Save height

        Iterator<Graph.Edge> outEdgeIter = tree.getOutgoingEdges(v);
        // search-tree edges rooted at v
        while (outEdgeIter.hasNext()) {
            Graph.Edge edge = outEdgeIter.next();
            // (v, w)
            int w = edge.to;
            // If w is unvisited, recursively call on w on next level.
            // If the result of the recur call is greater than height, update
            if (tree.getMark(w) == UNVISITED) {
                int dfsResult = dfsWithLevels(tree, w, level + 1);
                height = Math.max(height, dfsResult);
            }
        }

        // Return height + 1 to account for the current level
        return 1 + height;
    }

    /**
     * Does the directed graph have a cycle of directed edges ? (Extra credit)
     * 
     * @param g
     * @return
     */
    public static boolean cycleExistsDirect(Graph g) {
        // Mark all vertices unvisited
        for (int v = 0; v < g.numVertices(); v++) {
            g.setMark(v, UNVISITED);
        }

        // Run modified DFS on all unvisited vertifices
        for (int v = 0; v < g.numVertices(); v++) {
            if (g.getMark(v) == UNVISITED) {
                if (cycleExistsDirect(g, v, -1)) // -1 indicates no parent
                    return true;
            }
        }

        return false;
    }

    public static boolean cycleExistsDirect(Graph g, int v, int parent) {
        int IN_PATH = 2;
        g.setMark(v, IN_PATH);
        Iterator<Graph.Edge> outEdgeIter = g.getOutgoingEdges(v);
        while (outEdgeIter.hasNext()) {
            // (v, w)
            Graph.Edge edge = outEdgeIter.next();
            int w = edge.to;
            // If in path, that means we encounter that vertex again in the traversal,
            // Thus thee is a cycle
            if (g.getMark(w) == IN_PATH) {
                return true;
            }
            // If visited, recursively call DFS
            else if (g.getMark(w) != UNVISITED) {
                if (cycleExistsDirect(g, w, v)) {
                    return true;
                }
            }
        }
        // Remark vertex from in_path to visited
        g.setMark(v, VISITED);
        return false;
    }

    public static void main(String[] args) throws Exception {
        Graph g = new AdjListGraph(8, true);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(0, 3);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 0);
        g.addEdge(3, 2);
        g.addEdge(4, 3);
        g.addEdge(4, 6);
        g.addEdge(5, 7);
        g.addEdge(6, 5);
        g.addEdge(7, 5);
        g.addEdge(7, 6);
        System.out.println(g);
        graphTraverseBFS(g);
        graphTraverseDFS(g);
        // System.out.println("Directed cycle exists in g ? " + cycleExistsDirect(g));
        g = new AdjListGraph(8, false);
        g.addEdge(0, 1);
        g.addEdge(1, 3);
        g.addEdge(3, 2);
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        g.addEdge(5, 7);
        g.addEdge(4, 6);
        System.out.println(g);
        graphTraverseBFS(g);
        graphTraverseDFS(g);
        // System.out.println("Cycle exists in g ? " + cycleExists(g));
        g = new AdjListGraph(8, false);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);
        g.addEdge(1, 4);
        g.addEdge(3, 2);
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        g.addEdge(6, 5);
        g.addEdge(5, 7);
        g.addEdge(7, 6);
        System.out.println(g);
        graphTraverseBFS(g);
        graphTraverseDFS(g);
        // System.out.println("Cycle exists in g ? " + cycleExists(g));
        // System.out.println("Odd cycle exists in g ? " + oddCycleExists(g));
        // g = new AdjListGraph(7, false);
        // g.addEdge(0,1);
        // g.addEdge(0,2);
        // g.addEdge(0,3);
        // g.addEdge(2,4);
        // g.addEdge(2,5);
        // g.addEdge(3,6);
        // System.out.println(g);
        // System.out.println("Cycle exists in g ? " + cycleExists(g));
        // System.out.println("Diameter of g = " + diameter(g));
        // assert diameter(g) == 4;
        // g = new AdjListGraph(7, false);
        // g.addEdge(0,1);
        // g.addEdge(1,2);
        // g.addEdge(2,3);
        // g.addEdge(3,0);
        // g.addEdge(3,4);
        // g.addEdge(4,5);
        // g.addEdge(5,6);
        // g.addEdge(6,3);
        // System.out.println(g);
        // System.out.println("Cycle exists in g ? " + cycleExists(g));
        // System.out.println("Odd cycle exists in g ? " + oddCycleExists(g));
    }

}
