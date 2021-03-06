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
		memIndex.setParent(this);
		value.setParent(this);
	}
	
	/** Returns the memory index of this update. */
	public Expr getMemIndex()
	{
		return memIndex;
	}
	
	/** Sets the value of {@code memIndex} to {@code newIndex}. */
	public void setMemIndex(Expr newIndex)
	{
		this.memIndex = newIndex;
	}
	
	/** Returns the new value to be set for this update. */
	public Expr getValue()
	{
		return value;
	}
	
	/** Sets the value of {@code value} to {@code newVal}. */
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
	public boolean acceptMutation(Mutation m)
	{
		boolean result = m.mutate(this);
		return result;
	}
	@Override
	public boolean replaceChild(Node child, Node replacement)
	{
		if(child == this.memIndex)
		{
			this.memIndex = (Expr) replacement;
			memIndex.setParent(this);
			return true;
		}
		else if(child == this.value)
		{
			this.value = (Expr) replacement;
			value.setParent(this);
			return true;
		}
		return false;
	}
	
	@Override
	public Node searchChildrenForSimilarType()
	{
		//Since no child of an update node will be of type update, this method is unsupported.
		return null;
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

	@Override
	public NodeType getType()
	{
		return NodeType.UPDATE;
	}
}