package controller.model;

import java.util.ArrayList;
import java.util.Stack;

public class DecisionTree {
	public static class Element {
		private String value = "";
		
		public Element(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
		public String toString() {
			return value;
		}
		
	}
	
	public static class Edge {
		private String name = "";
		
		public Edge(String name) {
			this.name = name;
		}
		
		public Edge(Edge copy) {
			this.name = copy.name;
		}
		
		public String getName() {
			return name;
		}
		
		public String toString() {
			return name;
		}
		
	}
	
	public static class Node {
		private String name = "";
		private Node ancestor = null;
		private Edge ancestorEdge = null;
		private ArrayList<Edge> descendantEdges = null;
		private ArrayList<Element> elements = null;
		
		public Node(String name) {
			this.name = name;
			this.ancestor = null;
			this.ancestorEdge = null;
			this.descendantEdges = new ArrayList<>();
			this.elements = new ArrayList<>();
		}
		
		public Node(String name, ArrayList<Element> elements) {
			this.name = name;
			this.ancestor = null;
			this.ancestorEdge = null;
			this.descendantEdges = new ArrayList<>();
			this.elements = new ArrayList<>();
			for(int i=0; i<elements.size(); i++) {
				this.elements.add(elements.get(i));
			}
		}
		
		public Node(Node copy) {
			this.name = copy.name;
			this.ancestor = copy.ancestor;
			this.ancestorEdge = copy.ancestorEdge;
			this.descendantEdges = new ArrayList<>();
			for(int i=0; i<copy.descendantEdges.size(); i++) {
				this.descendantEdges.add(copy.descendantEdges.get(i));
			}
			this.elements = new ArrayList<>();
			for(int i=0; i<copy.elements.size(); i++) {
				this.elements.add(copy.elements.get(i));
			}
		}

		public String getName() {
			return name;
		}

		public Node getAncestor() {
			return ancestor;
		}

		public void setAncestor(Node ancestor) {
			this.ancestor = ancestor;
		}

		public Edge getAncestorEdge() {
			return ancestorEdge;
		}
		
		public void setAncestorEdge(Edge ancestorEdge) {
			this.ancestorEdge = ancestorEdge;
		}

		public ArrayList<Edge> getDescendantEdges() {
			return descendantEdges;
		}
		
		public void setDescendantEdges(ArrayList<Edge> descendantEdges) {
			this.descendantEdges = new ArrayList<>();
			for(int i=0; i<descendantEdges.size(); i++) {
				this.descendantEdges.add(descendantEdges.get(i));
			}
		}

		public ArrayList<Element> getElements() {
			return elements;
		}
		
		public String toString() {
			String node = "";
			node += "name: " + name + ", ";
			node += "ancestor: " + (ancestor == null ? "null" : ancestor.getName()) + ", ";
			node += "ancestorEdge: " + ancestorEdge + ", ";
			node += "descendantEdges: [";
			if(descendantEdges.size() == 0) {
				node += "], ";
			} else {
				for(int i=0; i<descendantEdges.size(); i++) {
					node += descendantEdges.get(i) + ((i == descendantEdges.size() - 1) ? "], " : ", ");  
				}
			}
			node += "elements: [";
			if(elements.size() == 0) {
				node += "]";
			} else {
				for(int i=0; i<elements.size(); i++) {
					node += elements.get(i) + ((i == elements.size() - 1) ? "]" : ", ");  
				}
			}
			return node;
		}
	
	}
	
	private ArrayList<Node> nodes = null;
	
	public DecisionTree(Stack<Node> nodes) {
		this.nodes = new ArrayList<>();
		
		while(!nodes.empty()) {
			this.nodes.add(nodes.pop());
		}
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}
	
	public String toString() {
		String tree = "";
		for(int i=0; i<nodes.size(); i++) {
			tree += nodes.get(i).toString();
			if(!(i == nodes.size() - 1)) {
				tree += ";";
			}
		}
		return tree;
	}
	
}
