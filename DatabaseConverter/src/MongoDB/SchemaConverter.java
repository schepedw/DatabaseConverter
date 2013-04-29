package MongoDB;

import java.util.ArrayList;

import RelationalDB.Column;
import RelationalDB.Database;
import RelationalDB.ForeignKey;
import RelationalDB.Table;

public class SchemaConverter {
	Database database;
	ArrayList<Collection> collections;

	public SchemaConverter(Database database) {
		this.database = database;
		this.collections = new ArrayList<Collection>();
	}

	private Table getOutermostTable(ArrayList<Table> tableList) {
		Table currentTable = tableList.get(0);
		for (Table table : tableList) {
			if (table.getForeignKeys().size() > currentTable.getForeignKeys()
					.size()) {
				currentTable = table;
			}
		}
		return currentTable;
	}
	
	public ArrayList<Collection> getCollectionsFromSchema() {
		ArrayList<Table> leftoverTables = this.database.getTables();
		ArrayList<Collection> collections = new ArrayList<Collection>();
		while (leftoverTables.size() > 0) {
			Collection collection = createCollectionsHierarchy(leftoverTables);
			collections.add(collection);
			leftoverTables = removeUsedTables(collection, leftoverTables);
		}
		return collections;
	}
	
	public ArrayList<Table> removeUsedTables(Collection collection, ArrayList<Table> leftoverTables) {
		String name = collection.getName();
		Table table = null;
		for (Table t : leftoverTables) {
			if (t.getName().equals(name)) {
				table = t;
			}
		}
		if (table != null) {
			leftoverTables.remove(table);
		}
		if (collection.getLowerCollections().size() == 0) {
			return leftoverTables;
		} else {
			for (Collection c : collection.getLowerCollections()) {
				removeUsedTables(c, leftoverTables);
			}
		}
		return leftoverTables;
	}
	
	public Collection createCollectionsHierarchy(ArrayList<Table> tables) {
		Table currentTable = getOutermostTable(tables);
		return buildHierarchy(currentTable, new Collection(currentTable.getName()));
	}
	
	private Collection buildHierarchy(Table table, Collection collection) {
		for (Column c : table.getColumns()) {
			if (!table.getForeignKeyColumns().contains(c)) {
				Field field = new Field(c.getName());
				collection.addField(field);
			}
		}
		if (table.getForeignKeys().size() == 0) {
			return collection;
		} else {
			for (ForeignKey fk : table.getForeignKeys()) {
				if (tableExistsAbove(fk.getKeyTable().getName(), collection)) {
					return collection;
				} else {
					Collection newCollection = new Collection(fk.getKeyTable().getName());
					collection.lowerCollections.add(newCollection);
					newCollection.setHigherCollection(collection);
					
					buildHierarchy(fk.getKeyTable(), newCollection);
				}
			}
		}
		return collection;
	}
	
	private boolean tableExistsAbove(String tableName, Collection collection) {
		Collection currentCol = collection;
		
		while (currentCol != null) {
			if (currentCol.getName().equals(tableName)) {
				return true;
			}
			currentCol = currentCol.getHigherCollection();
		}
		return false;
	}

}
