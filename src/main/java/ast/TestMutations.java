package ast;

import java.util.LinkedList;

import ast.BinaryCondition.Operator;
import ast.BinaryExpr.MathOp;
import ast.Relation.RelOp;
import ast.UnaryExpr.ExprType;

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
		UnaryExpr e = new UnaryExpr(new UnaryExpr(7), ExprType.MEMORYVAL);
		BinaryExpr e5 = new BinaryExpr(new UnaryExpr(6), MathOp.ADD, new UnaryExpr(67));
		UnaryExpr e1 = new UnaryExpr(2);
		UnaryExpr e2 = new UnaryExpr(12);
		BinaryExpr e3 = new BinaryExpr(e1, MathOp.MULTIPLY, e2);
		Relation r1 = new Relation(e, RelOp.NOTEQUAL, new UnaryExpr(17));
		Relation r2 = new Relation(e3, RelOp.GREATER, e5);
		Condition con = new BinaryCondition(r1, Operator.AND, r2);
		ll.add(u); ll.add(u2); ll.add(u3); ll.add(u4); ll.add(u5);
		Command c = new Command(ll, u6);
		Rule r = new Rule(con, c);
		System.out.println(c.toString() + "\n");
		c.acceptMutation(new MutationSwap());
		System.out.println(c.toString() + "\n");
		
		//u6.acceptMutation(new MutationRemove());
		//System.out.println(c.toString());
		
		c.acceptMutation(new MutationDuplicate());
		System.out.println(c.toString());
		
	}
}