package com.neo.bee.operator;

import java.util.Stack;

public class ObjectStack {

	private Stack<Object> stack;
	private Stack<Operator> operatorStack;

	public ObjectStack() {
		stack = new Stack<Object>();
		operatorStack = new Stack<Operator>();
	}

	public Object pop() {
		Object object = stack.pop();
		if (object instanceof Operator) {
			operatorStack.pop();
		}
		return object;
	}

	public void push(Object object) {
		stack.push(object);
		if (object instanceof Operator) {
			operatorStack.push((Operator) object);
		}
	}

	public Object peekObject() {
		return stack.peek();
	}

	public Operator peekOperator() {
		return operatorStack.peek();
	}

	public boolean hasObject() {
		return !stack.isEmpty();
	}

	public boolean hasOperator() {
		return !operatorStack.isEmpty();
	}

	public int size() {
		return stack.size();
	}
}
