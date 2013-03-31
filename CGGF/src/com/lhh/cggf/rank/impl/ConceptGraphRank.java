package com.lhh.cggf.rank.impl;

import com.lhh.cggf.cg.gen.Generator;
import com.lhh.cggf.cg.gen.impl.LimitedGenerator;
import com.lhh.cggf.classifier.Classifier;
import com.lhh.cggf.classifier.impl.NaiveBayesClassifier;
import com.lhh.cggf.nlp.Splitter;
import com.lhh.cggf.rank.Rankable;
import com.lhh.cggf.test.RankerTester;

public class ConceptGraphRank implements Rankable {
	private ConnectionRank ct;
	private NamedEntityRank et;

	public ConceptGraphRank() {
		Classifier clf = NaiveBayesClassifier.loadInstance("d:/stat.txt");
		Generator gen = new LimitedGenerator("d:/cg.txt");
		ct = new ConnectionRank(clf, gen);

		et = new NamedEntityRank(5);
	}

	public double rank(String keyWord, String title, String snippet, double pr) {
		double ctResult = ct.rank(keyWord, title, snippet, pr);
		double etResult = et.rank(keyWord, title, snippet, pr);

		return (ctResult + etResult) / 2;
	}

	public static void main(String[] args) {
		Splitter.init();

		ConceptGraphRank rank = new ConceptGraphRank();

		RankerTester.test(rank, "d:/44.txt");
	}
}
