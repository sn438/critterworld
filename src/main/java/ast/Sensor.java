package ast;

/** An AST representation of a Sensor node. */
public class Sensor extends AbstractNode implements Expr
{
	/** The type of this Sensor node. */
	private SensorType type;
	private Expr index;
	
	/** Creates a new Sensor node with the specified type and index. */
	public Sensor(SensorType s, Expr e)
	{
		type = s;
		index = e;
	}
	
	public Sensor()
	{
		type = SensorType.SMELL;
		index = null;
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		switch(type)
		{
			case NEARBY:
				sb.append("nearby[" + index.toString() + "]");
				break;
			case AHEAD:
				sb.append("ahead[" + index.toString() + "]");
				break;
			case RANDOM:
				sb.append("random[" + index.toString() + "]");
				break;
			case SMELL:
				sb.append("smell");
				break;
			default:
				break;
		}
		return sb;
	}
	
	@Override
	public int size()
	{
		if(type == SensorType.SMELL)
			return 1;
		return 1 + index.size();
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