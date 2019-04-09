package controller.builders.treebuilder.rules;

import java.util.ArrayList;

import controller.model.Case;

public class FinishingRule {
	public static boolean isFinished(ArrayList<Case> caseList) {
		String classificationOfTheFirstCase = caseList.get(0).getClassification();
		String classificationOfTheCurrentCase;
		for(int i=1; i<caseList.size(); i++) {
			classificationOfTheCurrentCase = caseList.get(i).getClassification();
			if(!classificationOfTheCurrentCase.equals(classificationOfTheFirstCase)) {
				return false;
			}
		}	
		return true;
	}
}
