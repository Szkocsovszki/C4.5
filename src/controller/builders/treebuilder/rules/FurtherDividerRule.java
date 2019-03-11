package controller.builders.treebuilder.rules;

import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

import controller.informations.CaseInformation;
import controller.informations.CuttingInformation;
import controller.model.Case;
import controller.model.TreeElement;
import service.VectorOperations;

public class FurtherDividerRule {
	public static Stack<TreeElement> tree;
	
	public static Stack<TreeElement> divider(ArrayList<Case> caseList) {
		Stack<ArrayList<Case>> stack = new Stack<>();
		tree = new Stack<>();
				
		stack.add(caseList);
		
		ArrayList<Case> i = new ArrayList<>();
		
		while(!stack.isEmpty()) {
			CuttingInformation.caseList = stack.pop();
			
			i = CuttingInformation.caseList;
			VectorOperations.deleteVectors(i);
			if(FinishingRule.isFinished(i)) {
				tree.push(new TreeElement("leaf", ClassifierRule.determineTheClassOfTheLeaf(i)));
				for(int j=0; j<i.size(); j++) {
					tree.push(new TreeElement("index", i.get(j).getIndex() + ""));
				}				
			} else {
				divide(stack);
			}
		}
		
		return tree;
	}

	private static void divide(Stack<ArrayList<Case>> stack) {
		/*System.out.println("vektorlista");
		System.out.println(VectorInformation.vectorList);
		System.out.println("!!!");*/
		
		String attributeToCut = AttributeSelectorRule.selectAttributeForCutting();
		tree.push(new TreeElement("node", attributeToCut));
		
		Set<String> set = CuttingInformation.possibleValuesForAttribute(attributeToCut);
		
		for(String value : set) {
			ArrayList<Case> newCaseList = new ArrayList<>();
			tree.push(new TreeElement("edge", value));
			for(Case actualCase : CuttingInformation.caseList) {
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
