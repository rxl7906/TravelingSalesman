/*
 * Vertex.java - vertex class holds the row and col
 * in the adjacency matrix with the distance for lookup
 * Author: Robin Li
 */
public class Vertex {
	int x;
	int y;
	double distance;
	int num;
	public Vertex(int x, int y, int num) {
		this.x = x;
		this.y = y;
		this.num = num;
	}
}
