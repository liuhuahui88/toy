package com.neo.glm.feature;

public class WordPrefixFeature implements Feature {
	
	private static final String HEADER = "PREFIX";
	
	private int length;
	
	public WordPrefixFeature(int length) {
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
		String prefix = word.substring(0, length);
		StringBuffer buffer = new StringBuffer();
		buffer.append(HEADER).append(":");
		buffer.append(prefix).append(":");
		buffer.append(length).append(":");
		buffer.append(tag);
		return buffer.toString();
	}
}
