import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
/*
 * Graph.java - adjacency matrix represents undirected graph
 * Author: Robin Li
 */

public class Graph {
	private static int inputSeed;
	private static int numVertices; // number of vertices
	private double[][] adjMatrix; // adjacency matrix to hold edge weights
	private static int[][] verticesList; // list holds vertices
	private ArrayList<Edge> greedyPath; // path holds greedy edges
	private boolean[] visited; // for DFS
	private boolean[][] boolMST; // boolean matrix for MSTGraph
	// linkedhashmap iterates in the order in which entries were added
	private LinkedHashMap<Integer,Integer> traversal;
	ArrayList<Vertex> pairs;
	
	public Graph(int n, int seed) {
		adjMatrix = new double[n][n]; // give matrix dimensions
		verticesList = new int[n][2]; // array of points
		numVertices = n; // n is input # of vertices
		inputSeed = seed;
		pairs = new ArrayList<Vertex>();
		boolMST = new boolean[n][n];
		traversal = new LinkedHashMap<Integer,Integer>(); // traversal path
		
		generatePairs(); // added
		
		// generate new coordinates
		Random randX = new Random(seed); 
		Random randY = new Random(seed*2); 
		
		// boolean array for checking x
		boolean[] checkVertice = new boolean[n]; // every index has a false
		int vcount = 0; // number of vertices
		
		// loop and add randomized (x,y) coordinates to verticesList
		if( n <= 10) {
			System.out.println("X-Y Coordinates:");
		}
		while(vcount < n) { // until # of vertices = n
			// generate random x and y
			int x = randX.nextInt(n); 
			int y = randY.nextInt(n);
			// make sure x-coordinates are unique
			if( checkVertice[x] == false) { // if there is a false
				verticesList[vcount][0] = x; 
				verticesList[vcount][1] = y;
				checkVertice[x] = true;
				if(n <= 10) {
					System.out.print("v" + vcount + ": ");
					System.out.print("(" + x + "," + y + ") ");
				}
				vcount++;
			}
			
		}
		if(n <= 10) {
			System.out.println("\n");
		}
		
		// make an empty array of edges for graph
		greedyPath = new ArrayList<Edge>(n*n);
		// add to adjacency matrix
		for(int j = 0; j < n; j++) { // row
			for(int k = 0; k < n; k++) { // col
				adjMatrix[j][k] = calcVertexDist( verticesList[j] , verticesList[k]); // add distance
			}
		}
		
	}
	
	// getter for return the matrix
	public double[][] getAdjMatrix() {
		return this.adjMatrix;
	}
	
