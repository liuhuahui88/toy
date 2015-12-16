package com.neo.bee.operator;

import com.neo.bee.expression.AndExpression;
import com.neo.bee.expression.Expression;

public class AndOperator implements Operator {

	@Override
	public void reduce(ObjectStack objectStack) {
		Expression firstExpression;
		Expression secondExpression;

		if (objectStack.peekObject() instanceof Expression) {
			secondExpression = (Expression) objectStack.pop();
		} else {
			throw new IllegalArgumentException("reduce AND failed");
		}

		if (objectStack.peekObject() == this) {
			objectStack.pop();
		} else {
			throw new IllegalArgumentException("reduce AND failed");
		}

		if (objectStack.peekObject() instanceof Expression) {
			firstExpression = (Expression) objectStack.pop();
		} else {
			throw new IllegalArgumentException("reduce AND failed");
		}

		Expression newExpression = new AndExpression(firstExpression,
				secondExpression);
		objectStack.push(newExpression);
	}
}
