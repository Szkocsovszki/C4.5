package controller.builders.treebuilder.rules;

import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

import controller.converters.CaseConverter;
import controller.informations.CaseInformation;
import controller.informations.TreeInformation;
import controller.model.Case;
import controller.model.ColumnVector;

public class FurtherDividerRule {
	public static Stack<TreeInformation> tree;
	
	public static Stack<TreeInformation> divider(ArrayList<Case> caseList) {
		Stack<ArrayList<Case>> stack = new Stack<>();
		tree = new Stack<>();
				
		stack.add(caseList);
		
		ArrayList<Case> i = new ArrayList<>();
		
		while(!stack.isEmpty()) {
			i = stack.pop();
			
			/////////////////////////////
			CaseInformation.caseList = i;
			ArrayList<Integer> indexes = new ArrayList<>();
			ArrayList<Integer> toDelete = new ArrayList<>();
			for(int index=0; index<CaseInformation.defaultNumberOfCases; index++) {
				try {
					indexes.add(i.get(index).getIndex());
				} catch (IndexOutOfBoundsException e) {}
			}
			for(int index=1; index<=CaseInformation.defaultNumberOfCases; index++) {
				if(!indexes.contains(index)) {
					toDelete.add(index);
				}
			}
			if(toDelete.size() > 0) {
				for(ColumnVector vector : CaseConverter.vectorList) {
					System.out.println("\nHívás: " + vector.getName());
					vector.deleteCases(toDelete);
				}
			}
			/////////////////////////////
			
			if(FinishingRule.isFinished(i)) {
				tree.push(new TreeInformation("leaf", ClassifierRule.determineTheClassOfTheLeaf(i)));
				for(int j=0; j<i.size(); j++) {
					tree.push(new TreeInformation("element", i.get(j).getIndex() + ""));
				}				
			} else {
				divide(stack/*, i*/);
			}
		}
		
		return tree;
	}

	private static void divide(Stack<ArrayList<Case>> stack/*, ArrayList<Case> caseList*/) {
		System.out.println("vektorlista");
		System.out.println(CaseConverter.vectorList);
		System.out.println("!!!");
		
		String attributeToCut = AttributeSelectorRule.selectAttributeForCutting(CaseInformation.caseList);
		tree.push(new TreeInformation("node", attributeToCut));
		
		Set<String> set = CaseInformation.possibleValuesForAttribute(attributeToCut);
		
		for(String value : set) {
			ArrayList<Case> newCaseList = new ArrayList<>();
			tree.push(new TreeInformation("edge", value));
			for(Case actualCase : CaseInformation.caseList) {
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
