package controller.informations;

import java.util.ArrayList;
import java.util.Set;

import controller.model.ColumnVector;

public class VectorInformation {
	public static ArrayList<ColumnVector> vectorList = null;
	public static ArrayList<ColumnVector> copyVectorList = null;
	public static boolean moreThanTwoClassifications = false;
	public static Set<String> classifications = null;
	public static Set<String> negatedClassifications = null;
	
	public static void saveVectorList() {
		copyVectorList = new ArrayList<>();
		for(ColumnVector vector : vectorList) {
			copyVectorList.add(new ColumnVector(vector));
		}
	}
	
	public static void restoreVectorList() {
		vectorList = new ArrayList<>();
		for(ColumnVector vector : copyVectorList) {
			vectorList.add(new ColumnVector(vector));
		}
	}
	
	public static ColumnVector getVectorFromVectorListByName(String name) {
		return getVectorFromVectorListByName(name, vectorList);
	}
	
	public static ColumnVector getVectorFromVectorListByName(String name, ArrayList<ColumnVector> list) {
		//System.out.println(list);
		for(ColumnVector i : list) {
			if(i.getName().equals(name)) {
				return i;
			}
		}
		
		return null;
	}
}
