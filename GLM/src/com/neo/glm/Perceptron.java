package com.neo.glm;

import java.util.ArrayList;
import java.util.HashMap;

import com.neo.glm.feature.Feature;
import com.neo.glm.feature.FeatureManager;
import com.neo.glm.feature.TagNGramFeature;
import com.neo.glm.feature.TagWordFeature;
import com.neo.glm.feature.WordPrefixFeature;
import com.neo.glm.feature.WordSuffixFeature;
import com.neo.util.FileUtility;

public class Perceptron {
	
	private static class Instance {
		
		public ArrayList<String> tags;
		public ArrayList<String> words;
		
		public Instance(ArrayList<String> tags, ArrayList<String> words) {
			this.tags = tags;
			this.words = words;
		}
	}

	public static void main(String[] args) {
		int round = 5;
		
		String instanceString = FileUtility.read("data/gene.train");
		ArrayList<Instance> instances = readInstance(instanceString);
		
		HashMap<String, Double> featureWeightMap =
				new HashMap<String, Double>();
		GlobalLinearModel model = createModel(featureWeightMap);
		
		for (int i = 0; i < round; i++) {
			System.out.println("round : " + (i + 1));
			for (Instance instance : instances) {
				train(instance, model, featureWeightMap);
			}
		}
		
		String weightString = writeWeight(featureWeightMap);
		FileUtility.write("data/tag.model.2", weightString);
	}

	private static ArrayList<Instance> readInstance(String string) {
		ArrayList<Instance> instances = new ArrayList<Instance>();
		String sentences[] = string.split("\n\n");
		for (String sentence : sentences) {
			ArrayList<String> tags = new ArrayList<String>();
			ArrayList<String> words = new ArrayList<String>();
			String entries[] = sentence.split("\n");
			for (String entry : entries) {
				String elements[] = entry.split("\\s+");
				tags.add(elements[1]);
				words.add(elements[0]);
			}
			instances.add(new Instance(tags, words));
		}
		return instances;
	}
	
	private static GlobalLinearModel createModel(
			HashMap<String, Double> featureWeightMap) {
		ArrayList<Feature> features = new ArrayList<Feature>();
		features.add(new TagWordFeature());
		features.add(new TagNGramFeature("TRIGRAM", 3));
		features.add(new WordSuffixFeature(1));
		features.add(new WordSuffixFeature(2));
		features.add(new WordSuffixFeature(3));
		features.add(new WordPrefixFeature(1));
		features.add(new WordPrefixFeature(2));
		features.add(new WordPrefixFeature(3));
		FeatureManager featureManager = new FeatureManager(features);
		
		String startTag = "*";
		String stopTag = "STOP";
		ArrayList<String> tagList = new ArrayList<String>();
		tagList.add("O");
		tagList.add("I-GENE");
		
		GlobalLinearModel model = new GlobalLinearModel(featureWeightMap,
				featureManager,	startTag, stopTag, tagList);
		
		return model;
	}
	
	private static void train(Instance instance, GlobalLinearModel model,
			HashMap<String, Double> featureWeightMap) {
			ArrayList<String> tags = model.decode(instance.words);
		HashMap<String, Double> actualVector =
				model.getVector(instance.words, tags);
		HashMap<String, Double> expectedVector =
				model.getVector(instance.words, instance.tags);
		merge(featureWeightMap, actualVector, -1);
		merge(featureWeightMap, expectedVector, 1);
	}
	
	private static void merge(HashMap<String, Double> a,
			HashMap<String, Double> b, int sign) {
		for (String key : b.keySet()) {
			Double aValue = a.get(key);
			Double bValue = b.get(key);
			if (aValue == null) {
				a.put(key, sign * bValue);
			} else {
				a.put(key, aValue + sign * bValue);
			}
		}
	}

	private static String writeWeight(
			HashMap<String, Double> featureWeightMap) {
		StringBuffer buffer = new StringBuffer();
		for (String featureString : featureWeightMap.keySet()) {
			buffer.append(featureString).append(" ");
			buffer.append(featureWeightMap.get(featureString)).append("\n");
		}
		return buffer.toString();
	}
}
