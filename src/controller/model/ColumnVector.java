package controller.model;

import java.util.ArrayList;

import controller.informations.CaseInformation;

public class ColumnVector {
	private String name = "";
	private double[] vector;
	private double[] startVector;
	private double sum = 0.0;
	private int[] indexes;
	private ArrayList<Double> values;
	
	public ColumnVector(String name, double[] vector) {
		this.name = name;
		this.vector = new double[vector.length];
		this.startVector = new double[vector.length];
		this.indexes = new int[vector.length];
		//startVector
		for(int i=0; i<vector.length; i++) {
			this.vector[i] = vector[i];
			this.startVector[i] = vector[i];
			this.indexes[i] = 1;
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
	
	/*public void deleteCases(ArrayList<Integer> indexes) {
		for(int i=0; i<CaseInformation.defaultNumberOfCases; i++) {
			this.indexes[i] = 1;
		}
		
		int size = CaseInformation.defaultNumberOfCases - indexes.size();
		double[] copy = new double[size];
		
		for(Integer index : indexes) {
			this.indexes[index - 1] = 0;
		}		
		
		int index = 0;
		for(int i=0; i<this.indexes.length; i++) {
			if(this.indexes[i] != 0) {
				copy[index++] = this.startVector[i];
			}
		}
		
		this.vector = new double[size];
		for(int i=0; i<size; i++) {
			this.vector[i] = copy[i];
		}
	}*/
	
	public void deleteCases(ArrayList<Integer> indexes) {
		for(int i=0; i<CaseInformation.defaultNumberOfCases; i++) {
			this.indexes[i] = 1;
		}
		
		int size = CaseInformation.defaultNumberOfCases - indexes.size();
		double[] copy = new double[size];
		
		//////////////////////////
		System.out.println(CaseInformation.defaultNumberOfCases  + " - " + indexes.size() + " = " + size);
		System.out.println(indexes);
		for(int i=0; i<this.indexes.length; i++) {
			System.out.print(i+1 + " ");
		}
		System.out.println();
		for(int i=0; i<this.indexes.length; i++) {
			System.out.print(this.indexes[i] + " ");
		}
		System.out.println();
		//////////////////////////
		
		for(Integer index : indexes) {
			this.indexes[index - 1] = 0;
		}
		
		///////////////////////////////////////////
		for(int i=0; i<this.indexes.length; i++) {
			System.out.print(this.indexes[i] + " ");
		}
		System.out.println();
		System.out.print("***RÉGI ÉRTÉK***\n");
		for(int i=0; i<this.vector.length; i++) {
			System.out.print(vector[i] + " ");
		}
		System.out.println();
		///////////////////////////////////////////
		
		
		int index = 0;
		for(int i=0; i<this.indexes.length; i++) {
			if(this.indexes[i] != 0) {
				copy[index++] = this.startVector[i];
			} else {
				System.out.print((i+1) + "! ");
			}
		}
		System.out.println();
		this.vector = new double[size];
		for(int i=0; i<size; i++) {
			this.vector[i] = copy[i];
		}
		
		//////////////////////////////////////////
		System.out.print("***ÚJ ÉRTÉK***\n");
		for(int i=0; i<this.vector.length; i++) {
			System.out.print(vector[i] + " ");
		}
		System.out.println();
		System.out.println();
		//////////////////////////////////////////
	}
	
	public void putValue(double value) {
		values.add(value);
	}
	
	public void createColumnVector() {
		this.indexes = new int[values.size()];
		this.vector = new double[values.size()];
		this.startVector = new double[values.size()];
		for(int i=0; i<values.size(); i++) {
			this.indexes[i] = 1;
			this.vector[i] = values.get(i);
			this.startVector[i] = values.get(i);
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
