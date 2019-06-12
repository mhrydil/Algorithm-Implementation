// CS 1501 Summer 2019
// HybridTrieST<V> class

package TriePackage;

import java.util.*;
import java.io.*;

public class HybridTrieST<V> {

    private TrieNodeInt<V> root;
    int treeType = 0;

    public HybridTrieST(int type)
    {
        root = null;
        treeType = type;
    }

    public void put(String key, V val) {
        root = put(root, key, val, 0);
    }

    // This method requires us to create a new node -- which in turn requires
    // a class.  This is the only place where a MTNode<V> object is explicitly
    // used.


    private TrieNodeInt<V> put(TrieNodeInt<V> x, String key, V val, int d)
    {
        if (treeType == 0) {
            if (x == null) x = new MTAlphaNode<V>();
            if (d == key.length()) {
                x.setData(val);
                return x;
            }
            char c = key.charAt(d);
            x.setNextNode(c, put(x.getNextNode(c), key, val, d + 1));
            return x;
        }
        else if (treeType == 1){
            if (x == null) x = new DLBNode<V>();
            if (d == key.length()) {
                x.setData(val);
                return x;
            }
            char c = key.charAt(d);
            x.setNextNode(c, put(x.getNextNode(c), key, val, d + 1));
            return x;
        }
        else{// (treeType == 2){
            if (x == null) x = new DLBNode<V>();
            if (d == key.length()) {
                x.setData(val);
                return x;
            }

            char c = key.charAt(d);
            x.setNextNode(c, put(x.getNextNode(c), key, val, d+1));
            if (x instanceof DLBNode<?> && x.getDegree() == 12){
                x = new MTAlphaNode<>((DLBNode<V>)x);
            }
            return x;
        }
    }

    public V get(String key){
        TrieNodeInt<V> x = root;
        for (int i=0; i<key.length(); i++){
            x = x.getNextNode(key.charAt(i));
            if (x == null) return null;
        }
        return x.getData();
    }

    public int searchPrefix(String key){ //taken primarily from the TrieSTNew class from Assignment 1
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
        return getSize(root);
    }

    private int getSize(TrieNodeInt root){
        int size = 0;
        size = root.getSize();
        Iterable<TrieNodeInt<V>> iter = root.children();
        for(TrieNodeInt<V> child : iter){
            size += getSize(child);
        }
        return size;
    }

    public int[] degreeDistribution(){
        int[] distribution = new int[27];
        degreeDistribution(root, distribution);
        return distribution;
    }

    private void degreeDistribution(TrieNodeInt root, int[] dist){
        dist[root.getDegree()]++;
        Iterable<TrieNodeInt<V>> iter = root.children();
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

    private int countAlpha(TrieNodeInt root){
        int count = 0;
        if(root instanceof MTAlphaNode) count++;
        Iterable<TrieNodeInt<V>> iter = root.children();
        for(TrieNodeInt<V> child : iter){
            count += countAlpha(child);
        }
        return count;
    }

    private int countDLB(TrieNodeInt root){
        int count = 0;
        if(root instanceof DLBNode) count++;
        Iterable<TrieNodeInt<V>> iter = root.children();
        for(TrieNodeInt<V> child : iter){
            count += countDLB(child);
        }
        return count;
    }

    	// treeType = 0 --> multiway trie
    	// treeType = 1 --> DLB
    	// treeType = 2 --> hybrid

	// You must supply the methods for this class.  See test program
	// HybridTrieTest.java for details on the methods and their
	// functionality.  Also see handout TrieSTMT.java for a partial
	// implementation.
}
