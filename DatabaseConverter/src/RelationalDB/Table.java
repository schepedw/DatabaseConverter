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
	private HashMap<Column, HashMap<Table, Column>> foreignKeys;
	private ArrayList<Column> primaryKeys;

	public Table(String name) {
		this.foreignKeys = new HashMap<Column, HashMap<Table, Column>>();
		this.primaryKeys=new ArrayList<Column>();
		this.columns = new ArrayList<Column>();
		this.name = name;
	}

	public void addColumn(Column c) {
		this.columns.add(c);
	}
	public void addPrimaryKeyByColumnName(String name) throws Exception{
		Column c=getColumnByName(name);
		this.primaryKeys.add(c);			
	}
	
	protected Column getColumnByName(String name) throws Exception {
		for (Column c: this.columns){
			if (c.getName().equals(name)){
				return c;
			}
		}
		throw new Exception("Column "+ name+" does not exist in table "+ getName());
	}

	protected Boolean removeColumn(Column c) {
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
	
	public String getName(){
		return this.name;
	}
	
}
