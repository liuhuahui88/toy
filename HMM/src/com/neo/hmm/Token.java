package com.neo.hmm;

public class Token {
	
	private static final String UNKNOWN_STATUS = "UNKNOWN";

	public String observation;
	public String status;
	
	public Token(String observation, String status) {
		this.observation = observation;
		this.status = status;
	}
	
	public String toString() {
		return "[" + observation + ", " + status + "]";
	}
	
	public static Token read(String string) {
		String elements[] = string.split("\\s+");
		if (elements.length == 1) {
			return new Token(elements[0], UNKNOWN_STATUS);
		} else {
			return new Token(elements[0], elements[1]);
		}
	}
	
	public static String write(Token token) {
		if (token.status.equals(UNKNOWN_STATUS)) {
			return token.observation + "\n";
		} else {
			return token.observation + " " + token.status + "\n";
		}
	}
	
	public static void main(String args[]) {
		String string = "A a";
		Token token = Token.read(string);
		System.out.println(token);
	}
}
