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
	public int evaluateNode()
	{
		throw new UnsupportedOperationException();
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
	
	/** An enumeration of all the possible Sensor types. */
	public enum SensorType
	{
		NEARBY, AHEAD, RANDOM, SMELL;
	}
}