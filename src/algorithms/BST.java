package algorithms;

import main.BSTInterface;

import java.util.HashSet;
import java.util.Iterator;


class Node{
    boolean bst_dbg = false;

    final int key;
    Node left;
    Node right;
    Node parent;

    void print_in_order(){

        if(right != null){
            right.print_in_order();
        }
        System.out.println(key);
        if(left != null){
            left.print_in_order();
        }
    }
    int size(){
        //if(bst_dbg) System.out.println("thread " + Thread.currentThread() + " Size() for tree with key " + key);

        int left_size = 0;
        int right_size = 0;
        if(right != null){
            right_size = right.size();
        }
        if(left != null){
            left_size = left.size();
        }
        return right_size + left_size + 1;
    }
    long sum(){
        long left_sum = 0;
        long right_sum = 0;
        if(right != null){
            right_sum = right.sum();
        }
        if(left != null){
            left_sum = left.sum();
        }
        return right_sum + left_sum + key;
    }
    public Node(int key, Node parent) {
        //if(bst_dbg) System.out.println("thread " + Thread.currentThread() + " Node::Node(" + key + ")");
        this.key = key;
        left = null;
        right = null;
        this.parent = parent;
    }

    public void swap(Node victim, Node child, String prefix){

        if(bst_dbg) System.out.println(prefix + "Node::Swap 1 parent (" + key + ")" + "victim " +victim.key + " new child " + (child == null ? "null" : child.key) + ")");

        if (victim == right){
            if(bst_dbg) System.out.println(prefix + "Node::Swap  2 - Setting " + (child == null ? "null" : child.key) + " as right chile");
            right = child;
        }
        else if (victim == left){
            if(bst_dbg) System.out.println(prefix + "Node::Swap 3 - Setting " + (child == null ? "null" : child.key) + " as left chile");
            left = child;
        }
        if (child != null){
            child.parent = this;
        }
//        if(bst_dbg) System.out.println(prefix + "Node::Swap 4 done");
    }

    public Node smallest(){
        if (this.left == null){
            return this;
        }
        return this.left.smallest();
    }

    public Node find(int key) {
        if (key==this.key){
            if(bst_dbg) System.out.println( "Node::find 1 (" + key + ")" + " == " + this.key);
            return this;
        }
        if (key < this.key){
            if (left == null){
                if(bst_dbg) System.out.println( "Node::find 2 (" + key + ")" + "> " + this.key + " and no left son - finished ");
                return this;
            }
            return left.find(key);
        }
        if (right == null){
            if(bst_dbg) System.out.println( "Node::find 3 (" + key + ")" + "> " + this.key + " and no right son - finished ");
            return this;
        }
        return right.find(key);

    }
}
public class BST implements BSTInterface {
    boolean bst_dbg = true;

    private final HashSet<Long> setA;
    private Node dummy;
    Node head() { return dummy.left; };
    public BST() {
        setA = new HashSet<Long>();
        dummy =  new Node(Integer.MAX_VALUE,null);
    }

    public final boolean contains(final int key) {
        //return
        if (head() == null){
            return false;
        }
        boolean res = head().find(key).key == key;
        assert setA.contains((long)key) == res;
        return res;
    }

    public final boolean insert(final int key) {
        assert this.size() == setA.size();

        boolean ref = setA.add((long)key);
        long ref_size = setA.size();

        if(bst_dbg) System.out.println("thread " + Thread.currentThread() + " ----------\nBST::insert(" + key + ")" );

        if (head() == null){
            dummy.left = new Node(key, dummy);
            assert ref;
            assert this.size() == ref_size;
            return true;
        }
        Node parent = head().find(key);

        if (parent.key == key){
            if(bst_dbg) System.out.println("thread " + Thread.currentThread() + " BST::insert() - already contains " + key + " return false" );
            assert !ref;
            assert this.size() == ref_size;
            return false;
        }
        //synchronized(parent){
            if(bst_dbg) System.out.println("thread " + Thread.currentThread() + " BST::insert() - Inserting key " + key );

            if (key < parent.key){
                assert parent.left == null;
                parent.left = new Node(key, parent);
                if(bst_dbg) System.out.println("thread " + Thread.currentThread() + " BST::insert() - as left child, size=" + this.size() );
                assert ref;
                assert this.size() == ref_size;
                return true;
            }
            assert parent.right == null;
            parent.right = new Node(key, parent);
            if(bst_dbg) System.out.println("thread " + Thread.currentThread() + " BST::insert() - as right child, size=" + this.size() );
            assert ref;
            assert this.size() == ref_size;
            return true;
        }//}

