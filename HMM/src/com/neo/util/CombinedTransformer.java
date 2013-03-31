package com.neo.util;

public class CombinedTransformer implements Transformer {

	private MapTransformer mapTransformer;
	private RegexTransformer regexTransformer;
	
	public CombinedTransformer(MapTransformer mapTransformer,
			RegexTransformer regexTransformer) {
		this.mapTransformer = mapTransformer;
		this.regexTransformer = regexTransformer;
	}

	public String transform(String word) {
		String mapTransformedWord = mapTransformer.transform(word);
		if (mapTransformedWord.equals(word)) {
			return mapTransformedWord;
		}
		String regexTransformedWord = regexTransformer.transform(word);
		if (regexTransformedWord.equals(word)) {
			return mapTransformedWord;
		} else {
			return regexTransformedWord;
		}
	}
	
	public static CombinedTransformer read(String string) {
		MapTransformer mapTransformer = MapTransformer.read(string);
		RegexTransformer regexTransformer = new RegexTransformer();
		return new CombinedTransformer(mapTransformer, regexTransformer);
	}
}
