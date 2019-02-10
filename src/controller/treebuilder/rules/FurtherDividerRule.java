package controller.treebuilder.rules;

import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

import controller.model.Case;
import controller.model.CaseInformation;
import controller.model.TreeInformation;

public class FurtherDividerRule {
	public static Stack<TreeInformation> tree;
	
	public static Stack<TreeInformation> divider(ArrayList<Case> caseList) {
		Stack<ArrayList<Case>> stack = new Stack<>();
		tree = new Stack<>();
				
		stack.add(caseList);
		
		ArrayList<Case> i = new ArrayList<>();
		
		while(!stack.isEmpty()) {
			i = stack.pop();
			if(FinishingRule.isFinished(i)) {
				tree.push(new TreeInformation("leaf", ClassifierRule.determineTheClassOfTheLeaf(i)));
				for(int j=0; j<i.size(); j++) {
					tree.push(new TreeInformation("element", i.get(j).getIndex() + ""));
				}
			} else {
				divide(stack, i);
			}
		}
		
		return tree;
	}

	private static void divide(Stack<ArrayList<Case>> stack, ArrayList<Case> caseList) {
		String attributeToCut = AttributeSelectorRule.selectAttributeForCutting(caseList);
		tree.push(new TreeInformation("node", attributeToCut));
		
		Set<String> set = AttributeSelectorRule.possibleValuesForAttribute(attributeToCut);
		
		for(String value : set) {
			ArrayList<Case> newCaseList = new ArrayList<>();
			tree.push(new TreeInformation("edge", value));
			for(Case actualCase : caseList) {
				if(actualCase.getAttributeValue(attributeToCut).equals(value)) {
					actualCase.removeAttribute(attributeToCut);
					newCaseList.add(actualCase);
				}
			}
			stack.push(newCaseList);
		}
		
		CaseInformation.attributeNames.remove(attributeToCut);
	}
}
