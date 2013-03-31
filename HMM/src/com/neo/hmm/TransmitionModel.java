package com.neo.hmm;

import java.util.HashMap;
import java.util.Set;

public class TransmitionModel {

	private HashMap<String, HashMap<String, Double>> statusObservationProbMap;
	
	public TransmitionModel(
			HashMap<String, HashMap<String, Double>> statusObservationProbMap) {
		this.statusObservationProbMap = statusObservationProbMap;
	}
	
	public Set<String> getStatusSet() {
		return statusObservationProbMap.keySet();
	}
	
	public Double getProb(String status, String observation) {
		HashMap<String, Double> observationProbMap =
			statusObservationProbMap.get(status);
		if (observationProbMap == null) {
			return 0.0;
		}
		Double prob = observationProbMap.get(observation);
		if (prob == null) {
			return 0.0;
		}
		return prob;
	}
	
	public static TransmitionModel read(String string) {
		String lines[] = string.split("\n");
		HashMap<String, HashMap<String, Double>> statusObservationProbMap =
			new HashMap<String, HashMap<String, Double>>();
		HashMap<String, Integer> statusCountMap =
			new HashMap<String, Integer>();
		for (String line : lines) {
			String elements[] = line.split("\\s+");
			if (elements[1].equals("WORDTAG")) {
				Integer count = Integer.parseInt(elements[0]);
				String status = elements[2];
				String observation = elements[3];
				insert(statusObservationProbMap, statusCountMap,
						status, observation, count);
			}
		}
		normalize(statusObservationProbMap, statusCountMap);
		return new TransmitionModel(statusObservationProbMap);
	}
	
	private static void insert(
			HashMap<String, HashMap<String, Double>> statusObservationProbMap,
			HashMap<String, Integer> statusCountMap,
			String status, String word, Integer count) {
		HashMap<String, Double> observationProbMap =
			statusObservationProbMap.get(status);
		if (observationProbMap == null) {
			observationProbMap = new HashMap<String, Double>();
			statusObservationProbMap.put(status, observationProbMap);
		}
		observationProbMap.put(word, new Double(count));
		
		Integer totalCount = statusCountMap.get(status);
		if (totalCount == null) {
			statusCountMap.put(status, count);
		} else {
			statusCountMap.put(status, totalCount + count);
		}
	}
	
	private static void normalize(
			HashMap<String, HashMap<String, Double>> statusObservationProbMap,
			HashMap<String, Integer> statusCountMap) {
		for (String status : statusObservationProbMap.keySet()) {
			HashMap<String, Double> observationProbMap =
				statusObservationProbMap.get(status);
			Integer totalCount = statusCountMap.get(status);
			for (String observation : observationProbMap.keySet()) {
				Double count = observationProbMap.get(observation);
				observationProbMap.put(observation, count / totalCount);
			}
		}
	}
}
