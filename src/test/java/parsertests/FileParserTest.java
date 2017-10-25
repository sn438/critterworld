package parsertests;

import static org.junit.Assert.*;
import java.io.*;
import org.junit.Test;
import console.FileParser;

public class FileParserTest
{

	@Test
	public void testParseAttributes1()
	{
		try
		{
			String[] test = FileParser.parseAttributes(new BufferedReader(new FileReader("examples/example-critter.txt")));
			//for(int i = 0; i < test.length; i++)
				//System.out.println("Element " + i + ": "+ test[i]);
			
			assertEquals(test.length, 7);
			assertTrue(test[0].equals("example"));
			assertTrue(test[1].equals("9"));
			assertTrue(test[2].equals("2"));
			assertTrue(test[3].equals("3"));
			assertTrue(test[4].equals("1"));
			assertTrue(test[5].equals("500"));
			assertTrue(test[6].equals("17"));
		}
		catch (FileNotFoundException e)
		{
			fail();
		}
	}
	
	@Test
	public void testParseAttributes2()
	{
		try
		{
			String[] test = FileParser.parseAttributes(new BufferedReader(new FileReader("examples/failure-example-critter1.txt")));
			//for(int i = 0; i < test.length; i++)
				//System.out.println("Element " + i + ": "+ test[i]);
			
			assertEquals(test.length, 7);
			assertTrue(test[0].equals(""));
			assertTrue(test[1].equals(""));
			assertTrue(test[2].equals(""));
			assertTrue(test[3].equals(""));
			assertTrue(test[4].equals(""));
			assertTrue(test[5].equals(""));
			assertTrue(test[6].equals(""));
		}
		catch (FileNotFoundException e)
		{
			fail();
		}
	}
}