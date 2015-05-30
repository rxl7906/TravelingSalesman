import java.text.DecimalFormat;
import java.util.Arrays;

/*
 * OptimalTSP.java - calculates optimal path for traveling salesman problem
 * Author: Robin Li
 */

public class OptimalTSP {
	private static int n; // number of vertices
	private static int seed; // input seed #
	private static int[] path; // path for salesman
	private static double[][] adjMatrix; // adjacency matrix to hold edge weights
	
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
			if(n < 1 || n > 13) {
				System.out.println("Number of vertices must be between 1 and 13");
				System.exit(0);
			}
		} catch (NumberFormatException e) {
			System.out.println("Command line args must be integers");
			System.exit(0);
		}
		
		long startTime = System.currentTimeMillis(); // start timing program
		Graph graph = new Graph(n, seed); // create graph
		
		if(n <= 10) { // print list of x,y coordinates and matrix
			graph.printAdjMatrix();
		}
		adjMatrix = graph.getAdjMatrix();
		path = new int[n]; // give path size
		
		// generate first path
		for(int i = 1; i < n; i++) {
			path[i] = i;
		}
		// generate permutation (n-1)^2 paths
		int numPaths = 1; // number of permutations possible
		for(int j = 1; j <= n-1; j++) { // calculate factorial
			numPaths = numPaths * j;
		}
		
		double initialDist = getDistance(path); // get distance of first path
		double lowestDist = initialDist; // set initial distance
		int[] lowestPath = Arrays.copyOf(path, n); // copy starting path to lowestPath
		
		DecimalFormat df = new DecimalFormat("0.00");
		int count = 0; // increment until you went through all permutations
		while( count < numPaths ) {
			double dist = getDistance(path); // get distance 
			if (n <= 5) { // if n <= 5 print paths and distances
				Permutation.permutationToString(path); // print path
				System.out.println("distance = "+df.format(dist)); // print distance of path

				if( lowestDist > dist ) { // if found a lower distance
					lowestPath = Arrays.copyOf(path, n); // copy this path to lowestPath
					lowestDist = dist; // set new lowest distance
				}
			} else { // n > 5 don't print paths and distances
				if( lowestDist > dist ) {
					lowestPath = Arrays.copyOf(path, n);
					lowestDist = dist;
				}
			}
			Permutation.nextPermutation(path); // get next permutation
			count++;
		}
		if(n <= 5) {
			System.out.print("\n");
		}
		
		// print optimal distance
		System.out.print("Optimal distance: " + df.format(lowestDist) + " for path "); 
		for(int k = 0; k < n; k++) { // print the optimal path
			System.out.print(lowestPath[k] + " ");
		}
		System.out.println("0");
		long endTime = System.currentTimeMillis(); // stop timing program
		System.out.println("Runtime for optimal TSP   : "+ (endTime - startTime) + " milliseconds");
		System.out.println("\n");
	}
	
	// get the distance from the matrix using the path array's vertices
	private static double getDistance(int[] path) { //
		double result = 0.00;
		for(int i = 0; i < n; i++) { // go through path list
			if( i == n -1 ) { // from one vertex to the next in path list
				result += adjMatrix[ path[i] ][ 0 ]; 
			} else {
				result += adjMatrix[ path[i] ][ path[i+1] ];
			}
		}
		return result;
	  }
}
