package Testing.Examples;


import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Test;

import Converter.DataParser;
import RelationalDB.Database;

/**
 * TODO Put here a description of what this class does.
 *
 * @author schepedw.
 *         Created Apr 17, 2013.
 */
public class ParserTest {

	@Test
	public void testGetName() throws FileNotFoundException {
		File ex=new File("src/Testing/Examples/SampleNoDataOutput.txt");
		//String name= DataParser.getName(ex);
		Database db= DataParser.parse(ex);
		//assertEquals("vcf_analyzer",name);
	}

}
