/*
 * Key.java: objects in the priority queue
 * Author: Robin Li 
 */
public class Key implements Comparable<Key> {
	private int id; // id # of vertex v
	private double weight; // weight of smallest edge from u to v
	private int otherID; // vertex u, on the other side of this edge

	/*
	 * Constructor: Key data objects
	 * - Each Key object holds:
	 * 1. The ID number of vertex v
	 * 2. Weight of the smallest edge (the priority) from 
	 * a vertex u in the tree to v
	 * 3. Tree vertex, u, on the other side of this edge
	 */
	public Key(int id, double weight, int otherID){
		this.id = id;
		this.weight = weight;
		this.otherID = otherID;
	}
	
	public int id(){
		return id;
	}
	public double weight(){
		return weight;
	}
	public int otherID(){
		return otherID;
	}
	public void setWeight(double weight){
		this.weight = weight;
	}
	public void setOther(int otherID){
		this.otherID = otherID;
	}
	
	public void printString(){
		System.out.println("ID: "+id);
		System.out.println("Weight: "+weight);
		System.out.println("OtherID: "+otherID);
	}
	
	/*public String toString(){
		return String.format(format, args)
	}*/

	@Override
	public int compareTo(Key other) {
		return (((Double)this.weight).compareTo(other.weight())*-1);
	}
	
}
