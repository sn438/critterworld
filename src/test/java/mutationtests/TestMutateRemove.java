package mutationtests;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import ast.BinaryCondition;
import ast.BinaryExpr;
import ast.Command;
import ast.Condition;
import ast.MutationRemove;
import ast.Program;
import ast.Relation;
import ast.Rule;
import ast.UnaryExpr;
import ast.Update;
import ast.BinaryCondition.Operator;
import ast.BinaryExpr.MathOp;
import ast.Relation.RelOp;
import ast.UnaryExpr.ExprType;
import parse.Parser;
import parse.ParserFactory;
import parsertests.ParserTest;

/**
 * This test class essentially tests the Remove Mutation by applying the mutation to random nodes in the
 * program. If the mutation cannot be handled then an error statement is printed and the test ends gracefully.
 */
public class TestMutateRemove
{
	Program prog;

	@Before
	public void setup()
	{
		InputStream in = ParserTest.class.getResourceAsStream("example-rules.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser p = ParserFactory.getParser();
		prog = p.parse(r);
	}
	
	@Test
	public void testRandom()
	{
		int n = 0;
		for (int i = 0; i < prog.size(); i++)
		{
			n = (int) (Math.random() * (prog.size()));
			try 
			{
				System.out.println(prog.mutate(n, new MutationRemove(true)).toString());
			}
			catch (NullPointerException e)
			{
				 System.out.println("Incompatible node type");
			}
		}
	}
	
	@Test
	public void testUpdate1()
	{
		Update u = new Update(new UnaryExpr(1), new UnaryExpr(1));
		Update u2 = new Update(new UnaryExpr(2), new UnaryExpr(2));
		Update u3 = new Update(new UnaryExpr(3), new UnaryExpr(3));
		Update u4 = new Update(new UnaryExpr(4), new UnaryExpr(4));
		Update u5 = new Update(new UnaryExpr(5), new UnaryExpr(5));
		Update u6 = new Update(new UnaryExpr(6), new UnaryExpr(6));
		LinkedList<Update> ll = new LinkedList<Update>();
		ll.add(u); ll.add(u2); ll.add(u3); ll.add(u4); ll.add(u5);
		Command c = new Command(ll, u6);
		
		int previousSize = c.size();
		u2.acceptMutation(new MutationRemove(true));
		assertTrue(previousSize > c.size());
		assertTrue(c.toString().equals(u.toString() + "\n" + u3.toString() + "\n" + u4.toString() + "\n" + u5.toString() + "\n" + u6.toString()));
	}
	
	@Test
	public void testUpdate2()
	{
		Update u = new Update(new UnaryExpr(1), new UnaryExpr(1));
		Update u2 = new Update(new UnaryExpr(6), new UnaryExpr(6));
		LinkedList<Update> ll = new LinkedList<Update>();
		ll.add(u);
		Command c = new Command(ll, u2);
		
		int previousSize = c.size();
		u2.acceptMutation(new MutationRemove(true));
		assertTrue(previousSize > c.size());
		assertTrue(c.toString().equals(u.toString()));
	}
	
	@Test
	public void testUpdate3()
	{
		Update u = new Update(new UnaryExpr(1), new UnaryExpr(1));
		Update u2 = new Update(new UnaryExpr(6), new UnaryExpr(6));
		LinkedList<Update> ll = new LinkedList<Update>();
		ll.add(u);
		Command c = new Command(ll, u2);
		
		int previousSize = c.size();
		u.acceptMutation(new MutationRemove(true));
		assertTrue(previousSize > c.size());
		assertTrue(c.toString().equals(u2.toString()));
	}
	@Test
	public void testChildlessUnaryExpr()
	{
		UnaryExpr ue = new UnaryExpr(3);
		BinaryExpr be = new BinaryExpr(ue, MathOp.ADD, new UnaryExpr(2));
		String before = be.toString();
		ue.acceptMutation(new MutationRemove(true));
		assertTrue(before.equals(be.toString()));
	}
	
	@Test
	public void testUnaryExpr()
	{
		UnaryExpr ue = new UnaryExpr(new UnaryExpr(3), ExprType.MEMORYVAL);
		BinaryExpr be = new BinaryExpr(ue, MathOp.ADD, new UnaryExpr(2));
		
		int previousSize = be.size();
		String before = be.toString();
		ue.acceptMutation(new MutationRemove(true));
		assertTrue(previousSize > be.size());
		assertTrue("3 + 2".equals(be.toString()));
	}
	
	@Test
	public void testCondition()
	{
		UnaryExpr e = new UnaryExpr(new UnaryExpr(7), ExprType.MEMORYVAL);
		BinaryExpr e5 = new BinaryExpr(new UnaryExpr(6), MathOp.ADD, new UnaryExpr(67));
		UnaryExpr e1 = new UnaryExpr(2);
		UnaryExpr e2 = new UnaryExpr(12);
		BinaryExpr e3 = new BinaryExpr(e1, MathOp.MULTIPLY, e2);
		Relation r1 = new Relation(e, RelOp.NOTEQUAL, new UnaryExpr(17));
		Relation r2 = new Relation(e3, RelOp.GREATER, e5);
		Condition con = new BinaryCondition(r1, Operator.AND, r2);
		Update u = new Update(new UnaryExpr(7), new UnaryExpr(17));
		LinkedList<Update> ll = new LinkedList<Update>();
		Update u2 = new Update(new UnaryExpr(7), new UnaryExpr(17));
		Update u3 = new Update(new UnaryExpr(7), new UnaryExpr(17));
		Update u4 = new Update(new UnaryExpr(7), new UnaryExpr(17));
		ll.add(u2);
		ll.add(u3);
		ll.add(u4);
		Command c = new Command(ll, u);
		Rule rule = new Rule(con, c);
		
		con.acceptMutation(new MutationRemove(true));
		Rule predictedMutatedRule = new Rule(r1, c);
		assertTrue(predictedMutatedRule.toString().equals(rule.toString()));
	}
}
