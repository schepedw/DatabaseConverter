package Converter;

import java.util.Scanner;

/**
 * TODO Put here a description of what this class does.
 *
 * @author schepedw.
 *         Created May 2, 2013.
 */
public class Main {

	
	public static void main(String[] args) {
		Scanner input=new Scanner(System.in);
		System.out.println("What is the name of the targetDB?");
		String targetDB= input.next();
		System.out.println("What is your username?");
		String user=input.next();
		System.out.println("What is your password?");
		String password=input.next();
		DataConverter puller=new DataConverter(targetDB, user, password);
		puller.pullSchema();
		puller.convertData();

	}

}
