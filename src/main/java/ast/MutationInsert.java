package ast;

import ast.BinaryCondition.Operator;
import ast.Node.NodeType;

public class MutationInsert implements Mutation
{
	@Override
	public boolean equals(Mutation m)
	{
		return (m instanceof MutationInsert);
	}	
	
	@Override
	public boolean mutate(BinaryCondition c)
	{
		//finds the root of the AST
		Node root = c.getParent();
		while(root.getParent() != null)
			root = root.getParent();
		
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
		BinaryCondition toInsert = new BinaryCondition(c, op, copy);
		Node parent = c.getParent();
		parent.replaceChild(c, toInsert);
		return true;
	}
	
	public boolean mutate(Relation r)
	{
		//finds the root of the AST
		Node root = r.getParent();
		while(root.getParent() != null)
			root = root.getParent();
			
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
		BinaryCondition toInsert = new BinaryCondition(r, op, copy);
		Node parent = r.getParent();
		parent.replaceChild(r, toInsert);
		return true;
	}

	public boolean mutate(BinaryExpr be)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean mutate(UnaryExpr ue)
	{
		System.out.println(parentNode(ue));
		return false;
	}
	
	public boolean mutate(Sensor s)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public ProgramImpl parentNode(Node child) {
		Node parent = child.getParent();
		if (parent != null) {
		while (parent != null) {
			child = parent;
			parent = parent.getParent();
		}
		}
		return (ProgramImpl) child;
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