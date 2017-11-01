package interpret;

import ast.*;
import ast.Action.ActType;
import ast.Node.NodeType;
import simulation.AbstractWorld;
import simulation.SimpleCritter;

import java.util.LinkedList;

/** Interprets and executes the AST of a critter. */
public class InterpreterImpl implements Interpreter
{
	/** The critter whose AST this Interpreter interprets. */
	private SimpleCritter c;
	/** The world in which the critter inhabits. */
	private AbstractWorld world;
	
	/** Creates a new InterpreterImpl. */
	public InterpreterImpl(SimpleCritter cr, AbstractWorld aw)
	{
		c = cr;
		world = aw;
	}
	
	/** Executes the results of one critter turn. */
	public void simulateCritterTurn()
	{
		Action a = interpret(c.getProgram());
		executeAction(a);
		System.out.println("RightCount: " + c.readMemory(10) + "forwardCount: " + c.readMemory(11));
	}
	
	/**
     * Execute program {@code p} until either the maximum number of rules per turn is reached or some rule
     * whose command contains an action is executed.
     * @param p
     * @return the action to be performed
     */
	private Action interpret(Program p)
	{
		LinkedList<Rule> rl = p.getRulesList();
		Action a = null;
		boolean actionInterpreted = false;
		while (!actionInterpreted && c.readMemory(5) < world.getMaxRules())
		{
			for (Rule r : rl)
			{
				boolean ruleCondition = r.getCond().acceptEvaluation(this);
				if(ruleCondition)
				{
					Command ruleCommand = r.getComm();
					for(Update u : ruleCommand.getUpdateList())
						applyUpdate(u);
					if(ruleCommand.getLast().getType() == NodeType.ACTION)
					{
						a = (Action) ruleCommand.getLast();
						//System.out.println(r.toString());//TODO remove
						actionInterpreted = true;
					}
					else
						applyUpdate((Update) ruleCommand.getLast());
					c.setLastRule(r.toString());
					break;
				}
			}
			c.incrementPass();
		}
		c.setMemory(0, 5);
		
		if(a == null)
			a = new Action(ActType.WAIT);
		System.out.println(a.toString()); //TODO remove when done testing
		return a;
	}
	
	/** Executes an action returned by the {@code interpret} method. */
	private void executeAction(Action a)
	{
		int val = 0;
		if(a.getVal() != null)
			val = a.getVal().acceptEvaluation(this);
		
		switch(a.getActType())
		{
			case FORWARD:
				world.moveCritter(c, true);
				break;
			case BACKWARD:
				world.moveCritter(c, false);
				break;
			case LEFT:
				world.turnCritter(c, false);
				break;
			case RIGHT:
				world.turnCritter(c, true);
				break;
			case EAT:
				world.critterEat(c);
				break;
			case ATTACK:
				world.critterBattle(c);
				break;
			case GROW:
				world.growCritter(c);
				break;
			case BUD:
				world.critterBud(c);
				break;
			case MATE:
				world.critterMate(c);
				break;
			case TAG:
				world.critterTag(c, val);
				break;
			case SERVE:
				world.critterServe(c, val);
				break;
			case WAIT:
				world.critterSoakEnergy(c);
				break;
		}
	}
	/** Applies the effects of a single update to a critter. */
	private void applyUpdate(Update u)
	{
		int index = u.getMemIndex().acceptEvaluation(this);
		int val = u.getValue().acceptEvaluation(this);
		c.setMemory(val, index);
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
		if(r.getCond() != null)
		{
			result = r.getCond().acceptEvaluation(this);
			return result;
		}
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
		//System.out.println("Left: " + r.getLeft().toString() + left);
		//System.out.println("Right: " + r.getRight().toString() + right);
		//System.out.println(r.toString() + result);
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
				result = c.readMemory(index);
				if(result == Integer.MIN_VALUE)
					result = 0;
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