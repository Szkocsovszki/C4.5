package controller.treebuilder.rules;

import java.util.ArrayList;

import controller.model.Case;

public class ClassifierRule {
	public static String determineTheClassOfTheLeaf(ArrayList<Case> caseList) {		
		return caseList.get(0).getCaseClass();
	}
}
