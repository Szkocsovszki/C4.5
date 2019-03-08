package reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Stack;

import controller.builders.stackbuilder.StackBuilder;
import controller.converters.CaseConverter;
import controller.converters.Discretizer;
import controller.informations.CaseInformation;
import controller.informations.CuttingInformation;
import controller.informations.VectorInformation;
import controller.model.Case;
import controller.model.ColumnVector;

public class CaseReader {
	
	public static boolean readInVectorFormat = true;
	
	public static Stack<ArrayList<Case>> reading() {
		ArrayList<Case> caseList = null;
		Stack<ArrayList<Case>> stack = null;
		try {
			//BufferedReader source = new BufferedReader(new FileReader(new File("src\\reader\\source.txt")));
			BufferedReader source = new BufferedReader(new FileReader(new File("src\\reader\\sourceVectorFormat.txt")));
			caseList = new ArrayList<Case>();
			// first row contains the name of the attributes
			String line = source.readLine();
			CaseInformation.saveAttributeNames(line.split(";"));
			CaseInformation.setAttributeNames();
			
			if(readInVectorFormat) {
				CaseConverter.inputIsInVectorFormat = true;
				VectorInformation.vectorList = new ArrayList<>();
				// if the input is given in vector format, the next row contains the possible values for attributes
				line = source.readLine();
				String[] possibleValues = line.split(";");
				
				Discretizer.valuesOfTheAttribute = new LinkedHashMap<>();
				
				// noa + 1, mert az osztályozás oszlopát is be kell olvasni
				for(int i=0; i<CaseInformation.numberOfAttributes + 1; i++) {
					String[] valueNames = possibleValues[i].split(",");
					// lementjük az attribútum-érték párokat
					// ha attribútum lehetséges értékeiről van szó
					if(i < CaseInformation.numberOfAttributes) {
						Discretizer.valuesOfTheAttribute.put(CaseInformation.attributeNames.get(i), valueNames);
					}
					// ha osztályozás értékekről van szó
					else {
						Discretizer.valuesOfTheAttribute.put(CaseInformation.className, valueNames);
					}
					// attribútum lehetséges értékeivel üres vektorokat hozunk létre
					for(int j=0; j<valueNames.length; j++) {
						VectorInformation.vectorList.add(new ColumnVector(valueNames[j]));
					}
				}
				
				// esetek olvasása
				while ((line = source.readLine()) != null) {
					// attribútumok szerinti szétbontás
					String[] actualCase = line.split(";");
					int jump = 0;
					for(int i=0; i<actualCase.length; i++) {
						// attribútumérték szerinti szétbontás
						String[] values = actualCase[i].split(",");
						// adott oszlopvektor feltöltése az eset megfelelő értékével
						for(int j=0; j<values.length; j++) {
							VectorInformation.vectorList.get(jump++).putValue(Double.parseDouble(values[j]));
						}
					}
				}
				
				CaseConverter.convertCaseToVector();
				
				/* 
				 * ezt a Discretizerbe kell majd rakni, mert attribútumonként van szükség határértékre
				 */
				// határérték előállítása
				double threshold = 0.5;
				
				// folytonos változók diszkretizálása
				// a Discretizer fogja feltöleni a caseListet megfelelő diszkrét esetekkel
				caseList = Discretizer.discretize(VectorInformation.vectorList, threshold);
				
			}
			
			// nem vektorformátumú beolvasás
			else {
				VectorInformation.vectorList = new ArrayList<>();
				VectorInformation.classifications = new HashSet<>();
				int numberOfClassifications = 0;
				// olvassuk az attribútumok lehetséges értékeit -> ez az esetekből derül ki, nem előre adott, mint vektorosnál
				while ((line = source.readLine()) != null) {
					String[] actualCase = line.split(";");
					caseList.add(new Case(actualCase));
					for(int i=0; i<actualCase.length; i++) {
						// leellenőrizzük, hogy létezik-e már ilyen vektor
						boolean newVector = true;
						for(int j=0; j<VectorInformation.vectorList.size(); j++) {
							if(VectorInformation.vectorList.get(j).getName().equals(actualCase[i])) {
								newVector = false;
								break;
							}
						}
						// ha még nem létezik ilyen nevű vektor, akkor létrehozunk egy újat
						if(newVector) {
							VectorInformation.vectorList.add(new ColumnVector(actualCase[i]));
							if(i == actualCase.length - 1) {
								if(!VectorInformation.classifications.contains(actualCase[i])) {
									VectorInformation.classifications.add(actualCase[i]);
									numberOfClassifications++;
								}
							}
						}
					}
				}
				
				if(numberOfClassifications > 2) {
					CaseConverter.moreThanTwoClassification = true;
					VectorInformation.negatedClassifications = new HashSet<>();
					for(String classification : VectorInformation.classifications) {
						VectorInformation.vectorList.add(new ColumnVector("¬" + classification));
						VectorInformation.negatedClassifications.add("¬" + classification);
					}
				}
				
				CuttingInformation.caseList = caseList;
				CaseConverter.convertCaseToVector();
				if(numberOfClassifications > 2) {
					VectorInformation.saveVectorList();
				}
			}
			
			CuttingInformation.defaultNumberOfCases = caseList.size();
			stack = StackBuilder.createStack(caseList);
			
			source.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stack;
	}
	
}
