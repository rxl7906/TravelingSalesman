import java.util.ArrayList;
import java.util.List;

/*
 * BinaryHeap.java - binary heap used to implement priority queue
 * and to quickly find minimum in an array
 * Author: Robin Li
 */
public class BinaryHeap {
	private List<Key> minHeap; // list of keys in minimum heap
	private int[] indices; // integer array holding locations in min heap
	
	/*
	 * Constructor: BinaryHeap is an array of keys
	 * Args: - n = number of nodes in graph
	 */
	public BinaryHeap(int n){
		minHeap = new ArrayList<Key>(n);
		// 1st key in minheap holds nothing
		minHeap.add(new Key(-1,-1,-1));
		indices = new int[n]; 
		for(int i = 0; i < n; i++){
			indices[i] = -1; // -1 means not in heap / empty slot
		}
	}
	
	// swap parent and child by index positions
	private void exch(int parent, int child){
		// perform typical swap operation for minHeap
		Key temp = minHeap.get(parent);
		minHeap.set(parent, minHeap.get(child));
		minHeap.set(child, temp);
		
		int parentid = minHeap.get(parent).id();
		int childid = minHeap.get(child).id();
		//swap on the indices array also
		int tmp = indices[parentid];
		indices[parentid] = indices[childid];
		indices[childid] = tmp;
	}
	
	/*
	 * swim: Promotion in a Heap
	 * - exchange child with parent
	 * - repeat until heap order is restored
	 */
	private void swim(int k){
		while(k > 1 && minHeap.get(k).compareTo(minHeap.get(k/2)) > 0) {
		      exch(k/2, k);
		      k = k/2;
		    }
	}
	
	/*
	 * sink: Demotion is a Heap
	 * - exchange parent with the higher priority child
	 * - repeat until heap order restored
	 */
	private void sink(int k){
	    int n = minHeap.size() - 1;
	    while ((2 * k) < n) {
	    	int j = (2 * k);
	    	// children of node at k are j = 2k
	    	// children of node at k are j = 2k+1
	    	if (j < n && minHeap.get(j).compareTo(minHeap.get(j+1)) < 0) j++;
	    	if (minHeap.get(k).compareTo(minHeap.get(j)) >= 0) break;
	    	exch(k, j);
	    	k = j;
	    }
	}
	
	/*
	 * insert: Insertion into a Heap
	 * - insert node at end of array, then swim it up
	 */
	public void insert(Key x){
		minHeap.add(x);
	    int N = minHeap.size() - 1;
	    indices[x.id()] = N;
	    swim(N);
	}
	
	/*
	 * delMax: Deletion from a Heap
	 * - remove the root(highest priority), replace
	 * it with the last node and sink it down
	 */
	public Key delMax(){
		int N = minHeap.size() - 1;
	    Key key = minHeap.get(1);
	    exch(1, N);
	    minHeap.remove(N);
	    indices[key.id()] = -1;
	    sink(1);
	    return key;
	}

	public boolean empty(){
		return minHeap.size() == 1;
	}
	
	
	// update the key if it has a lower priority
	private void update(Key key){
		// compare key to key in minHeap
		if (key.compareTo(minHeap.get(indices[key.id()])) > 0) {
		      minHeap.set(indices[key.id()], key); // set new minHeap key
		      swim(indices[key.id()]); // swim key up
		}
	}
	
	// insert into heap or update key
	public void insertOrUpdate(Key key){
		if (indices[key.id()] == -1) {
		      insert(key);
		    } else {
		      update(key);
		    }
	}
	
	// print heap 
	public void printHeap(){
		for(int i = 0; i < minHeap.size(); i++){
			System.out.println(minHeap.get(i).id());
		}
	}
	
	public String toString() {
	    String string = "{ ";
	    for (Key k : minHeap) {
	      string += k.toString();
	    }
	    string += " }";
	    return string;
	  }
	
	// for testing binary heap
	/*public static void main(String[] args){
		BinaryHeap h = new BinaryHeap(5);
	    Key k = new Key(0, 1, 1);
	    h.insertOrUpdate(k);
	    System.out.println(h.toString());
	    k = new Key(1, 2, 2);
	    h.insertOrUpdate(k);
	    System.out.println(h.toString());
	    h.delMax();
	    h.delMax();
	}*/
}
