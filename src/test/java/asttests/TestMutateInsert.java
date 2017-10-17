package asttests;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Before;
import org.junit.Test;

import ast.BinaryExpr;
import ast.Condition;
import ast.MutationInsert;
import ast.Node;
import ast.Program;
import ast.Relation;
import ast.Sensor;
import ast.UnaryExpr;
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
        int i = 0;
        /*
        while (i < prog.size()) {
        	System.out.println(i + "\n");
        	System.out.println(prog.nodeAt(i).getType());
        	i++;
        }
        */
	}
	/**
	 * Each of the testMutateX methods tests a different type of insert mutation for each of the valid types of 
	 * nodes. Each tests prints out information about the mutation so most of the tests are commented out. The 
	 * printing can be disabled by passing false to MutationInsert.
	 */
/*
	@Test
	public void testMutateCondition() {
		System.out.println("testMutateCondition");
		Condition a = (Condition) prog.nodeAt(62);
		a.acceptMutation(new MutationInsert(true));
	}

	@Test
	public void testMutateRelation() {
		System.out.println("testMutateRelation");
		Relation a = (Relation) prog.nodeAt(2);
		a.acceptMutation(new MutationInsert(true));
	}
	
	
	@Test
	public void testMutateBinaryExpr() {
		System.out.println("testMutateBinaryExpr");
		BinaryExpr a = (BinaryExpr) prog.nodeAt(39);
		a.acceptMutation(new MutationInsert(true));
	}
	
	
	@Test
	public void testMutateBinaryExpr() {
		System.out.println("testMutateUnaryExpr");
		UnaryExpr a = (UnaryExpr) prog.nodeAt(40);
		a.acceptMutation(new MutationInsert(true));
	}
	*/
	@Test
	public void testMutateBinaryExpr() {
		System.out.println("testMutateSensor");
		Sensor a = (Sensor) prog.nodeAt(82);
		a.acceptMutation(new MutationInsert(true));
	}
}
