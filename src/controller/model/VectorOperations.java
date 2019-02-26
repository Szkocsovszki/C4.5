package controller.model;

import java.util.ArrayList;

import controller.informations.CaseInformation;

public class VectorOperations {
	public static ColumnVector getVectorFromVectorListByName(String name, ArrayList<ColumnVector> list) {
		System.out.println(list);
		for(ColumnVector i : list) {
			if(i.getName().equals(name)) {
				return i;
			}
		}
		
		return null;
	}
	
	public static double[] scalarMultiplication(double[] v1, double[] v2) {
		double[] vector = new double[CaseInformation.caseList.size()];
		for(int i=0; i<CaseInformation.caseList.size(); i++) {
			vector[i] = v1[i] * v2[i];
		}
		
		return vector;
	}
}
