package com.neo.glm.feature;

import java.util.ArrayList;

public class FeatureManager {
	
	private ArrayList<Feature> features;
	
	public FeatureManager(ArrayList<Feature> features) {
		this.features = features;
	}
	
	public int getOrder() {
		int maxOrder = 1;
		for (Feature feature : features) {
			int order = feature.getOrder();
			if (maxOrder < order) {
				maxOrder = order;
			}
		}
		return maxOrder;
	}
	
	public ArrayList<String> extract(History history, String tag) {
		ArrayList<String> featureStrings = new ArrayList<String>();
		for (Feature feature : features) {
			String featureString = feature.extract(history, tag);
			if (featureString != null) {
				featureStrings.add(featureString);
			}
		}
		return featureStrings;
	}
	
	public static void main(String args[]) {
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("O");

		ArrayList<String> words = new ArrayList<String>();
		words.add("a");
		words.add("b");
		words.add("c");
		
		int index = 1;
		
		History history = new History(tags, words, index);
		
		Feature tagWordFeature = new TagWordFeature();
		System.out.println(tagWordFeature.extract(history, "O"));
		System.out.println(tagWordFeature.extract(history, "G"));
		
		Feature tagNGramFeature = new TagNGramFeature("T_BIGRAM", 2);
		System.out.println(tagNGramFeature.extract(history, "O"));
		System.out.println(tagNGramFeature.extract(history, "G"));
	}
}
