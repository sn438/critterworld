package parse;

import java.io.Reader;

import ast.Condition;
import ast.Expr;
import ast.Program;
import ast.ProgramImpl;
import ast.Rule;
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
		while (t.hasNext()) {
			if (t.peek().toString().equals("-->")) {
				parseRule(t);
			}
			t.next();
		}
		return new ProgramImpl();
	}

	public static Rule parseRule(Tokenizer t) throws SyntaxError {
		Rule returnRule = new Rule();
		returnRule.setCondition(parseCondition(t));
		return new Rule();
	}

	public static Condition parseCondition(Tokenizer t) throws SyntaxError {
		
		throw new UnsupportedOperationException();
	}

	public static Expr parseExpression(Tokenizer t) throws SyntaxError {
		// TODO
		throw new UnsupportedOperationException();
	}

	public static Expr parseTerm(Tokenizer t) throws SyntaxError {
		// TODO
		throw new UnsupportedOperationException();
	}

	public static Expr parseFactor(Tokenizer t) throws SyntaxError {
		// TODO
		throw new UnsupportedOperationException();
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
