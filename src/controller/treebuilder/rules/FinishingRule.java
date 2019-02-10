package controller.treebuilder.rules;

import java.util.ArrayList;

import controller.model.Case;

public class FinishingRule {
	public static boolean isFinished(ArrayList<Case> caseList) {
		String classValueOfThePreviousCase;
		String classValueOfTheActualCase;
		
		for(int i=1; i<caseList.size(); i++) {
			classValueOfThePreviousCase = caseList.get(i-1).getCaseClass();
			classValueOfTheActualCase = caseList.get(i).getCaseClass();
			if(!classValueOfThePreviousCase.equals(classValueOfTheActualCase)) {
				return false;
			}
		}
		
		return true;
	}
}
