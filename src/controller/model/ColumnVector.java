package controller.model;

import java.util.ArrayList;

public class ColumnVector {
	private String name = "";
	private double[] vector;
	private double sum = 0.0;
	private ArrayList<Double> values;
	
	public ColumnVector(String name, double[] vector) {
		this.name = name;
		this.vector = new double[vector.length];
		for(int i=0; i<vector.length; i++) {
			this.vector[i] = vector[i];
			sum += vector[i];
		}
	}
	
	public ColumnVector(String name) {
		this.name = name;
		this.vector = null;
		this.values = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public double[] getVector() {
		return vector;
	}
	
	public double getSum() {
		return sum;
	}
	
	public void putValue(double value) {
		values.add(value);
	}
	
	public void createColumnVector() {
		this.vector = new double[values.size()];
		for(int i=0; i<values.size(); i++) {
			this.vector[i] = values.get(i);
			sum += values.get(i);
		}
	}
	
	public String toString() {
		String vectorString = "(";
		try {
			for(int i=0; i<vector.length; i++) {
				vectorString += vector[i] + (i == vector.length - 1 ? ")" : " ");
			}
		} catch(NullPointerException e) {
			vectorString += ")";
		}
		return name + vectorString;
	}
	
}
