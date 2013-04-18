package Testing.Examples;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import Converter.DataParser;

/**
 * TODO Put here a description of what this class does.
 *
 * @author schepedw.
 *         Created Apr 17, 2013.
 */
public class ParserTest {

	@Test
	public void testGetName() {
		System.out.println("Name= "+DataParser.getName(new File("src/Testing/Examples/SampleNoDataOutput.txt")));
		fail("nothing to test yet");
	}

}
