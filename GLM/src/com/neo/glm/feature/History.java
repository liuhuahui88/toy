package com.neo.glm.feature;

import java.util.ArrayList;

public class History {
	
	public ArrayList<String> suffixTags;
	public ArrayList<String> words;
	public int index;
	
	public History(ArrayList<String> tags, ArrayList<String> words, int index) {
		this.suffixTags = tags;
		this.words = words;
		this.index = index;
	}
	
	public int getLength() {
		return suffixTags.size();
	}
}
