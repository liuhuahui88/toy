package com.lhh.cggf.test;

import com.lhh.cggf.algorithm.PearsonDistance;
import com.lhh.cggf.rank.Rankable;

// Ranker Tester
public class RankerTester {
	public static void test(Rankable t, String filePath) {
		TestCase[] tcs = TestCase.loadFromFile(filePath);

		double[] human = new double[tcs.length];
		double[] machine = new double[tcs.length];

		for (int i = 0; i < tcs.length; i++) {
			TestCase tc = tcs[i];
			
			String keyWord = tc.getKeyWord();
			String title = tc.getTitle();
			String snippet = tc.getSnippet();
			double pagerank = tc.getPagerank();
			
			double humanRank = tc.getHumanRank();
			
			double machineRank = t.rank(keyWord, title, snippet, pagerank);
			
			human[i] = humanRank;
			machine[i] = machineRank;
		}

		System.out.println("Pearson Distance: " + PearsonDistance.calc(human, machine));
	}
}
