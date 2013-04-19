package RelationalDB;

public class ForeignKey {
	private Column<?> column;
	private String keyTable;
	private String keyColumn;
	
	public ForeignKey(Column<?> column, String keyTable, String keyColumnName) throws Exception {
		this.column = column;
		this.keyTable = keyTable;
		this.keyColumn = keyColumnName;
	}
	
	public Column<?> getColumn() {
		return column;
	}
	public void setColumn(Column<?> column) {
		this.column = column;
	}

	public String getKeyTable() {
		return keyTable;
	}

	public void setKeyTable(String keyTable) {
		this.keyTable = keyTable;
	}

	public String getKeyColumn() {
		return keyColumn;
	}

	public void setKeyColumn(String keyColumn) {
		this.keyColumn = keyColumn;
	}
}
