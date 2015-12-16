package com.neo.bee.operator;

import com.neo.bee.expression.Expression;
import com.neo.bee.expression.NotExpression;

public class NotOperator implements Operator {

	@Override
	public void reduce(ObjectStack objectStack) {
		Expression expression;

		if (objectStack.peekObject() instanceof Expression) {
			expression = (Expression) objectStack.pop();
		} else {
			throw new IllegalArgumentException("reduce NOT failed");
		}

		if (objectStack.peekObject() == this) {
			objectStack.pop();
		} else {
			throw new IllegalArgumentException("reduce NOT failed");
		}

		Expression newExpression = new NotExpression(expression);
		objectStack.push(newExpression);
	}
}
