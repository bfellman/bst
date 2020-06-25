package algorithms;

import main.BSTInterface;

import java.util.HashSet;
import java.util.Iterator;


class Node{
    boolean bst_dbg = false && true;

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
        if(bst_dbg) System.out.println("Size() for tree with key " + key);

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
        if(bst_dbg) System.out.println("Node::Node(" + key + ")");
        this.key = key;
        left = null;
        right = null;
        this.parent = parent;
    }

    public void swap(Node victim, Node child){

        if(bst_dbg) System.out.println("Node::Swap(" + key + ")" +  victim.key + " with " + (child == null ? "null" : child.key) + ")");

        if (victim == right){
            if(bst_dbg) System.out.println("Node::Swap - Setting " + (child == null ? "null" : child.key) + " as right chile");
            right = child;
        }
        else if (victim == left){
            if(bst_dbg) System.out.println("Node::Swap - Setting " + (child == null ? "null" : child.key) + " as left chile");
            left = child;
        }
        if (child != null){
            child.parent = this;
        }
        if(bst_dbg) System.out.println("Node::Swap done");
    }

    public Node smallest(){
        if (this.left == null){
            return this;
        }
        return this.left.smallest();
    }

    public Node find(int key) {
        if(bst_dbg) System.out.println("Node::find(" + key + "), this.key = " + this.key );
        if (key==this.key){
            if(bst_dbg) System.out.println( key + " == " + this.key + ", FOUND! ");
            return this;
        }
        if (key < this.key){
            if(bst_dbg) System.out.println( key + " < " + this.key);
            if (left == null){
                if(bst_dbg) System.out.println( "no right son - finished");
                return this;
            }
            return left.find(key);
        }
        if(bst_dbg) System.out.println( key + " > " + this.key);
        if (right == null){
            if(bst_dbg) System.out.println( "no left son - finished ");
            return this;
        }
        return right.find(key);

    }
}
public class BST implements BSTInterface {
    boolean bst_dbg = false && true;

    private final HashSet<Long> setA;
    private Node head;
    public BST() {
        setA = new HashSet<Long>();
        head = null;
    }

    public final boolean contains(final int key) {
        //return
        if (head == null){
            return false;
        }
        boolean res = head.find(key).key == key;
        assert setA.contains((long)key) == res;
        return res;
    }

    public final boolean insert(final int key) {
        setA.add((long)key);
        if(bst_dbg) System.out.println("BST::insert(" + key + ")" );

        if (head == null){
            head = new Node(key, null);
        }
        Node parent = head.find(key);
        if (parent.key == key){
            if(bst_dbg) System.out.println("BST::insert() - already contains " + key + " return false" );
            return false;
        }
        if(bst_dbg) System.out.println("BST::insert() - Inserting key " + key );

        if (key < parent.key){
            assert parent.left == null;
            parent.left = new Node(key, parent);
            if(bst_dbg) System.out.println("BST::insert() - as left child, size=" + head.size() );
            return true;
        }
        assert parent.right == null;
        parent.right = new Node(key, parent);
        if(bst_dbg) System.out.println("BST::insert() - as right child, size=" + head.size() );
        return true;
    }

    public final boolean remove(final int key) {
        setA.remove((long)key);
        if(bst_dbg) System.out.println("BST::remove(" + key + ")" );

        if (head == null){
            if(bst_dbg) System.out.println("BST::remove - Tree is empty");
            return false;
        }
        Node victim = head.find(key);
        if(key != victim.key){
            if(bst_dbg) System.out.println("BST::remove - " + key + "not in tree" );
            return false;
        }
        if(victim.left == null && victim.right == null) {
            if(bst_dbg) System.out.println("BST::remove - parent key (" + victim.parent.key + ") has no children" );
            victim.parent.swap(victim, null);
        }
        else if(victim.left == null){
            if(bst_dbg) System.out.println("BST::remove - parent key (" + victim.parent.key + ") has 1 right child" );
            victim.parent.swap(victim, victim.right);
        }
        else if(victim.right == null){
            if(bst_dbg) System.out.println("BST::remove - parent key (" + victim.parent.key + ") has 1 left child" );
            victim.parent.swap(victim, victim.left);
        }
        else {
            if(bst_dbg) System.out.println("BST::remove - victim key (" + victim.key + ") has 2 children (" + victim.left.key + " , " + victim.right.key + ")"  );

            Node successor = victim.right.smallest();
            assert successor.left == null;
            if (successor.right == null){
//                if(bst_dbg) System.out.println("BST::remove - successor has no right child");
//                successor.parent.left = null;
                successor.parent.swap(successor, null);
            }
            else {
//                if(bst_dbg) System.out.println("BST::remove - successor has a right child with key " + successor.right.key);
                successor.parent.swap(successor, successor.right);
            }

            successor.left = victim.left;
            successor.left.parent = successor;

            successor.right = victim.right;
            if (successor.right != null) {
                successor.right.parent = successor;
            }
            if(victim.parent == null) {
                assert head == victim;
            }
            else {
                victim.parent.swap(victim, successor);
            }
        }
        if(bst_dbg) System.out.println("BST::remove() - size=" + head.size() );
        return true;
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
        int ref = setA.size();
        int res = 0;
        if (head == null){
            res = 0;
        }
        else {
            res = head.size();
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
        if (head == null){
            res_sum = 0;
        }
        else {
            res_sum = head.sum();
        }
        assert res_sum == ref_sum;
        return res_sum;
    }
}
