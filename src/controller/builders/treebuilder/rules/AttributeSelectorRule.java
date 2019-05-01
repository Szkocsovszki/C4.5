package controller.builders.treebuilder.rules;

import java.util.ArrayList;

import controller.informations.CaseInformation;
import controller.informations.CuttingInformation;
import controller.informations.VectorInformation;
import controller.model.ColumnVector;
import controller.operations.VectorOperations;

public class AttributeSelectorRule {
	
	private static double entropy(double w_plus, double xki_minus, double w_minus, double xki_plus) {
		//return 1 / (1 + w_plus*((1-xki_minus)/xki_minus) + w_minus*((1-xki_plus)/xki_plus));
		return (xki_plus*xki_minus) / (w_plus*xki_plus + w_minus*xki_minus);
	}
	
	private static void createEntropyList(
			ArrayList<Double> entropyList, 
			ColumnVector classVectorPositive, ColumnVector classVectorNegative, 
			ArrayList<ColumnVector> r_plus, ArrayList<ColumnVector> r_minus
	) {
		int size = CuttingInformation.caseList.size();
		double w_plus = (double) classVectorPositive.getSum() / size;
		double w_minus = (double) classVectorNegative.getSum() / size;
		double xki_plus, xki_minus;
		double attributeEntropy;
		String name;
		for(String attributeName : CaseInformation.attributeNames) {
			attributeEntropy = 0;
			for(String value : CuttingInformation.possibleValuesForAttribute(attributeName)) {
				name = VectorInformation.getVectorFromVectorListByName(value).getName();
				xki_plus = VectorInformation.getVectorFromVectorListByName(CaseInformation.positiveClassification + name, r_plus).getSum() / 
						   classVectorPositive.getSum();
				xki_minus = VectorInformation.getVectorFromVectorListByName(CaseInformation.negativeClassification + name, r_minus).getSum() / 
						   classVectorNegative.getSum();
				attributeEntropy += entropy(w_plus, xki_minus, w_minus, xki_plus);
			}
			entropyList.add(attributeEntropy);
		}
	}
	
	public static String selectAttributeForCutting() {
		ColumnVector classVectorPositive = VectorInformation.getVectorFromVectorListByName(CaseInformation.positiveClassification);
		ColumnVector classVectorNegative = VectorInformation.getVectorFromVectorListByName(CaseInformation.negativeClassification);
		
		ArrayList<ColumnVector> r_plus = new ArrayList<>();
		ArrayList<ColumnVector> r_minus = new ArrayList<>();
		
		VectorOperations.scalarMultiplicationWithClassVectors(classVectorPositive, classVectorNegative, r_plus, r_minus);
		
		ArrayList<Double> entropyList = new ArrayList<>();
		createEntropyList(entropyList, classVectorPositive, classVectorNegative, r_plus, r_minus);
		
		double min = entropyList.get(0);
		int counter = 0, index = 0;
		for(double i : entropyList) {
			if(i < min) {
				min = i;
				index = counter;
			}
			counter++;
		}
		
		/*System.out.println(min);*/
		
		return CaseInformation.attributeNames.get(index);
	}
}
