package MongoDB;

import java.util.ArrayList;

import RelationalDB.Column;
import RelationalDB.Database;
import RelationalDB.Table;

public class SchemaConverterFlat {
	public Database database;
	public ArrayList<Collection> collections;
	
	public SchemaConverterFlat(Database database) {
		this.database = database;
		this.collections = convertDatabaseToCollections();
	}
	
	public ArrayList<Collection> getCollections() {
		return this.collections;
	}
	
	public ArrayList<Collection> convertDatabaseToCollections() {
		ArrayList<Collection> returnCollection = new ArrayList<Collection>();
		for (Table t : this.database.getTables()) {
			Collection tempCollection = new Collection(t.getName());
			for (Column c : t.getColumns()) {
				Field tempField = new Field(c.getName());
				tempCollection.addField(tempField);
			}
			returnCollection.add(tempCollection);
		}
		return returnCollection;
	}
}
