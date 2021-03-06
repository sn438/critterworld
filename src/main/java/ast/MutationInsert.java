package ast;

import ast.BinaryCondition.Operator;
import ast.BinaryExpr.MathOp;
import ast.Node.NodeType;

public class MutationInsert extends AbstractMutation
{
	public MutationInsert(boolean p)
	{
		super(p);
	}
	
	@Override
	public boolean equals(Mutation m)
	{
		return (m instanceof MutationInsert);
	}	
	
	@Override
	public boolean mutate(BinaryCondition c)
	{
		Node root = findRoot(c);

		//The only possible parent of a condition node is another condition.
		//BinaryCondition nodes require two children, so we search the rest of the tree for another condition node
		Condition copy = null;
		int size = root.size();
		int rand = (int) (Math.random() * size);
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			if(root.nodeAt(index).getType() == c.getType() || root.nodeAt(index).getType() == NodeType.RELATION)
			{
				copy = (Condition) (root.nodeAt(index)).clone();
				break;
			}
		}
		if(copy == null)
			return false;
		
		//Generates a random binary operator for the condition node to be inserted
		Operator op = null;
		int n = (int) (Math.random() * 2);
		switch(n)
		{
			case 0:
				op = BinaryCondition.Operator.AND;
				break;
			case 1:
				op = BinaryCondition.Operator.OR;
				break;
		}
		Node parent = c.getParent();
		BinaryCondition toInsert = new BinaryCondition(c, op, copy);
		parent.replaceChild(c, toInsert);
		if(printMutationDetail)
			System.out.println("Inserted the binary condition node " + copy.toString());
		return true;
	}
	
	@Override
	public boolean mutate(Relation r)
	{
		//finds the root of the AST
		Node root = findRoot(r);
				
		//The only possible parent of a relation node is another condition.
		//BinaryCondition nodes require two children, so we search the rest of the tree for another condition node
		Condition copy = null;
		int size = root.size();
		int rand = (int) (Math.random() * size);
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			if(root.nodeAt(index).getType() == r.getType() || root.nodeAt(index).getType() == NodeType.BINARYCONDITION)
			{
				copy = (Condition) (root.nodeAt(index)).clone();
				break;
			}
		}
		if(copy == null)
			return false;
		
		Operator op = null;
		int n = (int) (Math.random() * 2);
		switch(n)
		{
			case 0:
				op = BinaryCondition.Operator.AND;
				break;
			case 1:
				op = BinaryCondition.Operator.OR;
				break;
		}
		Node parent = r.getParent();
		BinaryCondition toInsert = new BinaryCondition(r, op, copy);
		parent.replaceChild(r, toInsert);
		if (printMutationDetail == true)
			System.out.println("Inserted the binary condition node " + copy.toString() + "with parent " + parent.toString());
		return true;
	}

	@Override
	public boolean mutate(BinaryExpr be)
	{
		//finds the root of the AST
		Node root = findRoot(be);
		
		//Finds another compatible expression node
		Expr copy = null;
		int size = root.size();
		int rand = (int) (Math.random() * size);
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			if(root.nodeAt(index).getType() == be.getType() || root.nodeAt(index).getType() == NodeType.UNARYEXPR
															|| root.nodeAt(index).getType() == NodeType.SENSOR)
			{
				copy = (Expr) (root.nodeAt(index)).clone();
				break;
			}
		}
		if(copy == null)
			return false;
				
		MathOp op = null;
		int n = (int) (Math.random() * 5);
		switch(n) 
		{
			case 0:
				op = BinaryExpr.MathOp.ADD;
				break;
			case 1:
				op = BinaryExpr.MathOp.DIVIDE;
				break;
			case 2:
				op = BinaryExpr.MathOp.MOD;
				break;
			case 3: 
				op = BinaryExpr.MathOp.MULTIPLY;
				break;
			case 4:
				op = BinaryExpr.MathOp.SUBTRACT;
				break;
		}
		Node parent = be.getParent();
		BinaryExpr toInsert = new BinaryExpr(be, op, copy);
		parent.replaceChild(be, toInsert);
		if (printMutationDetail == true)
			System.out.println("Inserted the binary condition node " + copy.toString() + "with parent " + parent.toString());
		return true;
	}
	
	@Override
	public boolean mutate(UnaryExpr ue)
	{
		//finds the root of the AST
		Node root = findRoot(ue);
		
		//Finds another compatible expression node
		Expr copy = null;
		int size = root.size();
		int rand = (int) (Math.random() * size);
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			if(root.nodeAt(index).getType() == ue.getType() || root.nodeAt(index).getType() == NodeType.BINARYEXPR
															|| root.nodeAt(index).getType() == NodeType.SENSOR)
			{
				copy = (Expr) (root.nodeAt(index)).clone();
				break;
			}
		}
		if(copy == null)
			return false;
				
		MathOp op = null;
		int n = (int) (Math.random() * 5);
		switch(n) 
		{
			case 0:
				op = BinaryExpr.MathOp.ADD;
				break;
			case 1:
				op = BinaryExpr.MathOp.DIVIDE;
				break;
			case 2:
				op = BinaryExpr.MathOp.MOD;
				break;
			case 3: 
				op = BinaryExpr.MathOp.MULTIPLY;
				break;
			case 4:
				op = BinaryExpr.MathOp.SUBTRACT;
				break;
		}
		Node parent = ue.getParent();
		BinaryExpr toInsert = new BinaryExpr(ue, op, copy);
		parent.replaceChild(ue, toInsert);
		if (printMutationDetail == true) {
			System.out.println("Inserted the expr condition node " + copy.toString() + "with parent " + parent.toString());
			}
		return true;
	}
	
	@Override
	public boolean mutate(Sensor s)
	{
		//finds the root of the AST
		Node root = findRoot(s);

		//Finds another compatible expression node
		Expr copy = null;
		int size = root.size();
		int rand = (int) (Math.random() * size);
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			if(root.nodeAt(index).getType() == s.getType() || root.nodeAt(index).getType() == NodeType.UNARYEXPR
															|| root.nodeAt(index).getType() == NodeType.BINARYEXPR)
			{
				copy = (Expr) (root.nodeAt(index)).clone();
				break;
			}
		}
		if(copy == null)
			return false;
				
		MathOp op = null;
		int n = (int) (Math.random() * 5);
		switch(n) 
		{
			case 0:
				op = BinaryExpr.MathOp.ADD;
				break;
			case 1:
				op = BinaryExpr.MathOp.DIVIDE;
				break;
			case 2:
				op = BinaryExpr.MathOp.MOD;
				break;
			case 3: 
				op = BinaryExpr.MathOp.MULTIPLY;
				break;
			case 4:
				op = BinaryExpr.MathOp.SUBTRACT;
				break;
		}
		Node parent = s.getParent();
		BinaryExpr toInsert = new BinaryExpr(s, op, copy);
		parent.replaceChild(s, toInsert);
		if (printMutationDetail == true) {
			System.out.println("Inserted the binary expression node " + copy.toString() + "with parent " + parent.toString());
			}
		return true;
	}
	
	//Unsupported methods, which return false by default
	@Override
	public boolean mutate(ProgramImpl p)
	{
		return false;
	}
	@Override
	public boolean mutate(Rule r)
	{
		return false;
	}
	@Override
	public boolean mutate(Command comm)
	{
		return false;
	}
	@Override
	public boolean mutate(Update u)
	{
		return false;
	}
	@Override
	public boolean mutate(Action a)
	{
		return false;
	}
}