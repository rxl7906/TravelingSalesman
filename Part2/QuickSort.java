import java.util.ArrayList;

/*
 * QuickSort.java - Quicksort Algorithm
 * 
 * @author: Robin Li rxl7906
 */

public class QuickSort {
	
	// quicksort function takes in edges and runs partition
	public static ArrayList<Edge> quickSort(ArrayList<Edge> edges) {
		partition(edges, 0, edges.size() - 1);
		return edges;
	}
	
	// partition edge array
	private static int partition(ArrayList<Edge> edges, int lo, int hi) {
		int i = lo;
		int j = hi;
		Edge v = edges.get(lo+(hi-lo)/2); // pivot

		while( i<=j ){
			while( less(edges.get(i), v) ) { // less comparison 
				i++;
			}
			while( less(v, edges.get(j) )) { // less comparison
				j--;
			}
			if( i <= j) {
				swap(edges,i,j); // swap two edges
				i++;
				j--;
			}
		}
		if(lo < j) {
			partition(edges,lo,j);
		}
		if(i < hi) {
			partition(edges,i, hi);
		}
		return j;
	}

	/* swap two edges in array*/
	public static void swap(ArrayList<Edge> edges, int x, int y) {
		Edge temp = edges.get(x);
		edges.set(x, edges.get(y) );
		edges.set(y, temp);
	}
	
	/* less comparison for two edges*/
	public static boolean less(Edge edge1, Edge edge2) {
		return (edge1.compareTo(edge2) < 0);
	}
}