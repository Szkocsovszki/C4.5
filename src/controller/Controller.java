package controller;

import java.util.ArrayList;
import java.util.Stack;

import controller.model.Case;
import controller.treebuilder.TreeBuilder;
import reader.CaseReader;

public class Controller {
	private Stack<ArrayList<Case>> stack;
	
	public Controller() {
		stack = CaseReader.reading();
		new TreeBuilder(stack);
	}

}
