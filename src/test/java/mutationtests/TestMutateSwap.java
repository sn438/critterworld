package mutationtests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Before;
import org.junit.Test;

import parse.Parser;
import parse.ParserFactory;
import parsertests.ASTParserTest;
import ast.MutationSwap;
import ast.Program;

/**
 * 
 * This test class essentially tests the Swap Mutation by applying the Swap Mutation to random nodes 
 * in the program. If the mutation cannot be handled then an error statement is printed and the test ends gracefully.
 *
 */
public class TestMutateSwap {

	Program prog;
	@Before
	public void setup() {
		InputStream in = ASTParserTest.class.getResourceAsStream("example-rules.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser p = ParserFactory.getParser();
        prog = p.parse(r);
	}
	@Test
	public void testMutate() {
		int n = 0;
		for (int i = 0; i < prog.size(); i++)
			n = (int) (Math.random() * (prog.size()));
			try {
				System.out.println(prog.mutate(n, new MutationSwap(true)).toString());
			} catch (NullPointerException e) {
				 System.out.println("Incompatible node type");
			}
	}
}
