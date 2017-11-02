package mutationtests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Before;
import org.junit.Test;

import ast.MutationReplace;
import ast.Program;
import parse.Parser;
import parse.ParserFactory;
import parsertests.ASTParserTest;

/**
 * 
 * This test class essentially tests the Replace Mutation by applying the Replace Mutation to random nodes 
 * in the program. If the mutation cannot be handled then an error statement is printed and the test ends gracefully.
 *
 */
public class TestMutateReplace {

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
				System.out.println(prog.mutate(n, new MutationReplace(true)).toString());
			} catch (NullPointerException e) {
				 System.out.println("Incompatible node type");
			}
	}
}
