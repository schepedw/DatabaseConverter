package Converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import RelationalDB.Column;
import RelationalDB.Database;
import RelationalDB.Table;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author schepedw. Created Apr 17, 2013.
 */
public class DataParser {
	
	private Database db;
	private File schema;
	public DataParser (File schema){
		this.schema=schema;
	}
	
	public Database parse(File schema) throws Exception {
		this.db = new Database(getName(schema));
		String line;
		Scanner input = new Scanner(schema);
		input.useDelimiter("\n");

		while (input.hasNext()) {
			line = input.next();
			while (!line.contains("CREATE TABLE")) {
				line = input.next();
			}
			Table table = addTable(line);
			line = input.next();
			while (line.charAt(0) != ')') {
				if (containsSpecialFlag(line,table)){
					line=input.next();
					continue;
				}
				
				addColumns(table, line);
				line = input.next();

			}
		}
		input.close();
		return this.db;
	}

	private boolean containsSpecialFlag(String line,Table table) throws Exception {
		if (line.contains("CONSTRAINT")||line.contains("KEY")) {
			System.out.println("found a constraint");
			addForeignKey(table, line);
			return true;
		}
		else if (line.contains("PRIMARY KEY")){
			System.out.println("Found a primary key");
			addPrimaryKey(table, line);
			return true;
		}
		else if(line.contains("KEY")) {
			System.out.println("found a key. Ignoring it.");
			return true;
		}
		return false;
	}

	private void addForeignKey(Table table, String line) {
		String[] splitLine=line.split("`");
		String columnName=splitLine[3];
		String refTableName=splitLine[5];
		String refColumnName=splitLine[7];
		Table refTable=this.db.getTableByTableName(refTableName);
		table.addForeignKey(columnName,refTableName,refColumnName);
	}

	private Column addColumns(Table table, String line) {
		String columnName=line.split("`")[1];
		System.out.println("Adding line " + columnName + " to table "
				+ table.getName());
		String type=getType(line);
		Column c = null;
		if (type.equals("int")){
			c = new Column<Integer>(columnName);
			
		}
		else if (type.equals("float")){
			c = new Column<Float>(columnName);
			
		}
		else if (type.equals("String")){
			c = new Column<String>(columnName);
			
		}
		table.addColumn(c);
		return c;

	}
	
	private void addPrimaryKey(Table table, String line) throws Exception{
		String keyName=line.split("`")[1];
		table.addPrimaryKeyByColumnName(keyName);
	}


	private String getType(String line) {
//		if (line.matches("[ ]*`([a-z]|[A-Z])*` int([0-9]*)*"))
//			return "int";
//		else if (line.matches("*bigint([0-9]*)*"))	
//			return "int";
//		else if (line.matches("*float*"))
//			return "float";
//		else if (line.matches("*char([0-9]*)*"))
//			return "String";
		if (line.contains("int(")){
			return "int";
		}
		
		else if (line.contains("` float")){
			return "float";
		}
		else if (line.contains("char(")){
			return "String";
		}
		else if (line.contains("` text")){
			return "String";
		}
		else{
			System.out.println("ERROR: DID NOT MATCH "+ line);
		}
		return null;
	}

	private Table addTable(String line) {
		String tableName=line.split("`")[1];
		System.out.println("Adding table " + tableName + " to db");
		Table table=new Table(tableName);
		return table;
	}

	public String getName(File schema) {
		String name = "";
		try {
			String nameLine;
			Scanner input = new Scanner(schema);
			while (!(nameLine = input.next()).contains("Database:")) {
				System.out.println(nameLine);
			}
			name = input.next();
		} catch (FileNotFoundException exception) {

			exception.printStackTrace();
		}
		return name;
	}
}
