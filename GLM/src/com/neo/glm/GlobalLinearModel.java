package com.neo.glm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.neo.glm.feature.Feature;
import com.neo.glm.feature.FeatureManager;
import com.neo.glm.feature.History;
import com.neo.glm.feature.TagNGramFeature;
import com.neo.glm.feature.TagWordFeature;
import com.neo.glm.feature.WordPrefixFeature;
import com.neo.glm.feature.WordSuffixFeature;
import com.neo.util.FileUtility;

public class GlobalLinearModel {

	private HashMap<String, Double> featureWeightMap;
	
	private FeatureManager featureManager;
	
	private String startTag;
	private String stopTag;
	private ArrayList<String> tagList;
	
	private int order;

	public GlobalLinearModel(HashMap<String, Double> featureWeightMap,
			FeatureManager featureManager, String startTag, String stopTag,
			ArrayList<String> tagList) {
		this.featureManager = featureManager;
		this.featureWeightMap = featureWeightMap;
		
		this.startTag = startTag;
		this.stopTag = stopTag;
		this.tagList = tagList;
		
		order = featureManager.getOrder();
	}
	
	private static class Data {
		
		public Double score;
		public String previousTag;
		
		public Data(Double score, String previousTag) {
			this.score = score;
			this.previousTag = previousTag;
		}
	}
	
	public ArrayList<String> decode(ArrayList<String> words) {
		ArrayList<HashMap<ArrayList<String>, Data>> mapList =
			new ArrayList<HashMap<ArrayList<String>, Data>>(words.size() + 1);
		
		HashMap<ArrayList<String>, Data> tagDataMap = createSuffixTagDataMap();
		mapList.add(tagDataMap);

		for (int i = 0; i < words.size(); i++) {
			tagDataMap = updateSuffixTagDataMap(tagDataMap, words, i);
			mapList.add(tagDataMap);
		}
		
		return generateTags(mapList, words);
	}
	
	public HashMap<String, Double> getVector(ArrayList<String> words,
			ArrayList<String> tags) {
		HashMap<String, Double> vector = new HashMap<String, Double>();
		
		ArrayList<String> suffixTags = new ArrayList<String>();
		for (int i = 0; i < order; i++) {
			suffixTags.add(startTag);
		}
		History history = new History(suffixTags, words, 0);
		
		for (int i = 0; i < tags.size(); i++) {
			String tag = tags.get(i);
			updateVector(vector, history, tag);
			history.suffixTags = leftShiftTags(history.suffixTags, tag);
			history.index++;
		}
		updateVector(vector, history, stopTag);
		
		return vector;
	}

	private HashMap<ArrayList<String>, Data> createSuffixTagDataMap() {
		HashMap<ArrayList<String>, Data> map =
				new HashMap<ArrayList<String>, Data>();
		
		ArrayList<String> startSuffixTags = new ArrayList<String>();
		for (int i = 0; i < order; i++) {
			startSuffixTags.add(startTag);
		}
		
		Data data = new Data(0.0, startTag);
		
		map.put(startSuffixTags, data);
		
		return map;
	}

	private HashMap<ArrayList<String>, Data> updateSuffixTagDataMap(
			HashMap<ArrayList<String>, Data> map, ArrayList<String> words,
			int index) {
		HashMap<ArrayList<String>, Data> newMap =
				new HashMap<ArrayList<String>, Data>();

		History history = new History(null, words, index);
		for (ArrayList<String> suffixTags : map.keySet()) {
			history.suffixTags = suffixTags;
			double score = map.get(suffixTags).score;
			for (String tag : tagList) {
				double newScore = score + evaluate(history, tag);
				ArrayList<String> newSuffixTags =
						leftShiftTags(suffixTags, tag);
				Data data = newMap.get(newSuffixTags);
				if (data == null || data.score <= newScore) {
					newMap.put(newSuffixTags,
							new Data(newScore, suffixTags.get(0)));
				}
			}
		}

		return newMap;
	}

