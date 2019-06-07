// CS 1501 Summer 2019
// DLB Trie Node implemented as an external class which
// implements the TrieNodeInt<V> Interface

package TriePackage;

import java.util.*;
public class DLBNode<V> implements TrieNodeInt<V>
{
    protected Nodelet front;
    protected int degree;
	protected V val;
	
    protected class Nodelet
    {
    	protected char cval;
    	protected Nodelet rightSib;
    	protected TrieNodeInt<V> child;
    }
    		
	// You must supply the methods for this class.  See TrieNodeInt.java for the
	// interface specifications.  You will also need a constructor or two.
}
