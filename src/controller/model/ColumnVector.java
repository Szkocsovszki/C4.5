package controller.model;

import java.util.ArrayList;

import controller.informations.CuttingInformation;

public class ColumnVector {
	private String name = "";
	private double[] vector;
	private double[] startVector;
	private double sum = 0.0;
	private boolean[] indexes;
	private ArrayList<Double> values;
	
	public ColumnVector(String name, double[] vector) {
		int size = vector.length;
		this.name = name;
		this.vector = new double[size];
		this.startVector = new double[size];
		this.indexes = new boolean[size];
		for(int i=0; i<size; i++) {
			this.vector[i] = vector[i];
			this.startVector[i] = vector[i];
			this.indexes[i] = true;
			sum += vector[i];
		}
	}
	
	public ColumnVector(String name) {
		this.name = name;
		this.vector = null;
		this.values = new ArrayList<>();
	}
	
	public ColumnVector(ColumnVector copy) {
		int size = copy.vector.length;
		this.name = copy.name;
		this.vector = new double[size];
		this.startVector = new double[size];
		this.indexes = new boolean[size];
		for(int i=0; i<size; i++) {
			this.vector[i] = copy.vector[i];
			this.startVector[i] = vector[i];
			this.indexes[i] = true;
			sum += vector[i];
		}
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
	
	public ArrayList<Double> getValues() {
		return values;
	}
	
	public void putValue(double value) {
		values.add(value);
	}
	
	public void createColumnVector() {
		int size = values.size();
		this.indexes = new boolean[size];
		this.vector = new double[size];
		this.startVector = new double[size];
		for(int i=0; i<size; i++) {
			double value = values.get(i);
			this.indexes[i] = true;
			this.vector[i] = value;
			this.startVector[i] = value;
			sum += value;
		}
	}
	
	public void deleteCases(ArrayList<Integer> indexes) {
		// indexek alaphelyzetbe állítása
		for(int i=0; i<CuttingInformation.defaultNumberOfCases; i++) {
			this.indexes[i] = true;
		}
		
		// jelenlegi érvényes esetek számának megfelelően tömb létrehozása
		int size = CuttingInformation.defaultNumberOfCases - indexes.size();
		this.vector = new double[size];
		
		// kapott elemek törlése
		for(Integer index : indexes) {
			this.indexes[index - 1] = false;
		}
		
		// megmaradt elemek lementése
		int index = 0;
		for(int i=0; i<this.indexes.length; i++) {
			if(this.indexes[i]) {
				this.vector[index++] = this.startVector[i];
			}
		}
	}
	
	/*public void deleteCases(ArrayList<Integer> indexes) {
		for(int i=0; i<CuttingInformation.defaultNumberOfCases; i++) {
			this.indexes[i] = 1;
		}
		
		int size = CuttingInformation.defaultNumberOfCases - indexes.size();
		double[] copy = new double[size];
		
		//////////////////////////
		System.out.println(CuttingInformation.defaultNumberOfCases  + " - " + indexes.size() + " = " + size);
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
	}*/
	
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
