package asttests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import ast.BinaryExpr;
import ast.Command;
import ast.BinaryExpr.*;
import ast.MutationSwap;
import ast.UnaryExpr;
import ast.Update;

public class TestMutateSwap {

	@Test
	public void testMutateBinaryExpression() {
		UnaryExpr e = new UnaryExpr(5);
		BinaryExpr be = new BinaryExpr(e, MathOp.DIVIDE, new UnaryExpr(7));
		be.acceptMutation(new MutationSwap(true));
		assertTrue(be.getLeft().toString().equals("7"));
		assertTrue(be.getRight().toString().equals("5"));
	}
	
	@Test
	public void testMutateCommand() {
		Update u = new Update(new UnaryExpr(1), new UnaryExpr(1));
		Update u2 = new Update(new UnaryExpr(2), new UnaryExpr(2));
		Update u3 = new Update(new UnaryExpr(3), new UnaryExpr(3));
		Update u4 = new Update(new UnaryExpr(4), new UnaryExpr(4));
		Update u5 = new Update(new UnaryExpr(5), new UnaryExpr(5));
		Update u6 = new Update(new UnaryExpr(6), new UnaryExpr(6));
		LinkedList<Update> ll = new LinkedList<Update>();
		ll.add(u); ll.add(u2); ll.add(u3); ll.add(u4); ll.add(u5);
		Command c = new Command(ll, u6);
		System.out.println(c.toString() + "\n");
		c.acceptMutation(new MutationSwap(true));
		System.out.println(c.toString() + "\n");
	}
}
