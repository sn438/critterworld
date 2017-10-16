package asttests;

import java.util.LinkedList;

import org.junit.Test;

import ast.Action;
import ast.BinaryCondition;
import ast.BinaryExpr;
import ast.Command;
import ast.Condition;
import ast.MutationTransform;
import ast.Relation;
import ast.Sensor;
import ast.UnaryExpr;
import ast.Update;

public class TestMutateTransform {

	@Test
	public void testMutateCondition() {
		System.out.println("testMutateCondition");
		Relation a = new Relation(new UnaryExpr(5), Relation.RelOp.GREATER, new UnaryExpr(7));
		Relation b = new Relation(new UnaryExpr(7), Relation.RelOp.GREATER, new UnaryExpr(5));
		Condition c = new BinaryCondition(a, BinaryCondition.Operator.AND, b);
		System.out.println(c.toString() + "\n");
		c.acceptMutation(new MutationTransform());
		System.out.println(c.toString() + "\n");
	}
	
	@Test
	public void testMutateActionOne() {
		System.out.println("testMutateActionOne");
		BinaryExpr a = new BinaryExpr(new UnaryExpr(5), BinaryExpr.MathOp.ADD, new UnaryExpr(7));
		Action b = new Action(Action.ActType.SERVE, a);
		System.out.println(b.toString() + "\n");
		b.acceptMutation(new MutationTransform());
		System.out.println(b.toString() + "\n");
	}
	
	@Test
	public void testMutateActionTwo() {
		System.out.println("testMutateActionTwo");
		BinaryExpr a = new BinaryExpr(new UnaryExpr(5), BinaryExpr.MathOp.ADD, new UnaryExpr(7));
		Action b = new Action(Action.ActType.TAG, a);
		System.out.println(b.toString() + "\n");
		b.acceptMutation(new MutationTransform());
		System.out.println(b.toString() + "\n");
	}
	
	@Test
	public void testMutateActionThree() {
		System.out.println("testMutateActionThree");
		Action b = new Action(Action.ActType.ATTACK);
		System.out.println(b.toString() + "\n");
		b.acceptMutation(new MutationTransform());
		System.out.println(b.toString() + "\n");
	}
	
	
	@Test
	public void testMutateRelationOne() {
		System.out.println("testMutateRelationOne");
		Relation a = new Relation(new UnaryExpr(5), Relation.RelOp.GREATER, new UnaryExpr(7));
		System.out.println(a.toString() + "\n");
		a.acceptMutation(new MutationTransform());
		System.out.println(a.toString() + "\n");
	}
	
	
	@Test
	public void testMutataRelationTwo() {
		System.out.println("testMutateRelationTwo");
		Relation a = new Relation(new UnaryExpr(5), Relation.RelOp.GREATER, new UnaryExpr(7));
		Relation b = new Relation(new UnaryExpr(7), Relation.RelOp.GREATER, new UnaryExpr(5));
		Condition c = new BinaryCondition(a, BinaryCondition.Operator.AND, b);
		Relation d = new Relation(c);
		System.out.println(d.toString() + "\n");
		d.acceptMutation(new MutationTransform());
		System.out.println(d.toString() + "\n");
	}
	
	@Test
	public void testMutateBinaryExpression() {
		System.out.println("testMutateBinaryExpr");
		BinaryExpr a = new BinaryExpr(new UnaryExpr(5), BinaryExpr.MathOp.ADD, new UnaryExpr(7));
		System.out.println(a.toString() + "\n");
		a.acceptMutation(new MutationTransform());
		System.out.println(a.toString() + "\n");
	}
	
	@Test
	public void testMutateUnaryExprOne() {
		System.out.println("testMutateUnaryExprOne");
		UnaryExpr a = new UnaryExpr(5);
		System.out.println(a.toString() + "\n");
		a.acceptMutation(new MutationTransform());
		System.out.println(a.toString() + "\n");
	}
	
	@Test
	public void testMutateUnaryExprTwo() {
		System.out.println("testMutateUnaryExprTwo");
		BinaryExpr a = new BinaryExpr(new UnaryExpr(5), BinaryExpr.MathOp.ADD, new UnaryExpr(7));
		UnaryExpr b = new UnaryExpr(a, UnaryExpr.ExprType.EXPRESSION);
		System.out.println(b.toString() + "\n");
		b.acceptMutation(new MutationTransform());
		System.out.println(b.toString() + "\n");
	}
	
	@Test
	public void testMutateUnaryExprThree() {
		System.out.println("testMutateUnaryExprThree");
		BinaryExpr a = new BinaryExpr(new UnaryExpr(5), BinaryExpr.MathOp.ADD, new UnaryExpr(7));
		UnaryExpr b = new UnaryExpr(a, UnaryExpr.ExprType.MEMORYVAL);
		System.out.println(b.toString() + "\n");
		b.acceptMutation(new MutationTransform());
		System.out.println(b.toString() + "\n");
	}
	
	@Test
	public void testMutateUnaryExprFour() {
		System.out.println("testMutateUnaryExprFour");
		UnaryExpr b = new UnaryExpr(new UnaryExpr(5), UnaryExpr.ExprType.NEGATION);
		System.out.println(b.toString() + "\n");
		b.acceptMutation(new MutationTransform());
		System.out.println(b.toString() + "\n");
	}
	
	@Test
	public void testMutateUnaryExprFive() {
		System.out.println("testMutateUnaryExprFive");
		UnaryExpr b = new UnaryExpr(new UnaryExpr(5), UnaryExpr.ExprType.SENSORVAL);
		System.out.println(b.toString() + "\n");
		b.acceptMutation(new MutationTransform());
		System.out.println(b.toString() + "\n");
	}
	
	@Test
	public void testMutateSensorOne() {
		System.out.println("testMutateSensorOne");
		Sensor a = new Sensor();
		System.out.println(a.toString() + "\n");
		a.acceptMutation(new MutationTransform());
		System.out.println(a.toString() + "\n");
	}
	
	
	@Test
	public void testMutateSensorTwo() {
		System.out.println("testMutateSensorTwo");
		UnaryExpr a = new UnaryExpr(5);
		Sensor b = new Sensor(Sensor.SensorType.AHEAD, a);
		System.out.println(b.toString() + "\n");
		b.acceptMutation(new MutationTransform());
		System.out.println(b.toString() + "\n");
	}
}
