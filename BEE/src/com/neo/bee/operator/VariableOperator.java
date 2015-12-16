package com.neo.bee.operator;

import com.neo.bee.expression.Expression;
import com.neo.bee.expression.VariableExpression;

public class VariableOperator implements Operator {

	private String name;

	public VariableOperator(String name) {
		this.name = name;
	}

	@Override
	public void reduce(ObjectStack objectStack) {
		if (objectStack.peekObject() == this) {
			objectStack.pop();
		} else {
			throw new IllegalArgumentException("reduce VARIABLE failed");
		}

		Expression newExpression = new VariableExpression(name);
		objectStack.push(newExpression);
	}
}
