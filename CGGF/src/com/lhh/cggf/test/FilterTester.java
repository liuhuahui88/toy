package com.lhh.cggf.test;

import com.lhh.cggf.filter.Filterable;

// Filter Tester
public class FilterTester {
	public static void test(Filterable t, String filePath) {
		TestCase[] tcs = TestCase.loadFromFile(filePath);

		int t2t = 0; // True To True
		int t2f = 0; // True To False
		int f2t = 0; // False To True
		int f2f = 0; // False To False

		for (int i = 0; i < tcs.length; i++) {
			TestCase tc = tcs[i];
			
			String keyWord = tc.getKeyWord();
			String title = tc.getTitle();
			String snippet = tc.getSnippet();

			String humanFilter = tc.getHumanFilter();

			// Other Human Filter Tags Might Exist
			// Ignore The Corresponding Test Case
			if (!humanFilter.equals("<false>") && !humanFilter.equals("<true>"))
				continue;

			String machineFilter = t.filter(keyWord, title, snippet);

			if (humanFilter.equals("<false>")
					&& machineFilter.equals("<false>"))
				f2f++;
			else if (humanFilter.equals("<false>")
					&& machineFilter.equals("<true>"))
				f2t++;
			else if (humanFilter.equals("<true>")
					&& machineFilter.equals("<false>"))
				t2f++;
			else if (humanFilter.equals("<true>")
					&& machineFilter.equals("<true>"))
				t2t++;
		}

		double precision = 100.0 * t2t / (t2t + f2t);
		double recall = 100.0 * t2t / (t2t + t2f);
		double fmeasure = 2 * precision * recall / (precision + recall);

		System.out.println("True To True: " + t2t);
		System.out.println("True To False: " + t2f);
		System.out.println("False To True: " + f2t);
		System.out.println("False To False: " + f2f);
		System.out.println("Precision: " + precision);
		System.out.println("Recall: " + recall);
		System.out.println("F-Measure: " + fmeasure);
	}
}
