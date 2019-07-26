import java.io.*;
import java.util.*;

public class Assig5 {

    public static void main(String[] args) throws IOException{
        if(args.length != 1){
            System.out.println("Please include a .txt file as an argument.");
            System.exit(0);
        }
        Scanner file = new Scanner(new FileReader(args[0]));
        Scanner input = new Scanner(System.in);
        MyEdgeWeightedDigraph myGraph = new MyEdgeWeightedDigraph(file);

        while(true){
            String option = input.nextLine();
            System.out.println("Command: " + option);
            System.out.println("-----------------------------");
            String[] strings = option.split("\\s"); // splits the line input by the user into an array of Strings

            if (strings[0].equals("R")) myGraph.report();
            if (strings[0].equals("S")){
                if(strings.length != 3) System.out.println("Invalid argument");
                else {
                    int i = Integer.parseInt(strings[1]);
                    int j = Integer.parseInt(strings[2]);
                    myGraph.shortestPath(i, j);
                }
            }
            if (strings[0].equals("M")) myGraph.min();
            if (strings[0].equals("P")){
                if (strings.length != 4 ) System.out.println("Invalid argument");
                else {
                    int i = Integer.parseInt(strings[1]);
                    int j = Integer.parseInt(strings[2]);
                    int k = Integer.parseInt(strings[3]);
                    myGraph.paths(i, j, k);
                }
            }
            if (strings[0].equals("D")){
                if (strings.length != 2 ) System.out.println("Invalid argument");
                else myGraph.down(Integer.parseInt(strings[1]));
            }
            if (strings[0].equals("U")){
                if (strings.length != 2 ) System.out.println("Invalid argument");
                else myGraph.up(Integer.parseInt(strings[1]));
            }
            if (strings[0].equals("C")){
                if (strings.length != 4 ) System.out.println("Invalid argument");
                else {
                    int i = Integer.parseInt(strings[1]);
                    int j = Integer.parseInt(strings[2]);
                    int k = Integer.parseInt(strings[3]);
                    myGraph.change(i, j, k);
                }
            }
            if (strings[0].equals("Q")) System.exit(0);

        }
    } //end main


}
