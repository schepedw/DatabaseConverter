package Converter;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import MongoDB.Collection;
import MongoDB.SchemaConverter;
import RelationalDB.Database;

import java.util.Arrays;

public class DataPuller {

	private String targetDB;
	private File schemaOutputFile;
	private SQLDBConnection conn;
	private Statement stmt;

	public DataPuller(String targetDB, String schemaOutputFile, String user,
			String password) {
		this.schemaOutputFile = new File(schemaOutputFile);
		this.targetDB = targetDB;
		try {
			this.conn = new SQLDBConnection(user, password, targetDB);
		} catch (Exception exception) {
			// TODO Auto-generated catch-block stub.
			exception.printStackTrace();
		}
	}

	// When we actually convert the data, we may want to pull the schema and
	// data at the same time
	protected void pullSchema() {
		String result = "";
		try {
			String s;
			String pullCommand = "mysqldump -p --no-data --skip-add-drop-table "
					+ this.targetDB + ">" + this.schemaOutputFile.toString();
			Process p = Runtime.getRuntime().exec(pullCommand);
		} catch (IOException e) {
			System.out.println("exception: ");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void convertData() {
		SchemaParser dp = new SchemaParser(this.schemaOutputFile);
		Database db;
		try {
			db = dp.parse();
			SchemaConverter sc = new SchemaConverter(db);
			ArrayList<Collection> collections = sc.getCollectionsFromSchema();
			for (Collection c : collections) {
				pullCollectionDataFromSQL(c,"");//the blank is ok in the upper levels: there's no FKs to match
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}


	private ResultSet pullCollectionDataFromSQL(Collection c, String keyValue) {
		String sql;
		if (c.getHigherCollection()==null){
			sql="Select * from `" + c.getName() + "`";
		}
		else {
			String myKey=getForeignKeyName(c);
			sql = "Select * from `" + c.getName() + "` where '"
				+ myKey + "' = '" + keyValue + "'";//will this work for ints and strings?
		}
		return this.conn.executeQuery(sql);
	}

	// These sql statements may need converted to mySQL
	private String getPrimaryKeyName(Collection c) {
		try {
			String sql = "SELECT k.column_name"
					+ "FROM information_schema.table_constraints t"
					+ "JOIN information_schema.key_column_usage k"
					+ "USING ( constraint_name, table_schema, table_name )"
					+ "WHERE t.constraint_type =  'PRIMARY KEY'"
					+ "AND t.table_name =  '" + c.getName() + "'";
			ResultSet r = this.conn.executeQuery(sql);
			String parentKey = getValFromResultSet(r).get(0);
			r.close();
			return parentKey;
		} catch (Exception exception) {
			exception.printStackTrace();
			return null;
		}

	}

	private String getForeignKeyName(Collection c) {
		try {
			String sql = "Select * from ( SELECT OBJECT_NAME(f.parent_object_id) AS TableName,"
					+ " OBJECT_NAME (f.referenced_object_id) AS ReferenceTableName,"
					+ " COL_NAME(fc.parent_object_id, fc.parent_column_id) AS ColumnName,"
					+ " COL_NAME(fc.referenced_object_id, fc.referenced_column_id) AS ReferenceColumnName "
					+ "FROM sys.foreign_keys AS f INNER JOIN sys.foreign_key_columns AS fc"
					+ " ON (f.OBJECT_ID = fc.constraint_object_id )) "
					+ "as t1 where t1.ReferenceTableName='"
					+ c.getName()
					+ "' and t1.TableName='" + c.getHigherCollection() + "'";
			ResultSet r = this.conn.executeQuery(sql);
			String parentKey = getValFromResultSet(r).get(0);
			r.close();
			return parentKey;
		} catch (Exception exception) {

			exception.printStackTrace();
			return null;
		}

	}

	private ArrayList<String> getValFromResultSet(ResultSet r)
			throws SQLException {
		ArrayList<String> result = new ArrayList<String>();
		while (r.next()) {
			result.add(r.getString(1));
		}
		r.close();
		return result;
	}
	
	

}