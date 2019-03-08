package controller.converters;

import java.util.ArrayList;
import java.util.Set;

import controller.informations.CaseInformation;
import controller.informations.CuttingInformation;
import controller.informations.VectorInformation;
import controller.model.Case;
import controller.model.ColumnVector;
import service.VectorOperations;

public class CaseConverter {
	
	public static boolean inputIsInVectorFormat = false;
	public static boolean moreThanTwoClassification = false;
	
	private static void caseExamination(Set<String> possibleValues, String attributeName) {
		int i;
		double[] vector = new double[CuttingInformation.caseList.size()];
		for(String value : possibleValues) {
			i = 0;
			for(Case actualCase : CuttingInformation.caseList) {
				if(attributeName.isEmpty()) {
					vector[i++] = actualCase.getCaseClass().equals(value) ? 1.0 : 0.0;
				} else {
					vector[i++] = actualCase.getAttributeValue(attributeName).equals(value) ? 1.0 : 0.0;
				}
			}
			VectorInformation.vectorList.add(new ColumnVector(value, vector));
		}
	}
	
	private static void fillNegatedClassificationVectors() {
		double[] vector = new double[CuttingInformation.caseList.size()];
		for(String value : VectorInformation.classifications) {
			double[] copy = VectorOperations.getVectorFromVectorListByName(value, VectorInformation.vectorList).getVector();
			for(int i=0; i<copy.length; i++) {
				vector[i] = copy[i];
			}
			for(int i=0; i<vector.length; i++) {
				vector[i] = Math.abs(vector[i] - 1);
			}
			VectorInformation.vectorList.add(new ColumnVector("¬" + value, vector));
		}
	}
	
	private static void caseToVector() {
		// névhez hozzá kéne adni az attribútum nevét is, mert lehet, hogy más attribútumnak is van ilyen lehetséges értéke
		VectorInformation.vectorList = new ArrayList<>();
		for(String attributeName : CaseInformation.attributeNames) {
			caseExamination(CaseInformation.possibleValuesForAttribute(attributeName), attributeName);
		}
		
		caseExamination(VectorInformation.classifications, "");
		
		if(moreThanTwoClassification) {
			fillNegatedClassificationVectors();
		}
	}
	

	public static void convertCaseToVector() {
		if(inputIsInVectorFormat) {
			for(ColumnVector vector : VectorInformation.vectorList) {
				vector.createColumnVector();
			}
		} else {
			caseToVector();
		}
	}
}
