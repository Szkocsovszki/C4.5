package reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Stack;

import controller.model.Case;
import controller.model.CaseInformation;

public class CaseReader {
	
	public static Stack<ArrayList<Case>> reading() {
		ArrayList<Case> caseList = null;
		Stack<ArrayList<Case>> stack = null;
		try {
			BufferedReader source = new BufferedReader(new FileReader(new File("src\\reader\\source4.txt")));
			caseList = new ArrayList<Case>();
			//first row contains the name of the attributes
			String line = source.readLine();
			CaseInformation.saveAttributeNames(line.split(";"));
			CaseInformation.setAttributeNames();
			
			while ((line = source.readLine()) != null) {
				String[] actualCase = line.split(";");
				caseList.add(new Case(actualCase));
			}
			
			stack = createStack(caseList);
			
			source.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stack;
	}
	
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
	
	private static Stack<ArrayList<Case>> createStack(ArrayList<Case> caseList) {
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
