package com.neo.bee;

import java.util.ArrayList;

public class Lexicalizer {

	public static final String AND = "&";
	public static final String OR = "|";
	public static final String NOT = "!";
	public static final String LP = "(";
	public static final String RP = ")";

	public static final char AND_CHAR = '&';
	public static final char OR_CHAR = '|';
	public static final char NOT_CHAR = '!';
	public static final char LP_CHAR = '(';
	public static final char RP_CHAR = ')';

	public static ArrayList<String> lexicalize(String string) {
		ArrayList<String> lexicals = new ArrayList<String>();
		char chars[] = string.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (Character.isWhitespace(chars[i])) {
				continue;
			} else if (Character.isLetter(chars[i]) ||
					Character.isDigit(chars[i])) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append(chars[i]);
				while ((i + 1) < chars.length &&
						(Character.isLetter(chars[i + 1]) ||
						Character.isDigit(chars[i + 1]))) {
					stringBuilder.append(chars[i + 1]);
					i++;
				}
				lexicals.add(stringBuilder.toString());
			} else if (chars[i] == AND_CHAR) {
				lexicals.add(AND);
			} else if (chars[i] == OR_CHAR) {
				lexicals.add(OR);
			} else if (chars[i] == NOT_CHAR) {
				lexicals.add(NOT);
			} else if (chars[i] == LP_CHAR) {
				lexicals.add(LP);
			} else if (chars[i] == RP_CHAR) {
				lexicals.add(RP);
			} else {
				return null;
			}
		}
		return lexicals;
	}
}
