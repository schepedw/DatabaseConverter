package Converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import RelationalDB.Database;

/**
 * TODO Put here a description of what this class does.
 *
 * @author schepedw.
 *         Created Apr 17, 2013.
 */
public class DataParser {

	public static Database parse(File schema){
		Database db= new Database(getName(schema));
		
		
		return null;
	}
	
	
	public static String getName(File schema) {
		String name="";
		try {
			String nameLine;
			Scanner input =new Scanner(schema);
			input.useDelimiter("*Database: ");
			while (!(nameLine=input.next()).contains("Database: ")){
				System.out.println(nameLine);
			}
			
			
		} catch (FileNotFoundException exception) {

			exception.printStackTrace();
		}
		
		
		return null;
	}
}
