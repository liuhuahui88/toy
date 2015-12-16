package com.neo.bee.expression;

import java.util.HashMap;

public class AndExpression implements Expression {

	private Expression firstOperand;
	private Expression secondOperand;

	public AndExpression(Expression firstOperand, Expression secondOperand) {
		this.firstOperand = firstOperand;
		this.secondOperand = secondOperand;
	}

	@Override
	public boolean evaluate(HashMap<String, Boolean> parameterMap) {
		if (!firstOperand.evaluate(parameterMap)) {
			return false;
		} else {
			return secondOperand.evaluate(parameterMap);
		}
	}
}
