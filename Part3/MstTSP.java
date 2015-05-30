import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;


public class MstTSP {
	private static int n; // number of vertices
	private static int seed; // input seed #
	private static Graph MSTGraph; // greedy graph
	//private static ArrayList<Edge> path; // greedy path taken
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
		MSTGraph = new Graph(n, seed);
		if(n <= 10){
			MSTGraph.printAdjMatrix(); 
		}
		//MSTGraph.initEmptyWeights();
		long startTime = System.currentTimeMillis(); // start timing program
		prim(MSTGraph);
		MSTGraph.preorder();
		long endTime = System.currentTimeMillis(); // stop timing program
		
		DecimalFormat df = new DecimalFormat("0.00");

		if(n <= 10){
			MSTGraph.printMSTMatrix();
			System.out.println("Total weight of mst: "+df.format(MSTGraph.getWeights()) +"\n");
			MSTGraph.printTraversal();
		}
		
		System.out.print("\nDistance using mst: "+df.format(MSTGraph.MSTDistance()) + " for path ");
		LinkedHashMap<Integer,Integer> path = MSTGraph.traversal();
		for (Integer i : path.keySet())
		       System.out.print(i + " ");
		System.out.print("0\n");
		System.out.print("Runtime for Mst TSP   : "+ (endTime - startTime) + " milliseconds\n");
		
	}
	
	// prim's algorithm on a graph
	public static void prim(Graph g){
		// select starting vertex which is vertex 0
		// build initial fringe from vertices connected to starting vertex
		BinaryHeap fringe = new BinaryHeap(g.getAdjMatrix().length);
	    for (Key key : g.neighbors(0)) {
	      fringe.insertOrUpdate(key);
	    }
	    // while there are vertices left
	    while (!fringe.empty()) {
	    	// choose edge to the fringe with smallest weight
	        Key edge = fringe.delMax();
	        // add associated vertex to tree
	        g.insertIntoMst(edge);
	        // update fringe:
	        for (Key neighbor : g.neighbors(edge.id())) {
	        	// add vertices to fringe connected to the new vertex
	        	// or update edges to the fringe so that they are the smallest
	          if (g.notInMST(neighbor.id())) {
	            fringe.insertOrUpdate(neighbor);
	          }
	        }
	      }
	}
}
