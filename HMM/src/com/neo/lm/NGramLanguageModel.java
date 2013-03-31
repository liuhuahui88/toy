package com.neo.lm;

import java.util.HashMap;
import java.util.Set;

public class NGramLanguageModel {
	
	private int n;
	private HashMap<Phrase, HashMap<String, Double>> phraseWordProbMap;
	
	public NGramLanguageModel(int n,
			HashMap<Phrase, HashMap<String, Double>> phraseWordProbMap) {
		this.n = n;
		this.phraseWordProbMap = phraseWordProbMap;
	}
	
	public int getN() {
		return n;
	}
	
	public Set<Phrase> getPhraseSet() {
		return phraseWordProbMap.keySet();
	}
	
	public HashMap<String, Double> getWordProbMap(Phrase phrase) {
		return phraseWordProbMap.get(phrase);
	}
	
	public Double getProb(Phrase phrase, String word) {
		HashMap<String, Double> wordProbMap = phraseWordProbMap.get(phrase);
		if (wordProbMap == null) {
			return 0.0;
		}
		Double prob = wordProbMap.get(word);
		if (prob == null) {
			return 0.0;
		}
		return prob;
	}
	
	public static NGramLanguageModel read(String string, int n) {
		String lines[] = string.split("\n");
		HashMap<Phrase, HashMap<String, Double>> phraseWordProbMap =
			new HashMap<Phrase, HashMap<String, Double>>();
		HashMap<Phrase, Integer> phraseCountMap =
			new HashMap<Phrase, Integer>();
		for (String line : lines) {
			String elements[] = line.split("\\s+");
			if (elements[1].equals(n + "-GRAM")) {
				Integer count = Integer.parseInt(elements[0]);
				String words[] = new String[n - 1];
				for (int i = 0; i < n - 1; i++) {
					words[i] = elements[2 + i];
				}
				Phrase phrase = new Phrase(words);
				String word = elements[2 + n - 1];
				insert(phraseWordProbMap, phraseCountMap, phrase, word, count);
			}
		}
		normalize(phraseWordProbMap, phraseCountMap);
		return new NGramLanguageModel(n, phraseWordProbMap);
	}
	
	private static void insert(
			HashMap<Phrase, HashMap<String, Double>> phraseWordProbMap,
			HashMap<Phrase, Integer> phraseCountMap,
			Phrase phrase, String word, Integer count) {
		HashMap<String, Double> wordProbMap = phraseWordProbMap.get(phrase);
		if (wordProbMap == null) {
			wordProbMap = new HashMap<String, Double>();
			phraseWordProbMap.put(phrase, wordProbMap);
		}
		wordProbMap.put(word, new Double(count));
		
		Integer totalCount = phraseCountMap.get(phrase);
		if (totalCount == null) {
			phraseCountMap.put(phrase, count);
		} else {
			phraseCountMap.put(phrase, totalCount + count);
		}
	}
	
	private static void normalize(
			HashMap<Phrase, HashMap<String, Double>> phraseWordProbMap,
			HashMap<Phrase, Integer> phraseCountMap) {
		for (Phrase phrase : phraseWordProbMap.keySet()) {
			HashMap<String, Double> wordProbMap = phraseWordProbMap.get(phrase);
			Integer totalCount = phraseCountMap.get(phrase);
			for (String word : wordProbMap.keySet()) {
				Double count = wordProbMap.get(word);
				wordProbMap.put(word, count / totalCount);
			}
		}
	}
}
