package com.neo.glm.feature;

import java.util.ArrayList;

public class TagNGramFeature implements Feature {

	private String header;
	private int n;
	
	public TagNGramFeature(String header, int n) {
		this.header = header;
		this.n = n;
	}
	
	@Override
	public int getOrder() {
		return n - 1;
	}

	@Override
	public String extract(History history, String tag) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(header).append(":");
		ArrayList<String> tokens = history.suffixTags;
		int offset = tokens.size() - n + 1;
		for (int i = 0; i < n - 1; i++) {
			buffer.append(tokens.get(i + offset)).append(":");
		}
		buffer.append(tag);
		return buffer.toString();
	}
}
