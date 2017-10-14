package ast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;

import ast.BinaryExpr;
import ast.BinaryCondition.Operator;
import ast.BinaryExpr.MathOp;
import ast.Relation.RelOp;
import ast.UnaryExpr.ExprType;
import parse.Parser;
import parse.ParserFactory;
import parse.Tokenizer;
import parsertests.ParserTest;

public class Test
{
	public static void main(String[] args)
	{
		/*UnaryExpr e1 = new UnaryExpr(2);
		UnaryExpr e2 = new UnaryExpr(12);
		BinaryExpr e3 = new BinaryExpr(e1, MathOp.MULTIPLY, e2);
		UnaryExpr e4 = new UnaryExpr(e3, ExprType.NEGATION);
		BinaryExpr e5 = new BinaryExpr(new UnaryExpr(6), MathOp.ADD, new UnaryExpr(67));
		Relation r = new Relation(e4, RelOp.EQUAL, e5);
		
		System.out.println(r.toString());
		System.out.println(r.size());*/
		
		UnaryExpr e = new UnaryExpr(new UnaryExpr(7), ExprType.MEMORYVAL);
		BinaryExpr e5 = new BinaryExpr(new UnaryExpr(6), MathOp.ADD, new UnaryExpr(67));
		UnaryExpr e1 = new UnaryExpr(2);
		UnaryExpr e2 = new UnaryExpr(12);
		BinaryExpr e3 = new BinaryExpr(e1, MathOp.MULTIPLY, e2);
		Relation r1 = new Relation(e, RelOp.NOTEQUAL, new UnaryExpr(17));
		Relation r2 = new Relation(e3, RelOp.GREATER, e5);
		Condition con = new BinaryCondition(r1, Operator.AND, r2);
		Update u = new Update(new UnaryExpr(7), new UnaryExpr(17));
		LinkedList<Update> ll = new LinkedList<Update>();
		Update u2 = new Update(new UnaryExpr(7), new UnaryExpr(17));
		Update u3 = new Update(new UnaryExpr(7), new UnaryExpr(17));
		Update u4 = new Update(new UnaryExpr(7), new UnaryExpr(17));
		ll.add(u2);
		ll.add(u3);
		ll.add(u4);
		Command c = new Command(ll, u);
		Rule rule = new Rule(con, c);
		
		System.out.println(rule.toString() + "\n" + rule.size() + "\n" + rule.nodeAt(13));
		System.out.println("" + rule.size() + "\n" + con.size() + "\n" + c.size());
		
		/*Sensor s = new Sensor();
		System.out.println(s.nodeAt(0).toString());*/
		
		/*InputStream in = ParserTest.class.getResourceAsStream("example-rules.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Tokenizer t = new Tokenizer(r);
        Parser p = ParserFactory.getParser();
        Program prog = p.parse(r);
        System.out.println(prog.toString());*/
		
		/*Update u = new Update(new UnaryExpr(7), new UnaryExpr(17));
		System.out.println(u.size() + "\n" + u.nodeAt(0) + "\nmem[" + u.nodeAt(1) + "] := " + u.nodeAt(2));
		UnaryExpr e1 = new UnaryExpr(new UnaryExpr(5), ExprType.NEGATION);
		System.out.println(e1.size() + "\n" + e1.nodeAt(1));
		BinaryExpr e5 = new BinaryExpr(new UnaryExpr(6), MathOp.ADD, new UnaryExpr(67));
		System.out.println(e5.size() + "\n" + e5.nodeAt(0));*/
	}
}
