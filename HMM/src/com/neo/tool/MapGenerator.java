package com.neo.tool;

import java.io.File;
import java.util.HashMap;

import com.neo.util.FileUtility;

public class MapGenerator {
	
	private static final File INPUT_FILE = new File("data/gene.counts");
	private static final File OUTPUT_FILE = new File("data/gene.map");
	
	private static final String RARE_WORD = "_RARE_";
	
	private static final int COUNT_THRESHOLD = 5;

	public static void main(String args[]) {
		HashMap<String, Integer> wordCountMap = read();
		generate(wordCountMap);
	}
	
	private static HashMap<String, Integer> read() {
		String content = FileUtility.read(INPUT_FILE);
		
		HashMap<String, Integer> wordCountMap = new HashMap<String, Integer>();
		
		String lines[] = content.split("\n");
		for (String line : lines) {
			String elements[] = line.split("\\s+");
			if (elements[1].equals("WORDTAG")) {
				Integer count = Integer.parseInt(elements[0]);
				String word = elements[3];

				Integer totalCount = wordCountMap.get(word);
				if (totalCount == null) {
					wordCountMap.put(word, count);
				} else {
					wordCountMap.put(word, totalCount + count);
				}
			}
		}
		
		return wordCountMap;
	}
	
	private static void generate(HashMap<String, Integer> wordCountMap) {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append(RARE_WORD).append("\n");
		
		for (String word : wordCountMap.keySet()) {
			Integer count = wordCountMap.get(word);
			if (count < COUNT_THRESHOLD) {
				stringBuilder.append(word).append(" ");
				stringBuilder.append(RARE_WORD).append("\n");
			} else {
				stringBuilder.append(word).append(" ");
				stringBuilder.append(word).append("\n");
			}
		}
		
		FileUtility.write(OUTPUT_FILE, stringBuilder.toString());
	}
}
