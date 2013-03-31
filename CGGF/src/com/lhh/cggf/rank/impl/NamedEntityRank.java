package com.lhh.cggf.rank.impl;

import java.util.HashSet;

import com.lhh.cggf.ne.NamedEntity;
import com.lhh.cggf.nlp.Extractor;
import com.lhh.cggf.nlp.Splitter;
import com.lhh.cggf.rank.Rankable;
import com.lhh.cggf.test.RankerTester;

// Named Entity Rank
// The More Types, The Higher Rank
public class NamedEntityRank implements Rankable {
	private int typeNumber;

	public NamedEntityRank(int tn) {
		typeNumber = tn;
	}

	public double rank(String keyWord, String title, String snippet, double pr) {
		HashSet<String> typeSet = new HashSet<String>();

		String str = title + snippet;
		NamedEntity[] nes = Extractor.extract(str);

		for (int i = 0; i < nes.length; i++)
			if (!nes[i].getType().equals("Token"))
				typeSet.add(nes[i].getType());

		return ((double) typeSet.size()) / typeNumber;
	}

	public static void main(String[] args) {
		Splitter.init();

		NamedEntityRank rank = new NamedEntityRank(5);

		RankerTester.test(rank, "d:/44.txt");
	}

}
