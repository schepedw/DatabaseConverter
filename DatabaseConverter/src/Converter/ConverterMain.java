package Converter;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import MongoDB.Collection;
import MongoDB.SchemaConverterFlat;
import RelationalDB.Database;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author schepedw. Created May 2, 2013.
 */
public class ConverterMain {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("What is the name of the targetDB?");
		String targetDB =input.next();
		System.out
				.println("What is your username? Enter 'none' if there is none");
		String user = input.next();
		String password="";
		if (!user.equals("none")) {
			System.out.println("What is your password?");
			password = input.next();
		}
		DataConverter puller = new DataConverter(targetDB, user, password);
		puller.pullSchema();
		SchemaParser dp = new SchemaParser(new File(targetDB+"_schemaOutput.txt"));
		//SchemaParser dp = new SchemaParser(new File("src/Testing/Examples/SampleNoDataOutput.txt"));
		Database db = null;
		try {
			db = dp.parse();
		} catch (Exception exception) {
			System.err.println("Error in db.parse "+exception.getMessage());
			exception.printStackTrace();
		}
		SchemaConverterFlat sc = new SchemaConverterFlat(db);
		ArrayList<Collection> collections = sc.getCollections();
		puller.convertData(collections);

	}

}
