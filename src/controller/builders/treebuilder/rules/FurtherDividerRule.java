package controller.builders.treebuilder.rules;

import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

import controller.informations.CaseInformation;
import controller.informations.CuttingInformation;
import controller.model.Case;
import controller.model.TreeElement;
import controller.operations.VectorOperations;

public class FurtherDividerRule {
	private static Stack<TreeElement> tree;
	
	public static Stack<TreeElement> divider(ArrayList<Case> caseList) {
		tree = new Stack<>();
		ArrayList<Case> i = new ArrayList<>();
		Stack<ArrayList<Case>> stack = new Stack<>();
		stack.add(caseList);
		while(!stack.isEmpty()) {
			i = stack.pop();
			if(FinishingRule.isFinished(i)) {
				tree.push(new TreeElement("leaf", ClassifierRule.determineTheClassOfTheLeaf(i)));
				for(int j=0; j<i.size(); j++) {
					tree.push(new TreeElement("index", i.get(j).getIndex() + ""));
				}				
			} else {
				CuttingInformation.caseList = i;
				divide(stack);
			}
		}
		
		return tree;
	}

	private static void divide(Stack<ArrayList<Case>> stack) {
		VectorOperations.keepIndexes(CuttingInformation.caseList);
		/*System.out.println("vektorlista");
		System.out.println(VectorInformation.vectorList);
		System.out.println("!!!");*/
		
		String attributeToCut = AttributeSelectorRule.selectAttributeForCutting();
		tree.push(new TreeElement("node", attributeToCut));
		
		Set<String> set = CuttingInformation.possibleValuesForAttribute(attributeToCut);
		
		for(String value : set) {
			ArrayList<Case> newCaseList = new ArrayList<>();
			tree.push(new TreeElement("edge", value));
			for(Case currentCase : CuttingInformation.caseList) {
				if(currentCase.getAttributeValue(attributeToCut).equals(value)) {
					currentCase.removeAttribute(attributeToCut);
					newCaseList.add(currentCase);
				}
			}
			stack.push(newCaseList);
		}
		
		CaseInformation.deleteAttribute(attributeToCut);
	}
}
