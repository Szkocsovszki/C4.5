package controller;

import java.util.ArrayList;
import java.util.Stack;

import controller.builders.treebuilder.TreeBuilder;
import controller.model.Case;
import reader.CaseReader;

public class Controller {
	private Stack<ArrayList<Case>> stack;
	
	public Controller() {
		stack = CaseReader.reading();
		new TreeBuilder(stack);
	}

}
