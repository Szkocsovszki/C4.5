package controller.informations;

import java.util.ArrayList;

import controller.model.Case;

public class CaseInformation {
	public static int index = 0;
	public static ArrayList<String> attributeNames = null;
	public static ArrayList<String> savedAttributeNames = null;
	public static int numberOfAttributes = 0;
	public static String className;
	public static String positiveClassification = "";
	public static String negativeClassification = "";
	
	public static void saveAttributeNames(String[] attributeNames) {
		CaseInformation.savedAttributeNames = new ArrayList<>();
		
		numberOfAttributes = attributeNames.length - 1;
		
		for(int i=0; i<numberOfAttributes; i++) {
			CaseInformation.savedAttributeNames.add(attributeNames[i]);
		}
		
		className = attributeNames[attributeNames.length - 1];
	}
	
	public static void setPossibleClassifications(ArrayList<Case> caseList) {
		ArrayList<String> classificationList = new ArrayList<>();
		
		for(int i=0; i<caseList.size(); i++) {
			String classification = caseList.get(i).getClassification(); 
			if(!classificationList.contains(classification)) {
				classificationList.add(classification);
			}
		}
		
		if(classificationList.size() == 2) {
			positiveClassification = classificationList.get(0);
			negativeClassification = classificationList.get(1);
		}
	}
	
	public static void setAttributeNames() {
		attributeNames = new ArrayList<>();
		
		for(int i=0; i<savedAttributeNames.size(); i++) {
			attributeNames.add(savedAttributeNames.get(i));
		}
	}
	
	public static void deleteAttribute(String attribute) {
		attributeNames.remove(attribute);
	}
	
}