    public final boolean remove(final int key) {
        assert this.size() == setA.size();

        boolean ref = setA.remove((long)key);
        if(bst_dbg) System.out.println("thread " + Thread.currentThread() + " --------\nBST::remove(" + key + ")" );

        if (head() == null){
            if(bst_dbg) System.out.println("thread " + Thread.currentThread() + " BST::remove - Tree is empty");
            return false;
        }
        Node victim = head().find(key);
        //synchronized (victim.parent){
            //synchronized (victim){
                if(key != victim.key){
                    if(bst_dbg) System.out.println("thread " + Thread.currentThread() + " BST::remove - " + key + " not in tree" );

                    assert !ref;
                    assert this.size() == setA.size();
                    return false;
                }

                if(victim.left == null && victim.right == null) {
                    if(bst_dbg) System.out.println("thread " + Thread.currentThread() + " BST::remove - victim key (" + victim.key + ") has no children" );
                    victim.parent.swap(victim, null,"BST::remove(" + key + ")" );
                }
                else if(victim.left == null){
                    if(bst_dbg) System.out.println("thread " + Thread.currentThread() + " BST::remove - victim key (" + victim.key + ") has 1 right child" );
                    victim.parent.swap(victim, victim.right,"BST::remove(" + key + ")" );
                }
                else if(victim.right == null){
                    if(bst_dbg) System.out.println("thread " + Thread.currentThread() + " BST::remove - victim  key (" + victim.key + ") has 1 left child" );
                    victim.parent.swap(victim, victim.left,"BST::remove(" + key + ")" );
                }
                else {

                    Node successor = victim.right.smallest();
                    if(bst_dbg) System.out.println("thread " + Thread.currentThread() + " BST::remove - victim key (" + victim.key + ") has 2 children (" + victim.left.key + " , " + victim.right.key + "), succ=" + successor.key );
                    assert successor.left == null;
                    successor.parent.swap(successor, successor.right, "BST::remove(" + key + ")" );

                    successor.left = victim.left;
                    successor.left.parent = successor;

                    successor.right = victim.right;
                    if (successor.right != null) {
                        successor.right.parent = successor;
                    }
                    if(victim.parent == null) {
                        assert false;
                        assert head() == victim;
                        //dummy.left=successor;
                        successor.parent=null;
                    }
                    else {
                        victim.parent.swap(victim, successor, "BST::remove(" + key + ")" );
                    }
                }
                long res = this.size();
                if(bst_dbg) System.out.println("thread " + Thread.currentThread() + " BST::remove() - size = " + res );
                assert ref;
                assert res == setA.size();
                return true;
            }//}}

    // Return your ID #
    public String getName() {
        return "301115168";
    }

    // Returns size of the tree.
    public final int size() {
        // NOTE: Guaranteed to be called without concurrent operations,
        // so need to be thread-safe.  The method will only be called
        // once the benchmark completes.
        int ref = setA.size();
        int res = 0;
        if (head() == null){
            res = 0;
        }
        else {
            res = head().size();
        }
        assert res == ref;
        return res;
    }

    // Returns the sum of keys in the tree
    public final long getKeysum() {
        // NOTE: Guaranteed to be called without concurrent operations,
        // so no need to be thread-safe.
        //
        // Make sure to sum over a "long" variable or you will get incorrect
        // results due to integer overflow!
        long ref_sum=0;
        Iterator<Long> iter = setA.iterator();
        while(iter.hasNext()){
            ref_sum = ref_sum + iter.next();
        }

        long res_sum;
        if (head() == null){
            res_sum = 0;
        }
        else {
            res_sum = head().sum();
        }
        assert res_sum == ref_sum;
        return res_sum;
    }
}
