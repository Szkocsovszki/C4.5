package controller.converters;

import java.util.ArrayList;
import java.util.HashMap;

import controller.informations.VectorInformation;
import controller.model.Case;

public class Discretizer {
	// (attribútumnév, [lehetséges értékei])
	public static HashMap<String, String[]> valuesOfTheAttribute = null;
	private static ArrayList<Case> caseList = null;
	
	public static ArrayList<Case> discretize(double threshold) {
		caseList = new ArrayList<>();
		// ennyi eset van
		for(int i=0; i<VectorInformation.vectorList.size() - 1; i++) {
			// ennyi attribútuma van egy esetnek
			String[] caseValues = new String[valuesOfTheAttribute.size()];
			int actualAttribute = 0;
			
			// diszkretizálás
			// lehetséges attribútumok
			for(String attribute : valuesOfTheAttribute.keySet()) {
				int needsFurtherInvestigation = 0;
				// attribútum lehetséges értékei
				String[] values = valuesOfTheAttribute.get(attribute);
				// értékek értékei
				double max = VectorInformation.getVectorFromVectorListByName(values[0]).getVector()[i];
				String maxName = values[0];
				for(String name : values) {
					double value = VectorInformation.getVectorFromVectorListByName(name).getVector()[i];
					if(value >= threshold) {
						caseValues[actualAttribute++] = name;
						break;
					} else {
						needsFurtherInvestigation++;
						if(value >= max) {
							max = value;
							maxName = name;
						}
						if(needsFurtherInvestigation == values.length) {
							caseValues[actualAttribute++] = maxName;
							break;
						}
					}
				}
			}
			
			caseList.add(new Case(caseValues));
		}
		return caseList;
	}
}
