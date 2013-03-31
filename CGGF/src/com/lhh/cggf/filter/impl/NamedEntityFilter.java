package com.lhh.cggf.filter.impl;

import java.util.HashSet;

import com.lhh.cggf.filter.Filterable;
import com.lhh.cggf.ne.NamedEntity;
import com.lhh.cggf.nlp.Extractor;
import com.lhh.cggf.nlp.Splitter;
import com.lhh.cggf.test.FilterTester;

// Named Entity Filter
// The More Types And Instances, The Bigger Chance To Survive
public class NamedEntityFilter implements Filterable {
	private int typeCountThreshold;
	private int instanceCountThreshold;

	public NamedEntityFilter(int tct, int ict) {
		typeCountThreshold = tct;
		instanceCountThreshold = ict;
	}

	public String filter(String keyWord, String title, String snippet) {
		String str = title + snippet;

		HashSet<String> typeSet = new HashSet<String>();
		HashSet<String> instanceSet = new HashSet<String>();

		NamedEntity[] nes = Extractor.extract(str);
		for (int i = 0; i < nes.length; i++)
			if (!nes[i].getType().equals("Token")) {
				typeSet.add(nes[i].getType());
				instanceSet.add(nes[i].getContent());
			}

		if (instanceSet.size() >= instanceCountThreshold
				&& typeSet.size() >= typeCountThreshold)
			return "<true>";
		else
			return "<false>";
	}

	public static void main(String[] args) {
		Splitter.init();

		NamedEntityFilter filter = new NamedEntityFilter(3, 3);

		FilterTester.test(filter, "d:/44.txt");
	}

}
