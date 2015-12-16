package com.neo.bee.expression;

import java.util.HashMap;

public class VariableExpression implements Expression {

	private String name;

	public VariableExpression(String name) {
		this.name = name;
	}

	@Override
	public boolean evaluate(HashMap<String, Boolean> parameterMap) {
		return parameterMap.get(name);
	}
}
