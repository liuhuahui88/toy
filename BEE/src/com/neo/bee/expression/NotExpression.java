package com.neo.bee.expression;

import java.util.HashMap;

public class NotExpression implements Expression {

	private Expression operand;

	public NotExpression(Expression operand) {
		this.operand = operand;
	}

	@Override
	public boolean evaluate(HashMap<String, Boolean> parameterMap) {
		return !operand.evaluate(parameterMap);
	}
}
