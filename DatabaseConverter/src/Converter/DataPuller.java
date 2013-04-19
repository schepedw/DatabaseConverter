package Converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * TODO Put here a description of what this class does.
 * 
 * @author schepedw. Created Apr 17, 2013.
 */
public class DataPuller {

	private String targetDB;

	public DataPuller(String targetDB) {
		this.targetDB = targetDB;
	}

	// When we actually convert the data, we may want to pull the schema and
	// data at the same time
	protected String pullSchema() {
		String result = "";
		try {
			String s;
			String pullCommand = "mysqldump -p --no-data --skip-add-drop-table "
					+ this.targetDB;
			Process p = Runtime.getRuntime().exec(pullCommand);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			while ((s = stdInput.readLine()) != null) {
				System.out.println(s);
				result += s;
			}

			System.out
					.println("Here is the standard error of the command (if any):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
		} catch (IOException e) {
			System.out.println("exception: ");
			e.printStackTrace();
			System.exit(-1);
		}
		return result;
	}
}
