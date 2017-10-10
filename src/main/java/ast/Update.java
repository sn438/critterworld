package ast;

public class Update extends AbstractNode implements CommandComponent
{
	/** The index in memory to update. */
	private Expr index;
	/** The value that mem[index.evaluateNode()] will be changed to. */
	private Expr value;
	
	/** 
	 * Creates a new Update node that updates one value in the critter's memory array.
	 * @param i - the Expr node that evaluates to the index of the memory array that will be updated
	 * @param val - the Expr node that evaluates to the value that mem[index.evaluateNode()] 
	 * 				will be changed to
	 */
	public Update(Expr i, Expr val)
	{
		index = i;
		value = val;
	}
	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		sb.append("mem[" + index.toString() + "] := " + value.toString());
		return sb;
	}
}