package reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Stack;

import controller.builders.stackbuilder.StackBuilder;
import controller.converters.CaseConverter;
import controller.converters.ValueSelector;
import controller.informations.CaseInformation;
import controller.informations.CuttingInformation;
import controller.informations.VectorInformation;
import controller.model.Case;
import controller.model.ColumnVector;

public class CaseReader {
	public static boolean inputIsInVectorFormat = true;
	
	private static ArrayList<Case> readInVectorFormat(BufferedReader source) throws IOException {
		VectorInformation.vectorList = new ArrayList<>();
		// if the input is given in vector format, the next row contains the possible values for attributes
		String line = source.readLine();
		String[] possibleValues = line.split(";");
		
		ValueSelector.valuesOfTheAttributes = new LinkedHashMap<>();
		
		// noa + 1, mert az osztályozás oszlopát is be kell olvasni
		for(int i=0; i<CaseInformation.numberOfAttributes + 1; i++) {
			String[] valueNames = possibleValues[i].split(",");
			// lementjük az attribútum-érték párokat
			// ha attribútum lehetséges értékeiről van szó
			if(i < CaseInformation.numberOfAttributes) {
				ValueSelector.valuesOfTheAttributes.put(CaseInformation.attributeNames.get(i), valueNames);
			}
			// ha osztályozás értékekről van szó
			else {
				ValueSelector.valuesOfTheAttributes.put(CaseInformation.className, valueNames);
			}
			// attribútum lehetséges értékeivel üres vektorokat hozunk létre
			for(int j=0; j<valueNames.length; j++) {
				VectorInformation.vectorList.add(new ColumnVector(valueNames[j]));
			}
		}
		
		// esetek olvasása
		while ((line = source.readLine()) != null) {
			// attribútumok szerinti szétbontás
			String[] currentCase = line.split(";");
			int vectorIndex = 0;
			for(int i=0; i<currentCase.length; i++) {
				// attribútumérték szerinti szétbontás
				String[] values = currentCase[i].split(",");
				// adott oszlopvektor feltöltése az eset megfelelő értékével
				for(int j=0; j<values.length; j++) {
					VectorInformation.vectorList.get(vectorIndex++).putValue(Double.parseDouble(values[j]));
				}
			}
		}
		
		CaseConverter.convertCasesToVectors();
		
		// vektoros alakban adott attribútumnak megfelelő érték választás
		return ValueSelector.selectValueForAttributes();
	}
	
	private static ArrayList<Case> readInNonVectorFormat(BufferedReader source) throws IOException {
		String line;
		ArrayList<Case> caseList = new ArrayList<Case>();
		VectorInformation.vectorList = new ArrayList<>();
		VectorInformation.classifications = new HashSet<>();
		int numberOfClassifications = 0;
		// olvassuk az attribútumok lehetséges értékeit -> ez az esetekből derül ki, nem előre adott, mint vektorosnál
		while ((line = source.readLine()) != null) {
			String[] currentCase = line.split(";");
			caseList.add(new Case(currentCase));
			for(int i=0; i<currentCase.length; i++) {
				// leellenőrizzük, hogy létezik-e már ilyen vektor
				boolean isNewVector = true;
				for(int j=0; j<VectorInformation.vectorList.size(); j++) {
					if(VectorInformation.vectorList.get(j).getName().equals(currentCase[i])) {
						isNewVector = false;
						break;
					}
				}
				// ha még nem létezik ilyen nevű vektor, akkor létrehozunk egy újat
				if(isNewVector) {
					VectorInformation.vectorList.add(new ColumnVector(currentCase[i]));
					if(i == currentCase.length - 1) {
						if(!VectorInformation.classifications.contains(currentCase[i])) {
							VectorInformation.classifications.add(currentCase[i]);
							numberOfClassifications++;
						}
					}
				}
			}
		}
		
		if(numberOfClassifications > 2) {
			VectorInformation.moreThanTwoClassifications = true;
			VectorInformation.negatedClassifications = new HashSet<>();
			for(String classification : VectorInformation.classifications) {
				VectorInformation.vectorList.add(new ColumnVector("¬" + classification));
				VectorInformation.negatedClassifications.add("¬" + classification);
			}
		}
		
		CuttingInformation.caseList = caseList;
		CaseConverter.convertCasesToVectors();
		if(numberOfClassifications > 2) {
			VectorInformation.saveVectorList();
		}
		
		return caseList;
	}
	
	public static Stack<ArrayList<Case>> reading() {
		Stack<ArrayList<Case>> stack = null;
		ArrayList<Case> caseList = null;
		try {
			//BufferedReader source = new BufferedReader(new FileReader(new File("src\\reader\\input\\source.txt")));
			BufferedReader source = new BufferedReader(new FileReader(new File("src\\reader\\input\\sourceVectorFormat.txt")));
			// first row contains the name of the attributes
			String line = source.readLine();
			CaseInformation.saveAttributeNames(line.split(";"));
			CaseInformation.setAttributeNames();
			
			caseList = inputIsInVectorFormat ? readInVectorFormat(source) : readInNonVectorFormat(source);
			
			CuttingInformation.defaultNumberOfCases = caseList.size();
			stack = StackBuilder.createStack(caseList);
			
			source.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stack;
	}
	
}
