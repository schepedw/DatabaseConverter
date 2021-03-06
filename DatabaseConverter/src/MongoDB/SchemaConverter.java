package MongoDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		this.collections = new ArrayList<Collection>();
		initializeFKMap(leftoverTables);
		while (leftoverTables.size() > 0) {
			Collection collection = createCollectionsHierarchy(leftoverTables);
			this.collections.add(collection);
			leftoverTables = removeUsedTables(collection, leftoverTables);
		}
		return this.collections;
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
		return buildHierarchy(currentTable, new Collection(currentTable.getName()), true);
	}
	
	private Collection buildHierarchy(Table table, Collection collection, boolean outerCollection) {
		addFieldsToCollection(table, collection);
		ArrayList<ForeignKey> foreignKeys = getForeignKeysPointingToTable(table);
		ArrayList<ForeignKey> otherKeys = new ArrayList<ForeignKey>();
		if (!outerCollection) {
			otherKeys = table.getForeignKeys();
			foreignKeys.removeAll(otherKeys);
		}
		if (foreignKeys.size() == 0) {
			return collection;
		} else {
			for (ForeignKey fk : foreignKeys) {
				if (tableExistsAbove(fk.getTable().getName(), collection)) {
					continue;
				} else {
					boolean nest = getToNestFromUser(fk.getTable(), collection);
					if (nest) {
						Collection newCollection = new Collection(fk.getTable()
								.getName());
						collection.lowerCollections.add(newCollection);
						newCollection.setHigherCollection(collection);
						if (!outerCollection) {
							for (ForeignKey k : otherKeys) {
								Field field = new Field(k.getTable().getName());
								if (!tableExistsAbove(k.getTable().getName(),
										newCollection)) {
									newCollection.addField(field);
								}
							}
						}
						buildHierarchy(fk.getTable(), newCollection, false);
					} else {
						Field field = new Field(fk.getTable().getName());
						collection.addField(field);
						
						Collection newCollection = new Collection(fk.getTable().getName());
						this.collections.add(newCollection);
						buildHierarchy(fk.getTable(), newCollection, false);
					}
				}
			}
		}
		return collection;
	}

	private boolean getToNestFromUser(Table table, Collection collection) {
		System.out.print("Do you want to nest " + table.getName() + " inside of " + collection.getName() + "? ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Character s = '0';
        try {
			s = br.readLine().trim().toLowerCase().charAt(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return s.equals('y');
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
