package com.lhh.simseg.seg.impl;

import java.util.ArrayList;

import com.lhh.simseg.dic.Dictionary;
import com.lhh.simseg.seg.Segmenter;

public class ForwardMinSegmenter implements Segmenter {
	private Dictionary dic;

	public ForwardMinSegmenter(Dictionary dic) {
		this.dic = dic;
	}

	public String[] handle(String str) {
		if (str == null)
			return null;

		ArrayList<String> array = new ArrayList<String>();

		int index = 0;
		int strLength = str.length();

		while (index < strLength) {
			boolean found = false;

			String word = null;

			for (int wordLength = 2; wordLength <= 4; wordLength++) {
				if (index + wordLength > strLength)
					continue;

				word = str.substring(index, index + wordLength);
				if (dic.lookup(word)) {
					found = true;
					array.add(word);
					index += wordLength;
					break;
				}
			}

			if (!found) {
				word = str.substring(index, index + 1);
				array.add(word);
				index++;
			}
		}

		String[] ret = new String[array.size()];
		array.toArray(ret);
		return ret;
	}
}
