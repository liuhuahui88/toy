package com.neo.hmm;

import java.util.ArrayList;
import java.util.HashMap;

import com.neo.lm.NGramLanguageModel;
import com.neo.lm.Phrase;
import com.neo.util.Transformer;

public class HighOrderHMM implements HMM {

	private String startStatus;
	private String stopStatus;
	private NGramLanguageModel nGramLanguageModel;

	private TransmitionModel transmitionModel;
	private Transformer transformer;
	
	private int order;
	
	public HighOrderHMM(String startStatus, String stopStatus,
			NGramLanguageModel nGramLanguageModel,
			TransmitionModel transmitionModel,
			Transformer transformer) {
		this.startStatus = startStatus;
		this.stopStatus = stopStatus;
		this.nGramLanguageModel = nGramLanguageModel;
		this.transmitionModel = transmitionModel;
		this.transformer = transformer;
		
		order = nGramLanguageModel.getN() - 1;
	}
	
	private static class Data {
		
		public Double logProb;
		public String previousStatus;
		
		public Data(Double logProb, String previousStatus) {
			this.logProb = logProb;
			this.previousStatus = previousStatus;
		}
	}
	
	public int getOrder() {
		return order;
	}

	public double decode(Sequence sequence) {
		ArrayList<HashMap<Phrase, Data>> mapList =
			new ArrayList<HashMap<Phrase, Data>>(sequence.tokens.length + 1);

		HashMap<Phrase, Data> suffixPhraseDataMap = createSuffixPhraseDataMap();
		mapList.add(suffixPhraseDataMap);
		
		for (Token token : sequence.tokens) {
			suffixPhraseDataMap = updateSuffixPhraseDataMap(
					suffixPhraseDataMap, token.observation);
			mapList.add(suffixPhraseDataMap);
		}
		
		double logProb = updateSequenceStatus(sequence, mapList);
		
		return logProb;
	}
	
	private HashMap<Phrase, Data> createSuffixPhraseDataMap() {
		HashMap<Phrase, Data> suffixPhraseStepMap = new HashMap<Phrase, Data>();

		String startWords[] = new String[order];
		for (int i = 0; i < startWords.length; i++) {
			startWords[i] = startStatus;
		}
		Phrase startPhrase = new Phrase(startWords);
		
		Data step = new Data(Math.log(1.0), startStatus);
		
		suffixPhraseStepMap.put(startPhrase, step);
		
		return suffixPhraseStepMap;
	}
	
	private HashMap<Phrase, Data> updateSuffixPhraseDataMap(
			HashMap<Phrase, Data> suffixPhraseStepMap, String observation) {
		HashMap<Phrase, Data> newSuffixPhraseStepMap =
			new HashMap<Phrase, Data>();
		
		String transformedObservation = transformer.transform(observation);
		
		for (Phrase suffixPhrase : suffixPhraseStepMap.keySet()) {
			String newPreviousStatus = suffixPhrase.words[0];
			Data step = suffixPhraseStepMap.get(suffixPhrase);
			double logProb = step.logProb;
			
			HashMap<String, Double> wordProbMap =
				nGramLanguageModel.getWordProbMap(suffixPhrase);
			if (wordProbMap == null) {
				continue;
			}
			
			for (String word : wordProbMap.keySet()) {
				
				Phrase newSuffixPhrase = leftShiftPhrase(suffixPhrase, word);
				double newLogProb = logProb + calculateLogSubProb(wordProbMap,
						word, transformedObservation);
				
				Data newStep = newSuffixPhraseStepMap.get(newSuffixPhrase);
				if (newStep == null || newStep.logProb < newLogProb) {
					newStep = new Data(newLogProb, newPreviousStatus);
					newSuffixPhraseStepMap.put(newSuffixPhrase, newStep);
				}
			}
		}

		return newSuffixPhraseStepMap;
	}
	
	private double updateSequenceStatus(Sequence sequence,
			ArrayList<HashMap<Phrase, Data>> mapList) {
		HashMap<Phrase, Data> lastMap = mapList.get(mapList.size() - 1);
		Phrase suffixPhrase = searchBestSuffixPhrase(lastMap);
		double logProb = lastMap.get(suffixPhrase).logProb;

		for (int i = mapList.size() - 1; i > 0; i--) {
			String lastWordInSuffixPhrase =
				suffixPhrase.words[suffixPhrase.words.length - 1];
			sequence.tokens[i - 1].status = lastWordInSuffixPhrase;
			
			Data data = mapList.get(i).get(suffixPhrase);
			suffixPhrase = rightShiftPhrase(suffixPhrase, data.previousStatus);
		}

		return logProb;
	}
	
	private Phrase leftShiftPhrase(Phrase phrase, String word) {
		String words[] = new String[phrase.words.length];
		for (int i = 0; i < words.length - 1; i++) {
			words[i] = phrase.words[i + 1];
		}
		words[words.length - 1] = word;

		return new Phrase(words);
	}
	
	private Phrase rightShiftPhrase(Phrase phrase, String word) {
		String words[] = new String[phrase.words.length];
		words[0] = word;
		for (int i = 1; i < words.length; i++) {
			words[i] = phrase.words[i - 1];
		}
		
		return new Phrase(words);
	}
	
	private double calculateLogSubProb(HashMap<String, Double> wordProbMap,
			String word, String observation) {
		double statusProb = wordProbMap.get(word);

		double observationProb = transmitionModel.getProb(word, observation);

		double subProb = statusProb * observationProb;

		return Math.log(subProb);
	}
	
	private Phrase searchBestSuffixPhrase(
			HashMap<Phrase, Data> suffixPhraseStepMap) {
		Phrase bestSuffixPhrase = null;
		double bestLogProb = Double.NEGATIVE_INFINITY;
		for (Phrase phrase : suffixPhraseStepMap.keySet()) {
			Data data = suffixPhraseStepMap.get(phrase);
			double logProb = data.logProb;
			double newLogProb = logProb + Math.log(
					nGramLanguageModel.getProb(phrase, stopStatus));
			if (newLogProb > bestLogProb) {
				bestSuffixPhrase = phrase;
				bestLogProb = newLogProb;
			}
		}
		return bestSuffixPhrase;
	}
}
