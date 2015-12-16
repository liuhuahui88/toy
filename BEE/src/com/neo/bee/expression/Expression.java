package com.neo.bee.expression;

import java.util.HashMap;

public interface Expression {

	public boolean evaluate(HashMap<String, Boolean> parameterMap);
}
