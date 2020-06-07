package algorithms;

import main.BSTInterface;

import java.util.HashSet;
import java.util.Iterator;
import java.util.HashSet;

public class BST implements BSTInterface {

    class Node {
        final int key; // key is immutable
        Node next;
        boolean marked;

        public Node(int key) { // Node constructor
            this.key = key;
            this.next = null;
            this.marked = false;
        }
    }
    final Node head;
    public BST() {
        head = new Node(Integer.MIN_VALUE);
        head.next = new Node(Integer.MAX_VALUE);
    }


    public final boolean contains(final int key) {
        Node curr = head;
        while (curr.key < key)
            curr = curr.next;
        return curr.key == key && !curr.marked;
    }

    public final boolean insert(final int key) {
        while (true) {
            Node pred = head;
            Node curr = pred.next;
            while (curr.key < key) {
                pred = curr; curr = curr.next;
            }
            synchronized(pred) {
                synchronized(curr) {
                    if (validate (pred, curr)) {
                        if (curr.key == key) {
                            return false;
                        } else {
                            Node node = new Node(key);
                            node.next = curr;
                            pred.next = node;
                            return true;
                        }
                    }
                }
            }
        }
    }

    public final boolean remove(final int key) {

        while (true) {
            Node pred = head;
            Node curr = pred.next;
            while (curr.key < key) {
                pred = curr; curr = curr.next;
            }
            synchronized(pred) {
                synchronized(curr) {
                    if (validate (pred, curr)) {
                        if (curr.key != key) {
                            return false;
                        } else {
                            curr.marked = true;
                            pred.next = curr.next;
                            return true;
                        }
                    }
                }
            }
        }
    }

    // Return your ID #
    public String getName() {
        return "301115168";
    }

    // Returns size of the tree.
    public final int size() {
    // NOTE: Guaranteed to be called without concurrent operations,
	// so need to be thread-safe.  The method will only be called
	// once the benchmark completes.
        Node iter = head;
        int count = 0;
        while(iter.next != null)
            count++;
        return count;
    }

    // Returns the sum of keys in the tree
    public final long getKeysum() {

    // NOTE: Guaranteed to be called without concurrent operations,
	// so no need to be thread-safe.
	//
	// Make sure to sum over a "long" variable or you will get incorrect
	// results due to integer overflow!
        Node iter = head;
        long sum = 0;
        while(iter.next != null)
            sum += iter.key;
        return sum;
    }
    boolean validate(Node pred, Node curr) {
        return !pred.marked && !curr.marked && pred.next == curr;
    }
}
