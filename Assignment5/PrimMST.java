import java.util.*;
/*
Modified version of PrimMSTTrace.java.
I removed the trace functions so this simply calculates the minimum spanning tree for the graph that is passed in.

 */
/******************************************************************************
 *  Compilation:  javac PrimMST.java
 *  Execution:    java PrimMST V E
 *  Dependencies: EdgeWeightedGraph.java Edge.java Queue.java IndexMinPQ.java
 *                UF.java
 *
 *  Prim's algorithm to compute a minimum spanning forest.
 *
 ******************************************************************************/

public class PrimMST {
    private DirectedEdge[] edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
    private double[] distTo;      // distTo[v] = weight of shortest such edge
    private boolean[] marked;     // marked[v] = true if v on tree, false otherwise
    private IndexMinPQ<Double> pq;

    public PrimMST(CC.ConnectedComp G) {
        edgeTo = new DirectedEdge[G.getV()];
        distTo = new double[G.getV()];
        marked = new boolean[G.getV()];
        pq = new IndexMinPQ<Double>(G.getV());
        for (int v = 0; v < G.getV(); v++) distTo[v] = Double.POSITIVE_INFINITY;

        for (int v = 0; v < G.getV(); v++)      // run from each vertex to find
            if (!marked[v]) prim(G, v);      // minimum spanning forest
    }

    // run Prim's algorithm in graph G, starting from vertex s
    private void prim(CC.ConnectedComp G, int s) {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(G, v);
        }
    }

    // scan vertex v
    private void scan(CC.ConnectedComp G, int v) {
        marked[v] = true;
        for (DirectedEdge e : G.getNeighbors(v)) {
            int w = e.to();
            if (marked[w])
            {
            	continue;         // v-w is obsolete edge
            }
            if (e.getWeight() < distTo[w]) {
                distTo[w] = e.getWeight();
                edgeTo[w] = e;
                if (pq.contains(w))
                {
                		pq.change(w, distTo[w]);
                }
                else              
                {
                		pq.insert(w, distTo[w]);
                }
            }
        }
    }


    // return iterator of edges in MST
    public Iterable<DirectedEdge> edges() {
        LinkedList<DirectedEdge> mst = new LinkedList<DirectedEdge>();
        for (int v = 0; v < edgeTo.length; v++) {
            DirectedEdge e = edgeTo[v];
            if (e != null) {
                mst.add(e);
            }
        }
        return mst;
    }


    // return weight of MST
    public double weight() {
        double weight = 0.0;
        for (DirectedEdge e : edges())
            weight += e.getWeight();
        return weight;
    }

}
