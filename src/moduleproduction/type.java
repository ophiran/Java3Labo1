package moduleproduction;

public enum type {

	TEST("text"),
	TEST2("Text2");
	
	String type;
	
	type(String type) {
		this.type = type;
	}
	
	String getType() {
		return type;
	}
	
}
