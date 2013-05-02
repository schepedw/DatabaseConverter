package RelationalDB;

public class ForeignKey {
	private Table table;
	private Column column;
	private Table keyTable;
	private Column keyColumn;
	
	public ForeignKey(Table table, Column column, Table keyTable, Column keyColumn) throws Exception {
		this.column = column;
		this.keyTable = keyTable;
		this.keyColumn = keyColumn;
		this.table = table;
	}
	
	public Table getTable() {
		return this.table;
	}
	
	public void setTable(Table table) {
		this.table = table;
	}
	
	public Column getColumn() {
		return column;
	}
	public void setColumn(Column column) {
		this.column = column;
	}

	public Table getKeyTable() {
		return keyTable;
	}

	public void setKeyTable(Table keyTable) {
		this.keyTable = keyTable;
	}

	public Column getKeyColumn() {
		return keyColumn;
	}

	public void setKeyColumn(Column keyColumn) {
		this.keyColumn = keyColumn;
	}
}
