package MongoDB;

import java.util.ArrayList;

public class Collection {

	String name;
	Collection higherCollection;
	ArrayList<Collection> lowerCollections;
	ArrayList<Field> fields;
	
	public Collection(String name) {
		this.name = name;
		lowerCollections = new ArrayList<Collection>();
		fields = new ArrayList<Field>();
	}
	
	public void setHigherCollection(Collection c) {
		this.higherCollection = c;
	}
	
	public Collection getHigherCollection() {
		return this.higherCollection;
	}
	
	public ArrayList<Collection> getLowerCollections() {
		return this.lowerCollections;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void addField(Field field) {
		this.fields.add(field);
	}
	
	public ArrayList<Field> getFields() {
		return this.fields;
	}
}
