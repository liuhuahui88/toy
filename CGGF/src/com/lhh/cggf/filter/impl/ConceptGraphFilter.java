package com.lhh.cggf.filter.impl;

import com.lhh.cggf.cg.gen.Generator;
import com.lhh.cggf.cg.gen.impl.LimitedGenerator;
import com.lhh.cggf.classifier.Classifier;
import com.lhh.cggf.classifier.impl.NaiveBayesClassifier;
import com.lhh.cggf.filter.Filterable;
import com.lhh.cggf.nlp.Splitter;
import com.lhh.cggf.test.FilterTester;

public class ConceptGraphFilter implements Filterable {
	private ConnectionFilter ct;
	private NamedEntityFilter et;

	public ConceptGraphFilter() {
		Classifier clf = NaiveBayesClassifier.loadInstance("d:/stat.txt");
		Generator gen = new LimitedGenerator("d:/cg.txt");
		ct = new ConnectionFilter(clf, gen);

		et = new NamedEntityFilter(3, 3);
	}

	public String filter(String keyWord, String title, String snippet) {
		String ctString = ct.filter(keyWord, title, snippet);
		boolean ctResult = ctString.equals("<true>");

		String etString = et.filter(keyWord, title, snippet);
		boolean etResult = etString.equals("<true>");

		return (ctResult && etResult) ? "<true>" : "<false>";
	}

	public static void main(String[] args) {
		Splitter.init();

		ConceptGraphFilter filter = new ConceptGraphFilter();

		FilterTester.test(filter, "d:/44.txt");
	}
}
