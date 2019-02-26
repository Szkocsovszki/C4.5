package controller.informations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import controller.model.Case;

public class CaseInformation {
	public static int index = 0;
	public static ArrayList<String> attributeNames = null;
	public static ArrayList<String> savedAttributeNames = null;
	public static int numberOfAttributes = 0;
	public static String className;
	public static String positiveCase = "";
	public static String negativeCase = "";
	public static ArrayList<Case> caseList = null;
	public static int defaultNumberOfCases = 0;
	
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
	
	public static Set<String> possibleClasses() {
		Set<String> set = new HashSet<>();
		
		set.add(positiveCase);
		set.add(negativeCase);
		
		return set;
	}
	
	public static Set<String> possibleValuesForAttribute(String attributeName) {
		Set<String> set = new HashSet<>();
		
		for(int i=0; i<caseList.size(); i++) {
			set.add(caseList.get(i).getAttributeValue(attributeName));
		}
		
		/*System.out.println(attributeName + ": " + set.size());
		System.out.println(set.toString());*/
		
		return set;
	}
	
}
