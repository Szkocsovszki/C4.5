package reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Stack;

import controller.model.Case;
import controller.model.CaseInformation;
import controller.model.ColumnVector;

public class CaseReader {
	
	public static boolean vectorFormat = false;
	public static ArrayList<ColumnVector> vectorList = null;
	
	public static Stack<ArrayList<Case>> reading() {
		ArrayList<Case> caseList = null;
		Stack<ArrayList<Case>> stack = null;
		try {
			BufferedReader source = new BufferedReader(new FileReader(new File("src\\reader\\source4.txt")));
			//BufferedReader source = new BufferedReader(new FileReader(new File("src\\reader\\sourceVectorFormat.txt")));
			caseList = new ArrayList<Case>();
			// first row contains the name of the attributes
			String line = source.readLine();
			CaseInformation.saveAttributeNames(line.split(";"));
			CaseInformation.setAttributeNames();
			
			if(vectorFormat) {
				vectorList = new ArrayList<>();
				// if the input is given in vector format, the next row contains the possible values for attributes
				line = source.readLine();
				String[] possibleValues = line.split(";");
				
				HashMap<String, String[]> valuesOfTheAttribute = new LinkedHashMap<>();
				
				// noa + 1, mert az osztályozás oszlopát is be kell olvasni
				for(int i=0; i<CaseInformation.numberOfAttributes + 1; i++) {
					String[] valueNames = possibleValues[i].split(",");
					if(i < CaseInformation.numberOfAttributes) {
						valuesOfTheAttribute.put(CaseInformation.attributeNames.get(i), valueNames);
					} else {
						valuesOfTheAttribute.put(CaseInformation.className, valueNames);
					}
					for(int j=0; j<valueNames.length; j++) {
						vectorList.add(new ColumnVector(valueNames[j]));
					}
				}
				
				while ((line = source.readLine()) != null) {
					String[] actualCase = line.split(";");
					int jump = 0;
					for(int i=0; i<actualCase.length; i++) {
						String[] values = actualCase[i].split(",");
						for(int j=0; j<values.length; j++) {
							vectorList.get(jump++).putValue(Double.parseDouble(values[j]));
						}
					}
				}
				
				for(ColumnVector vector : vectorList) {
					vector.createColumnVector();
				}
				
				// határérték előállítása
				double threshold = 0.5;
				
				// folytonos változók diszkretizálása
				discretize(vectorList, valuesOfTheAttribute, caseList, threshold);
				
				for(int i=0; i<caseList.size(); i++) {
					System.out.println(caseList.get(i));
				}
			} else {
				while ((line = source.readLine()) != null) {
					String[] actualCase = line.split(";");
					caseList.add(new Case(actualCase));
				}
			}
			
			stack = createStack(caseList);
			
			source.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stack;
	}
	
	private static ColumnVector getVectorFromVectorListByName(String name, ArrayList<ColumnVector> list) {
		for(ColumnVector i : list) {
			if(i.getName().equals(name)) {
				return i;
			}
		}
		
		return null;
	}
	
	private static void discretize(
			ArrayList<ColumnVector> vectorList,
			HashMap<String, String[]> valuesOfTheAttribute,
			ArrayList<Case> caseList,
			double threshold
	) {
		// ennyi eset van
		for(int i=0; i<vectorList.size() - 1; i++) {
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
				double max = getVectorFromVectorListByName(values[0], vectorList).getVector()[i];
				String maxName = values[0];
				for(String name : values) {
					double value = getVectorFromVectorListByName(name, vectorList).getVector()[i];
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
