package com.lhh.cggf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Iterator;

import com.lhh.cggf.nlp.Parser;
import com.lhh.cggf.nlp.Splitter;

import edu.stanford.nlp.trees.TypedDependency;

public class CorpusParser {

	public static void parse(String inputPath, String outputPath) {
		BufferedReader br = null;
		BufferedWriter bw = null;
		String temp = null;
		try {
			// Open Buffered Reader & Writer
			br = new BufferedReader(new FileReader(inputPath));
			bw = new BufferedWriter(new FileWriter(outputPath));

			// For Each Sentence
			while ((temp = br.readLine()) != null) {
				// Original Sentence
				bw.write(temp + "\n");

				// Part Of Speech
				String[] tokensWithTag = Splitter.splitWithPOS(temp);
				for (int i = 0; i < tokensWithTag.length; i++)
					bw.write(tokensWithTag[i] + " ");
				bw.write("\n");

				// Dependency Grammar
				String[] tokensWithoutTag = Splitter.splitWithoutPOS(temp);
				Collection<TypedDependency> tdl = Parser
						.parseDependency(tokensWithoutTag);
				Iterator<TypedDependency> iter = tdl.iterator();
				while (iter.hasNext()) {
					TypedDependency td = iter.next();
					bw.write((td.gov().index()-1) + "," + (td.dep().index()-1) + ","
							+ td.reln().getShortName() + " ");
				}
				bw.write("\n");
			}

			bw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Corpus Parse Failed!");
			System.exit(-1);
		}
	}

	public static void main(String[] args) throws Exception {
		Splitter.init();

		Parser.init();

		parse("file/Sentence.txt", "file/Parsed.txt");
	}

}
