package com.neo.bee;

import java.util.ArrayList;

import com.neo.bee.expression.Expression;
import com.neo.bee.operator.AndOperator;
import com.neo.bee.operator.LPOperator;
import com.neo.bee.operator.NotOperator;
import com.neo.bee.operator.ObjectStack;
import com.neo.bee.operator.Operator;
import com.neo.bee.operator.OperatorComparator;
import com.neo.bee.operator.OrOperator;
import com.neo.bee.operator.RPOperator;
import com.neo.bee.operator.TermOperator;
import com.neo.bee.operator.VariableOperator;

public class Parser {

	private static final String TERM = "$";

	private static final OperatorComparator COMPARATOR =
			new OperatorComparator();

	public static Expression parse(String string) {
		ArrayList<String> lexicals = Lexicalizer.lexicalize(string);
		if (lexicals == null) {
			return null;
		}

		TermOperator termOperator = new TermOperator();
		ObjectStack objectStack = new ObjectStack();
		objectStack.push(termOperator);
		for (String lexical : lexicals) {
			Operator operator = createOperator(lexical);
			reduce(objectStack, operator);
		}
		reduce(objectStack, termOperator);
		Expression expression = extract(objectStack);
		return expression;
	}

	private static Operator createOperator(String lexical) {
		if (lexical.equals(Lexicalizer.AND)) {
			return new AndOperator();
		} else if (lexical.equals(Lexicalizer.OR)) {
			return new OrOperator();
		} else if (lexical.equals(Lexicalizer.NOT)) {
			return new NotOperator();
		} else if (lexical.equals(Lexicalizer.LP)) {
			return new LPOperator();
		} else if (lexical.equals(Lexicalizer.RP)) {
			return new RPOperator();
		} else if (lexical.equals(TERM)) {
			return new TermOperator();
		} else {
			return new VariableOperator(lexical);
		}
	}

	private static void reduce(ObjectStack objectStack, Operator operator) {
		while (objectStack.hasOperator()) {
			Operator topOperator = objectStack.peekOperator();
			Integer comparison = COMPARATOR.compare(topOperator, operator);
			if (comparison == null) {
				throw new IllegalArgumentException("reduce failed");
			}
			if (comparison <= 0) {
				break;
			}
			topOperator.reduce(objectStack);
		}
		objectStack.push(operator);
	}

	private static Expression extract(ObjectStack objectStack) {
		Operator operator = objectStack.peekOperator();
		operator.reduce(objectStack);
		if (objectStack.size() == 1) {
			return (Expression) objectStack.pop();
		} else {
			throw new IllegalArgumentException("extract failed");
		}
	}
}
