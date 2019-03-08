package controller.model;

public class TreeElement {
	private String type = "";
	private String value = "";
	
	public TreeElement(String type, String value) {
		this.type = type;
		this.value = value;
	}
	
	public TreeElement(TreeElement copy) {
		this.type = copy.type;
		this.value = copy.value;
	}

	public String getType() {
		return type;
	}

	public String getValue() {
		return value;
	}
	
	public String toString() {
		return type + ": " + value;
	}
	
}
