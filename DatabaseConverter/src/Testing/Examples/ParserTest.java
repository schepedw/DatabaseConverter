package Testing.Examples;

import java.io.File;
import java.util.ArrayList;

import org.junit.Test;

import Converter.SchemaParser;
import MongoDB.Collection;
import MongoDB.Field;
import MongoDB.SchemaConverter;
import RelationalDB.Column;
import RelationalDB.Database;
import RelationalDB.ForeignKey;
import RelationalDB.Table;

public class ParserTest {

	@Test
	public void testGetName() throws Exception {
		File ex = new File("src/Testing/Examples/SampleNoDataOutput.txt");
		SchemaParser dp = new SchemaParser(ex);
		Database db = dp.parse();
		
		SchemaConverter sc = new SchemaConverter(db);
		ArrayList<Collection> collections = sc.getCollectionsFromSchema();
		
		for (Collection c : collections) {
			printHierarchyCollection(c, 0);
		}
	}

	public void printHierarchyDatabase(Database db) {
		System.out.println("Database name: " + db.getName());
		for (Table table : db.getTables()) {
			System.out.println("   Table name: " + table.getName());
			for (Column pk : table.getPrimaryKeys()) {
				System.out.println("      Primary key: " + pk.getName());
			}
			for (Column column : table.getColumns()) {
				if (!table.getPrimaryKeys().contains(column)) {
					System.out.println("      " + column.getName());
				}
			}
			for (ForeignKey key : table.getForeignKeys()) {
				String column = key.getColumn().getName();
				String kTable = key.getKeyTable().getName();
				String kColumn = key.getKeyColumn().getName();
				System.out.println(String.format("      FK from %s to %s.%s", column, kTable, kColumn));
			}
		}
	}
	
	public void printHierarchyCollection(Collection collection, int index) {
		String spaces = "";
		for (int i = 0; i < index; i++) {
			spaces += "   ";
		}
		if (collection.getLowerCollections().size() == 0) {
			return;
		}
		System.out.println(spaces + collection.getName());
		for (Field f : collection.getFields()) {
			System.out.println(spaces + "   " + f.getName());
		}
		for (Collection c1 : collection.getLowerCollections()) {
			printHierarchyCollection(c1, index + 1);
		}
	}
}
