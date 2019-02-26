package controller.builders.stackbuilder;

import java.util.ArrayList;
import java.util.Stack;

import controller.model.Case;

public class StackBuilder {
	private static ArrayList<String> getPossibleClassValues(ArrayList<Case> caseList) {
		ArrayList<String> values = new ArrayList<>();
		
		for(int i=0; i<caseList.size(); i++) {
			String value = caseList.get(i).getCaseClass(); 
			if(!values.contains(value)) {
				values.add(value);
			}
		}
		
		return values;
	}
	
	private static int getNumberOfPossibleClassValues(ArrayList<Case> caseList) {
		return getPossibleClassValues(caseList).size();
	}
	
	public static Stack<ArrayList<Case>> createStack(ArrayList<Case> caseList) {
		Stack<ArrayList<Case>> stack = new Stack<>();
		if(getNumberOfPossibleClassValues(caseList) > 2) {
			ArrayList<String> values = getPossibleClassValues(caseList);
			for(String value : values) {
				ArrayList<Case> i = new ArrayList<>();
				for(Case actualCase : caseList) {
					if(actualCase.getCaseClass().equals(value)) {
						i.add(new Case(actualCase));
					} else {
						Case newCase = new Case(actualCase);
						newCase.setCaseClass("¬" + value);
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
