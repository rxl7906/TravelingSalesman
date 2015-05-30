/*
 * BitonicTSP.java - generates an approximately optimal
 * TSP tour by finding the optimal bitonic tour using
 * a dynamic programming approach. 
 * Author: Robin Li
 */

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Stack;

public class BitonicTSP {
	private static int seed; // input seed
	Graph graph;
	static DecimalFormat df = new DecimalFormat("0.00");
	static int n;
	static double[][] ltable;
	static int[][] ntable;
	int[] bitonicPath; // bitonic tour
	static ArrayList<Vertex> sortedVertices; //the sorted list of points
	double distance;
	
	// bitonic TSP constructor
	BitonicTSP(int n, int seed){
		this.n = n;
		bitonicPath = new int[n];
		ltable = new double[n][n];
		ntable = new int[n][n];
		graph = new Graph(n, seed);
		if(n <= 10){
			graph.printAdjMatrix();
		}
		// initialize the ntable to -1
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				ntable[i][j] = -1;
			}
		}
	}
	
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
		
		BitonicTSP bit = new BitonicTSP(n, seed); // initialize bitonicTSP
		bit.sortVertices(); // sorted the edges

		if(n <= 10){
			printSortedVertices(); // print the sorted vertices
		}
		
		long startTime = System.currentTimeMillis(); // start timing program
		bit.bitonic(); // perform bitonic algorithm
		if(n <= 10){
			printLTable();
			printNTable();
		}
		bit.constructPath(); // build the path
		long endTime = System.currentTimeMillis(); // stop timing program
		System.out.print("Runtime for bitonic TSP   : "+ (endTime - startTime) + " milliseconds\n");
	}
	
	// sorts points found using Euclidean distance for bitonic algorithm
	@SuppressWarnings("unchecked")
	public void sortVertices(){
		// get pairs from the graph class
		sortedVertices = (ArrayList<Vertex>) graph.pairs.clone();
		Vertex t1;
		Vertex t2;
		int i = 0;
		while(i < sortedVertices.size()){
			while(sortedVertices.get(i).x != i){
				t1 = sortedVertices.get(i);
				int index = t1.x;
				t2 = sortedVertices.get(index);
				sortedVertices.set(i, t2);
				sortedVertices.set(index, t1);
			}
			i++;
		}
	}
	
	// print the sorted vertices
	public static void printSortedVertices(){
		System.out.println("Sorted X-Y Coordinates:");
		for(int i = 0; i < sortedVertices.size(); i++){
			System.out.print("v" + sortedVertices.get(i).num + ": (" + sortedVertices.get(i).x + "," + sortedVertices.get(i).y + ") ");
		}
		System.out.println("\n");
	}
	
	/*
	 *  Generate values for the L-Table and N-Table using the 
	 *  dynamic programming approach. 
	 */
	public void bitonic(){
		double tempDist;
		tempDist = getEuclideanDist(sortedVertices.get(0), sortedVertices.get(1));
		ltable[0][1] = tempDist;
		ntable[0][1] = 0;
		double temp;
		double min;
		for(int j = 2; j < n; j++){
			for(int i = 0; i < j; i++){
				min = Integer.MAX_VALUE;
					if (j <= (i + 1) ) {
				        for( int k = 0; k < i; k++ ) {
				        tempDist = getEuclideanDist(sortedVertices.get(k), sortedVertices.get(j));
				          temp = ltable[k][i] + tempDist;
				          if(min > temp){
				        	  min = temp;
				        	  ntable[i][j] = k;
				          }
				        }        
				        ltable[i][j] = min;        
				      } else
				      {
				    	  tempDist = getEuclideanDist(sortedVertices.get(j-1), sortedVertices.get(j));
				    	  ltable[i][j] = ltable[i][j-1] + tempDist;
				    	  ntable[i][j] = j-1;
				      }
			}
		}
	
	}
	
	// calculate the euclidean distance
	public double getEuclideanDist(Vertex v1, Vertex v2){
		return Math.sqrt(Math.pow(v2.x-v1.x,2)+Math.pow(v2.y-v1.y,2));
	}

	// print the L-Table
	public static void printLTable(){
		System.out.println("L-Table:");
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				System.out.print(String.format("%5s",df.format(ltable[i][j])) + "  ");
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	// print the N-Table
	public static void printNTable(){
		System.out.println("N-Table:");
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				System.out.print(String.format("%3s", ntable[i][j]) + "");
			}
			System.out.println("");
		}
		System.out.println("");
	}
	
	// Using values from the N-Table, get the path
	public void constructPath(){
		Stack<Integer> s1 = new Stack<Integer>();
		Stack<Integer> s2 = new Stack<Integer>();
		int i = n-2;
		int k = 0; //alternate between stack 1 and 2
		int j = n-1;
		// put the vertices in correct order
		// stack 1 acts as path starting from beginning
		// stack 2 acts as path starting from end
		// 0 is added after all other vertices are separated
		while(j > 0){
			if(k == 0){
				s1.push(j);
			}
			else{
				s2.push(j);
			}
			j = ntable[i][j];
			if(j < i){
				int temp = i;
				i = j;
				j = temp;
				if(k==0){
					k=1;
				}
				else{
					k=0;
				}
			}
		}
		// push 0 onto stack
		s1.push(0);
		// combine the stacks
		while(!s2.empty()){
			s1.push(s2.pop());
		}
		
		for(i = 0; i < n; i++){
			int index = 0;
			int value = s1.pop();
			for(int m = 0; m < n; m++){
				if(value == graph.pairs.get(m).x){
					index = m;
					break;
				}
			}
			bitonicPath[i] = index;
		}

		// set the total tour distance
		distance = ltable[n-2][n-1] + getEuclideanDist(sortedVertices.get(n-1), sortedVertices.get(n-2));
		adjustPath();
	}
	
	// shift the numbers added to the bitonicPath to begin with 0
	public void adjustPath(){
		ArrayList<Integer> path= new ArrayList<Integer>();
	        int zeroLoc = -1;
	        for (int i = 0; i < bitonicPath.length; i++ ) {
	            if ( bitonicPath[i] == 0 )
	                zeroLoc = i;
	            if ( zeroLoc != -1 )
	                path.add( bitonicPath[i] );
	        }
	    
	        for ( int i=0; i < zeroLoc; i++ ){
	            path.add(bitonicPath[i] );
	        }
	        path.add(0);
	     
	        for ( int i=0; i<bitonicPath.length; i++ ){
	            bitonicPath[i] = path.get(i);
	           
	        }
	    	printBitonicPath();
	}
	
	// print the bitonic path
	public void printBitonicPath(){
		System.out.print("Distance using bitonic: " + df.format(distance) + " for path ");
		for(int i = 0; i < bitonicPath.length; i++){
			System.out.print(bitonicPath[i] + " ");
		}
		System.out.println("0");
	}
}
