package model;

public class Type_notification {
private int id;
private String name;

	public Type_notification() {}
	
	public Type_notification(int id, String name) {
		this.id = id;
		this.name=name;
	}

public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
	

}
