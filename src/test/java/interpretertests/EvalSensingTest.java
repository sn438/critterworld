package interpretertests;

import static org.junit.Assert.*;

import org.junit.Before;

import ast.*;
import ast.BinaryCondition.Operator;
import ast.BinaryExpr.MathOp;
import ast.Relation.RelOp;
import ast.UnaryExpr.ExprType;
import console.Console;
import interpret.Interpreter;
import interpret.InterpreterImpl;
import simulation.Critter;
import simulation.World;

import org.junit.Test;

public class EvalSensingTest
{
	int[] arr = {3, 5};
	Interpreter i;
	
	@Before
	public void setUp()
	{
		i = new InterpreterImpl(new Critter(null, arr, "TESTCRITTER"), new World());
	}
	
	@Test
	public void testEvalBinaryCondition()
	{
		UnaryExpr e1 = new UnaryExpr(2);
		UnaryExpr e2 = new UnaryExpr(12);
		BinaryExpr e3 = new BinaryExpr(e1, MathOp.MULTIPLY, e2); //should be 24
		UnaryExpr e4 = new UnaryExpr(e3, ExprType.NEGATION); //should be -24
		BinaryExpr e5 = new BinaryExpr(new UnaryExpr(6), MathOp.ADD, new UnaryExpr(67)); //should be be 73
		BinaryExpr e6 = new BinaryExpr(e5, MathOp.DIVIDE, e4); //should be -3
		
		Relation r1 = new Relation(e5, RelOp.GREATER, e6); //should be true
		Relation r2 = new Relation(e5, RelOp.NOTEQUAL, e6); //should be true
		Relation r3 = new Relation(e1, RelOp.EQUAL, e2); //should be false
		Relation r4 = new Relation(e1, RelOp.LESSOREQ, e2); //should be true
		Relation r5 = new Relation(e1, RelOp.GREATEROREQ, e2); //should be false
		
		assertTrue(new BinaryCondition(r1, Operator.AND, r2).acceptEvaluation(i));
		assertTrue(new BinaryCondition(r1, Operator.OR, r2).acceptEvaluation(i));
		assertTrue(new BinaryCondition(r1, Operator.OR, r3).acceptEvaluation(i));
		assertFalse(new BinaryCondition(r1, Operator.AND, r3).acceptEvaluation(i));
		assertFalse(new BinaryCondition(r3, Operator.AND, r4).acceptEvaluation(i));
		assertFalse(new BinaryCondition(r5, Operator.OR, r3).acceptEvaluation(i));
	}

	@Test
	public void testEvalRelation()
	{
		int[] arr = {3, 5};
		Interpreter i = new InterpreterImpl(new Critter(null, arr, "TESTCRITTER"), new World());
		UnaryExpr e1 = new UnaryExpr(2);
		UnaryExpr e2 = new UnaryExpr(12);
		BinaryExpr e3 = new BinaryExpr(e1, MathOp.MULTIPLY, e2); //should be 24
		UnaryExpr e4 = new UnaryExpr(e3, ExprType.NEGATION); //should be -24
		BinaryExpr e5 = new BinaryExpr(new UnaryExpr(6), MathOp.ADD, new UnaryExpr(67)); //should be be 73
		BinaryExpr e6 = new BinaryExpr(e5, MathOp.DIVIDE, e4); //should be -3
		
		Relation r1 = new Relation(e5, RelOp.GREATER, e6); //should be true
		Relation r2 = new Relation(e5, RelOp.NOTEQUAL, e6); //should be true
		Relation r3 = new Relation(e1, RelOp.EQUAL, e2); //should be false
		Relation r4 = new Relation(e1, RelOp.LESSOREQ, e2); //should be true
		Relation r5 = new Relation(e1, RelOp.GREATEROREQ, e2); //should be false
		assertTrue(r1.acceptEvaluation(i));
		assertTrue(r2.acceptEvaluation(i));
		assertFalse(r3.acceptEvaluation(i));
		assertTrue(r4.acceptEvaluation(i));
		assertFalse(r5.acceptEvaluation(i));
	}

	@Test
	public void testEvalBinaryExpr()
	{
		int[] arr = {3, 5};
		Interpreter i = new InterpreterImpl(new Critter(null, arr, "TESTCRITTER"), new World());
		UnaryExpr e1 = new UnaryExpr(2);
		UnaryExpr e2 = new UnaryExpr(12);
		BinaryExpr e3 = new BinaryExpr(e1, MathOp.MULTIPLY, e2); //should be 24
		UnaryExpr e4 = new UnaryExpr(e3, ExprType.NEGATION); //should be -24
		BinaryExpr e5 = new BinaryExpr(new UnaryExpr(6), MathOp.ADD, new UnaryExpr(67)); //should be be 73
		
		BinaryExpr e6 = new BinaryExpr(e5, MathOp.DIVIDE, e4); //should be -3
		assertEquals(e3.acceptEvaluation(i), 24);
		assertEquals(e4.acceptEvaluation(i), -24);
		assertEquals(e5.acceptEvaluation(i), 73);
		assertEquals(e6.acceptEvaluation(i), -3);
	}

	@Test
	public void testEvalUnaryExpr()
	{
		int[] arr = {3, 5};
		Interpreter i = new InterpreterImpl(new Critter(null, arr, "TESTCRITTER"), new World());
		
		UnaryExpr e1 = new UnaryExpr(123123);
		UnaryExpr e2 = new UnaryExpr(253);
		
		assertEquals(123123, e1.acceptEvaluation(i));
		assertEquals(253, e2.acceptEvaluation(i));
	}
	
	@Test
	/** If the critter in this world senses the world boundary, it will backup. */
	public void testSenseWorldEdge()
	{
		Console c = new Console();
		c.loadWorld("SensingWorld.txt");
		c.worldInfo();
		c.advanceTime(1);
		c.worldInfo();
	}
}