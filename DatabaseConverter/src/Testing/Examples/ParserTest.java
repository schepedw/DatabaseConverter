package Testing.Examples;

import java.io.File;

import org.junit.Test;

import Converter.SchemaParser;
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
		printHierarchy(db);
	}

	public void printHierarchy(Database db) {
		System.out.println("Database name: " + db.getName());
		for (Table table : db.getTables()) {
			System.out.println("   Table name: " + table.getName());
			for (Column<?> pk : table.getPrimaryKeys()) {
				System.out.println("      Primary key: " + pk.getName());
			}
			for (Column<?> column : table.getColumns()) {
				if (!table.getPrimaryKeys().contains(column)) {
					System.out.println("      " + column.getName());
				}
			}
			for (ForeignKey key : table.getForeignKeys()) {
				String column = key.getColumn().getName();
				String kTable = key.getKeyTable();
				String kColumn = key.getKeyColumn();
				System.out.println(String.format("      FK from %s to %s.%s", column, kTable, kColumn));
			}
		}
	}
}
