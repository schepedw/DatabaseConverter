package Converter;

/**
 * TODO Put here a description of what this class does.
 *
 * @author schepedw.
 *         Created May 2, 2013.
 */



import java.util.Set;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoDBConnection {
	private DB db;
	
	public MongoDBConnection(String serverAddress, int port, String dbName){
		try {
			MongoClient mongoClient=new MongoClient(serverAddress,port);
			this.db= mongoClient.getDB(dbName);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		
	}
	
	public DB getDB(){
		return this.db;
	}
	
	public Set<String> getCollectionsNames(){
		return this.db.getCollectionNames();
	}
	
	public DBCollection getCollection(String collectionName){
		return this.db.getCollection(collectionName);

	}
	
	
}


