import java.util.*;

public class CC {

    private boolean[] marked;                        // marked[v] = has vertex v been visited yet?
    private int[] id;                                //id[v] = which connected component is vertex v in?
    private int[] size;                              //size[c] = number of vertices in component containing v
    private int count;                               //number of connected components
    private LinkedList<DirectedEdge>[] components;   //components[id] = list of edges in component id
    private ConnectedComp[] graphs;                  //Array of connected component graphs for printing purposes


    public CC(MyEdgeWeightedDigraph G){
        marked = new boolean[G.getV()];
        id = new int[G.getV()];
        size = new int[G.getV()];
        components = (LinkedList<DirectedEdge>[]) new LinkedList[G.getV()];
        graphs = new ConnectedComp[G.getV()];
        for(int c=0; c<components.length; c++){
            components[c] = new LinkedList<>();
            graphs[c] = new ConnectedComp(G.getV()); //initialize each graph to be a new graph with G.getV() vertices
            id[c] = -1; //initialize the id of each vertex to -1 (not in a connected component)
        }
        count = 0;

        for(int v=0; v<G.getV(); v++){
            if(G.isActive(v) && !marked[v]){
                dfs(G, v);
                count++;
            }
        }

    }

    private void dfs(MyEdgeWeightedDigraph G, int v){
        marked[v] = true; // mark vertex v as seen
        id[v] = count; //the component that vertex v is in is count
        size[count]++; //increment the size of connected component v
        for(DirectedEdge neighbor : G.neighbors(v)){
            if(G.isActive(neighbor.to())){
                graphs[count].addEdge(neighbor);
                if(!marked[neighbor.to()]){
                    dfs(G, neighbor.to());
                }
            }
        }
    }

    //returns the number of connected components
    public int numComponents(){
        return count;
    }

    // returns the linked list of all the edges in the connected component
    public LinkedList<DirectedEdge> getComponent(int  id){
        return components[id];
    }

    public boolean isConnected(){
        return (count == 1);
    }

    public void printComps(){
        System.out.println("\033[1mThe connected components are:\033[0m");
        for(int i=0; i<count; i++){
            System.out.println("Component " + i + ":");
            System.out.print("Vertices in component " + i + ": ");
            for(int j=0; j<id.length; j++){
                if (id[j]==i) System.out.print(j + " "); //prints the vertex if that vertex is a member of CC j
            }
            System.out.println();
            if (size[i]==1) System.out.println("There are no edges in this component.");
            ConnectedComp graph = graphs[i]; //graphs[i] is the current connected component
            for(LinkedList<DirectedEdge> list : graph.getNeighbors()){ //list is the list of neighbors for each vertex
                if(list.size() > 0){
                    int vertexNum = list.get(0).from();
                    System.out.print(vertexNum + ": "); //print vertexNum:
                    for(DirectedEdge e : list){
                        System.out.print(e + " "); //print each edge in the list of neighbors for vertex
                    }
                    System.out.println();
                }
            }
            System.out.println();
        }
    }



}
