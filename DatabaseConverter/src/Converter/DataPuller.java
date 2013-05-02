package Converter;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import MongoDB.Collection;
import MongoDB.SchemaConverter;
import RelationalDB.Database;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;

import java.util.Arrays;


public class DataPuller {

	private String targetDB;
	private File schemaOutputFile;
	private SQLDBConnection conn;
	private Statement stmt;
	

	public DataPuller(String targetDB, String schemaOutputFile, String user, String password) {
		this.schemaOutputFile = new File(schemaOutputFile);
		this.targetDB = targetDB;
		try {
			this.conn=new SQLDBConnection(user, password, targetDB);
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
					+ this.targetDB+">"+this.schemaOutputFile.toString();
			Process p = Runtime.getRuntime().exec(pullCommand);
		}
		catch (IOException e) {
			System.out.println("exception: ");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public void pullData() {
		SchemaParser dp = new SchemaParser(this.schemaOutputFile);
		Database db;
		try {
			db = dp.parse();
			SchemaConverter sc = new SchemaConverter(db);
			ArrayList<Collection> collections = sc.getCollectionsFromSchema();
			for (Collection c : collections) {
				pullCollection(c);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}

	}

	private String getKeyForCollection(Collection c) {
		String sql="SELECT k.column_name"+
		"FROM information_schema.table_constraints t"+
		"JOIN information_schema.key_column_usage k"+
		"USING ( constraint_name, table_schema, table_name )"+ 
		"WHERE t.constraint_type =  'PRIMARY KEY'"+
		"AND t.table_name =  '"+c.getName()+"'";
		this.conn.executeQuery(sql);
		
		return null;
	}