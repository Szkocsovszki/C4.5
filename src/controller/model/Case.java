package controller.model;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Case {
	private int index;
	private HashMap<String, String> allAttributeValues;
	private String caseClass = null;
	
	public Case(String[] attributeValues) {
		this.allAttributeValues = new LinkedHashMap<>();
		
		index = ++CaseInformation.index;
		
		for(int i=0; i<CaseInformation.numberOfAttributes; i++) {	
			this.allAttributeValues.put(CaseInformation.attributeNames.get(i), attributeValues[i]);
		}
		
		caseClass = attributeValues[attributeValues.length - 1];
	}
	
	public Case(Case copyCase) {
		index = copyCase.getIndex();
		allAttributeValues = new HashMap<>();
		for(String key : copyCase.getAllAttributeValues().keySet()) {
			allAttributeValues.put(key, copyCase.getAttributeValue(key));
		}
		caseClass = copyCase.getCaseClass();
	}

	public HashMap<String, String> getAllAttributeValues() {
		return allAttributeValues;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getCaseClass() {
		return caseClass;
	}

	public String getAttributeValue(String attribute) {
		if(allAttributeValues.containsKey(attribute)) {
			return allAttributeValues.get(attribute);
		}
		
		return "";
	}
	
	public void removeAttribute(String attribute) {
		if(allAttributeValues.containsKey(attribute)) {
			allAttributeValues.remove(attribute);
		}
	}
	
	public void setCaseClass(String caseClass) {
		this.caseClass = caseClass;
	}

	public String toString() {
		return allAttributeValues.toString() + CaseInformation.className + ": " + caseClass + "\n";
	}
}
