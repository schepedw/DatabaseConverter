package RelationalDB;
import java.util.ArrayList;
import java.util.List;


/**
 * TODO Put here a description of what this class does.
 *
 * @author schepedw.
 *         Created Apr 17, 2013.
 */
public class Database {

	
	private String name;
	private ArrayList<Table> tables;
	public Database(String name){
		this.name=name;
		this.tables= new ArrayList<Table>();
	}
	
	
	protected void addTable(Table t){
		this.tables.add(t);
	}
	
	public Table getTableByTableName(String name) throws Exception{
		for (Table t: this.tables){
			if (t.getName().equals(name)){
				return t;
			}
		}
		throw new Exception("Column "+ name+" does not exist in table "+ getName());
	}
	
	protected Boolean removeTable(Table t){
		return this.tables.remove(t);
	}
	
	protected int getNumTables(){
		return this.tables.size();
	}
	
	protected String getName(){
		return this.name;
	}
	
}
