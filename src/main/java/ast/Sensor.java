package ast;

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
}