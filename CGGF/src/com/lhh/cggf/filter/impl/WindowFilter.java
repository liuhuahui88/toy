package com.lhh.cggf.filter.impl;

import com.lhh.cggf.algorithm.PatternDistribution;
import com.lhh.cggf.filter.Filterable;
import com.lhh.cggf.nlp.Splitter;
import com.lhh.cggf.test.FilterTester;

// Window Filter
// Use Window Detection Algorithm
// WindowSize = KeyWordLength * ReleaseFactor
public class WindowFilter implements Filterable {
	private int releaseFactor;

	public WindowFilter(int rf) {
		releaseFactor = rf;
	}

	public String filter(String keyWord, String title, String snippet) {
		String str = title + snippet;

		String[] strArray = Splitter.splitWithoutPOS(str);
		String[] keyWordArray = Splitter.splitWithoutPOS(keyWord);

		int width = PatternDistribution.calc(strArray, keyWordArray);

		if (width < 0)
			return "<false>";
		else if (width <= keyWordArray.length * releaseFactor)
			return "<true>";
		else
			return "<false>";
	}

	public static void main(String[] args) {
		Splitter.init();

		WindowFilter filter = new WindowFilter(3);

		FilterTester.test(filter, "d:/44.txt");
	}

}
