package com.lhh.cggf.test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import com.lhh.cggf.nlp.Preprocessor;

public class TestCase {
	private String humanFilter;
	private double humanRank;

	private String keyWord;
	private String title;
	private String snippet;
	private double pagerank;

	// Load Test Case From File
	public static TestCase[] loadFromFile(String filePath) {
		ArrayList<TestCase> tcArray = new ArrayList<TestCase>();

		BufferedReader br = null;

		String humanFilter = null;
		double humanRank = 0.0;
		String keyWord = null;
		String title = null;
		String snippet = null;
		double pagerank = 0.0;

		// File Format:
		// <Human Filter>
		// <Human Rank>
		// <Key Word>
		// <Title>
		// <Snippet>
		// <URL>
		// <Page Rank>
		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((humanFilter = br.readLine()) != null) {
				humanRank = Double.parseDouble(br.readLine());
				keyWord = Preprocessor.strip(br.readLine());
				title = Preprocessor.strip(br.readLine());
				snippet = Preprocessor.strip(br.readLine());
				br.readLine(); // Discard URL
				pagerank = Double.parseDouble(br.readLine());
				tcArray.add(new TestCase(humanFilter, humanRank, keyWord,
						title, snippet, pagerank));
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed To Load Test Case File!");
			System.exit(1);
		}

		TestCase[] result = new TestCase[tcArray.size()];
		tcArray.toArray(result);
		return result;
	}

	public TestCase(String humanFilter, double humanRank, String keyWord,
			String title, String snippet, double pagerank) {
		this.humanFilter = humanFilter;
		this.humanRank = humanRank;

		this.keyWord = keyWord;
		this.title = title;
		this.snippet = snippet;
		this.pagerank = pagerank;
	}

	public String getHumanFilter() {
		return humanFilter;
	}

	public void setHumanFilter(String humanFilter) {
		this.humanFilter = humanFilter;
	}

	public double getHumanRank() {
		return humanRank;
	}

	public void setHumanRank(double humanRank) {
		this.humanRank = humanRank;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
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

	public double getPagerank() {
		return pagerank;
	}

	public void setPagerank(double pagerank) {
		this.pagerank = pagerank;
	}
}
