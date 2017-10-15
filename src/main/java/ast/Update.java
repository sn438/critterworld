package ast;

/** An AST representation of a critter update in memory. */
public class Update extends AbstractNode implements CommandComponent
{
	/** The index in memory to update. */
	private Expr memIndex;
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
		memIndex = i;
		value = val;
	}
	
	public Expr getMemIndex()
	{
		return memIndex;
	}
	public void setMemIndex(Expr newIndex)
	{
		this.memIndex = newIndex;
	}
	public Expr getValue()
	{
		return value;
	}
	public void setValue(Expr newVal)
	{
		this.value = newVal;
	}

	@Override
	public Update clone()
	{
		Expr tempIndex = memIndex.clone();
		Expr tempValue = value.clone();
		return new Update(tempIndex, tempValue);
	}
	
	@Override
	public void acceptMutation(Mutation m)
	{
		m.mutate(this);
	}
	@Override
	public int size()
	{
		return 1 + memIndex.size() + value.size();
	}
	@Override
	public Node nodeAt(int index)
	{
		if(index == 0)
			return this;
		if(index > size() - 1 || index < 0)
			throw new IndexOutOfBoundsException();
		if(index <= memIndex.size())
			return memIndex.nodeAt(index - 1);
		else
			return value.nodeAt(index - memIndex.size() - 1);
	}
	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		sb.append("mem[" + memIndex.toString() + "] := " + value.toString());
		return sb;
	}
}