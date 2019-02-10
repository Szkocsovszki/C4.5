package controller.model;

public class ColumnVector {
	private String name = "";
	private int[] vector;
	private int sum = 0; 
	
	public ColumnVector(String name, int[] vector) {
		this.name = name;
		this.vector = new int[vector.length];
		for(int i=0; i<vector.length; i++) {
			this.vector[i] = vector[i];
			sum += vector[i];
		}
	}

	public String getName() {
		return name;
	}

	public int[] getVector() {
		return vector;
	}
	
	public int getSum() {
		return sum;
	}
	
	public String toString() {
		String vectorString = "(";
		for(int i=0; i<vector.length; i++) {
			vectorString += vector[i] + (i == vector.length - 1 ? ")" : " "); 
		}
		return name + vectorString + "\n";
	}
	
}
