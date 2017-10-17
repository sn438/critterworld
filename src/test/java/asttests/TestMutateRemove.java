package asttests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Before;
import org.junit.Test;

import ast.BinaryCondition;
import ast.MutationRemove;
import ast.Program;
import ast.Rule;
import ast.Update;
import parse.Parser;
import parse.ParserFactory;
import parsertests.ParserTest;

public class TestMutateRemove {
	Program prog;
	@Before
	public void setup() {
		InputStream in = ParserTest.class.getResourceAsStream("example-rules.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser p = ParserFactory.getParser();
        prog = p.parse(r);
	}
	/*
	@Test
	public void testMutateRule() {
		System.out.println("testMutateCondition");
		Rule a = (Rule) prog.nodeAt(97);
		a.acceptMutation(new MutationRemove(true));
	}
		
	@Test
	public void testMutateBinaryCondition() {
		System.out.println("testBinaryCondition");
		BinaryCondition a = (BinaryCondition) prog.nodeAt(106);
		a.acceptMutation(new MutationRemove(true));
	}
	*/
	@Test
	public void testMutateUpdate() {
		System.out.println("testUpdate");
		Update a = (Update) prog.nodeAt(130);
		a.acceptMutation(new MutationRemove(true));
	}
}
