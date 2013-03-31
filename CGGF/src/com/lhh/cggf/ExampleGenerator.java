package com.lhh.cggf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class ExampleGenerator {

	public static void generate(String inputPath, String outputPath) {
		String temp = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			// Open Buffered Reader & Writer
			br = new BufferedReader(new FileReader(inputPath));
			bw = new BufferedWriter(new FileWriter(outputPath));
			while ((temp = br.readLine()) != null) { // <Original Sentence>
				temp = br.readLine(); // <Part Of Speech>
				String[] tokens = temp.trim().split(" ");

				temp = br.readLine(); // <Dependency Grammar>

				temp = br.readLine(); // <Positive Candidate>
				if (!temp.equals("")) {
					String[] posArcStr = temp.trim().split(" ");
					for (int i = 0; i < posArcStr.length; i++) {
						String[] nodeStr = posArcStr[i].split(",");
						int index1 = Integer.parseInt(nodeStr[0]);
						int index2 = Integer.parseInt(nodeStr[1]);
						String[] result = FeatureExtractor.extract(tokens, index1,
								index2);
						bw.write("POSITIVE");
						for (int j = 0; j < result.length; j++)
							bw.write(" " + result[j]);
						bw.write("\n");
					}
				}

				temp = br.readLine(); // <Positive Candidate>
				if (!temp.equals("")) {
					String[] negArcStr = temp.trim().split(" ");
					for (int i = 0; i < negArcStr.length; i++) {
						String[] nodeStr = negArcStr[i].split(",");
						int index1 = Integer.parseInt(nodeStr[0]);
						int index2 = Integer.parseInt(nodeStr[1]);
						String[] result = FeatureExtractor.extract(tokens, index1,
								index2);
						bw.write("NEGATIVE");
						for (int j = 0; j < result.length; j++)
							bw.write(" " + result[j]);
						bw.write("\n");
					}
				}
			}

			// Close Buffered Reader & Writer
			bw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Example Generate Failed!");
			System.exit(-1);
		}
	}

	public static void main(String[] args) throws Exception {
		generate("file/Candidate.txt", "file/Example.txt");
	}

}