	private ArrayList<String> generateTags(
			ArrayList<HashMap<ArrayList<String>, Data>> mapList,
			ArrayList<String> words) {
		HashMap<ArrayList<String>, Data> lastMap =
				mapList.get(mapList.size() - 1);
		ArrayList<String> suffixTags = searchBestSuffixTags(lastMap, words);
		
		ArrayList<String> tags = new ArrayList<String>();
		for (int i = mapList.size() - 1; i > 0; i--) {
			tags.add(0, suffixTags.get(suffixTags.size() - 1));
			
			Data data = mapList.get(i).get(suffixTags);
			suffixTags = rightShiftTags(suffixTags, data.previousTag);
		}
		
		return tags;
	}
	
	private ArrayList<String> searchBestSuffixTags(
			HashMap<ArrayList<String>, Data> map, ArrayList<String> words) {
		ArrayList<String> bestSuffixTags = null;
		double bestScore = Double.NEGATIVE_INFINITY;
		for (ArrayList<String> suffixTags : map.keySet()) {
			Data data = map.get(suffixTags);
			double score = data.score;
			History history = new History(suffixTags, words, words.size());
			double newScore = score + evaluate(history, stopTag);
			if (bestScore <= newScore) {
				bestSuffixTags = suffixTags;
				bestScore = newScore;
			}
		}
		return bestSuffixTags;
	}
	
	private double evaluate(History history, String tag) {
		double score = 0.0;
		ArrayList<String> featureStrings = featureManager.extract(history, tag);
		for (String featureString : featureStrings) {
			Double weight = featureWeightMap.get(featureString);
			if (weight != null) {
				score += weight;
			}
		}
		return score;
	}
	
	private void updateVector(HashMap<String, Double> vector,
			History history, String tag) {
		ArrayList<String> featureStrings = featureManager.extract(history, tag);
		for (String featuresString : featureStrings) {
			Double count = vector.get(featuresString);
			if (count == null) {
				vector.put(featuresString, 1.0);
			} else {
				vector.put(featuresString, count + 1.0);
			}
		}
	}

	private ArrayList<String> leftShiftTags(ArrayList<String> tags,
			String tag) {
		ArrayList<String> newTags = new ArrayList<String>();
		for (int i = 1; i < tags.size(); i++) {
			newTags.add(tags.get(i));
		}
		newTags.add(tag);
		return newTags;
	}

	private ArrayList<String> rightShiftTags(ArrayList<String> tags,
			String tag) {
		ArrayList<String> newTags = new ArrayList<String>();
		newTags.add(tag);
		for (int i = 0; i < tags.size() - 1; i++) {
			newTags.add(tags.get(i));
		}
		return newTags;
	}
	
	public static GlobalLinearModel read(String string,
			FeatureManager featureManager, String startTag, String stopTag,
			ArrayList<String> tagList) {
		String lines[] = string.split("\n");
		HashMap<String, Double> featureWeightMap =
				new HashMap<String, Double>();
		for (String line : lines) {
			String elements[] = line.split("\\s+");
			featureWeightMap.put(elements[0], Double.parseDouble(elements[1]));
		}
		return new GlobalLinearModel(featureWeightMap, featureManager,
				startTag, stopTag, tagList);
	}
	
	public static void main(String args[]) {
		String modelString = FileUtility.read("data/tag.model.2");
		
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
		
		GlobalLinearModel model = read(modelString, featureManager,
				startTag, stopTag, tagList);
		
		String testString = FileUtility.read("data/gene.dev");
		String sentences[] = testString.split("\n\n");
		StringBuffer buffer = new StringBuffer();
		int count = 0;
		for (String sentence : sentences) {
			System.out.println("sentence : " + (++count));
			ArrayList<String> words = new ArrayList<String>();
			words.addAll(Arrays.asList(sentence.split("\n")));
			ArrayList<String> tags = model.decode(words);
			for (int i = 0; i < words.size(); i++) {
				buffer.append(words.get(i)).append(" ");
				buffer.append(tags.get(i)).append("\n");
			}
			buffer.append("\n");
		}
		FileUtility.write("data/dev.out", buffer.toString());
	}
}
