package asttests;

import static org.junit.Assert.*;

import org.junit.Test;

import ast.*;
import ast.BinaryExpr.MathOp;
import ast.UnaryExpr.ExprType;

public class TestNodeAt
{

	@Test
	public void testBasicUnaryExpr1()
	{
		UnaryExpr e1 = new UnaryExpr(2);
		UnaryExpr e3 = new UnaryExpr(e1, ExprType.MEMORYVAL);
		String test = "mem[" + e3.nodeAt(1) + "]";
		assertTrue(test.equals(e3.toString()));
		assertTrue(e3.nodeAt(0).toString().equals(e3.toString()));
	}
	
	@Test
	public void testBasicUnaryExpr2()
	{
		UnaryExpr e1 = new UnaryExpr(2);
		UnaryExpr e3 = new UnaryExpr(e1, ExprType.NEGATION);
		String test = "-" + e3.nodeAt(1);
		assertTrue(test.equals(e3.toString()));
		assertTrue(e3.nodeAt(0).toString().equals(e3.toString()));
	}
	
	@Test
	public void testBasicBinaryExpr()
	{
		UnaryExpr e1 = new UnaryExpr(2);
		UnaryExpr e3 = new UnaryExpr(e1, ExprType.MEMORYVAL);
		BinaryExpr be = new BinaryExpr(e3, MathOp.MOD, new UnaryExpr(46));
		String test1 = be.nodeAt(1).toString() + " mod " + be.nodeAt(3).toString();
		assertTrue(test1.equals(be.toString()));
		assertTrue(be.nodeAt(0).toString().equals(be.toString()));
	}
}
