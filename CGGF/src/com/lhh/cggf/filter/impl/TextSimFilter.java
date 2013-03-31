package com.lhh.cggf.filter.impl;

import java.util.Arrays;

import com.lhh.cggf.algorithm.SequenceSimilarity;
import com.lhh.cggf.filter.Filterable;
import com.lhh.cggf.nlp.Splitter;
import com.lhh.cggf.test.FilterTester;
import com.lhh.cggf.test.TestCase;

// String Similarity Filter
// Use KNN Algorithm And Edit Distance Algorithm
public class TextSimFilter implements Filterable {
	private class DataWrappper implements Comparable<DataWrappper> {
		private String mark;
		private int sim;

		public DataWrappper(String mark, int sim) {
			this.mark = mark;
			this.sim = sim;
		}

		public int compareTo(DataWrappper other) {
			if (sim > other.sim)
				return -1;
			else if (sim < other.sim)
				return 1;
			else
				return 0;
		}
	}

	private TestCase[] examples;
	private int radius;

	public TextSimFilter(String filePath, int radius) {
		examples = TestCase.loadFromFile(filePath);
		this.radius = radius;
	}

	public String filter(String keyWord, String title, String snippet) {
		String str = title + snippet;

		DataWrappper[] dws = new DataWrappper[examples.length];

		for (int i = 0; i < examples.length; i++) {
			String exTitle = examples[i].getTitle();
			String exSnippet = examples[i].getSnippet();
			String exStr = exTitle + exSnippet;

			int sim = SequenceSimilarity.LCS(str, exStr);

			String exMark = examples[i].getHumanFilter();

			dws[i] = new DataWrappper(exMark, sim);
		}

		// Sort DWS So That Most Similar One Comes First
		Arrays.sort(dws);

		int trueCount = 0;
		int falseCount = 0;
		for (int i = 0; i < dws.length && (trueCount + falseCount) < radius; i++)
			if (dws[i].mark.equals("<true>"))
				trueCount += 1;
			else
				falseCount += 1;

		return (trueCount > falseCount) ? "<true>" : "<false>";
	}

	public static void main(String[] args) {
		Splitter.init();

		TextSimFilter filter = new TextSimFilter("d:/11.txt", 1);

		FilterTester.test(filter, "d:/44.txt");
	}
}
