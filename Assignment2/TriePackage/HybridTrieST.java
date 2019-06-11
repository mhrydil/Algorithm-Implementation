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
            if (x == null) x = new DLBNode<>();
            if (d == key.length()) {
                x.setData(val);
                return x;
            }
            char c = key.charAt(d);
            x.setNextNode(c, put(x.getNextNode(c), key, val, d + 1));
            return x;
        }
        else{// (treeType == 2){
            if (x == null) x = new DLBNode<>();
            if (d == key.length()) {
                x.setData(val);
                return x;
            }
            if (x instanceof DLBNode && x.getDegree() >= 11){
                x = new MTAlphaNode<V>((DLBNode)x);
            }
            char c = key.charAt(d);
            x.setNextNode(c, put(x.getNextNode(c), key, val, d+1));
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

    	// treeType = 0 --> multiway trie
    	// treeType = 1 --> DLB
    	// treeType = 2 --> hybrid

	// You must supply the methods for this class.  See test program
	// HybridTrieTest.java for details on the methods and their
	// functionality.  Also see handout TrieSTMT.java for a partial
	// implementation.
}
