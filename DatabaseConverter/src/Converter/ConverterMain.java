package Converter;

import java.util.Scanner;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author schepedw. Created May 2, 2013.
 */
public class ConverterMain {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("What is the name of the targetDB?");
		String targetDB = input.next();
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
		puller.convertData();

	}

}
