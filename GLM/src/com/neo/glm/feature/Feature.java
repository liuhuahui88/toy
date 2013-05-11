package com.neo.glm.feature;

public interface Feature {
	
	public int getOrder();

	public String extract(History history, String tag);
}
