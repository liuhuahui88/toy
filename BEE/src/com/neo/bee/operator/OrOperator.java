package com.neo.bee.operator;

import com.neo.bee.expression.Expression;
import com.neo.bee.expression.OrExpression;

public class OrOperator implements Operator {

	@Override
	public void reduce(ObjectStack objectStack) {
		Expression firstExpression;
		Expression secondExpression;

		if (objectStack.peekObject() instanceof Expression) {
			secondExpression = (Expression) objectStack.pop();
		} else {
			throw new IllegalArgumentException("reduce OR failed");
		}

		if (objectStack.peekObject() == this) {
			objectStack.pop();
		} else {
			throw new IllegalArgumentException("reduce OR failed");
		}

		if (objectStack.peekObject() instanceof Expression) {
			firstExpression = (Expression) objectStack.pop();
		} else {
			throw new IllegalArgumentException("reduce OR failed");
		}

		Expression newExpression = new OrExpression(firstExpression,
				secondExpression);
		objectStack.push(newExpression);
	}
}
