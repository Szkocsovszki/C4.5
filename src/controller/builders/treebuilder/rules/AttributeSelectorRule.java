package controller.builders.treebuilder.rules;

import java.util.ArrayList;

import controller.informations.CaseInformation;
import controller.informations.VectorInformation;
import controller.model.Case;
import controller.model.ColumnVector;
import service.VectorOperations;

public class AttributeSelectorRule {
	private static ArrayList<Case> caseList;
	private static ArrayList<ColumnVector> vectorList;

	/*private void possibleValuesForAllAttribute() {		
		for(int i=0; i<CaseInformation.numberOfAttributes; i++) {
			possibleValuesForAttribute(CaseInformation.attributeNames.get(i));
		}
	}*/
	
	/*private double information(ArrayList<Case> S) {
		//s - subset of caseList
		//ck - the class value for which we are looking
		//p - number of classes
		double information = 0;
		int S_Size = S.size();
		int Ck_Size;
		Set<String> classes = possibleClasses();
		for(String caseClass : classes) {
			List<Case> ckSubset = new ArrayList<>();
			for(Case i : S) {
				if(i.getCaseClass().equals(caseClass)) {
					ckSubset.add(i);
				}
			}
			Ck_Size = ckSubset.size();
			double ratio = (double)Ck_Size / S_Size;
			information += -(ratio * log2(ratio));
		}
		return information;
	}
	
	private double log2(double argument) {
		if(argument == 0) {
			return 0;
		}
		return Math.log(argument) / Math.log(2);
	}
	
	private double entropy(String attribute, ArrayList<Case> S) {
		double entropy = 0;
		int S_Size = S.size();
		int Sj_Size;
		Set<String> set = possibleValuesForAttribute(attribute);
		for(String attributeValue : set) {
			ArrayList<Case> casesWithActualAttributeValue = new ArrayList<>();
			for(Case i : S) {
				if(i.getAttributeValue(attribute).equals(attributeValue)) {
					casesWithActualAttributeValue.add(i);
				}
			}
			Sj_Size = casesWithActualAttributeValue.size();
			double ratio = (double)Sj_Size / S_Size;
			entropy += ratio * information(casesWithActualAttributeValue);
		}
		
		return entropy;
	}
	
	private double gain(String attribute, ArrayList<Case> S) {
		return information(S) - entropy(attribute, S);
	}*/
	
	private static double entropy(double w_plus, double xki_minus, double w_minus, double xki_plus) {
		//return 1 / (1 + w_plus*((1-xki_minus)/xki_minus) + w_minus*((1-xki_plus)/xki_plus));
		return (xki_plus*xki_minus) / (w_plus*xki_plus + w_minus*xki_minus);
	}
	
	private static void scalarMultiplicationWithClassVectors(
			ColumnVector classVectorPositive, ColumnVector classVectorNegative, 
			ArrayList<ColumnVector> r_plus, ArrayList<ColumnVector> r_minus
	) {
		double[] vector = new double[caseList.size()];
		String name;
		for(String attributeName : CaseInformation.attributeNames) {
			for(String value : CaseInformation.possibleValuesForAttribute(attributeName)) {
				name = VectorOperations.getVectorFromVectorListByName(value, vectorList).getName();
				vector = VectorOperations.getVectorFromVectorListByName(value, vectorList).getVector();
				r_plus.add(
						new ColumnVector(
							CaseInformation.positiveCase + name, 
							VectorOperations.scalarMultiplication(vector, classVectorPositive.getVector())
						)
				);
				r_minus.add(
						new ColumnVector(
							CaseInformation.negativeCase + name, 
							VectorOperations.scalarMultiplication(vector, classVectorNegative.getVector())
						)
				);
			}
		}
	}
	
	private static void createEntropyList(
			ArrayList<Double> entropyList, 
			ColumnVector classVectorPositive, ColumnVector classVectorNegative, 
			ArrayList<ColumnVector> r_plus, ArrayList<ColumnVector> r_minus
	) {
		double w_plus = (double) classVectorPositive.getSum() / caseList.size();
		double w_minus = (double) classVectorNegative.getSum() / caseList.size();
		double xki_plus, xki_minus;
		double attributeEntropy;
		String name;
		for(String attributeName : CaseInformation.attributeNames) {
			attributeEntropy = 0;
			for(String value : CaseInformation.possibleValuesForAttribute(attributeName)) {
				name = VectorOperations.getVectorFromVectorListByName(value, vectorList).getName();
				xki_plus = VectorOperations.getVectorFromVectorListByName(CaseInformation.positiveCase + name, r_plus).getSum() / 
						   classVectorPositive.getSum();
				xki_minus = VectorOperations.getVectorFromVectorListByName(CaseInformation.negativeCase + name, r_minus).getSum() / 
						   classVectorNegative.getSum();
				attributeEntropy += entropy(w_plus, xki_minus, w_minus, xki_plus);
			}
			entropyList.add(attributeEntropy);
		}
	}
	
	public static String selectAttributeForCutting(ArrayList<Case> caseList) {
		AttributeSelectorRule.caseList = caseList;
		
		AttributeSelectorRule.vectorList = VectorInformation.vectorList;
		
		ColumnVector classVectorPositive = VectorOperations.getVectorFromVectorListByName(CaseInformation.positiveCase, vectorList);
		ColumnVector classVectorNegative = VectorOperations.getVectorFromVectorListByName(CaseInformation.negativeCase, vectorList);
		
		ArrayList<ColumnVector> r_plus = new ArrayList<>();
		ArrayList<ColumnVector> r_minus = new ArrayList<>();
		
		scalarMultiplicationWithClassVectors(classVectorPositive, classVectorNegative, r_plus, r_minus);
		
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
