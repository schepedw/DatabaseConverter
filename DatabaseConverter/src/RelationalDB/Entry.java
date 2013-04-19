package RelationalDB;

/**
 * TODO Put here a description of what this class does.
 *
 * @author schepedw.
 *         Created Apr 17, 2013.
 */
public class Entry<T> {
	private T data;
	private Boolean nullable;
	private Entry(T data){
		this.data =data;
	}
	protected T getData(){
		return this.data;
	}
	
	
}
