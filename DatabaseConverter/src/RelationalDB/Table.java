package RelationalDB;

import java.util.ArrayList;

public class Table {

	private String name;
	private ArrayList<Column> columns;
	private ArrayList<ForeignKey> foreignKeys;
	private ArrayList<Column> primaryKeys;

	public Table(String name) {
		this();
		this.name = name;
	}
	
	public Table() {
		this.foreignKeys = new ArrayList<ForeignKey>();
		this.primaryKeys = new ArrayList<Column>();
		this.columns = new ArrayList<Column>();
	}

	public void addColumn(Column c) {
		this.columns.add(c);
	}
	
	public ArrayList<Column> getColumns() {
		return this.columns;
	}
	
	public ArrayList<Column> getPrimaryKeys() {
		return this.primaryKeys;
	}
	
	public ArrayList<ForeignKey> getForeignKeys() {
		return this.foreignKeys;
	}

	public void addPrimaryKeyByColumnName(String name) {
		Column c = getColumnByName(name);
		this.primaryKeys.add(c);
	}

	public Column getColumnByName(String name) {
		for (Column c : this.columns) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	protected Boolean removeColumn(Column c) {
		return this.columns.remove(c);
	}

	protected int getNumColumns() {
		return this.columns.size();
	}

	public void addForeignKey(Table table, Column column, Table keyTable, Column keyColumn) throws Exception {
		ArrayList<ForeignKey> keyList;
		if (this.foreignKeys == null) {
			keyList = new ArrayList<ForeignKey>();
		} else {
			keyList = this.foreignKeys;
		}
		keyList.add(new ForeignKey(table, column, keyTable, keyColumn));
	}

	protected ArrayList<ForeignKey> getForeignKeys(Table t) {
		return this.foreignKeys;
	}

	public String getName() {
		return this.name;
	}
	
	public ArrayList<Column> getForeignKeyColumns() {
		ArrayList<Column> columns = new ArrayList<Column>();
		for (ForeignKey fk : this.foreignKeys) {
			columns.add(fk.getColumn());
		}
		return columns;
	}

}
