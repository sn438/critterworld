package parse;

import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;

import ast.BinaryCondition;
import ast.BinaryExpr;
import ast.Command;
import ast.Condition;
import ast.Expr;
import ast.Program;
import ast.ProgramImpl;
import ast.Relation;
import ast.Rule;
import ast.Sensor;
import ast.UnaryExpr;
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
		while (t.hasNext()) {
			parseRule(t);
			break;
		}
		return new ProgramImpl();
	}

	public static Rule parseRule(Tokenizer t) throws SyntaxError {
		Condition condition = parseCondition(t);
		return new Rule(condition, new Command(null, null));
	}

	public static Condition parseCondition(Tokenizer t) throws SyntaxError {
		Expr expression = parseExpression(t);
		Condition returnCondition;
		if (t.peek().getType().category() == TokenCategory.RELOP) {
			String relationOperator = t.next().toString();
			switch(relationOperator) {
				case "<":
					
			}
		}
		return new BinaryCondition(null, null, null);
	}

	public static Expr parseExpression(Tokenizer t) throws SyntaxError {
		Expr expression = parseTerm(t);
		Expr returnExpression;
		return expression;
	}

	public static Expr parseTerm(Tokenizer t) throws SyntaxError {
		Expr expression = parseFactor(t);
		Expr returnExpression = null;
		while (t.peek().isAddOp()) {
			String addOperator = t.next().toString();
			switch (addOperator) {
			case "+":
				returnExpression = new BinaryExpr(expression, BinaryExpr.MathOp.ADD, parseFactor(t));
				return returnExpression;
			case "-":
				returnExpression = new BinaryExpr(expression, BinaryExpr.MathOp.SUBTRACT, parseFactor(t));
				return returnExpression;
			}
		}
		return expression;
	}

	public static Expr parseFactor(Tokenizer t) throws SyntaxError {
		
		Expr returnExpression = null;
		Expr expression = null;
		if (!t.peek().isAddOp() && t.peek().getType().category() != TokenCategory.RELOP) {
			if (t.peek().isNum()) {
				expression = new UnaryExpr(Integer.parseInt(t.next().toString()));
			}
			if (t.peek().isMemSugar()) {
				String testString = t.next().toString();
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
				default:
					System.out.println("Please enter a valid cipher type.");
					System.exit(0);
				}
			}
			if (t.peek().isSensor()) {
				String testString = t.next().toString();
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
			while (t.peek().isMulOp()) {
				String mulOperator = t.next().toString();
				switch (mulOperator) {
				case "*":
					returnExpression = new BinaryExpr(expression, BinaryExpr.MathOp.MULTIPLY, parseFactor(t));
					return returnExpression;
				case "/":
					returnExpression = new BinaryExpr(expression, BinaryExpr.MathOp.DIVIDE, parseFactor(t));
					return returnExpression;
				case "mod":
					returnExpression = new BinaryExpr(expression, BinaryExpr.MathOp.MOD, parseFactor(t));
					return returnExpression;
				}
			} 
		}
		return expression;
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
	}
}
