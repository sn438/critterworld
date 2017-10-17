package asttests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Before;
import org.junit.Test;

import ast.Condition;
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
	public void testMutateCondition() {
		System.out.println("testMutateCondition");
		Condition a = (Condition) prog.nodeAt(62);
		System.out.println("Program before being mutated: " + prog + "\n");
		System.out.println("Node that is being mutated: " + a + "\n");
		a.acceptMutation(new MutationInsert(true));
		System.out.println("Program after being mutated: " + prog + "\n");
		
	}
	
}
