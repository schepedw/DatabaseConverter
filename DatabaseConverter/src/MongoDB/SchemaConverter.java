package MongoDB;

import java.util.ArrayList;
import java.util.HashMap;

import RelationalDB.Column;
import RelationalDB.Database;
import RelationalDB.ForeignKey;
import RelationalDB.Table;

public class SchemaConverter {
	Database database;
	ArrayList<Collection> collections;
	HashMap<String, Integer> fkCounts;

	public SchemaConverter(Database database) {
		this.database = database;
		this.collections = new ArrayList<Collection>();
		this.fkCounts = new HashMap<String, Integer>();
	}

	private Table getOutermostTable(ArrayList<Table> tableList) {
		for (Table table : tableList) {
			for (ForeignKey fk : table.getForeignKeys()) {
				int count = fkCounts.get(fk.getKeyTable().getName()) + 1;
				fkCounts.put(fk.getKeyTable().getName(), count);
			}
		}
		
		Table currentTable = tableList.get(0);
		int foreignKeys = -1;
		for (Table table : tableList) {
			if (fkCounts.get(table.getName()) >= foreignKeys) {
				currentTable = table;
				foreignKeys = fkCounts.get(table.getName());
			}
		}
		return currentTable;
	}
	
	private void initializeFKMap(ArrayList<Table> tableList) {
		for (Table table : tableList) {
			this.fkCounts.put(table.getName(), 0);
		}
	}

	public ArrayList<Collection> getCollectionsFromSchema() {
		ArrayList<Table> leftoverTables = this.database.getTables();
		ArrayList<Collection> collections = new ArrayList<Collection>();
		initializeFKMap(leftoverTables);
		while (leftoverTables.size() > 0) {
			Collection collection = createCollectionsHierarchy(leftoverTables);
			collections.add(collection);
			leftoverTables = removeUsedTables(collection, leftoverTables);
		}
		return collections;
	}
	
	public ArrayList<Table> removeUsedTables(Collection collection, ArrayList<Table> leftoverTables) {
		String name = collection.getName();
		Table table = this.database.getTableByTableName(name);
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
		addFieldsToCollection(table, collection);
		ArrayList<ForeignKey> foreignKeys = getForeignKeysPointingToTable(table);
		if (foreignKeys.size() == 0) {
			return collection;
		} else {
			for (ForeignKey fk : foreignKeys) {
				if (tableExistsAbove(fk.getTable().getName(), collection)) {
					return collection;
				} else {
					Collection newCollection = new Collection(fk.getTable().getName());
					collection.lowerCollections.add(newCollection);
					newCollection.setHigherCollection(collection);
					
					buildHierarchy(fk.getTable(), newCollection);
				}
			}
		}
		return collection;
	}

	private ArrayList<ForeignKey> getForeignKeysPointingToTable(Table table) {
		ArrayList<ForeignKey> returnList = new ArrayList<ForeignKey>();
		for (Table t : database.getTables()) {
			for (ForeignKey fk : t.getForeignKeys()) {
				if (fk.getKeyTable().equals(table)) {
					returnList.add(fk);
				}
			}
		}
		return returnList;
	}

	private void addFieldsToCollection(Table table, Collection collection) {
		for (Column c : table.getColumns()) {
			if (!table.getForeignKeyColumns().contains(c)) {
				Field field = new Field(c.getName());
				collection.addField(field);
			}
		}
	}
	
	private boolean tableExistsAbove(String tableName, Collection collection) {
		Collection currentCol = collection.getHigherCollection();
		
		while (currentCol != null) {
			if (currentCol.getName().equals(tableName)) {
				return true;
			}
			currentCol = currentCol.getHigherCollection();
		}
		return false;
	}

}
