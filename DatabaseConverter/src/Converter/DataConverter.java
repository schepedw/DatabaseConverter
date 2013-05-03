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

	public void convertData() {
		MongoDBConnection conn = new MongoDBConnection("localhost", 27017,
				this.targetDB);// Got this from my mongo connection, not sure if
							// everyone's is the same.
		SchemaParser dp = new SchemaParser(this.schemaOutputFile);
		Database db;
		try {
			db = dp.parse();
			SchemaConverter sc = new SchemaConverter(db);
			ArrayList<Collection> collections = sc.getCollectionsFromSchema();
			for (Collection c : collections) {
				ResultSet outerTable = pullCollectionDataFromSQL(c, "");
				while (outerTable.next()) {// step through rows one at a time
					DBCollection coll = conn.getCollection(c.getName());
					BasicDBObject doc = getInsertDocFromCollection(c,
							new BasicDBObject(), outerTable);
					coll.insert(doc);
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private BasicDBObject getInsertDocFromCollection(Collection c,
			BasicDBObject dbo, ResultSet parentTuple) throws SQLException {
		for (Field f : c.getFields()) {
			dbo.append(f.getName(), parentTuple.getString(f.getName()));
		}
		for (Collection s : c.getLowerCollections()) {
			String parentPrimaryKeyVal = parentTuple
					.getString(getPrimaryKeyName(c));
			ResultSet innerTable = pullCollectionDataFromSQL(s,
					parentPrimaryKeyVal);
			while (innerTable.next()) {
				dbo.append(s.getName(),
						getInsertDocFromCollection(s, dbo, innerTable));
			}
		}
		return dbo;
	}

	private ResultSet pullCollectionDataFromSQL(Collection c, String keyValue) {
		String sql;
		SQLDBConnection SQLconn;
		try {
			SQLconn = new SQLDBConnection(this.user, this.password,
					this.targetDB);

			if (c.getHigherCollection() == null) {
				sql = "Select * from `" + c.getName() + "`";
			} else {
				String myKey = getForeignKeyName(c);
				sql = "Select * from `" + c.getName() + "` where '" + myKey
						+ "' = '" + keyValue + "'";
			}
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
			String sql = "SELECT k.column_name"
					+ "FROM information_schema.table_constraints t"
					+ "JOIN information_schema.key_column_usage k"
					+ "USING ( constraint_name, table_schema, table_name )"
					+ "WHERE t.constraint_type =  'PRIMARY KEY'"
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

	private String getForeignKeyName(Collection c) {
		try {
			SQLDBConnection SQLconn = new SQLDBConnection(this.user,
					this.password, this.targetDB);
			String sql = "Select * from ( SELECT OBJECT_NAME(f.parent_object_id) AS TableName,"
					+ " OBJECT_NAME (f.referenced_object_id) AS ReferenceTableName,"
					+ " COL_NAME(fc.parent_object_id, fc.parent_column_id) AS ColumnName,"
					+ " COL_NAME(fc.referenced_object_id, fc.referenced_column_id) AS ReferenceColumnName "
					+ "FROM sys.foreign_keys AS f INNER JOIN sys.foreign_key_columns AS fc"
					+ " ON (f.OBJECT_ID = fc.constraint_object_id )) "
					+ "as t1 where t1.ReferenceTableName='"
					+ c.getName()
					+ "' and t1.TableName='" + c.getHigherCollection() + "'";
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