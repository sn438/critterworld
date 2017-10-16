package ast;

import java.util.LinkedList;
import ast.BinaryExpr.MathOp;

public class TestMutations
{
	public static void main(String[] args)
	{
		/*
		UnaryExpr e = new UnaryExpr(5);
		
		BinaryExpr be = new BinaryExpr(e, MathOp.DIVIDE, new UnaryExpr(7));
		System.out.println(be.toString());
		be.acceptMutation(new MutationSwap());
		System.out.println(be.toString());
		System.out.println(be.toString());
		be.acceptMutation(new MutationSwap());
		System.out.println(be.toString());
		
		Update u = new Update(new UnaryExpr(2), new UnaryExpr(9));
		System.out.println(u.toString());
		u.acceptMutation(new MutationSwap());
		System.out.println(u.toString());
		
		System.out.println(u.getClass().getSimpleName());
		System.out.println(be.getClass().getSimpleName());*/
		
		
		
		//System.out.println(e.getParent().toString());
		
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
		c.acceptMutation(new MutationSwap());
		System.out.println(c.toString() + "\n");
		
		u6.acceptMutation(new MutationRemove());
		System.out.println(c.toString());
		
	}
}