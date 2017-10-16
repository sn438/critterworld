package asttests;

import org.junit.Test;

import ast.Action;
import ast.BinaryCondition;
import ast.Condition;
import ast.MutationTransform;
import ast.Relation;
import ast.UnaryExpr;

public class TestMutateTransform {

	@Test
	public void testMutateCondition() {
		Relation a = new Relation(new UnaryExpr(5), Relation.RelOp.GREATER, new UnaryExpr(7));
		Relation b = new Relation(new UnaryExpr(7), Relation.RelOp.GREATER, new UnaryExpr(5));
		Condition c = new BinaryCondition(a, BinaryCondition.Operator.AND, b);
		System.out.println(c.toString() + "\n");
		c.acceptMutation(new MutationTransform());
		System.out.println(c.toString() + "\n");
	}
	
	@Test
	public void testMutateAction() {
		Action a = new Action(Action.ActType.ATTACK, new UnaryExpr(5));
		System.out.println(a.toString() + "\n");
		a.acceptMutation(new MutationTransform());
		System.out.println(a.toString() + "\n");
	}
	/*
	@Test
	public void testMutateRelationOne() {
		Relation a = new Relation(new UnaryExpr(5), Relation.RelOp.GREATER, new UnaryExpr(7));
		System.out.println(a.toString() + "\n");
		a.acceptMutation(new MutationTransform());
		System.out.println(a.toString() + "\n");
	}
	*/
	@Test
	public void testMutataRelationTwo() {
		Relation a = new Relation(new UnaryExpr(5), Relation.RelOp.GREATER, new UnaryExpr(7));
		Relation b = new Relation(new UnaryExpr(7), Relation.RelOp.GREATER, new UnaryExpr(5));
		Condition c = new BinaryCondition(a, BinaryCondition.Operator.AND, b);
		Relation d = new Relation(c);
		System.out.println(d.toString() + "\n");
		a.acceptMutation(new MutationTransform());
		System.out.println(d.toString() + "\n");
	}
}
