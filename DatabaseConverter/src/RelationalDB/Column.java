package RelationalDB;
import java.util.ArrayList;


/**
 * TODO Put here a description of what this class does.
 *
 * @author schepedw.
 *         Created Apr 17, 2013.
 */
public class Column<T> {

	
	private String name;
	private ArrayList<Entry<T>> entries;
	public Column (String name) {
		this.entries=new ArrayList<Entry<T>>();
		this.name=name;
	}
	
	protected void addEntry(Entry e){
		this.entries.add(e);
	}
	
	protected Entry getEntry(Entry e){
		int i =this.entries.indexOf(e);
		return this.entries.get(i);
	}
	
	protected Boolean removeEntry(Entry e){
		return this.entries.remove(e);
	}
	
	public String getName(){
		return this.name;
	}
	
	protected int getNumEntries(){
		return this.entries.size();
	}
}
