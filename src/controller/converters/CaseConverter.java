package controller.converters;

import java.util.ArrayList;
import java.util.Set;

import controller.informations.CaseInformation;
import controller.model.Case;
import controller.model.ColumnVector;

public class CaseConverter {
	
	public static boolean inputIsInVectorFormat = false;
	public static ArrayList<ColumnVector> vectorList = null;
	
	private static void caseExamination(Set<String> possibleValues, String attributeName, ArrayList<ColumnVector> vectorList) {
		int i;
		double[] vector = new double[CaseInformation.caseList.size()];
		for(String value : possibleValues) {
			i = 0;
			for(Case actualCase : CaseInformation.caseList) {
				if(attributeName.isEmpty()) {
					vector[i++] = actualCase.getCaseClass().equals(value) ? 1.0 : 0.0;
				} else {
					vector[i++] = actualCase.getAttributeValue(attributeName).equals(value) ? 1.0 : 0.0;
				}
			}
			vectorList.add(new ColumnVector(value, vector));
		}
	}
	
	private static void caseToVector() {
		// névhez hozzá kéne adni az attribútum nevét is, mert lehet, hogy más attribútumnak is van ilyen lehetséges értéke
		vectorList = new ArrayList<>();
		for(String attributeName : CaseInformation.attributeNames) {
			caseExamination(CaseInformation.possibleValuesForAttribute(attributeName), attributeName, vectorList);
		}
		
		caseExamination(CaseInformation.possibleClasses(), "", vectorList);
	}
	
	public static void convertCaseToVector() {
		if(inputIsInVectorFormat) {
			for(ColumnVector vector : CaseConverter.vectorList) {
				vector.createColumnVector();
			}
		} else {
			caseToVector();
		}
	}
}
