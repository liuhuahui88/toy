package com.lhh.cggf;

import com.lhh.cggf.nlp.Splitter;

public class FeatureExtractor {
	public static String[] extract(String[] tokens, int index1, int index2) {
		// Swap Indexes If Necessary
		int tempIndex;
		if (index1 > index2) {
			tempIndex = index1;
			index1 = index2;
			index2 = tempIndex;
		}

		// Extract Words & POS Tags
		String[] words = new String[tokens.length];
		String[] tags = new String[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			String[] elems = tokens[i].split("/");
			words[i] = elems[0];
			tags[i] = elems[1];
		}

		String[] result = new String[8];

		// index1的词性
		result[0] = tags[index1];

		// index2的词性
		result[1] = tags[index2];

		// index1左边第一个词的词性
		result[2] = (index1 - 1 >= 0) ? tags[index1 - 1] : "null";

		// index2右边第一个词的词性
		result[3] = index2 < tokens.length - 1 ? tags[index2 + 1] : "null";

		// index1和index2之间的词性序列
		if (index1 + 1 == index2)
			result[4] = "null";
		else {
			result[4] = tags[index1 + 1];
			for (int i = index1 + 2; i < index2; i++)
				result[4] += "," + tags[i];
		}

		// index1和index2之间的标点和停用词的个数
		String[] arr = result[4].split(",");
		int puncCount = 0;
		int stopCount = 0;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].startsWith("w"))
				puncCount++;
			if (!arr[i].startsWith("a") && !arr[i].startsWith("n")
					&& !arr[i].startsWith("v") && !arr[i].startsWith("w"))
				stopCount++;
		}
		result[5] = "" + puncCount;
		result[6] = "" + stopCount;

		// index1和index2之间词的个数
		result[7] = "" + (index2 - index1 - 1);

		return result;
	}

	public static void main(String[] args) {
		Splitter.init();

		String sent = "我们,都是好学生。";

		String[] tokens = Splitter.splitWithPOS(sent);
		for (int i = 0; i < tokens.length; i++)
			System.out.print(tokens[i] + " ");
		System.out.println();

		String[] result = extract(tokens, 1, 4);
		for (int i = 0; i < result.length; i++)
			System.out.println(result[i]);
	}

}
