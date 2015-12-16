package com.neo.bee.operator;

import com.neo.bee.expression.Expression;

public class TermOperator implements Operator {

	@Override
	public void reduce(ObjectStack objectStack) {
		Expression expression;

		if (objectStack.peekObject() == this) {
			objectStack.pop();
		} else {
			throw new IllegalArgumentException("reduce TERM failed");
		}

		if (objectStack.peekObject() instanceof Expression) {
			expression = (Expression) objectStack.pop();
		} else {
			throw new IllegalArgumentException("reduce TERM failed");
		}

		if (objectStack.peekObject() instanceof TermOperator) {
			objectStack.pop();
		} else {
			throw new IllegalArgumentException("reduce TERM failed");
		}

		objectStack.push(expression);
	}
}
