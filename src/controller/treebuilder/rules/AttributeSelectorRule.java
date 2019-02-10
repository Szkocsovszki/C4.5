package controller.treebuilder.rules;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import controller.model.Case;
import controller.model.CaseInformation;
import controller.model.ColumnVector;

public class AttributeSelectorRule {
	private static ArrayList<Case> caseList;
	private static ArrayList<ColumnVector> vectorList;

	/*private void possibleValuesForAllAttribute() {		
		for(int i=0; i<CaseInformation.numberOfAttributes; i++) {
			possibleValuesForAttribute(CaseInformation.attributeNames.get(i));
		}
	}*/
	
	private static Set<String> possibleClasses() {
		Set<String> set = new HashSet<>();
		
		set.add(CaseInformation.positiveCase);
		set.add(CaseInformation.negativeCase);
		
		return set;
	}
	
	public static Set<String> possibleValuesForAttribute(String attributeName) {
		Set<String> set = new HashSet<>();
		
		for(int i=0; i<caseList.size(); i++) {
			set.add(caseList.get(i).getAttributeValue(attributeName));
		}
		
		/*System.out.println(attributeName + ": " + set.size());
		System.out.println(set.toString());*/
		
		return set;
	}
	
	private static void caseExamination(Set<String> possibleValues, String attributeName, ArrayList<ColumnVector> vectorList) {
		int i;
		int[] vector = new int[caseList.size()];
		for(String value : possibleValues) {
			i = 0;
			for(Case actualCase : caseList) {
				if(attributeName.isEmpty()) {
					vector[i++] = actualCase.getCaseClass().equals(value) ? 1 : 0;
				} else {
					vector[i++] = actualCase.getAttributeValue(attributeName).equals(value) ? 1 : 0;
				}
			}
			vectorList.add(new ColumnVector(value, vector));
		}
	}
	
	private static void caseToVector() {
		//névhez hozzá kéne adni az attribútum nevét is
		vectorList = new ArrayList<>();
		for(String attributeName : CaseInformation.attributeNames) {
			caseExamination(possibleValuesForAttribute(attributeName), attributeName, vectorList);
		}
		
		caseExamination(possibleClasses(), "", vectorList);
	}
	
	private static int[] scalarMultiplication(int[] v1, int[] v2) {
		int[] vector = new int[caseList.size()];
		for(int i=0; i<caseList.size(); i++) {
			vector[i] = v1[i] * v2[i];
		}		
		
		return vector;
	}
	
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
	
	private static ColumnVector getVectorFromVectorListByName(String name, ArrayList<ColumnVector> list) {
		for(ColumnVector i : list) {
			if(i.getName().equals(name)) {
				return i;
			}
		}
		
		return null;
	}
	
	private static void scalarMultiplicationWithClassVectors(
			ColumnVector classVectorPositive, ColumnVector classVectorNegative, 
			ArrayList<ColumnVector> r_plus, ArrayList<ColumnVector> r_minus
	) {
		int[] vector = new int[caseList.size()];
		String name;
		for(String attributeName : CaseInformation.attributeNames) {
			for(String value : possibleValuesForAttribute(attributeName)) {
				name = getVectorFromVectorListByName(value, vectorList).getName();
				vector = getVectorFromVectorListByName(value, vectorList).getVector();
				r_plus.add(
						new ColumnVector(
							CaseInformation.positiveCase + name, 
							scalarMultiplication(vector, classVectorPositive.getVector())
						)
				);
				r_minus.add(
						new ColumnVector(
							CaseInformation.negativeCase + name, 
							scalarMultiplication(vector, classVectorNegative.getVector())
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
			for(String value : possibleValuesForAttribute(attributeName)) {
				name = getVectorFromVectorListByName(value, vectorList).getName();
				xki_plus = (double) getVectorFromVectorListByName(CaseInformation.positiveCase + name, r_plus).getSum() / 
						   classVectorPositive.getSum();
				xki_minus = (double) getVectorFromVectorListByName(CaseInformation.negativeCase + name, r_minus).getSum() / 
						   classVectorNegative.getSum();
				attributeEntropy += entropy(w_plus, xki_minus, w_minus, xki_plus);
			}
			entropyList.add(attributeEntropy);
		}
	}
	
	public static String selectAttributeForCutting(ArrayList<Case> caseList) {
		AttributeSelectorRule.caseList = caseList;
		caseToVector();
		
		ColumnVector classVectorPositive = getVectorFromVectorListByName(CaseInformation.positiveCase, vectorList);
		ColumnVector classVectorNegative = getVectorFromVectorListByName(CaseInformation.negativeCase, vectorList);
		
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
		
		return CaseInformation.attributeNames.get(index);
	}
}
