package controller.converters;

import java.util.ArrayList;
import java.util.Set;

import controller.informations.CaseInformation;
import controller.informations.CuttingInformation;
import controller.informations.VectorInformation;
import controller.model.Case;
import controller.model.ColumnVector;
import reader.CaseReader;

public class CaseConverter {
	
	private static void caseExamination(Set<String> possibleValues, String attributeName) {
		for(String name : possibleValues) {
			ColumnVector vector = new ColumnVector(name);
			for(Case actualCase : CuttingInformation.caseList) {
				if(attributeName.isEmpty()) {
					vector.putValue(actualCase.getCaseClass().equals(name) ? 1.0 : 0.0);
				} else {
					vector.putValue(actualCase.getAttributeValue(attributeName).equals(name) ? 1.0 : 0.0);
				}
			}
			VectorInformation.vectorList.add(vector);
		}
	}
	
	private static void fillNegatedClassificationVectors() {
		for(String name : VectorInformation.classifications) {
			ColumnVector vector = new ColumnVector("¬" + name);
			ArrayList<Double> values = VectorInformation.getVectorFromVectorListByName(name).getValues();
			for(Double value : values) {
				vector.putValue(Math.abs(value - 1));
			}
			VectorInformation.vectorList.add(vector);
		}
	}
	
	private static void caseToVector() {
		// névhez hozzá kéne adni az attribútum nevét is, mert lehet, hogy más attribútumnak is van ilyen lehetséges értéke
		VectorInformation.vectorList = new ArrayList<>();
		
		for(String attributeName : CaseInformation.attributeNames) {
			caseExamination(CuttingInformation.possibleValuesForAttribute(attributeName), attributeName);
		}
		
		caseExamination(VectorInformation.classifications, "");
		
		if(VectorInformation.moreThanTwoClassifications) {
			fillNegatedClassificationVectors();
		}
	}
	

	public static void convertCaseToVector() {
		if(!CaseReader.inputIsInVectorFormat) {
			caseToVector();
		}
		for(ColumnVector vector : VectorInformation.vectorList) {
			vector.createColumnVector();
		}
	}
}