	// calculate vertex distance
	public double calcVertexDist(int[] v1, int[] v2) {
		// get (x1,y1)
		int x1 = v1[0];
		int y1 = v1[1];
		// get (x2,y2)
		int x2 = v2[0];
		int y2 = v2[1];
		// calculate vertex distance
		return Math.sqrt( Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2) );
	}
	
	// print adjacency matrix
	public void printAdjMatrix() {
		DecimalFormat df = new DecimalFormat("0.00");
		
		System.out.println("Adjacency matrix of graph weights: \n");

		for(int i = 0; i < numVertices; i++) { // print first row of adj matrix
			System.out.print("      "+i);
		}
		System.out.println("\n");

		for(int j = 0; j < adjMatrix.length; j++) { // print left side numbers
			System.out.print(j + "   ");

			for(int k = 0; k < adjMatrix.length; k++) { // print edge weights
				System.out.print(df.format( adjMatrix[j][k] ) + "   "); 
			}
			System.out.println("\n");
		}
	}
	
	// fill matrix with zeros
	public void initEmptyWeights(){
		for(int i = 0; i < adjMatrix.length; i++){
			for(int j = 0; j < adjMatrix.length; j++){
				adjMatrix[i][j] = 0.00;
			}
		}
	}
	
	// add edges to greedy path
	public void addEdge(Edge e) {
		greedyPath.add(e);
	}
	
	// add edge weights to matrix
	public void addEdgeWeight(int row, int col, double weight){
		adjMatrix[row][col] = weight;
	}
	
	public double getWeight(int row, int col){
		return adjMatrix[row][col];
	}
	
	// get greedy path
	public ArrayList<Edge> getGreedyPath(){
		return greedyPath;
	}
	
	// get the weighted edges
	public ArrayList<Edge> getWeightedEdges(){
		ArrayList<Edge> edges = new ArrayList<Edge>();
		int move = 0;
		for(int i = 0; i < adjMatrix.length; i++){
			for(int j = 1 + move; j < adjMatrix.length; j++){
				edges.add(new Edge(i,j, adjMatrix[i][j] ));
			}
			move++;
		}
		return edges;
	}
	
	// Depth First Search on matrix
	public ArrayList<Integer> dfsPath(){
		visited = new boolean[adjMatrix.length];
		for(int i = 0; i < adjMatrix.length; i++) {
			visited[i] = false;
		}
		ArrayList<Integer> path = new ArrayList<Integer>();
		path.add(0);
		dfs(0, path);
		path.add(0);
		return path;
	}
	
	// DFS helper function
	private ArrayList<Integer> dfs(int node, ArrayList<Integer> path){
		for(int i = 1; i < adjMatrix.length; i++){
			if(adjMatrix[node][i] > 0 && !visited[i] ) {
				path.add(i);
				visited[i] = true;
				dfs(i, path);
			}
		}
		return path;
	}
	
	// gets the neighbors from a key
	public List<Key> neighbors(int key){
		List<Key> keys = new ArrayList<Key>();
		//System.out.println("Length: " + adjMatrix.length);
		for(int i = 0; i < adjMatrix.length; i++){
			//System.out.println("Position: "+adjMatrix[key][i]);
			if(adjMatrix[key][i] > 0){
				//System.out.println("ADDING");
				keys.add(new Key(i, adjMatrix[key][i],key));
			}
		}
		return keys;
	}
	
	// add true values to the boolean MST(transposed)
	public void insertIntoMst(Key key){
		//System.out.println("ADDING: " +key.id());
		boolMST[key.id()][key.otherID()] = true;
		boolMST[key.otherID()][key.id()] = true;
	}
	
	// checks if node is in boolean MST or not
	public boolean notInMST(int node){
		boolean missing = true;
		for(int i = 0; i < boolMST.length; i++){
			if(boolMST[node][i]){
				missing = false;
			}
		}
		return missing;
	}
	
	// get the weighted paths through each vertex
	public double getWeights() {
		double weights = 0;
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < i; j++) {
				if (boolMST[i][j])
					weights += adjMatrix[i][j];
			}
		}
		return weights;
	}
	
	// get booleanMST matrix
	/*public boolean[][] getMST() {
		return boolMST;
	}*/
	
	// perform preorder traversal
	public LinkedHashMap<Integer,Integer> preorder() {
		preorder(0, -1);
		return traversal;
	}
	
	// given id and parent, do recursive process to do preorder
	private void preorder(int id, int parent) {
		traversal.put(id,parent);
		for (Integer neighbor : mstNeighbors(id)) {
			preorder(neighbor, id);
		}
	}
	
	// find a vertex's neighbors in matrix
	private List<Integer> mstNeighbors(int id) {
		List<Integer> neighbors = new ArrayList<Integer>();
		for (int i = 1; i < adjMatrix.length; i++) {
			if (boolMST[id][i] && !traversal.containsKey(i)) {
				neighbors.add(i);
			}
		}
		return neighbors;
	}
	
	// get the distance traveled
	public double MSTDistance() {
		double weight = 0;
		List<Integer> list = new ArrayList<Integer>(traversal.keySet());
		for (int i = 0; i < list.size()-1; i++) {
			weight += adjMatrix[list.get(i)][list.get(i+1)];
		}
		weight += adjMatrix[list.get(0)][list.get(list.size()-1)];
		return weight;
	}
	
	// linkedhashmap iterates in the order in which entries were added
	// holds the path traversal for which the MST traveled on
	public LinkedHashMap<Integer,Integer> traversal() {
		return traversal;
	}
	
	// print out pre-order Traversal
	public void printTraversal(){
		System.out.println("Pre-order traversal: ");
		for (Map.Entry<Integer,Integer> entry : traversal.entrySet()) {
			System.out.printf("Parent of %d is %d\n", entry.getKey(), entry.getValue());
		}
	}
	
	// print the MST matrix
	public void printMSTMatrix(){
		System.out.println("Minimum Spanning Tree:");
		System.out.println("Adjacency matrix of graph weights: \n");
		DecimalFormat df = new DecimalFormat("0.00");
		for(int i = 0; i < numVertices; i++) { // print first row of adj matrix
			System.out.print("      "+i);
		}
		System.out.println("\n");
		for (int i = 0; i < adjMatrix.length; i++) {
			System.out.print(i + "   ");
	        for (int j = 0; j < adjMatrix.length; j++) {
	          double distance = adjMatrix[i][j];
	          if (!boolMST[i][j]) distance = 0;
	          System.out.printf(df.format(distance) + "   ");
	        }
	        System.out.println("\n");
	    }
	}
	
	/*
	 *  Check to see if the x value of the vertex is
	 *  already a pair to make sure all pairs have 
	 *  a different x
	 */
	public boolean containsX(int x){
		for(int i = 0; i < pairs.size(); i++){
			if(pairs.get(i).x == x){
				return false;
			}
		}
		return true;
	}

	// creates the pairs used to form edges
	public void generatePairs(){
		Random randx = new Random(inputSeed); //fix this
		Random randy = new Random(2*inputSeed);
		int i = 0;
		while(pairs.size() < numVertices){ //make n pairs
			int x = randx.nextInt(numVertices);
			int y = randy.nextInt(numVertices);
			if(containsX(x)){
				pairs.add(new Vertex(x,y,i));
				i++;
			}
		}	
	}
}
