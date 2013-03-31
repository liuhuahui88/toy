package com.lhh.simseg.seg.impl;

import java.util.ArrayList;

import com.lhh.simseg.dic.Dictionary;
import com.lhh.simseg.seg.Segmenter;

public class BackwardMaxSegmenter implements Segmenter {
	private Dictionary dic;

	public BackwardMaxSegmenter(Dictionary dic) {
		this.dic = dic;
	}

	public String[] handle(String str) {
		if (str == null)
			return null;

		ArrayList<String> array = new ArrayList<String>();

		int index = str.length();

		while (index > 0) {
			boolean found = false;

			String word = null;

			for (int wordLength = 4; wordLength >= 2; wordLength--) {
				if (index - wordLength < 0)
					continue;

				word = str.substring(index - wordLength, index);
				if (dic.lookup(word)) {
					found = true;
					array.add(word);
					index -= wordLength;
					break;
				}
			}

			if (!found) {
				word = str.substring(index - 1, index);
				array.add(word);
				index--;
			}
		}

		String[] ret = new String[array.size()];
		for (int i = 0; i < ret.length; i++)
			ret[i] = array.get(ret.length - i - 1);
		return ret;
	}
}
