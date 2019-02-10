package controller.treebuilder;

import java.util.ArrayList;
import java.util.Stack;

import controller.model.Case;
import controller.model.CaseInformation;
import controller.model.DecisionTree;
import controller.model.DecisionTree.Edge;
import controller.model.DecisionTree.Element;
import controller.model.DecisionTree.Node;
import controller.model.TreeInformation;
import controller.treebuilder.rules.FurtherDividerRule;
import result.CreateResult;

public class TreeBuilder {	
	public TreeBuilder(Stack<ArrayList<Case>> stack) {
		ArrayList<Case> caseList;
		ArrayList<DecisionTree> results = new ArrayList<>();
		while(!stack.isEmpty()) {
			caseList = stack.pop();
			CaseInformation.setPossibleClassValues(caseList);
			results.add(buildTree(caseList));
			//attribute names must be reset, because they have been deleted from CaseInformation
			CaseInformation.setAttributeNames();
		}
		CreateResult.createResult(results);
	}

	private DecisionTree buildTree(ArrayList<Case> caseList) {
		Stack<TreeInformation> decisionTreeStack = FurtherDividerRule.divider(caseList);
		
		Stack<Element> elementStack = new Stack<>();
		Stack<Node> nodeStack = new Stack<>();
		Stack<Node> preparedNodeStack = new Stack<>();
		
		Node node;
		Stack<Node> nodes = new Stack<>();
		
		while(!decisionTreeStack.isEmpty()) {
			TreeInformation information = new TreeInformation(decisionTreeStack.pop());
			switch(information.getElementType()) {
				case "element":
					elementStack.push(new Element(information.getValue()));
					break;
				case "leaf":
					ArrayList<Element> elements = new ArrayList<>();
					while(!elementStack.isEmpty()) {
						elements.add(elementStack.pop());
					}
					nodeStack.push(new Node(information.getValue(), elements));
					break;
				case "edge":
					node = new Node(nodeStack.pop());
					node.setAncestorEdge(new Edge(information.getValue()));
					preparedNodeStack.push(node);
					break;
				case "node":
					node = new Node(information.getValue());
					ArrayList<Edge> edges = new ArrayList<>();
					while(!preparedNodeStack.isEmpty()) {
						Node preparedNode = new Node(preparedNodeStack.pop());
						preparedNode.setAncestor(new Node(information.getValue()));
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
