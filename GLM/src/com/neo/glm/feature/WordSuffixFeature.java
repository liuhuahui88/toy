package com.neo.glm.feature;

public class WordSuffixFeature implements Feature {
	
	private static final String HEADER = "SUFFIX";

	private int length;
	
	public WordSuffixFeature(int length) {
		this.length = length;
	}

	@Override
	public int getOrder() {
		return 0;
	}

	@Override
	public String extract(History history, String tag) {
		if (history.index >= history.words.size()) {
			return null;
		}
		String word = history.words.get(history.index);
		if (length >= word.length()) {
			return null;
		}
		String suffix = word.substring(word.length() - length);
		StringBuffer buffer = new StringBuffer();
		buffer.append(HEADER).append(":");
		buffer.append(suffix).append(":");
		buffer.append(length).append(":");
		buffer.append(tag);
		return buffer.toString();
	}
}
