package controller.builders.treebuilder.rules;

import java.util.ArrayList;

import controller.informations.CaseInformation;
import controller.informations.CuttingInformation;
import controller.informations.VectorInformation;
import controller.model.ColumnVector;
import controller.operations.VectorOperations;

public class AttributeSelectorRule {
	
	private static double vagueness(double w_plus, double xki_minus, double w_minus, double xki_plus) {
		//return 1 / (1 + w_plus*((1-xki_minus)/xki_minus) + w_minus*((1-xki_plus)/xki_plus));
		return (xki_plus*xki_minus) / (w_plus*xki_plus + w_minus*xki_minus);
	}
	
	private static void createVaguenessList(
			ArrayList<Double> vaguenessList, 
			ColumnVector classVectorPositive, ColumnVector classVectorNegative, 
			ArrayList<ColumnVector> r_plus, ArrayList<ColumnVector> r_minus
	) {
		int size = CuttingInformation.caseList.size();
		double w_plus = (double) classVectorPositive.getSum() / size;
		double w_minus = (double) classVectorNegative.getSum() / size;
		double xki_plus, xki_minus;
		double attributeVagueness;
		String name;
		for(String attributeName : CaseInformation.attributeNames) {
			attributeVagueness = 0;
			for(String value : CuttingInformation.possibleValuesForAttribute(attributeName)) {
				name = VectorInformation.getVectorFromVectorListByName(value).getName();
				xki_plus = VectorInformation.getVectorFromVectorListByName(CaseInformation.positiveClassification + name, r_plus).getSum() / 
						   classVectorPositive.getSum();
				xki_minus = VectorInformation.getVectorFromVectorListByName(CaseInformation.negativeClassification + name, r_minus).getSum() / 
						   classVectorNegative.getSum();
				attributeVagueness += vagueness(w_plus, xki_minus, w_minus, xki_plus);
			}
			vaguenessList.add(attributeVagueness);
		}
	}
	
	public static String selectAttributeForCutting() {
		ColumnVector classVectorPositive = VectorInformation.getVectorFromVectorListByName(CaseInformation.positiveClassification);
		ColumnVector classVectorNegative = VectorInformation.getVectorFromVectorListByName(CaseInformation.negativeClassification);
		
		ArrayList<ColumnVector> r_plus = new ArrayList<>();
		ArrayList<ColumnVector> r_minus = new ArrayList<>();
		
		VectorOperations.scalarMultiplicationWithClassVectors(classVectorPositive, classVectorNegative, r_plus, r_minus);
		
		ArrayList<Double> vaguenessList = new ArrayList<>();
		createVaguenessList(vaguenessList, classVectorPositive, classVectorNegative, r_plus, r_minus);
		
		double min = vaguenessList.get(0);
		int counter = 0, index = 0;
		for(double i : vaguenessList) {
			if(i < min) {
				min = i;
				index = counter;
			}
			counter++;
		}
		
		return CaseInformation.attributeNames.get(index);
	}
}
