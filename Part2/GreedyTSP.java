import java.text.DecimalFormat;
import java.util.ArrayList;

/*
 * GreedyTSP.java - generate approximately optimal tour
 * by continually choosing an edge with lowest cost of those
 * edges remaining. Compute cost of tour. 
 * 
 * @Author: Robin Li rxl7906
 */

public class GreedyTSP {
	private static int n; // number of vertices
	private static int seed; // input seed #
	private static Graph greedyGraph; // greedy graph
	private static ArrayList<Edge> path; // greedy path taken
	private static double distance; // distance traveled
	
	public static void main(String[] args) {
		// error conditions and messages
		if(args.length < 2) { // missing one or both command line arguments
			System.out.println("Usage: java OptimalTSP n seed");
			System.exit(0);
		}
		// one or both command line arguments is not an integer
		try {
			n = Integer.parseInt(args[0]);
			seed = Integer.parseInt(args[1]);
			if(n < 1) {
				System.out.println("Number of vertices must be greater than 0");
				System.exit(0);
			}
		} catch (NumberFormatException e) {
			System.out.println("Command line args must be integers");
			System.exit(0);
		}
		
		// initialize greedy graph
		greedyGraph = new Graph(n, seed);
		if(n <= 10){
			greedyGraph.printAdjMatrix(); 
		}
		long startTime = System.currentTimeMillis(); // start timing program
		greedyPath(); // run greedy algorithm
		long endTime = System.currentTimeMillis(); // stop timing program
		
		DecimalFormat df = new DecimalFormat("0.00");
		if(n <= 10){
			System.out.println("Greedy graph: ");
			greedyGraph.printAdjMatrix();
			System.out.println("Edges of tour from greedy graph:");
			for(Edge e: path){
				System.out.println(e.row + " " + e.col +  " weight = " + df.format(e.weight));
			}
		}
		System.out.print("\n");
		System.out.print("Distance using greedy: "+ df.format(distance) + " for path ");
		ArrayList<Integer> greedyTraversal = new ArrayList<Integer>(n+1);
		greedyTraversal = greedyGraph.dfsPath();
		for(Integer i: greedyTraversal){
			System.out.print(i +" ");
		}
		System.out.print("\n");
		
		System.out.print("Runtime for optimal TSP   : "+ (endTime - startTime) + " milliseconds");
	}

	public static void greedyPath(){
		// sort edges
		ArrayList<Edge> greedyEdges  = QuickSort.quickSort(greedyGraph.getWeightedEdges());
		greedyGraph.initEmptyWeights(); // zero out matrix

		// initialize disjoint set
		DisjointSet disjointSet = new DisjointSet(n);
		
		// edge counter to check for duplicates
		// a vertex corresponds to each index and each index
		// holds the counts of how many times that edge was seen
		int[] edgeCounter = new int[n];

		int includedCount = 0;
		int index = 0; // index into greedyEdges array

		// Perform union find path compression
		while(includedCount < n && index < greedyEdges.size() ){
			int root1 = disjointSet.find(greedyEdges.get(index).row);
			int root2 = disjointSet.find(greedyEdges.get(index).col);
			
			// check if root1 != root2 and check edgeCounter for duplicates
			if(root1 != root2 && edgeCounter[greedyEdges.get(index).row] < 2 && 
					edgeCounter[greedyEdges.get(index).col] < 2){
				greedyGraph.addEdge(greedyEdges.get(index));
				edgeCounter[greedyEdges.get(index).row] +=1;
				edgeCounter[greedyEdges.get(index).col] +=1;
				includedCount ++;
				index ++;
				disjointSet.union(root1, root2);
			} 
			// check root1 == root2 with includedCount = n-1 means you are at the last edge
			// adding the last edge will form a cycle. Check edgeCounter for duplicates
			else if( root1 == root2 && includedCount == n-1 && edgeCounter[greedyEdges.get(index).row] < 2 && 
					edgeCounter[greedyEdges.get(index).col] < 2){
				greedyGraph.addEdge(greedyEdges.get(index));
				edgeCounter[greedyEdges.get(index).row] +=1;
				edgeCounter[greedyEdges.get(index).col] +=1;
				includedCount ++;
				index ++;
			} else{
				index++;
			}
		}

		// get the greedy path
		path = greedyGraph.getGreedyPath();
		
		// add edge weight to greedyGraph
		for(Edge e: path){
			greedyGraph.addEdgeWeight(e.row, e.col, e.weight);
			greedyGraph.addEdgeWeight(e.col, e.row, e.weight);
		}

		distance = 0.00;
		// sum up distance
		for(Edge e: path){
			distance += e.weight;
		}
	}
}
