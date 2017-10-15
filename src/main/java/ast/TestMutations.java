package ast;

import javax.swing.plaf.synth.SynthSeparatorUI;

import ast.BinaryExpr.MathOp;

public class TestMutations
{
	public static void main(String[] args)
	{
		UnaryExpr e = new UnaryExpr(5);
		
		BinaryExpr be = new BinaryExpr(e, MathOp.DIVIDE, new UnaryExpr(7));
		/*System.out.println(be.toString());
		be.acceptMutation(new MutationSwap());
		System.out.println(be.toString());
		
		Update u = new Update(new UnaryExpr(2), new UnaryExpr(9));
		System.out.println(u.toString());
		u.acceptMutation(new MutationSwap());
		System.out.println(u.toString());
		
		System.out.println(u.getClass().getSimpleName());
		System.out.println(be.getClass().getSimpleName());*/
		
		System.out.println(e.getParent().toString());
	}
}
