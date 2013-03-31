package com.lhh.cggf.ne.impl;

import com.lhh.cggf.ne.NamedEntity;
import com.lhh.cggf.nlp.Splitter;

public class Token extends NamedEntity {
	private String token;

	public String getType() {
		return "Token";
	}

	public String getContent() {
		return token;
	}

	public Token(String t, int b, int e) {
		super(b, e);
		token = t;
	}
	
	public String getToken() {
		return token;
	}

	public static Token[] extract(String str) {
		String[] words = Splitter.splitWithoutPOS(str);
		Token[] ts = new Token[words.length];
		int sum = 0;
		for (int i = 0; i < ts.length; i++) {
			String t = words[i];
			int b = sum;
			int e = sum + words[i].length();
			ts[i] = new Token(t, b, e);
			sum += words[i].length();
		}

		return ts;
	}

	public static void main(String[] args) {
		Splitter.init();

		String str = "肖捷,男,汉族,1957年6月出生,辽宁开原人.";

		Token[] ts = extract(str);

		for (int i = 0; i < ts.length; i++)
			System.out.println(ts[i]);
	}
}
