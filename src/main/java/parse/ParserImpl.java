package parse;

import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.util.LinkedList;

import ast.*;
import exceptions.SyntaxError;

class ParserImpl implements Parser {
	/**
	 * tokens is the Tokenizer that contains all the tokens from which the AST is
	 * built.
	 */
	private Tokenizer tokens;
	private Program programAST;

	@Override
	public Program parse(Reader r) {
		this.tokens = new Tokenizer(r);
		try {
			this.programAST = parseProgram(tokens);
		} catch (SyntaxError e) {
			System.out.println("The program inputted does not have the proper syntax.");
			System.exit(0);
		}

		return this.programAST;

	}

	/**
	 * Parses a program from the stream of tokens provided by the Tokenizer,
	 * consuming tokens representing the program. All following methods with a name
	 * "parseX" have the same spec except that they parse syntactic form X.
	 * 
	 * @return the created AST
	 * @throws SyntaxError
	 *             if there the input tokens have invalid syntax
	 */
	public static ProgramImpl parseProgram(Tokenizer t) throws SyntaxError {

		/*
		 * if (!t.peek().toString().equals("-->")) { parseFactor(t); } }
		 */
		LinkedList<Rule> RuleList = new LinkedList<Rule>();
		while (t.hasNext()) {
			RuleList.add(parseRule(t));
			break; // TODO remove when completed
		}
		return new ProgramImpl(RuleList);
	}

	public static Rule parseRule(Tokenizer t) throws SyntaxError {
		Condition condition = parseCondition(t);
		consume(t, TokenType.ARR);
		Command command = parseCommand(t);
		consume(t, TokenType.SEMICOLON);
		return new Rule(condition, command);
	}
	
	public static Command parseCommand(Tokenizer t) throws SyntaxError {
		LinkedList<Update> UpdateList = new LinkedList<Update>();
		while(true) {
			if(t.peek().getType() == TokenType.SEMICOLON) {
				if(UpdateList.size() == 0)
					throw new SyntaxError();
				else {
					Update last = UpdateList.removeLast();
					return new Command(UpdateList, last);
				}
			}
			else if(t.peek().isAction()) {
				return new Command(UpdateList, parseAction(t));
			}
			UpdateList.add(parseUpdate(t));
		}
	}
	
	public static Update parseUpdate(Tokenizer t) throws SyntaxError {
		
	}
	
	public static Action parseAction(Tokenizer t) throws SyntaxError {
		
	}
	
	public static Condition parseCondition(Tokenizer t) throws SyntaxError {
		Condition condition = parseConjunction(t);
		return condition;
	}
	
	public static Condition parseConjunction(Tokenizer t) throws SyntaxError {
		Condition condition = parseRelation(t);
		
		System.out.println(condition.prettyPrint(new StringBuilder()));
		return condition;
	}
	public static Condition parseRelation(Tokenizer t) throws SyntaxError {
		Expr expression = parseExpression(t);
		System.out.println(expression.toString());
		Condition condition;
		if (t.peek().getType().category() == TokenCategory.RELOP) {
			String relationOperator = t.next().toString();
			switch (relationOperator) {
			case "<":
				condition = new Relation(expression, Relation.RelOp.LESS, parseExpression(t));
				return condition;
			case "<=":
				condition = new Relation(expression, Relation.RelOp.LESSOREQ, parseExpression(t));
				return condition;
			case "=":
				condition = new Relation(expression, Relation.RelOp.EQUAL, parseExpression(t));
				return condition;
			case ">":
				condition = new Relation(expression, Relation.RelOp.GREATER, parseExpression(t));
				return condition;
			case ">=":
				condition = new Relation(expression, Relation.RelOp.GREATEROREQ, parseExpression(t));
				return condition;
			case "!=":
				condition = new Relation(expression, Relation.RelOp.NOTEQUAL, parseExpression(t));
				return condition;
			}
		}
		return new BinaryCondition(null, null, null);

	}
	

	public static Expr parseExpression(Tokenizer t) throws SyntaxError {
		Expr expression = parseTerm(t);
		Expr returnExpression = null;
		while (t.peek().isAddOp()) {
			String addOperator = t.next().toString();
			switch (addOperator) {
			case "+":
				returnExpression = new BinaryExpr(expression, BinaryExpr.MathOp.ADD, parseExpression(t));
				return returnExpression;
			case "-":
				consume(t, TokenType.MINUS);
				returnExpression = new BinaryExpr(expression, BinaryExpr.MathOp.SUBTRACT, parseExpression(t));
				return returnExpression;
			}
		}
		return expression;
	}

