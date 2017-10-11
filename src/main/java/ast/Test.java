package ast;
import java.util.LinkedList;

import ast.BinaryExpr;
import ast.BinaryExpr.MathOp;
import ast.Relation.RelOp;
import ast.UnaryExpr.ExprType;

public class Test
{
	public static void main(String[] args)
	{
		/*UnaryExpr e1 = new UnaryExpr(2);
		UnaryExpr e2 = new UnaryExpr(12);
		BinaryExpr e3 = new BinaryExpr(e1, MathOp.MULTIPLY, e2);
		UnaryExpr e4 = new UnaryExpr(e3, ExprType.NEGATION);
		BinaryExpr e5 = new BinaryExpr(new UnaryExpr(6), MathOp.ADD, new UnaryExpr(67));
		Relation r = new Relation(e4, RelOp.EQUAL, e5);
		
		System.out.println(r.toString());
		System.out.println(r.size());*/
		
		UnaryExpr e = new UnaryExpr(new UnaryExpr(7), ExprType.MEMORYVAL);
		Relation r1 = new Relation(e, RelOp.NOTEQUAL, new UnaryExpr(17));
		Update u = new Update(new UnaryExpr(7), new UnaryExpr(17));
		LinkedList<Update> ll = new LinkedList<Update>();
		Command c = new Command(ll, u);
		Rule r = new Rule(r1, c);
		
		System.out.println(r.toString());
	}
}
