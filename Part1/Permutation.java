/*
 * Permutation.java - generate permutations for path
 * Author: Robin Li
 */
public class Permutation {
	// swap two integers in an array
	public static void swap(int[] array, int x, int y) {
		int temp = array[x];
		array[x] = array[y];
		array[y] = temp;
	}
	// find the next permutation of an array
	public static int[] nextPermutation(int[] array) {
		int i, j; 
		// find largest index i where a[i] < a[i+1]
		// if no such index exists, permutation is last one
		for (i = array.length - 2; i >= 0; i--) {
		     if (array[i] < array[i + 1])
		  break;
		 }
		 if (i < 0) {
		     return array;
		 }
		 // find largest index i where a[j] < a[i]. 
		 for (j = array.length - 1; j > i; j--) {
		     if (array[j] > array[i])
		  break;
		 }
		 swap(array, i++, j); // swap a[j] with a[i]
		 
		 // reverse sequence from a[j+1] up to final element a[n]
		 for (j = array.length - 1; j > i; i++, j--) {
		     swap(array, i, j);
		 }
		return array;
	}
	
	// print the permutation
	public static void permutationToString(int[] path) {
		System.out.print("Path: ");
		for(int i = 0; i < path.length; i++) {
			System.out.print(path[i] + " ");
		}
		System.out.print("0  ");
	}
}
