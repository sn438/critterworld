package parsertests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Test;

import ast.Program;
import parse.Parser;
import parse.ParserFactory;
import parse.Tokenizer;

/** This class contains tests for the Critter parser. */
public class ParserTest
{
    /** Checks that a valid critter program is not {@code null} when parsed. 
     * Different test cases were run using this same method by changing the filename for the input stream
     * This methodology was implemented because the program terminates elsewhere in the program, so the 
     * remainder of the tests cannot be performed. A key of the filename and the type of error it tests is included
     * in the overview.
     * */
	
    @Test
    public void testProgramIsNotNull()
    {
        InputStream in = ParserTest.class.getResourceAsStream("draw_critter.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser p = ParserFactory.getParser();
        Program prog = p.parse(r);
        System.out.println(prog.toString() + "\n");
        System.out.println(prog.toString());
        assertNotNull("A valid critter program should not be null.", prog);
    }

    /*@Test
    public void testEmptyProgram()
    {
        InputStream in = ParserTest.class.getResourceAsStream("failure-test-1.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser p = ParserFactory.getParser();
        Program prog = p.parse(r);
        System.out.println(prog.toString());
        assertNotNull("A valid critter program should not be null.", prog);
    }*/
    
}
