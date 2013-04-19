package Converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import RelationalDB.Column;
import RelationalDB.Database;
import RelationalDB.Table;

public class SchemaParser {

	private Database db;
	private File schema;

	public SchemaParser(File schema) {
		this.schema = schema;
	}

	public Database parse() throws Exception {
		this.db = new Database(getName());
		String line;
		Scanner input = new Scanner(this.schema);
		input.useDelimiter("\n");

		boolean insideTable = false;
		Table currentTable = new Table();
		while (input.hasNextLine()) {
			line = input.nextLine();	
			
			if (line.contains("CREATE TABLE")) {
				currentTable = addTable(line);
				db.addTable(currentTable);
				insideTable = true;
				line = input.nextLine();
			}
			
			if (!insideTable) { continue; }
			
			if (line.charAt(0) == ')') {
				insideTable = false;
			} else {
				if (containsSpecialFlag(line, currentTable)) {
					//need to move manipulation code 
					//here from containsSpecialFlag
				} else {
					addColumn(currentTable, line);
				}
			}
			
		}
		input.close();
		return this.db;
	}

	private boolean containsSpecialFlag(String line, Table table)
			throws Exception {
		if (line.contains("CONSTRAINT")) {
			String[] foreignKey = extractForeignKey(line);
			table.addForeignKey(foreignKey[0], foreignKey[1], foreignKey[2]);
			return true;
		} else if (line.contains("PRIMARY KEY")) {
			parsePrimaryKey(table, line);
			return true;
		} else if (line.contains("KEY")) {
			return true;
		}
		return false;
	}

	private String[] extractForeignKey(String line) {
		String[] splitLine = line.split("`");
		String[] foreignKey = { splitLine[3], splitLine[5], splitLine[7] };
		return foreignKey;
	}

	private Column<?> addColumn(Table table, String line) {
		String columnName = line.split("`")[1];
		String type = getType(line);
		Column<?> c = null;
		if (type.equals("int")) {
			c = new Column<Integer>(columnName);

		} else if (type.equals("float")) {
			c = new Column<Float>(columnName);

		} else if (type.equals("String")) {
			c = new Column<String>(columnName);

		}
		table.addColumn(c);
		return c;
	}

	private void parsePrimaryKey(Table table, String line) throws Exception {
		String keyName = line.split("`")[1];
		table.addPrimaryKeyByColumnName(keyName);
	}

	private String getType(String line) {
		// if (line.matches("[ ]*`([a-z]|[A-Z])*` int([0-9]*)*"))
		// return "int";
		// else if (line.matches("*bigint([0-9]*)*"))
		// return "int";
		// else if (line.matches("*float*"))
		// return "float";
		// else if (line.matches("*char([0-9]*)*"))
		// return "String";
		if (line.contains("int(")) {
			return "int";
		} else if (line.contains("` float")) {
			return "float";
		} else if (line.contains("char(")) {
			return "String";
		} else if (line.contains("` text")) {
			return "String";
		} else {
			System.out.println("ERROR: DID NOT MATCH " + line);
		}
		return null;
	}

	private Table addTable(String line) {
		String tableName = line.split("`")[1];
		Table table = new Table(tableName);
		return table;
	}

	public String getName() throws FileNotFoundException {
		Scanner input = new Scanner(this.schema);
		while (!(input.next()).contains("Database:"))
			;
		String name = input.next();
		input.close();
		return name;
	}
}
