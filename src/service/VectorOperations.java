package service;

import java.util.ArrayList;

import controller.informations.CuttingInformation;
import controller.informations.VectorInformation;
import controller.model.Case;
import controller.model.ColumnVector;

public class VectorOperations {
	public static ColumnVector getVectorFromVectorListByName(String name, ArrayList<ColumnVector> list) {
		//System.out.println(list);
		for(ColumnVector i : list) {
			if(i.getName().equals(name)) {
				return i;
			}
		}
		
		return null;
	}
	
	public static double[] scalarMultiplication(double[] v1, double[] v2) {
		double[] vector = new double[CuttingInformation.caseList.size()];
		for(int i=0; i<CuttingInformation.caseList.size(); i++) {
			vector[i] = v1[i] * v2[i];
		}
		
		return vector;
	}
	
	public static void deleteVectors(ArrayList<Case> caseList) {
		ArrayList<Integer> indexes = new ArrayList<>();
		ArrayList<Integer> toDelete = new ArrayList<>();
		
		// jelenlegi listából kigyűjti a benne lévő esetek indexeit
		for(int i=0; i<CuttingInformation.defaultNumberOfCases; i++) {
			try {
				indexes.add(caseList.get(i).getIndex());
			} catch (IndexOutOfBoundsException e) {}
		}
		
		// összegyűjti azokat az indexeket, amelyek nem szerepeltek a listában
		for(int index=1; index<=CuttingInformation.defaultNumberOfCases; index++) {
			if(!indexes.contains(index)) {
				toDelete.add(index);
			}
		}
		
		// a nem szereplő indexszel rendelkező elemeket törli a vektorokból 
		if(toDelete.size() > 0) {
			for(ColumnVector vector : VectorInformation.vectorList) {
				/*System.out.println("\nHívás: " + vector.getName());*/
				vector.deleteCases(toDelete);
			}
		}
	}
}
