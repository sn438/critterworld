package parse;

import java.io.Reader;
import java.util.LinkedList;

import ast.*;
import ast.Action.ActType;
import ast.BinaryCondition.Operator;
import ast.UnaryExpr.ExprType;
import exceptions.SyntaxError;

class ParserImpl implements Parser {
	/** tokens is the Tokenizer that contains all the tokens from which the AST is built. */
	private Tokenizer tokens;
	private Program programAST;

	@Override
	public Program parse(Reader r) {
		tokens = new Tokenizer(r);
		try {
			programAST = parseProgram(tokens);
		}
		catch (SyntaxError e) {
			programAST = null;
		}

		return programAST;
	}

	/**
	 * Parses a program from the stream of tokens provided by the Tokenizer,
	 * consuming tokens representing the program. All following methods with a name
	 * "parseX" have the same spec except that they parse syntactic form X.
	 * 
	 * @return the created AST
	 * @throws SyntaxError if there the input tokens have invalid syntax
	 */
	public static ProgramImpl parseProgram(Tokenizer t) throws SyntaxError {
		LinkedList<Rule> RuleList = new LinkedList<Rule>();
		while (t.hasNext()) {
			Rule r = parseRule(t);
			RuleList.add(r);
			
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
		if(t.peek().getType() == TokenType.MEM) {
			consume(t, TokenType.MEM);
			consume(t, TokenType.LBRACKET);
			Expr index = parseExpression(t);
			consume(t, TokenType.RBRACKET);
			consume(t, TokenType.ASSIGN);
			Expr val = parseExpression(t);
			return new Update(index, val);
		}
		if(t.peek().isMemSugar()) {
			String testString = t.peek().toString();
			consume(t, t.peek().getType());
			int index = 0;
			switch (testString) {
			case "MEMSIZE":
				index = 0;
				break;
			case "DEFENSE":
				index = 1;
				break;
			case "OFFENSE":
				index = 2;
				break;
			case "SIZE":
				index = 3;
				break;
			case "ENERGY":
				index = 4;
				break;
			case "PASS":
				index = 5;
				break;
			case "TAG":
				index = 6;
				break;
			case "POSTURE":
				index = 7;
				break;
			}
			consume(t, TokenType.ASSIGN);
			Expr val = parseExpression(t);
			return new Update(new UnaryExpr(index), val);
		}
		throw new SyntaxError();
	}
	
	public static Action parseAction(Tokenizer t) throws SyntaxError {
		Action result = null;
		String type = t.peek().toString();
		consume(t, t.peek().getType());
		switch(type) {
			case "wait":
				result = new Action(ActType.WAIT);
				break;
			case "forward":
				result = new Action(ActType.FORWARD);
				break;
			case "backward":
				result = new Action(ActType.BACKWARD);
				break;
			case "left":
				result = new Action(ActType.LEFT);
				break;
			case "right":
				result = new Action(ActType.RIGHT);
				break;
			case "eat":
				result = new Action(ActType.EAT);
				break;
			case "attack":
				result = new Action(ActType.ATTACK);
				break;
			case "grow":
				result = new Action(ActType.GROW);
				break;
			case "bud":
				result = new Action(ActType.BUD);
				break;
			case "mate":
				result = new Action(ActType.MATE);
				break;
			case "tag":
				consume(t, TokenType.LBRACKET);
				result = new Action(ActType.TAG, parseExpression(t));
				consume(t, TokenType.RBRACKET);
				break;
			case "serve":
				consume(t, TokenType.LBRACKET);
				result = new Action(ActType.SERVE, parseExpression(t));
				consume(t, TokenType.RBRACKET);
				break;
		}
		
		if(result == null)
			throw new SyntaxError();
		return result;
	}
	
	public static Condition parseCondition(Tokenizer t) throws SyntaxError {
		Condition conj = parseConjunction(t);
		while (t.peek().getType() == TokenType.OR) {
			consume(t, TokenType.OR);
			return new BinaryCondition(conj, Operator.OR, parseConjunction(t));
		}
		return conj;
	}
	
	public static Condition parseConjunction(Tokenizer t) throws SyntaxError {
		Relation rel = parseRelation(t);
		while (t.peek().getType() == TokenType.AND) {
			consume(t, TokenType.AND);
			return new BinaryCondition(rel, Operator.AND, parseRelation(t));
		}
		return rel;
	}
	public static Relation parseRelation(Tokenizer t) throws SyntaxError {
		Relation cond = null;
		
		if(t.peek().getType() == TokenType.LBRACE) {
			consume(t, TokenType.LBRACE);
			cond = new Relation(parseCondition(t));
			consume(t, TokenType.RBRACE);
			return cond;
		}
		
		Expr expression = parseExpression(t);
		if (t.peek().getType().category() == TokenCategory.RELOP) {
			String relationOperator = t.peek().toString();
			consume(t, t.peek().getType());
			switch (relationOperator) {
			case "<":
				return new Relation(expression, Relation.RelOp.LESS, parseExpression(t));
			case "<=":
				return new Relation(expression, Relation.RelOp.LESSOREQ, parseExpression(t));
			case "=":
				return new Relation(expression, Relation.RelOp.EQUAL, parseExpression(t));
			case ">":
				return new Relation(expression, Relation.RelOp.GREATER, parseExpression(t));
			case ">=":
				return new Relation(expression, Relation.RelOp.GREATEROREQ, parseExpression(t));
			case "!=":
				return new Relation(expression, Relation.RelOp.NOTEQUAL, parseExpression(t));
			}
		}
		throw new SyntaxError();
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
				//consume(t, TokenType.MINUS);
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
		Expr expression = null;
		if (t.peek().isNum()) {
			expression = new UnaryExpr(Integer.parseInt(t.next().toString()));
			return expression;
		}
		if(t.peek().getType() == TokenType.MEM) {
			consume(t, TokenType.MEM);
			consume(t, TokenType.LBRACKET);
			expression = new UnaryExpr(parseExpression(t), ExprType.MEMORYVAL);
			consume(t, TokenType.RBRACKET);
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
		if(expression == null)
			throw new SyntaxError();
		return expression;
	}


	/**
	 * Consumes a token of the expected type.
	 * @throws SyntaxError if the wrong kind of token is encountered.
	 */
	public static void consume(Tokenizer t, TokenType tt) throws SyntaxError {
		if (t.peek().toString().equals(tt.toString())) {
			t.next();
		}
		else
			throw new SyntaxError();
	}
}
