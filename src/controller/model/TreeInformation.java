package controller.model;

public class TreeInformation {
	private String elementType = "";
	private String value = "";
	
	public TreeInformation(String elementType, String value) {
		this.elementType = elementType;
		this.value = value;
	}
	
	public TreeInformation(TreeInformation copy) {
		this.elementType = copy.elementType;
		this.value = copy.value;
	}

	public String getElementType() {
		return elementType;
	}

	public String getValue() {
		return value;
	}
	
	public String toString() {
		return elementType + ": " + value;
	}
	
}
