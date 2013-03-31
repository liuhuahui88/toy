package com.lhh.cggf.ne.impl;

import java.util.ArrayList;

import com.lhh.cggf.ne.NamedEntity;
import com.lhh.cggf.nlp.Splitter;

public class Gender extends NamedEntity {
	private String gender;

	public String getType() {
		return "Gender";
	}

	public String getContent() {
		return gender;
	}

	public Gender(String g, int b, int e) {
		super(b, e);
		gender = g;
	}

	public String getGender() {
		return gender;
	}

	public static Gender[] extract(String str) {
		ArrayList<Gender> array = new ArrayList<Gender>();

		String[] words = Splitter.splitWithoutPOS(str);

		// Index Where NE Start
		int startIndex = 0;

		for (int i = 0; i < words.length; i++) {
			String word = words[i];

			if (words[i].matches("男性?"))
				array.add(new Gender("男", startIndex, startIndex
						+ word.length()));
			else if (words[i].matches("女性?"))
				array.add(new Gender("女", startIndex, startIndex
						+ word.length()));

			startIndex += word.length();
		}

		Gender[] gs = new Gender[array.size()];
		array.toArray(gs);

		return gs;
	}

	public static void main(String[] args) {
		Splitter.init();

		String str = "1953年3月生,男,汉族,山东荣成人.";

		Gender[] gs = extract(str);

		for (int i = 0; i < gs.length; i++)
			System.out.println(gs[i]);
	}
}
