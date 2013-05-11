package com.neo.glm.feature;

public class TagWordFeature implements Feature {
	
	private static final String HEADER = "TAG";
	
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
		StringBuffer buffer = new StringBuffer();
		buffer.append(HEADER).append(":");
		buffer.append(word).append(":");
		buffer.append(tag);
		return buffer.toString();
	}
}
