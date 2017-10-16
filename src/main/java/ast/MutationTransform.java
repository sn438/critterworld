package ast;

import java.util.Random;

import ast.Action.ActType;
import ast.BinaryCondition.Operator;
import ast.Relation.RelOp;

public class MutationTransform implements Mutation
{
	public boolean equals(Mutation m)
	{
		// TODO Auto-generated method stub
		return false;
	}	
	
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
	
	public boolean mutate(Command comm)
	{
		return false;
	}
	
	public boolean mutate(Update u)
	{
		// TODO Auto-generated method stub
		return false;
	}

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
		return false;
	}
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

	public boolean mutate(BinaryExpr be)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean mutate(UnaryExpr ue)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean mutate(Sensor s)
	{
		// TODO Auto-generated method stub
		return false;
	}
}