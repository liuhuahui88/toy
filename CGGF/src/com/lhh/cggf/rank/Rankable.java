package com.lhh.cggf.rank;

public interface Rankable {
	public double rank(String keyWord, String title, String snippet, double pagerank);
}
