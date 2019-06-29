// CS 1501 Summer 2019
// HybridTrieST<V> class

package TriePackageMod;

import java.util.*;
import java.io.*;

public class HybridTrieST<V> {

    private TrieNodeInt<V> root;
    int treeType = 0;
    private PrintWriter output;

    public HybridTrieST(int type)
    {
        root = null;
        treeType = type;
    }

    public void put(StringBuilder key, V val) {
        root = put(root, key, val, 0);
    }

    // This method requires us to create a new node -- which in turn requires
    // a class.  This is the only place where a MTNode<V> object is explicitly
    // used.


    private TrieNodeInt<V> put(TrieNodeInt<V> x, StringBuilder key, V val, int d)
    {
        if (treeType == 0) { // Trie contains only MTAlphaNodes
            if (x == null) x = new MTAlphaNode<V>();
            if (d == key.length()) {
                x.setData(val);
                return x;
            }
            char c = key.charAt(d);
            x.setNextNode(c, put(x.getNextNode(c), key, val, d + 1));
            return x;
        }
        else if (treeType == 1){ //Trie contains only DLBNodes
            if (x == null) x = new DLBNode<V>();
            if (d == key.length()) {
                x.setData(val);
                return x;
            }
            char c = key.charAt(d);
            x.setNextNode(c, put(x.getNextNode(c), key, val, d + 1));
            return x;
        }
        else{ // Trie contains both MTAlphaNodes and DLBNodes
            if (x == null) x = new DLBNode<V>(); //originally, all new nodes are DLBNodes because their degree is <12
            if (d == key.length()) {
                x.setData(val);
                return x;
            }

            char c = key.charAt(d);
            x.setNextNode(c, put(x.getNextNode(c), key, val, d+1));
            if (x instanceof DLBNode<?> && x.getDegree() == 12){
                x = new MTAlphaNode<>((DLBNode<V>)x);  //change the node to an MTAlphaNode from DLBNode
            }
            return x;
        }
    }

    public V get(StringBuilder key){
        TrieNodeInt<V> x = root;
        for (int i=0; i<key.length(); i++){
            x = x.getNextNode(key.charAt(i));
            if (x == null) return null;
        }
        return x.getData();
    }

    public int searchPrefix(StringBuilder key){ //taken primarily from the TrieSTNew class from Assignment 1
        int ans = 0;
        TrieNodeInt<V> curr = root;
        boolean done = false;
        int loc = 0; //position within the key

        // Loop until we get to the "end" loc or until we get to a
        // null pointer
        while (curr != null && !done)
        {
            // A key would be found in the node AFTER the last
            // pointer corresponding to a character in the key.  This
            // would be at the loc value == length of the key
            if (loc == key.length())
            {
                if (curr.getData() != null) // if Node has a value then
                {                                         // the key is a word in the
                    // TrieST
                    ans += 2;
                }
                if (curr.getDegree() > 0) // if Node has at least one child
                {                                        // then the key is a prefix to
                    // some longer key in the TrieST
                    ans += 1;
                }
                done = true;
            }
            else  // Move down to the next node using the current
            {         // character in the key
                curr = curr.getNextNode(key.charAt(loc));
                loc++;
            }
        }
        return ans;
    }

    public int getSize(){
        return getSize(root); //recursive method
    }

    private int getSize(TrieNodeInt root){ //recursive method
        int size = 0;
        size = root.getSize();
        Iterable<TrieNodeInt<V>> iter = root.children();
        for(TrieNodeInt<V> child : iter){
            size += getSize(child); //recursive call
        }
        return size;
    }

    public int[] degreeDistribution(){
        int[] distribution = new int[27];
        degreeDistribution(root, distribution); //recursive method
        return distribution;
    }

    private void degreeDistribution(TrieNodeInt root, int[] dist){ //recursive method
        dist[root.getDegree()]++;
        Iterable<TrieNodeInt<V>> iter = root.children(); //iterable allows us to iterate through the children of each node
        for(TrieNodeInt child : iter){
            degreeDistribution(child, dist);
        }
    }

    public int countNodes(int type){
        if (type == 1){ //counting MTAlphaNodes
            return countAlpha(root);
        }

        else{
            return countDLB(root);
        }
    }

    private int countAlpha(TrieNodeInt root){ //recursive call
        int count = 0;
        if(root instanceof MTAlphaNode<?>) count++;
        Iterable<TrieNodeInt<V>> iter = root.children();
        for(TrieNodeInt<V> child : iter){
            count += countAlpha(child); //recursive call - adds to count the count of all this nodes children
        }
        return count;
    }

    private int countDLB(TrieNodeInt root){
        int count = 0;
        if(root instanceof DLBNode<?>) count++;
        Iterable<TrieNodeInt<V>> iter = root.children();
        for(TrieNodeInt<V> child : iter){
            count += countDLB(child); //recursive call - adds to count the count of all this nodes children
        }
        return count;
    }

    public void save(String fileName) throws IOException
    {
        output = new PrintWriter(new FileWriter(fileName));
        save(root);
        output.close();
    }

    private void save(TrieNodeInt<V> root){
        if(root.getData() != null){
            output.println(root.getData()); //if the current node has data, it gets added to the file
        }
        Iterable<TrieNodeInt<V>> iter = root.children();
        for(TrieNodeInt<V> child : iter){
            save(child); //recursive call for each child of the current node
                            // because the nodes are all alphabetical, this is basically an in order traversal
                            // so they are output in alphabetical order
        }

    }

    	// treeType = 0 --> multiway trie
    	// treeType = 1 --> DLB
    	// treeType = 2 --> hybrid

	// You must supply the methods for this class.  See test program
	// HybridTrieTest.java for details on the methods and their
	// functionality.  Also see handout TrieSTMT.java for a partial
	// implementation.
}
