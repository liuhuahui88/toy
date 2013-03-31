package com.lhh.cggf.filter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.lhh.cggf.FeatureExtractor;
import com.lhh.cggf.cg.Arc;
import com.lhh.cggf.cg.Graph;
import com.lhh.cggf.cg.Node;
import com.lhh.cggf.cg.gen.Generator;
import com.lhh.cggf.cg.gen.impl.LimitedGenerator;
import com.lhh.cggf.classifier.Classifier;
import com.lhh.cggf.classifier.impl.NaiveBayesClassifier;
import com.lhh.cggf.filter.Filterable;
import com.lhh.cggf.nlp.Splitter;
import com.lhh.cggf.test.FilterTester;

public class ConnectionFilter implements Filterable {
	private Classifier clf;
	private Generator gen;

	public ConnectionFilter(Classifier clf, Generator gen) {
		this.clf = clf;
		this.gen = gen;
	}

	private String filter(String keyWord, String str) {
		String[] strArrayWithTag = Splitter.splitWithPOS(str);
		String[] strArrayWithoutTag = Splitter.splitWithoutPOS(str);

		Graph g = gen.generate(keyWord);
		if (g == null) {
			System.out.println("No CG for \"" + keyWord + "\"");
			return "<false>";
		}
		Node[] nodes = g.getAllNode();
		Arc[] arcs = g.getAllArc();

		Graph tg = new Graph();
		HashSet<String> set = new HashSet<String>();
		for (int i = 0; i < nodes.length; i++) {
			set.add(nodes[i].getName());
		}
		HashMap<String, ArrayList<Integer>> map = new HashMap<String, ArrayList<Integer>>();
		Iterator<String> iter = set.iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			map.put(key, new ArrayList<Integer>());
		}
		for (int i = 0; i < strArrayWithoutTag.length; i++)
			if (set.contains(strArrayWithoutTag[i])) {
				ArrayList<Integer> array = map.get(strArrayWithoutTag[i]);
				array.add(new Integer(i));
				map.put(strArrayWithoutTag[i], array);
				tg.addNode(new Node(i, strArrayWithoutTag[i]));
			}
		for (int i = 0; i < arcs.length; i++) {
			ArrayList<Integer> array1 = map.get(arcs[i].getFrom().getName());
			ArrayList<Integer> array2 = map.get(arcs[i].getTo().getName());
			int index1, index2;
			for (int j = 0; j < array1.size(); j++) {
				index1 = array1.get(j).intValue();
				for (int k = 0; k < array2.size(); k++) {
					index2 = array2.get(k).intValue();
					String[] feature = FeatureExtractor.extract(
							strArrayWithTag, index1, index2);
					if (clf.classify(feature).equals("pos")) {
						Node n1 = new Node(index1, strArrayWithoutTag[index1]);
						Node n2 = new Node(index2, strArrayWithoutTag[index2]);
						tg.addArc(new Arc("unknown", n1, n2));
						tg.addArc(new Arc("unknown", n2, n1));
					}
				}
			}
		}

		if (g.isCoveredBy(tg))
			return "<true>";
		else
			return "<false>";
	}

	public String filter(String keyWord, String title, String snippet) {
		if (filter(keyWord, title).equals("<true>"))
			return "<true>";
		else if (filter(keyWord, snippet).equals("<true>"))
			return "<true>";
		else
			return "<false>";
	}

	public static void main(String[] args) {
		Splitter.init();

		Classifier clf = NaiveBayesClassifier.loadInstance("d:/stat.txt");
		Generator gen = new LimitedGenerator("d:/cg.txt");
		ConnectionFilter test = new ConnectionFilter(clf, gen);

		FilterTester.test(test, "d:/44.txt");
	}

}
