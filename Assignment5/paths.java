import java.util.*;

/*
This class finds all of the distinct paths from vertex i to vertex j with total weight less than k
 */
public class Paths {
    private boolean[] marked; //indicates whether or not vertex marked[v] has been used or not in the current path
    LinkedList<DirectedEdge> currPath; //used in the recursive process of finding a valid path
    ArrayList<LinkedList<DirectedEdge>> solutions; //when a valid solution is found, it will be added to this arrayList
    MyEdgeWeightedDigraph graph;

    public Paths(MyEdgeWeightedDigraph g){
        graph = g;
        marked = new boolean[graph.getV()];
        currPath = new LinkedList<>();
        solutions = new ArrayList<>();
    }

    public ArrayList<LinkedList<DirectedEdge>> findPaths(int source, int dest, double maxWeight){
        for(DirectedEdge neighbor : graph.neighbors(source)){
            marked[source] = true;
            if(graph.isActive(neighbor.from())) {
                int from = neighbor.from();
                int to = neighbor.to();

                // if the current neighbor is the destination and the weight is less than the maxWeight, add that neighbor
                // to the path and add that path to the solution.
                if (graph.isActive(to) && (to == dest) && !marked[to] && neighbor.getWeight() <= maxWeight){
                    currPath.add(neighbor);
                    solutions.add(new LinkedList<DirectedEdge>(currPath));
                    currPath.removeLast();
                }
                //otherwise, if
                else if (graph.isActive(to) && !marked[to] && neighbor.getWeight() <= maxWeight){
                    marked[to] = true;
                    currPath.add(neighbor);
                    findPaths(to, dest, maxWeight-neighbor.getWeight());
                    currPath.removeLast();
                    marked[to] = false;
                }
            }
        }

        return solutions;
    }


}
