package com.neo.bee.operator;

import java.util.HashMap;

public class OperatorComparator {

	private static final int BIGGER = 1;
	private static final int SMALLER = -1;

	private HashMap<Class<? extends Operator>,
			HashMap<Class<? extends Operator>, Integer>> map;

	public OperatorComparator() {
		map = new HashMap<Class<? extends Operator>,
				HashMap<Class<? extends Operator>, Integer>>();
		map.put(AndOperator.class, createAndMap());
		map.put(OrOperator.class, createOrMap());
		map.put(NotOperator.class, createNotMap());
		map.put(LPOperator.class, createLPMap());
		map.put(RPOperator.class, createRPMap());
		map.put(VariableOperator.class, createVariableMap());
		map.put(TermOperator.class, createTermMap());
	}

	public Integer compare(Operator firstOperator, Operator secondOperator) {
		Class<? extends Operator> firstOperatorClass = firstOperator.getClass();
		Class<? extends Operator> secondOperatorClass = secondOperator
				.getClass();
		return map.get(firstOperatorClass).get(secondOperatorClass);
	}

	public HashMap<Class<? extends Operator>, Integer> createAndMap() {
		HashMap<Class<? extends Operator>, Integer> andMap =
				new HashMap<Class<? extends Operator>, Integer>();
		andMap.put(AndOperator.class, BIGGER);
		andMap.put(OrOperator.class, BIGGER);
		andMap.put(NotOperator.class, SMALLER);
		andMap.put(LPOperator.class, SMALLER);
		andMap.put(RPOperator.class, BIGGER);
		andMap.put(VariableOperator.class, SMALLER);
		andMap.put(TermOperator.class, BIGGER);
		return andMap;
	}

	public HashMap<Class<? extends Operator>, Integer> createOrMap() {
		HashMap<Class<? extends Operator>, Integer> orMap =
				new HashMap<Class<? extends Operator>, Integer>();
		orMap.put(AndOperator.class, SMALLER);
		orMap.put(OrOperator.class, BIGGER);
		orMap.put(NotOperator.class, SMALLER);
		orMap.put(LPOperator.class, SMALLER);
		orMap.put(RPOperator.class, BIGGER);
		orMap.put(VariableOperator.class, SMALLER);
		orMap.put(TermOperator.class, BIGGER);
		return orMap;
	}

	public HashMap<Class<? extends Operator>, Integer> createNotMap() {
		HashMap<Class<? extends Operator>, Integer> notMap =
				new HashMap<Class<? extends Operator>, Integer>();
		notMap.put(AndOperator.class, BIGGER);
		notMap.put(OrOperator.class, BIGGER);
		// relation <Not, Not> undefined
		notMap.put(LPOperator.class, SMALLER);
		notMap.put(RPOperator.class, BIGGER);
		notMap.put(VariableOperator.class, SMALLER);
		notMap.put(TermOperator.class, BIGGER);
		return notMap;
	}

	public HashMap<Class<? extends Operator>, Integer> createLPMap() {
		HashMap<Class<? extends Operator>, Integer> lpMap =
				new HashMap<Class<? extends Operator>, Integer>();
		lpMap.put(AndOperator.class, SMALLER);
		lpMap.put(OrOperator.class, SMALLER);
		lpMap.put(NotOperator.class, SMALLER);
		lpMap.put(LPOperator.class, SMALLER);
		lpMap.put(RPOperator.class, SMALLER);
		lpMap.put(VariableOperator.class, SMALLER);
		// relation <LP, Term> undefined
		return lpMap;
	}

	public HashMap<Class<? extends Operator>, Integer> createRPMap() {
		HashMap<Class<? extends Operator>, Integer> rpMap =
				new HashMap<Class<? extends Operator>, Integer>();
		rpMap.put(AndOperator.class, BIGGER);
		rpMap.put(OrOperator.class, BIGGER);
		// relation <RP, Not> undefined
		// relation <RP, LP> undefined
		rpMap.put(RPOperator.class, BIGGER);
		// relation <RP, Variable> undefined
		rpMap.put(TermOperator.class, BIGGER);
		return rpMap;
	}

	public HashMap<Class<? extends Operator>, Integer> createVariableMap() {
		HashMap<Class<? extends Operator>, Integer> variableMap =
				new HashMap<Class<? extends Operator>, Integer>();
		variableMap.put(AndOperator.class, BIGGER);
		variableMap.put(OrOperator.class, BIGGER);
		// relation <Variable, Not> undefined
		// relation <Variable, LP> undefined
		variableMap.put(RPOperator.class, BIGGER);
		// relation <Variable, Variable> undefined
		variableMap.put(TermOperator.class, BIGGER);
		return variableMap;
	}

	public HashMap<Class<? extends Operator>, Integer> createTermMap() {
		HashMap<Class<? extends Operator>, Integer> termMap =
				new HashMap<Class<? extends Operator>, Integer>();
		termMap.put(AndOperator.class, SMALLER);
		termMap.put(OrOperator.class, SMALLER);
		termMap.put(NotOperator.class, SMALLER);
		termMap.put(LPOperator.class, SMALLER);
		// relation <Term, RP> undefined
		termMap.put(VariableOperator.class, SMALLER);
		termMap.put(TermOperator.class, SMALLER);
		return termMap;
	}
}
