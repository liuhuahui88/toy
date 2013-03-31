package com.lhh.cggf.ne.impl;

import java.util.ArrayList;

import com.lhh.cggf.ne.NamedEntity;
import com.lhh.cggf.nlp.Splitter;

public class Name extends NamedEntity {
	private String name;

	public String getType() {
		return "Name";
	}

	public String getContent() {
		return name;
	}

	public Name(String n, int b, int e) {
		super(b, e);
		name = n;
	}
	
	public String getName() {
		return name;
	}

	public static Name[] extract(String str) {
		ArrayList<Name> array = new ArrayList<Name>();

		String[] tagWords = Splitter.splitWithPOS(str);

		// Index Where NE Start
		int startIndex = 0;

		for (int i = 0; i < tagWords.length; i++) {
			String word = tagWords[i].replaceAll("/.*", "");

			if (tagWords[i].endsWith("/nr"))
				array.add(new Name(word, startIndex, startIndex + word.length()));

			startIndex += word.length();
		}

		Name[] ns = new Name[array.size()];
		array.toArray(ns);

		return ns;
	}

	public static void main(String[] args) {
		Splitter.init();

		String str = "姓名:博古,性别:男.";

		Name[] ns = extract(str);

		for (int i = 0; i < ns.length; i++)
			System.out.println(ns[i]);
	}
}
