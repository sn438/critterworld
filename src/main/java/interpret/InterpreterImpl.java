package interpret;

import ast.*;
import simulation.Critter;
import simulation.SimpleWorld;

import java.util.LinkedList;

public class InterpreterImpl implements Interpreter
{
	/** The critter whose AST this Interpreter interprets. */
	private Critter c;
	
	private SimpleWorld world;
	
	/** Creates a new InterpreterImpl. */
	public InterpreterImpl(Critter cr, SimpleWorld sw)
	{
		c = cr;
		world = sw;
	}
	
	@Override
	public Outcome interpret(Program p)
	{
		// TODO Auto-generated method stub
		LinkedList<Rule> rl = p.getRulesList();
		return null;
	}

	@Override
	public boolean eval(BinaryCondition c)
	{
		boolean result = false;
		boolean left = c.getLeft().acceptEvaluation(this);
		boolean right = c.getRight().acceptEvaluation(this);
		switch(c.getOp())
		{
			case AND:
				result = left && right;
				break;
			case OR:
				result = left || right;
				break;
		}
		return result;
	}
	
	@Override
	public boolean eval(Relation r)
	{
		boolean result = false;
		int left = r.getLeft().acceptEvaluation(this);
		int right = r.getRight().acceptEvaluation(this);
		switch(r.getRelOp())
		{
			case LESS:
				result = left < right;
				break;
			case LESSOREQ:
				result = left <= right;
				break;
			case GREATER:
				result = left > right;
				break;
			case GREATEROREQ:
				result = left >= right;
				break;
			case EQUAL:
				result = left == right;
				break;
			case NOTEQUAL:
				result = left != right;
				break;
			case ISCOND:
				result = r.getCond().acceptEvaluation(this);
				break;
		}
		return result;
	}
	
	@Override
	public int eval(BinaryExpr e)
	{
		int result = 0;
		int left = e.getLeft().acceptEvaluation(this);
		int right = e.getRight().acceptEvaluation(this);
		switch(e.getOperator())
		{
			case ADD:
				result = left + right;
				break;
			case SUBTRACT:
				result = left - right;
				break;
			case MULTIPLY:
				result = left * right;
				break;
			case DIVIDE:
				if(right != 0)
					result = left / right;
				break;
			case MOD:
				if(right != 0)
					result = left % right;
				break;
		}
		return result;
	}
	
	@Override
	public int eval(UnaryExpr e)
	{
		int result = 0;
		switch(e.getExprType())
		{
			case CONSTANT:
				result = e.getValue();
				break;
			case MEMORYVAL:
				int index = e.getExp().acceptEvaluation(this);
				//TODO add clause to check out of bounds
				result = c.readMemory(index);
				break;
			case EXPRESSION:
				result = e.getExp().acceptEvaluation(this);
				break;
			case NEGATION:
				result = -1 * e.getExp().acceptEvaluation(this);
				break;
		}
		return result;
	}
	
	@Override
	public int eval(Sensor s)
	{
		int result = 0;
		int index;
		switch(s.getSensorType())
		{
			case NEARBY:
				index = s.getSensorIndex().acceptEvaluation(this);
				result = world.searchNearby(c, index);
				break;
			case AHEAD:
				index = s.getSensorIndex().acceptEvaluation(this);
				result = world.searchAhead(c, index);
				break;
			case RANDOM:
				index = s.getSensorIndex().acceptEvaluation(this);
				if(index < 2)
					result = 0;
				else
					result = (int) (Math.random() * index);
			case SMELL:
				result = 0;
		}
		return result;
	}
}