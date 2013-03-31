package com.lhh.cggf.ne.impl;

import java.util.ArrayList;

import com.lhh.cggf.ne.NamedEntity;
import com.lhh.cggf.nlp.Splitter;

public class Date extends NamedEntity {
	private int year;
	private int month;
	private int day;

	public String getType() {
		return "MyDate";
	}

	public String getContent() {
		String content = "";
		if (year != 0)
			content += year + "年";
		if (month != 0)
			content += month + "月";
		if (day != 0)
			content += day + "日";
		return content;
	}

	public Date(int y, int m, int d, int b, int e) {
		super(b, e);
		year = y;
		month = m;
		day = d;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	public static Date[] extract(String str) {
		ArrayList<Date> array = new ArrayList<Date>();

		String[] words = Splitter.splitWithoutPOS(str);

		// Index Where NE Start
		int startIndex = 0;

		int year, month, day;

		for (int i = 0; i < words.length; i++) {
			String word = words[i];

			year = month = day = 0;

			// Start With Year, Month Or Day?
			if (words[i].matches("[0-9]+年")) { // Year
				year = Integer.parseInt(words[i].substring(0,
						words[i].indexOf("年")));
				if (i + 1 < words.length && words[i + 1].matches("[0-9]+月")) {
					word += words[++i];
					month = Integer.parseInt(words[i].substring(0,
							words[i].indexOf("月")));
					if (i + 1 < words.length && words[i + 1].matches("[0-9]+日")) {
						word += words[++i];
						day = Integer.parseInt(words[i].substring(0,
								words[i].indexOf("日")));
					}
				}

			} else if (words[i].matches("[0-9]+月")) { // Month
				month = Integer.parseInt(words[i].substring(0,
						words[i].indexOf("月")));
				if (i + 1 < words.length && words[i + 1].matches("[0-9]+日")) {
					word += words[++i];
					day = Integer.parseInt(words[i].substring(0,
							words[i].indexOf("日")));
				}
			} else if (words[i].matches("[0-9]+日")) { // Day
				day = Integer.parseInt(words[i].substring(0,
						words[i].indexOf("日")));
			}

			if (year != 0 || month != 0 || day != 0)
				array.add(new Date(year, month, day, startIndex, startIndex
						+ word.length()));

			startIndex += word.length();
		}

		Date[] ds = new Date[array.size()];
		array.toArray(ds);

		return ds;
	}

	public static void main(String[] args) {
		Splitter.init();

		String str = "1111年11月1日,1111年11月,11月1日,1111年,11月,1日";

		Date[] pns = extract(str);

		for (int i = 0; i < pns.length; i++)
			System.out.println(pns[i]);
	}
}
