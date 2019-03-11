package controller.builders.treebuilder;

import java.util.ArrayList;
import java.util.Stack;

import controller.builders.treebuilder.rules.FurtherDividerRule;
import controller.informations.CaseInformation;
import controller.informations.VectorInformation;
import controller.model.Case;
import controller.model.DecisionTree;
import controller.model.DecisionTree.Edge;
import controller.model.DecisionTree.Index;
import controller.model.DecisionTree.Node;
import controller.model.TreeElement;
import result.CreateResult;

public class TreeBuilder {	
	public TreeBuilder(Stack<ArrayList<Case>> stack) {
		ArrayList<Case> caseList;
		ArrayList<DecisionTree> results = new ArrayList<>();
		while(!stack.isEmpty()) {
			caseList = stack.pop();
			CaseInformation.setPossibleClassValues(caseList);
			results.add(buildTree(caseList));
			if(VectorInformation.moreThanTwoClassifications) {
				// attribute names must be reset, because they have been deleted from CaseInformation
				CaseInformation.setAttributeNames();
				VectorInformation.restoreVectorList();
			}
		}
		CreateResult.createResult(results);
	}

	private DecisionTree buildTree(ArrayList<Case> caseList) {
		Stack<TreeElement> decisionTreeStack = FurtherDividerRule.divider(caseList);
		
		Stack<Index> indexStack = new Stack<>();
		Stack<Node> nodeStack = new Stack<>();
		Stack<Node> preparedNodeStack = new Stack<>();
		
		Node node;
		Stack<Node> nodes = new Stack<>();
		
		while(!decisionTreeStack.isEmpty()) {
			TreeElement element = new TreeElement(decisionTreeStack.pop());
			switch(element.getType()) {
				case "index":
					indexStack.push(new Index(element.getValue()));
					break;
				case "leaf":
					ArrayList<Index> indexes = new ArrayList<>();
					while(!indexStack.isEmpty()) {
						indexes.add(indexStack.pop());
					}
					nodeStack.push(new Node(element.getValue(), indexes));
					break;
				case "edge":
					node = new Node(nodeStack.pop());
					node.setAncestorEdge(new Edge(element.getValue()));
					preparedNodeStack.push(node);
					break;
				case "node":
					node = new Node(element.getValue());
					ArrayList<Edge> edges = new ArrayList<>();
					while(!preparedNodeStack.isEmpty()) {
						Node preparedNode = new Node(preparedNodeStack.pop());
						preparedNode.setAncestor(new Node(element.getValue()));
						nodes.push(preparedNode);
						edges.add(new Edge(preparedNode.getAncestorEdge()));	
					}
					node.setDescendantEdges(edges);
					nodeStack.push(node);
					//roots ancestor is indeed null
					if(decisionTreeStack.isEmpty()) {
						nodes.push(node);
					}
					break;
			}
		}
		
		return new DecisionTree(nodes);
	}
	
}
