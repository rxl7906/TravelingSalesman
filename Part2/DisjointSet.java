/*
 * DisjointSet.java - disjoint sets ADT. 
 * Performs union-by-rank and path compression.
 * 
 * @author: Robin Li rxl7906
 */
class DisjointSet {
	private int[] index;
	private int[] rank;
	
	/* Constructor DisjointSet*/
	public DisjointSet(int n){
		index = new int[n];
		rank = new int[n];
		// Fill index array from 0 to n
		// fill up rank array 0
		for(int i = 0; i < n; i++){
			index[i] = i;
			rank[i] = 0;
		}
	}
	
	/* Finds the root and performs path compression. */
	public int find(int v) {
		if( v != index[v] ){
			index[v] = find(index[v]);
		}
		return index[v];
	}
	
	/* Joins two arrays*/
	public void union (int u, int v){
		int i = find(u);
		int j = find(v);
		
		if(rank[i] > rank[j]){
			index[j] = i;
		}
		else {
			index[i] = j;
			if(rank[i] == rank[j]){
				rank[j] = rank[j] + 1;
			}
		}
	}
}
