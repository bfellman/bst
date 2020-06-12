package algorithms;

import main.BSTInterface;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Collections;

public class BST implements BSTInterface {

    private final Set<Long> setA;

    public BST() {
        Set<Long> ss = new HashSet<Long>();
        setA = Collections.synchronizedSet(ss);
//	ConcurrentHashMap<Long,Long> M = new ConcurrentHashMap<>();
//	setA = M.newKeySet();

    }

    public final boolean contains(final int key) {
        return setA.contains((long)key);
    }

    public final boolean insert(final int key) {
        return setA.add((long)key);
    }

    public final boolean remove(final int key) {
        return setA.remove((long)key);
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
        return setA.size();
    }

    // Returns the sum of keys in the tree
    public final long getKeysum() {
    // NOTE: Guaranteed to be called without concurrent operations,
	// so no need to be thread-safe.
	//
	// Make sure to sum over a "long" variable or you will get incorrect
	// results due to integer overflow!
        long sum=0;
        Iterator<Long> iter = setA.iterator();
        while(iter.hasNext()){
            sum = sum + iter.next();
        }
    return sum;
    }
}
