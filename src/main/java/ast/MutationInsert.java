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
<<<<<<< HEAD
		//finds the root of the AST
		Node root = c.getParent();
		while(root.getParent() != null)
			root = root.getParent();
		if (printMutationDetail) {
		System.out.println("Node that is being mutated: " + c + "\n");
		System.out.println("Program before being mutated: " + root + "\n");
		}
=======
		Node root = findRoot(c);
		
>>>>>>> be7a6a2b421e718c7e3eea8350bd0acbb7531348
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
			System.out.println("Replaced the binary condition node\n");
		return true;
	}
	
	public boolean mutate(Relation r)
	{
<<<<<<< HEAD
		//finds the root of the AST
		Node root = r.getParent();
		while(root.getParent() != null)
			root = root.getParent();
		if (printMutationDetail == true) {
			System.out.println("Node that is being mutated: " + r + "\n");
			System.out.println("Program before being mutated: " + root + "\n");
		}	
=======
		Node root = findRoot(r);
			
>>>>>>> be7a6a2b421e718c7e3eea8350bd0acbb7531348
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
		if (printMutationDetail == true) {
		System.out.println("Program after being mutated: " + root + "\n");
		}
		return true;
	}

	public boolean mutate(BinaryExpr be)
	{
<<<<<<< HEAD
		//finds the root of the AST
		Node root = be.getParent();
		while(root.getParent() != null)
			root = root.getParent();
		if (printMutationDetail == true) {
			System.out.println("Node that is being mutated: " + be + "\n");
			System.out.println("Program before being mutated: " + root + "\n");
		}	
=======
		Node root = findRoot(be);

>>>>>>> be7a6a2b421e718c7e3eea8350bd0acbb7531348
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
		if (printMutationDetail == true) {
			System.out.println("Program after being mutated: " + root + "\n");
			}
		return true;
	}
	
	public boolean mutate(UnaryExpr ue)
	{
<<<<<<< HEAD
		//finds the root of the AST
		Node root = ue.getParent();
		while(root.getParent() != null)
			root = root.getParent();
		if (printMutationDetail == true) {
			System.out.println("Node that is being mutated: " + ue + "\n");
			System.out.println("Parent of the Node: " + ue.getParent() + "\n");
			System.out.println("Program before being mutated: " + root + "\n");
		}
=======
		Node root = findRoot(ue);

>>>>>>> be7a6a2b421e718c7e3eea8350bd0acbb7531348
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
			System.out.println("Program after being mutated: " + root + "\n");
			}
		return true;
	}
	
	public boolean mutate(Sensor s)
	{
<<<<<<< HEAD
		//finds the root of the AST
		Node root = s.getParent();
		while(root.getParent() != null)
			root = root.getParent();
		if (printMutationDetail == true) {
			System.out.println("Node that is being mutated: " + s + "\n");
			System.out.println("Parent of the Node: " + s.getParent() + "\n");
			System.out.println("Program before being mutated: " + root + "\n");
		}
=======
		Node root = findRoot(s);
			
>>>>>>> be7a6a2b421e718c7e3eea8350bd0acbb7531348
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
			System.out.println("Program after being mutated: " + root + "\n");
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