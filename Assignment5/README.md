README.md

Assignment 5 models a computer network with a graph. It reads in a graph as a text file (see graph1.txt and graph2.txt). It creates a bidirectional weighted graph based on the .txt file. Assig5.java is the main client for this assignment. Based on different user input it will respond with different information.

M (inimum spanning tree) – show the vertices and edges (with weights) in the current minimum
spanning tree of the network. If the graph is not connected this commend should show the minimum
spanning tree of each connected subgraph.

• S i j – display the shortest path (by latency) from vertex i to vertex j in the graph. If vertices i and j are
not connected this fact should be stated.

• P i j x – display each of the distinct paths (differing by at least one edge) from vertex i to vertex j with
total weight less than or equal to x. All of the vertices and edges in each path should be shown and the
total number of distinct paths should be shown at the end.

• D i – node i in the graph will go down. All edges incident upon i will be removed from the graph as
well.

• U i – node i in the graph will come back up. All edges previously incident upon i (before it went down)
will now come back online with their previous weight values.

• C i j x – change the weight of edge (i, j) (and of edge (j, i)) in the graph to value x. If x is <= 0 the
edge(s) should be removed from the graph. If x > 0 and edge (i, j) previously did not exist, create it.

• Q – quit the program
