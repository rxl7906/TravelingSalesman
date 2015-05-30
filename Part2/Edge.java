/*
 * Edge.java - Edge data structure.
 * Implements comparable for sorting edges by weights,
 * then by column, then row
 * 
 * @author: Robin Li rxl7906
 */
public class Edge implements Comparable<Edge> {
	public int row;
	public int col; 
	public double weight;
	
	/* Constructor */
	public Edge(int row, int col, double weight){
		this.row = row;
		this.col = col;
		this.weight = weight;
	}
	
	/* Compare by weight, then column, then row.*/
	public int compareTo(Edge e) {
		int val1 = Double.compare(this.weight, e.weight);
		if(val1 == 0) {
			int val2 = Integer.compare(this.row, e.row);
			if(val2 == 0) {
				int val3 = Integer.compare(this.col, e.col);
				return val3;
			}else {
				return val2;
			}
		} else {
			return val1;
		}
	}
}
