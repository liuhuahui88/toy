package com.neo.lm;

public class Phrase {
	
	public String words[];
	
	public Phrase(String words[]) {
		this.words = words;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		} else if (object == this) {
			return true;
		} else if (object.getClass() != getClass()) {
			return false;
		} else {
			Phrase anotherPhrase = (Phrase) object;
			if (anotherPhrase.words.length != words.length) {
				return false;
			} else {
				for (int i = 0; i < words.length; i++) {
					if (!words[i].equals(anotherPhrase.words[i])) {
						return false;
					}
				}
				return true;
			}
		}
	}

	@Override
	public int hashCode() {
		int hashCode = 1;
		for (String word : words) {
			hashCode = (hashCode + word.hashCode()) * 13;
		}
		return hashCode;
	}
	
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("[");
		if (words.length != 0) {
			stringBuilder.append(words[0]);
			for (int i = 1; i < words.length; i++) {
				stringBuilder.append(", ").append(words[i]);
			}
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
}