package controller.builders.treebuilder.rules;

import java.util.ArrayList;

import controller.model.Case;

public class FinishingRule {
	public static boolean isFinished(ArrayList<Case> caseList) {
		String classValueOfTheFirstCase = caseList.get(0).getCaseClass();
		String classValueOfTheActualCase;
		
		for(int i=1; i<caseList.size(); i++) {
			classValueOfTheActualCase = caseList.get(i).getCaseClass();
			if(!classValueOfTheActualCase.equals(classValueOfTheFirstCase)) {
				return false;
			}
		}
		
		return true;
	}
}
