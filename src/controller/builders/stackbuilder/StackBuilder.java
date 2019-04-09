package controller.builders.stackbuilder;

import java.util.ArrayList;
import java.util.Stack;

import controller.model.Case;

public class StackBuilder {
	private static ArrayList<String> getPossibleClassifications(ArrayList<Case> caseList) {
		ArrayList<String> classificationList = new ArrayList<>();
		for(int i=0; i<caseList.size(); i++) {
			String classification = caseList.get(i).getClassification(); 
			if(!classificationList.contains(classification)) {
				classificationList.add(classification);
			}
		}
		return classificationList;
	}
	
	public static Stack<ArrayList<Case>> createStack(ArrayList<Case> caseList) {
		Stack<ArrayList<Case>> stack = new Stack<>();
		ArrayList<String> possibleClassifications = getPossibleClassifications(caseList);
		if(possibleClassifications.size() > 2) {
			for(String classification : possibleClassifications) {
				ArrayList<Case> i = new ArrayList<>();
				for(Case currentCase : caseList) {
					if(currentCase.getClassification().equals(classification)) {
						i.add(new Case(currentCase));
					} else {
						Case newCase = new Case(currentCase);
						newCase.setClassification("¬" + classification);
						i.add(newCase);
					}
				}
				stack.push(i);
			}
		} else {
			stack.push(caseList);
		}
		
		return stack;
	}
}
