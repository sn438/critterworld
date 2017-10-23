package ast;

import java.util.Random;
import interpret.Interpreter;

/** An AST representation of a Sensor node. */
public class Sensor extends AbstractNode implements Expr
{
	/** The type of this Sensor node. */
	private SensorType type;
	/** The sensor index of this node. Not compatible with SensorType SMELL. */
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
	
	/** Returns the type of this sensor. */
	public SensorType getSensorType()
	{
		return type;
	}
	
	/** 
	 * Returns the sensor index of this sensor, if there is one.
	 * Precondition: this sensor is not of type SMELL.
	 */
	public Expr getSensorIndex()
	{
		return sensorIndex;
	}
	
	/** Sets the SensorType of this sensor node. */
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
	public int acceptEvaluation(Interpreter i)
	{
		return i.eval(this);
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
}