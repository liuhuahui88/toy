package com.lhh.cggf.ne.impl;

import java.util.ArrayList;

import com.lhh.cggf.ne.NamedEntity;
import com.lhh.cggf.nlp.Splitter;

public class Location extends NamedEntity {
	private String loc;

	public String getType() {
		return "Location";
	}

	public String getContent() {
		return loc;
	}

	public Location(String l, int b, int e) {
		super(b, e);
		loc = l;
	}
	
	public String getLocation() {
		return loc;
	}

	public static Location[] extract(String str) {
		ArrayList<Location> array = new ArrayList<Location>();

		String[] tagWords = Splitter.splitWithPOS(str);

		// Index Where NE Start
		int startIndex = 0;

		for (int i = 0; i < tagWords.length; i++) {
			String word = tagWords[i].replaceAll("/.*", "");

			if (tagWords[i].endsWith("/ns")) {
				while (i + 1 < tagWords.length
						&& tagWords[i + 1].endsWith("/ns")) {
					i++;
					word += tagWords[i].replaceAll("/.*", "");
				}
				array.add(new Location(word, startIndex, startIndex
						+ word.length()));
			}

			startIndex += word.length();
		}

		for (int i = 0; i < tagWords.length; i++)
			tagWords[i] = tagWords[i].replaceAll("/.*", "");

		Location[] ls = new Location[array.size()];
		array.toArray(ls);

		return ls;
	}

	public static void main(String[] args) {
		Splitter.init();

		String str = "姓名:刘洋,性别:男,籍贯:吉林长春,出生日期:1962年6月16日,民族:朝鲜族.";

		Location[] ls = extract(str);

		for (int i = 0; i < ls.length; i++)
			System.out.println(ls[i]);
	}
}
