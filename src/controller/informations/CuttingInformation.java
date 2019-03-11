package controller.informations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import controller.model.Case;

public class CuttingInformation {
	public static int defaultNumberOfCases = 0;
	public static ArrayList<Case> caseList = null;
	
	public static Set<String> possibleValuesForAttribute(String attributeName) {
		Set<String> set = new HashSet<>();
		for(int i=0; i<caseList.size(); i++) {
			set.add(caseList.get(i).getAttributeValue(attributeName));
		}
		return set;
	}
}
