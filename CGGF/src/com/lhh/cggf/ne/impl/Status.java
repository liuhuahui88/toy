package com.lhh.cggf.ne.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

import com.lhh.cggf.ne.NamedEntity;
import com.lhh.cggf.nlp.Splitter;

public class Status extends NamedEntity {
	private static HashSet<String> statusSet;
	static {
		statusSet = new HashSet<String>();
		String inputPath = "身份.txt";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(inputPath)));
			String temp;
			while ((temp = br.readLine()) != null)
				statusSet.add(temp);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String stat;

	public String getType() {
		return "Status";
	}

	public String getContent() {
		return stat;
	}

	public Status(String s, int b, int e) {
		super(b, e);
		stat = s;
	}

	public String getStatus() {
		return stat;
	}
	
	public static Status[] extract(String str) {
		ArrayList<Status> array = new ArrayList<Status>();

		String[] words = Splitter.splitWithoutPOS(str);

		// Index Where NE Start
		int startIndex = 0;

		for (int i = 0; i < words.length; i++) {
			String word = words[i];

			if (statusSet.contains(words[i]))
				array.add(new Status(word, startIndex, startIndex
						+ word.length()));

			startIndex += word.length();
		}

		Status[] ss = new Status[array.size()];
		array.toArray(ss);

		return ss;
	}

	public static void main(String[] args) {
		Splitter.init();

		String str = "历任武汉军区首长秘书,武汉军区司令部军训部参谋.";

		Status[] ss = extract(str);

		for (int i = 0; i < ss.length; i++)
			System.out.println(ss[i]);
	}
}
