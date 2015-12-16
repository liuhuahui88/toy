package com.neo.bee.operator;

import com.neo.bee.expression.Expression;

public class RPOperator implements Operator {

	@Override
	public void reduce(ObjectStack objectStack) {
		Expression expression;

		if (objectStack.peekObject() == this) {
			objectStack.pop();
		} else {
			throw new IllegalArgumentException("reduce RP failed");
		}

		if (objectStack.peekObject() instanceof Expression) {
			expression = (Expression) objectStack.pop();
		} else {
			throw new IllegalArgumentException("reduce RP failed");
		}

		if (objectStack.peekObject() instanceof LPOperator) {
			objectStack.pop();
		} else {
			throw new IllegalArgumentException("reduce RP failed");
		}

		objectStack.push(expression);
	}
}
