package mutationtests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import ast.Action;
import ast.BinaryCondition;
import ast.BinaryExpr;
import ast.Command;
import ast.Condition;
import ast.MutationRemove;
import ast.MutationTransform;
import ast.Program;
import ast.Relation;
import ast.Sensor;
import ast.UnaryExpr;
import ast.Update;
import parse.Parser;
import parse.ParserFactory;
import parsertests.ParserTest;

/**
 * 
 * This test class essentially tests the Transform Mutation by applying the Transform Mutation to random nodes 
 * in the program. If the mutation cannot be handled then an error statement is printed and the test ends gracefully.
 *
 */
public class TestMutateTransform {

	Program prog;

	@Before
	public void setup() {
		InputStream in = ParserTest.class.getResourceAsStream("example-rules.txt");
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
				System.out.println(prog.mutate(n, new MutationTransform(true)).toString());
			} catch (NullPointerException e) {
				 System.out.println("Incompatible node type");
			}
	}
}
