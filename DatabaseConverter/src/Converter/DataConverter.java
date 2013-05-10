package Converter;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import MongoDB.Collection;
import MongoDB.Field;
import MongoDB.SchemaConverter;
import RelationalDB.Database;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class DataConverter {

	private String targetDB;
	private File schemaOutputFile;

	private String user;
	private String password;

	public DataConverter(String targetDB, String user,
			String password) {
		this.schemaOutputFile = new File(targetDB+"_schemaOutput.txt");
		this.targetDB = targetDB;
		this.user = user;
		this.password = password;
	}

	protected void pullSchema() {
		try {
			String pullCommand = "mysqldump -p --no-data --skip-add-drop-table "
					+ this.targetDB + ">" + this.schemaOutputFile.toString();
			Runtime.getRuntime().exec(pullCommand);
		} catch (IOException e) {
			System.out.println("exception: ");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void convertData(ArrayList<Collection> collections) {
		MongoDBConnection conn = new MongoDBConnection("localhost", 27017,
				this.targetDB);// Got this from my mongo connection, not sure if
							// everyone's is the same.
		try {
			for (Collection c : collections) {
				DBCollection coll = conn.getCollection(c.getName());//create a new collection
				ResultSet table = pullCollectionDataFromSQL(c);//get the corresponding table
				while (table.next()) {
					BasicDBObject doc = getInsertDocFromCollection(c,
							new BasicDBObject(), table);//get each row out
					coll.insert(doc);					
				}
				table.close();
			}
			System.out.println("Database converted!");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private BasicDBObject getInsertDocFromCollection(Collection c,
			BasicDBObject dbo, ResultSet parentTuple) throws SQLException {
		for (Field f : c.getFields()) {
			if (f.getName().equals(getPrimaryKeyName(c)))
				dbo.append("_id", parentTuple.getString(f.getName()));
			else 
			dbo.append(f.getName(), parentTuple.getString(f.getName()));
			
		}
		return dbo;
	}

	private ResultSet pullCollectionDataFromSQL(Collection c) {
		try {
			SQLDBConnection SQLconn = new SQLDBConnection(this.user, this.password,
					this.targetDB);

			String sql = "Select * from `" + c.getName() + "`";
			
			return SQLconn.executeQuery(sql);
		} catch (Exception exception) {
			System.err.println("error in pullCollectionData: "
					+ exception.getMessage());
			exception.printStackTrace();
			return null;
		}
	}

	// These sql statements may need converted to mySQL
	private String getPrimaryKeyName(Collection c) {
		try {
			SQLDBConnection SQLconn = new SQLDBConnection(this.user,
					this.password, this.targetDB);
			String sql = "SELECT k.column_name "
					+ "FROM information_schema.table_constraints t "
					+ "JOIN information_schema.key_column_usage k "
					+ "USING ( constraint_name, table_schema, table_name ) "
					+ "WHERE t.constraint_type =  'PRIMARY KEY' "
					+ "AND t.table_name =  '" + c.getName() + "'";
			ResultSet r = SQLconn.executeQuery(sql);
			String parentKey = convertResultSetToArray(r).get(0);
			r.close();
			return parentKey;
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}

	}

	private ArrayList<String> convertResultSetToArray(ResultSet r)
			throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		while (r.next()) {
			result.add(r.getString(1));
		}
		r.close();
		return result;
	}

}