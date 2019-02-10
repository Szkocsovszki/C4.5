package controller.model;

import java.util.ArrayList;

public class CaseInformation {
	public static int index = 0;
	public static ArrayList<String> attributeNames = null;
	public static ArrayList<String> savedAttributeNames = null;
	public static int numberOfAttributes = 0;
	public static String className;
	public static String positiveCase = "";
	public static String negativeCase = "";
	
	public static void saveAttributeNames(String[] attributeNames) {
		CaseInformation.savedAttributeNames = new ArrayList<>();
		
		numberOfAttributes = attributeNames.length - 1;
		
		for(int i=0; i<numberOfAttributes; i++) {
			CaseInformation.savedAttributeNames.add(attributeNames[i]);
		}
		
		className = attributeNames[attributeNames.length - 1];
	}
	
	public static void setPossibleClassValues(ArrayList<Case> caseList) {
		ArrayList<String> values = new ArrayList<>();
		
		for(int i=0; i<caseList.size(); i++) {
			String value = caseList.get(i).getCaseClass(); 
			if(!values.contains(value)) {
				values.add(value);
			}
		}
		
		if(values.size() == 2) {
			positiveCase = values.get(0);
			negativeCase = values.get(1);
		}
	}
	
	public static void setAttributeNames() {
		attributeNames = new ArrayList<>();
		
		for(int i=0; i<savedAttributeNames.size(); i++) {
			attributeNames.add(savedAttributeNames.get(i));
		}
	}
	
}