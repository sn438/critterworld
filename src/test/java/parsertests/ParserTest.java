package parsertests;

import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Test;

import ast.Program;
import parse.Parser;
import parse.ParserFactory;

/**
 * This class contains tests for the Critter parser.
 */
public class ParserTest {

    /** Checks that a valid critter program is not {@code null} when parsed. */
    @Test
    public void testProgramIsNotNull() {
        InputStream in = ParserTest.class.getResourceAsStream("draw_critter.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser p = ParserFactory.getParser();
        Program prog = p.parse(r);
        assertNotNull("A valid critter program should not be null.", prog);
    }

    // TODO continue adding tests

}
