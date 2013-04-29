package RelationalDB;

public class ForeignKey {
	private Column column;
	private Table keyTable;
	private Column keyColumn;
	
	public ForeignKey(Column column, Table keyTable, Column keyColumn) throws Exception {
		this.column = column;
		this.keyTable = keyTable;
		this.keyColumn = keyColumn;
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
