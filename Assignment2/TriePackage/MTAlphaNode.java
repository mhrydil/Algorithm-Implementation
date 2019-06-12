// CS 1501 Summer 2019
// MultiWay Trie Node implemented as an external class which
// implements the TrieNodeInt InterfaceAddress.  For this
// class it is assumed that all characters in any key will
// be letters between 'a' and 'z'.

package TriePackage;

import java.util.*;
public class MTAlphaNode<V>  implements TrieNodeInt<V>
{
	private static final int R = 26;	// 26 letters in
										// alphabet

    protected V val; //Reference is 4 bytes
    protected TrieNodeInt<V> [] next; //Array of references
	protected int degree; //primitive type - int is 4 bytes

	public MTAlphaNode() {
	    val = null;
	    next = (TrieNodeInt<V>[]) new TrieNodeInt<?>[R];
	    degree = 0;
    }

    public MTAlphaNode(V data){
	    val = data;
	    degree = 0;
	    next = (TrieNodeInt<V>[]) new TrieNodeInt<?>[R];
    }

    public MTAlphaNode(DLBNode<V> oldNode) {
        val = oldNode.getData();
        degree = oldNode.getDegree();
        next = (TrieNodeInt<V>[]) new TrieNodeInt<?>[R];
        DLBNode.Nodelet temp = oldNode.front;
        while (temp != null) {
            next[temp.cval - 97] = temp.child;
            //System.out.print(temp.cval); //for testing purposes
            temp = temp.rightSib;
        }
        //System.out.println();//for testing purposes
    }

    // Return the next node in the trie corresponding to character
    // c in the current node, or null if there is not next node for
    // that character.
    public TrieNodeInt<V> getNextNode(char c){
        if (c < 97 || c > 122){
            throw new IllegalArgumentException("Please enter a lowercase character");
        }
	    return next[c-97];
    }

    // Set the next node in the trie corresponding to character char
    // to the argument node.  If the node at that position was previously
    // null, increase the degree of this node by one (since it is now
    // branching by one more link).
    public void setNextNode(char c, TrieNodeInt<V> node)
    {
	    if (c < 97 || c > 122){
            throw new IllegalArgumentException("Please enter a lowercase character");
        }
	    if(next[c-97] == null){
	        degree++;
        }
        next[c-97] = node;
    }

    // Return the data at the current node (or null if there is no data)
    public V getData(){
	    return val;
    }

    // Set the data at the current node to the data argument
    public void setData(V data){
        val = data;
    }

    // Return the degree of the current node.  This corresponds to the
    // number of children that this node has.
    public int getDegree(){
	    return degree;
    }

    // Return the approximate size in bytes of the current node.  This is
    // a very rough approximation based on the following:
    // 1) Assume each reference in a node will use 4 bytes (whether it is
    //    used or it is null)
    // 2) Assume any primitive type is its specified size (see Java reference
    //    for primitive type sizes in bytes)
    // Note that the actual size of the node is implementation dependent and
    // is not specified in the Java language.  There are tools to give a better
    // approximation of this value but for our purposes, this approximation is
    // fine.
    public int getSize(){
	    return (R*4)+8;
    }

    // Return an Iterable collection of the references to all of the children
    // of this node.  Do not put any null references into this result.  The
    // order of the children as stored in the TrieNodeInt<V> node must be
    // maintained in the returned Iterable.  The easiest way to do this is to
    // put all of the references into a Queue and to return the Queue (since a
    // Queue implements Iterable and maintains the order of the children).
    // This method will allow us to access all of the children of a node without
    // having to know how the node is actually implemented.
    public Iterable<TrieNodeInt<V>> children(){
	    Queue<TrieNodeInt<V>> iterableQueue = new LinkedList<>();
	    for (int i=0; i<R; i++){
	        if (next[i] != null){
	            iterableQueue.add(next[i]);
            }
        }
	    return iterableQueue;
    }

	// You must supply the methods for this class.  See TrieNodeInt.java
	// for the specifications.  See also handout MTNode.java for a
	// partial implementation.  Don't forget to include the conversion
	// constructor (passing a DLBNode<V> as an argument).
}
