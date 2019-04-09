package service;

import java.util.ArrayList;

import controller.informations.CaseInformation;
import controller.informations.CuttingInformation;
import controller.informations.VectorInformation;
import controller.model.Case;
import controller.model.ColumnVector;

public class VectorOperations {	
	private static double[] scalarMultiplication(double[] v1, double[] v2) {
		int size = CuttingInformation.caseList.size();
		double[] vector = new double[size];
		for(int i=0; i<size; i++) {
			vector[i] = v1[i] * v2[i];
		}		
		return vector;
	}
	
	public static void scalarMultiplicationWithClassVectors(
			ColumnVector classVectorPositive, ColumnVector classVectorNegative, 
			ArrayList<ColumnVector> r_plus, ArrayList<ColumnVector> r_minus
	) {
		double[] vector = new double[CuttingInformation.caseList.size()];
		String name;
		for(String attributeName : CaseInformation.attributeNames) {
			for(String value : CuttingInformation.possibleValuesForAttribute(attributeName)) {
				name = VectorInformation.getVectorFromVectorListByName(value).getName();
				vector = VectorInformation.getVectorFromVectorListByName(value).getVector();
				r_plus.add(
						new ColumnVector(
							CaseInformation.positiveClassification + name, 
							scalarMultiplication(vector, classVectorPositive.getVector())
						)
				);
				r_minus.add(
						new ColumnVector(
							CaseInformation.negativeClassification + name, 
							scalarMultiplication(vector, classVectorNegative.getVector())
						)
				);
			}
		}
	}
		
	public static void deleteVectors(ArrayList<Case> caseList) {
		ArrayList<Integer> indexes = new ArrayList<>();
		ArrayList<Integer> toDelete = new ArrayList<>();
		
		// jelenlegi listából kigyűjti a benne lévő esetek indexeit
		for(Case currentCase : caseList) {
			indexes.add(currentCase.getIndex());
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
