package com.lhh.cggf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.lhh.cggf.se.Item;
import com.lhh.cggf.se.Result;
import com.lhh.cggf.se.SearchEngine;
import com.lhh.cggf.se.impl.Sogou;

public class SampleCollector {
	public static void collect(String inputPath, String outputPath,
			SearchEngine se, int num) throws Exception {

		File input = new File(inputPath);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(input)));
		File output = new File(outputPath);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(output)));

		int count = 0;
		String keyWord;
		while ((keyWord = br.readLine()) != null) {
			count++;
			System.out.println("[" + count + "] " + keyWord);
			Result result = se.search(keyWord, num);
			if (result != null) {
				Item[] items = result.getItems();
				for (int j = 0; j < items.length; j++) {
					bw.write(keyWord + "\n");
					bw.write(items[j].getTitle() + "\n");
					bw.write(items[j].getSnippet() + "\n");
					bw.write(items[j].getUrl() + "\n");
					bw.write(((num - j - 1) / ((double) (num - 1))) + "\n");
				}
				bw.flush();
			}
			Thread.sleep(1000 + (long) (2000 * Math.random()));
		}

		br.close();
		bw.flush();
		bw.close();
	}

	public static void main(String[] args) throws Exception {
		String inputPath = "file/KeyWord.txt";
		String outputPath = "file/SearchResult.txt";
		SearchEngine se = new Sogou();
		int num = 10;

		collect(inputPath, outputPath, se, num);
	}
}
