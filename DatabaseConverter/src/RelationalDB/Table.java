package RelationalDB;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author schepedw. Created Apr 17, 2013.
 */
public class Table {

	private String name;
	private ArrayList<Column> columns;
	private HashMap<Table, ArrayList<Column>> foreignKeys;

	public Table(String name) {
		this.foreignKeys = new HashMap<Table, ArrayList<Column>>();
		this.columns = new ArrayList<Column>();
		this.name = name;
	}

	protected void addTable(Column c) {
		this.columns.add(c);
	}

	protected Column getTable(Column c) {
		int i = this.columns.indexOf(c);
		return this.columns.get(i);
	}

	protected Boolean removeTable(Column c) {
		return this.columns.remove(c);
	}

	protected int getNumColumns() {
		return this.columns.size();
	}

	protected void addForeignKey(Table t, Column c) {
		ArrayList<Column> keyList;
		if (this.foreignKeys.get(t) == null) {
			keyList = new ArrayList<Column>();
		} else {
			keyList = this.foreignKeys.get(t);
		}
		keyList.add(c);
		this.foreignKeys.put(t, keyList);
	}
	
	protected ArrayList<Column> getForeignKeys(Table t){
		return this.foreignKeys.get(t);
	}
}
