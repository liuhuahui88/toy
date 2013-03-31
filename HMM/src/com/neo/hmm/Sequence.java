package com.neo.hmm;

public class Sequence {

	public Token tokens[];
	
	public Sequence(Token tokens[]) {
		this.tokens = tokens;
	}
	
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < tokens.length; i++) {
			stringBuilder.append(tokens[i].toString()).append(" ");
		}
		return stringBuilder.toString();
	}
	
	public static Sequence read(String string) {
		String lines[] = string.split("\n");
		Token tokens[] = new Token[lines.length];
		for (int i = 0; i < lines.length; i++) {
			tokens[i] = Token.read(lines[i]);
		}
		return new Sequence(tokens);
	}
	
	public static String write(Sequence sequence) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < sequence.tokens.length; i++) {
			stringBuilder.append(Token.write(sequence.tokens[i]));
		}
		stringBuilder.append("\n");
		return stringBuilder.toString();
	}
	
	public static void main(String args[]) {
		String string = "A a\nB b\nC c\n";
		Sequence sequence = Sequence.read(string);
		System.out.println(sequence);
	}
}
