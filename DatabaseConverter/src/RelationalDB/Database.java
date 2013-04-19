package RelationalDB;

import java.util.ArrayList;

public class Database {

	private String name;
	private ArrayList<Table> tables;

	public Database(String name) {
		this.name = name;
		this.tables = new ArrayList<Table>();
	}

	public void addTable(Table t) {
		this.tables.add(t);
	}
	
	public ArrayList<Table> getTables() {
		return this.tables;
	}

	public Table getTableByTableName(String name) throws Exception {
		for (Table t : this.tables) {
			if (t.getName().equals(name)) {
				return t;
			}
		}
		throw new Exception("Column " + name + " does not exist in table "
				+ getName());
	}

	protected Boolean removeTable(Table t) {
		return this.tables.remove(t);
	}

	protected int getNumTables() {
		return this.tables.size();
	}

	public String getName() {
		return this.name;
	}

}
