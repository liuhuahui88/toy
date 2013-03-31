package com.neo.util;

import java.util.HashMap;

public class MapTransformer implements Transformer {
	
	private HashMap<String, String> map;
	private String defaultValue;
	
	public MapTransformer(HashMap<String, String> map, String defaultValue) {
		this.map = map;
		this.defaultValue = defaultValue;
	}

	public String transform(String word) {
		if (map.containsKey(word)) {
			return map.get(word);
		} else {
			return defaultValue;
		}
	}
	
	public static MapTransformer read(String string) {
		String lines[] = string.split("\n");
		HashMap<String, String> map = new HashMap<String, String>();
		String defaultValue = lines[0];
		for (int i = 1; i < lines.length; i++) {
			String elements[] = lines[i].split("\\s+");
			map.put(elements[0], elements[1]);
		}
		return new MapTransformer(map, defaultValue);
	}
}