	public static Expr parseTerm(Tokenizer t) throws SyntaxError {
		Expr expression = parseFactor(t);
		Expr returnExpression = null;
		while (t.peek().isMulOp()) {
			String mulOperator = t.next().toString();
			switch (mulOperator) {
			case "*":
				returnExpression = new BinaryExpr(expression, BinaryExpr.MathOp.MULTIPLY, parseExpression(t));
				return returnExpression;
			case "/":
				returnExpression = new BinaryExpr(expression, BinaryExpr.MathOp.DIVIDE, parseExpression(t));
				return returnExpression;
			case "mod":
				returnExpression = new BinaryExpr(expression, BinaryExpr.MathOp.MOD, parseExpression(t));
				return returnExpression;
			}
		}
		return expression;
	}

	public static Expr parseFactor(Tokenizer t) throws SyntaxError {
		Expr returnExpression = null;
		Expr expression = null;
		if (t.peek().isNum()) {
			expression = new UnaryExpr(Integer.parseInt(t.next().toString()));
			return expression;
		}
		if (t.peek().isMemSugar()) {
			String testString = t.peek().toString();
			consume(t, t.peek().getType());
			switch (testString) {
			case "MEMSIZE":
				expression = new UnaryExpr(new UnaryExpr(0), UnaryExpr.ExprType.MEMORYVAL);
				break;
			case "DEFENSE":
				expression = new UnaryExpr(new UnaryExpr(1), UnaryExpr.ExprType.MEMORYVAL);
				break;
			case "OFFENSE":
				expression = new UnaryExpr(new UnaryExpr(2), UnaryExpr.ExprType.MEMORYVAL);
				break;
			case "SIZE":
				expression = new UnaryExpr(new UnaryExpr(3), UnaryExpr.ExprType.MEMORYVAL);
				break;
			case "ENERGY":
				expression = new UnaryExpr(new UnaryExpr(4), UnaryExpr.ExprType.MEMORYVAL);
				break;
			case "PASS":
				expression = new UnaryExpr(new UnaryExpr(5), UnaryExpr.ExprType.MEMORYVAL);
				break;
			case "TAG":
				expression = new UnaryExpr(new UnaryExpr(6), UnaryExpr.ExprType.MEMORYVAL);
				break;
			case "POSTURE":
				expression = new UnaryExpr(new UnaryExpr(7), UnaryExpr.ExprType.MEMORYVAL);
				break;
			}
		}
		if (t.peek().isSensor()) {
			String testString = t.peek().toString();
			consume(t, t.peek().getType());
			switch (testString) {
			case "nearby":
				consume(t, TokenType.LBRACKET);
				expression = new Sensor(Sensor.SensorType.NEARBY, parseExpression(t));
				consume(t, TokenType.RBRACKET);
				break;
			case "ahead":
				consume(t, TokenType.LBRACKET);
				expression = new Sensor(Sensor.SensorType.AHEAD, parseExpression(t));
				consume(t, TokenType.RBRACKET);
				break;
			case "random":
				consume(t, TokenType.LBRACKET);
				expression = new Sensor(Sensor.SensorType.RANDOM, parseExpression(t));
				consume(t, TokenType.RBRACKET);
				break;
			case "smell":
				expression = new Sensor();
				break;
			}
		}
		if (t.peek().toString().equals("-")) {
			consume(t, TokenType.MINUS);
			expression = new UnaryExpr(parseFactor(t), UnaryExpr.ExprType.NEGATION);
		}
		if (t.peek().toString().equals("(")) {
			consume(t, TokenType.LPAREN);
			expression = parseExpression(t);
			consume(t, TokenType.RPAREN);
			expression = new UnaryExpr(expression, UnaryExpr.ExprType.EXPRESSION);
			return expression;
		}
		throw new SyntaxError();
		
	}

	// TODO
	// add more as necessary...

	/**
	 * Consumes a token of the expected type.
	 * 
	 * @throws SyntaxError
	 *             if the wrong kind of token is encountered.
	 */
	public static void consume(Tokenizer t, TokenType tt) throws SyntaxError {
		if (t.peek().toString().equals(tt.toString())) {
			t.next();
		}
		else
			throw new SyntaxError();
	}
}
