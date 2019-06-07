// CS 1501 Summer 2019
// MultiWay Trie Node implemented as an external class which
// implements the TrieNodeInt InterfaceAddress.  For this
// class it is assumed that all characters in any key will
// be letters between 'a' and 'z'.

package TriePackage;

import java.util.*;
public class MTAlphaNode<V> implements TrieNodeInt<V>
{
	private static final int R = 26;	// 26 letters in
										// alphabet

    protected V val;
    protected TrieNodeInt<V> [] next;
	protected int degree;

	// You must supply the methods for this class.  See TrieNodeInt.java
	// for the specifications.  See also handout MTNode.java for a
	// partial implementation.  Don't forget to include the conversion
	// constructor (passing a DLBNode<V> as an argument).
}
