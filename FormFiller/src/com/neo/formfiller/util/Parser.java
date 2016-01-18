package com.neo.formfiller.util;

public class Parser {

	private String content;
	private int index;
	
	public Parser(String content) {
		this.content = content;
		index = 0;
	}
	
	public String next(String prefix, String suffix) {
		if (!chechAndMark(index)) {
			return null;
		}

		int start = content.indexOf(prefix, index);
		if (!chechAndMark(start)) {
			return null;
		}
		start += prefix.length();
		
		int end = content.indexOf(suffix, start);
		if (!chechAndMark(end)) {
			return null;
		}
		
		index = end + suffix.length();
		
		return content.substring(start, end); 
	}
	
	private boolean chechAndMark(int n) {
		if (n < 0 || n >= content.length()) {
			index = content.length();
			return false;
		}
		return true;
	}
	
	public static void main(String args[]) {
		Parser parser = new Parser("<a><bb><ccc>");
		String element = null;
		while ((element = parser.next("<", ">")) != null) {
			System.out.println(element);
		}
	}
}
