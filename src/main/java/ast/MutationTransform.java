package ast;

import java.util.Random;

import ast.Action.ActType;
import ast.BinaryCondition.Operator;
import ast.BinaryExpr.MathOp;
import ast.Relation.RelOp;
import ast.Sensor.SensorType;

public class MutationTransform implements Mutation
{
	@Override
	public boolean equals(Mutation m)
	{
		return (m instanceof MutationTransform);
	}	
	
	@Override
	public boolean mutate(BinaryCondition c)
	{
		Operator op = null;
		int index = (int) (Math.random() * 2);
		switch(index) {
		case 0:
			op = BinaryCondition.Operator.AND;
			break;
		case 1:
			op = BinaryCondition.Operator.OR;
			break;
		}
		c.setOperator(op);
		return true;
	}
	
	@Override
	public boolean mutate(Action a)
	{
		ActType at = null;
		int index = (int) (Math.random() * 12);
		switch(index) {
		case 0:
			at = Action.ActType.ATTACK;
			break;
		case 1:
			at = Action.ActType.BACKWARD;
			break;
		case 2: 
			at = Action.ActType.BUD;
			break;
		case 3:
			at = Action.ActType.EAT;
			break;
		case 4:
			at = Action.ActType.FORWARD;
			break;
		case 5:
			at = Action.ActType.GROW;
			break;
		case 6:
			at = Action.ActType.LEFT;
			break;
		case 7:
			at = Action.ActType.MATE;
			break;
		case 8:
			at = Action.ActType.RIGHT;
			break;
		case 9:
			at = Action.ActType.SERVE;
			break;
		case 10:
			at = Action.ActType.TAG;
			break;
		case 11:
			at = Action.ActType.WAIT;
			break;
			}
		a.setActType(at);
		return true;
	}
	
	@Override
	public boolean mutate(Relation r)
	{
		RelOp rel = null;
		int index = (int) (Math.random() * 7);
		switch(index) {
		case 0:
			rel = Relation.RelOp.EQUAL;
			break;
		case 1:
			rel = Relation.RelOp.GREATER;
			break;
		case 2:
			rel = Relation.RelOp.GREATEROREQ;
			break;
		case 3:
			rel = Relation.RelOp.ISCOND;
			break;
		case 4:
			rel = Relation.RelOp.LESS;
			break;
		case 5:
			rel = Relation.RelOp.LESSOREQ;
			break;
		case 6:
			rel = Relation.RelOp.NOTEQUAL;
		}
		r.setRelOp(rel);
		return true;
	}

	@Override
	public boolean mutate(BinaryExpr be)
	{
		MathOp op = null;
		int index = (int) (Math.random() * 5);
		switch(index) {
		case 0:
			op = BinaryExpr.MathOp.ADD;
			break;
		case 1:
			op = BinaryExpr.MathOp.DIVIDE;
			break;
		case 2:
			op = BinaryExpr.MathOp.MOD;
			break;
		case 3: 
			op = BinaryExpr.MathOp.MULTIPLY;
			break;
		case 4:
			op = BinaryExpr.MathOp.SUBTRACT;
			break;
		}
		be.setOperator(op);
		return true;
	}
	
	@Override
	public boolean mutate(UnaryExpr ue)
	{
		Random r = new Random();
		ue.setValue(java.lang.Integer.MAX_VALUE/r.nextInt());
		return true;
	}
	
	@Override
	public boolean mutate(Sensor s)
	{
		SensorType st = null;
		int index = (int) (Math.random() * 4);
		switch(index) {
		case 0:
			st = Sensor.SensorType.AHEAD;
			break;
		case 1:
			st = Sensor.SensorType.NEARBY;
			break;
		case 2:
			st = Sensor.SensorType.RANDOM;
			break;
		case 3:
			st = Sensor.SensorType.SMELL;
			break;
		}
		s.setSensorType(st);
		return true;
	}
	
	//Unsupported methods, which return false by default
	@Override
	public boolean mutate(ProgramImpl p)
	{
		return false;
	}
	@Override
	public boolean mutate(Rule r)
	{
		return false;
	}
	@Override
	public boolean mutate(Command comm)
	{
		return false;
	}
	@Override
	public boolean mutate(Update u)
	{
		return false;
	}
}