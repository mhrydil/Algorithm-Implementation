import java.util.*;
import java.io.*;

public class MyEdgeWeightedDigraph {
    private final int V;
    private int E;
    private LinkedList<DirectedEdge>[] neighbors;
    private int numActive;
    private boolean[] active; //if active[i] is true, vertex i in the graph is "up"/active


    MyEdgeWeightedDigraph(int numV){
        if (numV < 0) throw new RuntimeException("Number of vertices must be nonnegative");
        V = numV;
        E = 0;
        neighbors = (LinkedList<DirectedEdge>[]) new LinkedList[V];
        for(int i=0; i<V; i++){
            neighbors[i] = new LinkedList<>();
        }
    }

    MyEdgeWeightedDigraph(){
        V = 0;
        E = 0;
        neighbors = null;
    }



    MyEdgeWeightedDigraph(Scanner file) throws IOException
    {
        V = file.nextInt();
        E = file.nextInt();
        neighbors = (LinkedList<DirectedEdge>[]) new LinkedList[V];
        active = new boolean[V];
        for(int i=0; i<V; i++){
            neighbors[i] = new LinkedList<>();
            active[i] = true; //initialize all vertices to active
            numActive++;
        }
        while(file.hasNext()){
            int v = file.nextInt();
            int w = file.nextInt();
            int weight = file.nextInt();
            DirectedEdge e1 = new DirectedEdge(v, w, weight);
            DirectedEdge e2 = new DirectedEdge(w, v, weight);
            neighbors[v].add(e1); //add edge e1 to the neighbors of vertex v
            neighbors[w].add(e2);
        }

    }

    public void addEdge(DirectedEdge edge){
        neighbors[edge.from()].add(edge); //Add this edge to the adjacency list for vertex edge.from()
        E++;
    }

    public void removeEdge(int from, int to){
        if(this.containsEdge(from, to)){
            for(int v=0; v<V; v++){
                if (neighbors[from].get(v).to() == to){
                    neighbors[from].remove(v);
                    break;
                }
            }
            E--;
        }

    }


    public void report(){

        //report whether or not the graph is connected.
        System.out.print("The network is currently ");
        if(this.isConnected()) System.out.println("connected.");
        else System.out.println("disconnected.");

        //Print the active vertices
        System.out.println();
        System.out.println("\033[1mThe following vertices are active: \033[0m");
        if(numActive > 0) {
            for (int i = 0; i < active.length; i++) {
                if (active[i]) System.out.print(i + " ");
            }
        }
        else System.out.println("There are currently no active vertices.");

        //print the inactive vertices
        System.out.println();
        System.out.println();
        System.out.println("\033[1mThe following vertices are inactive: \033[0m"); // '\033[1m' makes it bold '\033[0m' makes it non-bold again
        if(numActive < V) {
            for (int i = 0; i < active.length; i++) {
                if (!active[i]) System.out.print(i + " ");
            }
            System.out.println();
        }
        else System.out.println("There currently no inactive vertices.");
        System.out.println();

        //print the connected components
        printComps();
    }


    public void shortestPath(int i, int j){
        DijkstraSP sp = new DijkstraSP(this, i);
        if(sp.hasPathTo(j)) {
            System.out.print("The shortest path from " + i + " to " + j + " is: ");
            System.out.println(sp.distTo(j));
            Stack<DirectedEdge> path = sp.pathTo(j);
            while(!path.empty()){
                System.out.println(path.pop());
            }
            System.out.println();
        }
        else System.out.println("There is no path from " + i + " to " + j + ".");

    }

    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append("Number of vertices: " + V + NEWLINE + "Number of active edges: " + getNumActiveEdges() + NEWLINE);
        for (int v = 0; v < V; v++) {
            if (active[v]) {
                s.append(v + ": ");
                for (DirectedEdge e : neighbors[v]) {
                    if(active[e.to()]) {
                        s.append(e + "  ");
                    }
                }
                s.append(NEWLINE);
            }
        }
        return s.toString();
    }

    public void min(){
        CC components = new CC(this);
        components.printMST();
    }

    public void paths(int i, int j, int k){
        System.out.println("Distinct paths from " + i + " to " + j);
        int solutionCount = 1;
        Paths paths = new Paths(this);
        ArrayList<LinkedList<DirectedEdge>> solutions = paths.findPaths(i, j, k);
        if (solutions.size() == 0){
            System.out.println("There are no paths from " + i + " to " + j + " with weight less than or equal to " + k);
        }
        for(LinkedList<DirectedEdge> solution : solutions){
            double solutionWeight = 0;
            System.out.println("Path " + solutionCount + ": ");
            for(DirectedEdge edge : solution){
                System.out.print(edge + " ");
                solutionWeight+=edge.getWeight();
            }
            System.out.println();
            System.out.println("Path " + solutionCount + " total weight: " + solutionWeight);
            solutionCount++;
            System.out.println();
        }


    }

    public void down(int i){
        if (i >= V){
            System.out.println("The vertex number entered was too large.");
            return;
        }
        if(active[i]) {
            active[i] = false;
            numActive--;
            System.out.println("Vertex " + i + " has gone down.");
        }
        else System.out.println("Vertex " + i + " was already down.");
    }

    public void up(int i){
        if(!active[i]) {
            active[i] = true;
            numActive++;
            System.out.println("Vertex " + i + " is back online.");
        }
        else System.out.println("Vertex " + i + " was already online.");
    }



    public void change(int i, int j, int k){
        if (i >= V || j >= V){
            System.out.println("One(or both) of the vertices entered is too large.");
            return;
        }

        if (!this.containsEdge(i, j)){
            if (k==0) return; //edge already does not exist
            addEdge(new DirectedEdge(i, j, k));
            addEdge(new DirectedEdge(j, i, k));
        }


        else if (k <= 0){
            //go through the linked list until we find the correct edge, then remove it
            removeEdge(i, j);
            removeEdge(j, i);
        }

        else { //change the weight of the edge ij and ji
            for (DirectedEdge edge : neighbors(i)) {
                if (edge.to() == j) edge.setWeight(k);
            }
            for (DirectedEdge edge : neighbors(j)) {
                if (edge.to() == i) edge.setWeight(k);
            }
        }

    }



    public boolean isConnected(){
        for(int v=0; v<V; v++){
            if (!active[v]) return false;
        }
        CC components = new CC(this);
        return components.isConnected();
    }

    public void printComps() {
        CC components = new CC(this);
        components.printComps();
    }

    public Iterable<DirectedEdge> neighbors(int vertex){ //returns the neighbors of vertex as an iterable (for DFS)
        return neighbors[vertex];

    }

    public int getV(){ //returns the number of Vertices in graph
        return V;
    }

    public int getE(){
        return E;
    }

    private boolean containsEdge(int i, int v){
        boolean found = false;
        for (DirectedEdge edge : neighbors(i)){
            if (edge.to() == v){
                found = true;
                break;
            }

        }
        return found;
    }

    public boolean isActive(int v){
        return active[v];
    }

    private int getNumActiveEdges(){
        int num = 0;
        for(int i=0; i<V; i++){
            if(active[i]){
                for(DirectedEdge neighbor : neighbors[i]){
                    if(active[neighbor.to()]) num++;
                }
            }
        }
        return num;
    }

    public LinkedList<DirectedEdge> edges(){ //returns all the edges
        LinkedList<DirectedEdge> edges = new LinkedList<>();

        for (LinkedList<DirectedEdge> neighbor : this.neighbors) {
            edges.addAll(neighbor);
        }
        return edges;
    }





}
