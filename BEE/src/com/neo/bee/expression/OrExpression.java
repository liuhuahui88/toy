package com.neo.bee.expression;

import java.util.HashMap;

public class OrExpression implements Expression {

	private Expression firstOperand;
	private Expression secondOperand;

	public OrExpression(Expression firstOperand, Expression secondOperand) {
		this.firstOperand = firstOperand;
		this.secondOperand = secondOperand;
	}

	@Override
	public boolean evaluate(HashMap<String, Boolean> parameterMap) {
		if (firstOperand.evaluate(parameterMap)) {
			return true;
		} else {
			return secondOperand.evaluate(parameterMap);
		}
	}

}
