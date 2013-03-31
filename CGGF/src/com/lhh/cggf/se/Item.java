package com.lhh.cggf.se;

public class Item {
	private String title;
	private String snippet;
	private String url;

	public Item(String t, String s, String u) {
		title = t;
		snippet = s;
		url = u;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String toString() {
		String str = "Title: " + title + "\n";
		str += "Snippet: " + snippet + "\n";
		str += "Url: " + url;
		return str;
	}
}
