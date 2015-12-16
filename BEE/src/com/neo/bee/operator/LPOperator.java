package com.neo.bee.operator;

public class LPOperator implements Operator {

	@Override
	public void reduce(ObjectStack objectStack) {
		throw new IllegalArgumentException("reduce LP failed");
	}
}
