import java.util.*;


/*
This is a simple class that stores the data for each of the connected components in a graph.
CC.java adds the items to the connected components as the DFS function goes through each graph.
 */
public class ConnectedComp {
    private int V;
    private int E;
    private LinkedList<DirectedEdge>[] neighbors;

    public ConnectedComp(int max){
        V = max;
        E = 0;
        neighbors = (LinkedList<DirectedEdge>[]) new LinkedList[max];
        for(int i=0; i<max; i++){
            neighbors[i] = new LinkedList<DirectedEdge>();
        }
    }

    public void addEdge(DirectedEdge e){
        neighbors[e.from()].add(e);
        E++;
    }

    public LinkedList<DirectedEdge>[] getNeighbors(){
        return neighbors;
    }
}
