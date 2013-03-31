package com.lhh.cggf;

import com.lhh.cggf.filter.Filterable;
import com.lhh.cggf.filter.impl.ConceptGraphFilter;
import com.lhh.cggf.nlp.Splitter;
import com.lhh.cggf.se.Item;
import com.lhh.cggf.se.Result;
import com.lhh.cggf.se.SearchEngine;
import com.lhh.cggf.se.impl.Sogou;

public class Main {
	public static void execute(Filterable f, SearchEngine se, String keyWord,
			int num) {
		Result result = se.search(keyWord, num);
		Item[] items = result.getItems();
		for (int i = 0; i < items.length; i++) {
			String title = items[i].getTitle();
			String snippet = items[i].getSnippet();
			String mark = f.filter(keyWord, title, snippet);
			System.out.println(mark);
			System.out.println(title);
			System.out.println(snippet);
			System.out.println();
		}
	}

	public static void main(String[] args) {
		Splitter.init();

		Filterable f = new ConceptGraphFilter();

		SearchEngine se = new Sogou();

		String keyWord = "赵本山简历";

		int num = 10;

		execute(f, se, keyWord, num);
	}
}
