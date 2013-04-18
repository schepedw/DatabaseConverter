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
	
	protected Table getTable(Table t){
		int i =this.tables.indexOf(t);
		return this.tables.get(i);
	}
	
	protected Boolean removeTable(Table t){
		return this.tables.remove(t);
	}
	
	protected int getNumTables(){
		return this.tables.size();
	}
	
}
