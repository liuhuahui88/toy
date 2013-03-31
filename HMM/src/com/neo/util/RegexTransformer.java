package com.neo.util;

public class RegexTransformer implements Transformer {
	
	private static final String NUMERIC = "_NUMERIC_";
	private static final String ALL_CAPITALS = "_ALL_CAPITALS_";
	private static final String LAST_CAPITAL = "_LAST_CAPITAL_";

	public String transform(String word) {
		if (word.matches(".*\\d+.*")) {
			return NUMERIC;
		} else if (word.matches("[A-Z]+")) {
			return ALL_CAPITALS;
		} else if (word.matches(".*[A-Z]+")) {
			return LAST_CAPITAL;
		} else {
			return word;
		}
	}
}
