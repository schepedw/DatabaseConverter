package RelationalDB;

public class Entry<T> {
	private T data;
	private Boolean nullable;
	private Entry(T data){
		this.data = data;
	}
	protected T getData(){
		return this.data;
	}
	
	Boolean getNullable() {
		return this.nullable;
	}
	
	
}
