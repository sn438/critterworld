package asttests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Before;
import org.junit.Test;

import ast.MutationInsert;
import ast.Node;
import ast.Program;
import parse.Parser;
import parse.ParserFactory;
import parsertests.ParserTest;

public class TestMutateInsert {

	Program prog;
	@Before
	public void setup() {
		InputStream in = ParserTest.class.getResourceAsStream("example-rules.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser p = ParserFactory.getParser();
        prog = p.parse(r);
	}

	@Test
	public void simpleTest() {
		System.out.println(prog.size());
		int i = 0;
		
		while (i < prog.size()) {
			System.out.println(i + "\n");
			System.out.println(prog.nodeAt(i));
			i++;
		}
		
	}
	
}
