package ast;

import java.util.Random;

/** An AST representation of a Sensor node. */
public class Sensor extends AbstractNode implements Expr
{
	/** The type of this Sensor node. */
	private SensorType type;
	private Expr sensorIndex;
	
	/** Creates a new Sensor node with the specified type and index. Not compatible with SensorType SMELL. */
	public Sensor(SensorType s, Expr e)
	{
		type = s;
		sensorIndex = e;
		sensorIndex.setParent(this);
	}
	
	/** Creates a new Sensor node of type SMELL. */
	public Sensor()
	{
		type = SensorType.SMELL;
		sensorIndex = null;
	}

	@Override
	public int size()
	{
		if(type == SensorType.SMELL)
			return 1;
		return 1 + sensorIndex.size();
	}
	
	@Override
	public Node nodeAt(int index)
	{
		if(index == 0)
			return this;
		if(index > size() - 1 || index < 0)
			throw new IndexOutOfBoundsException();
		return sensorIndex.nodeAt(index - 1);
	}
	
	@Override
	public Sensor clone()
	{
		if(type == SensorType.SMELL)
			return new Sensor();
		return new Sensor(type, sensorIndex.clone());
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
		if(type == SensorType.SMELL)
			return false;
		sensorIndex = (Expr) replacement;
		sensorIndex.setParent(this);
		return true;
	}
	
	@Override
	public Node searchChildrenForSimilarType()
	{
		if(type != SensorType.SMELL && (sensorIndex.getType() == NodeType.BINARYEXPR || sensorIndex.getType() == NodeType.UNARYEXPR
				  																     || sensorIndex.getType() == NodeType.SENSOR))
			return sensorIndex;
		return null;
	}
	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		switch(type)
		{
			case NEARBY:
				sb.append("nearby[" + sensorIndex.toString() + "]");
				break;
			case AHEAD:
				sb.append("ahead[" + sensorIndex.toString() + "]");
				break;
			case RANDOM:
				sb.append("random[" + sensorIndex.toString() + "]");
				break;
			case SMELL:
				sb.append("smell");
				break;
		}
		return sb;
	}
	
	@Override
	public int evaluate()
	{
		throw new UnsupportedOperationException();
	}
	
	/** An enumeration of all the possible Sensor types. */
	public enum SensorType
	{
		NEARBY, AHEAD, RANDOM, SMELL;
	}

	@Override
	public NodeType getType()
	{
		return NodeType.SENSOR;
	}
	
	public void setSensorType(SensorType st)
	{
		Random r = new Random();
		if (this.type.equals(SensorType.SMELL))
		{
			if (!st.equals(SensorType.SMELL))
				this.sensorIndex = new UnaryExpr(java.lang.Integer.MAX_VALUE/r.nextInt());
		}
		if (st.equals(SensorType.SMELL))
			this.sensorIndex = null;
		this.type = st;
	}
}