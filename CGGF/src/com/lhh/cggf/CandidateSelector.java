package com.lhh.cggf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import com.lhh.cggf.algorithm.GraphDistance;

public class CandidateSelector {

	private static int[][] calcDistance(int[][] arc) {
		int[][] graph = GraphDistance.arcToGraph(arc);
		int[][] dist = GraphDistance.calc(graph);
		int[][] result = GraphDistance.graphToArc(dist);

		int count = 0;
		for (int i = 0; i < result.length; i++)
			if (result[i][0] < result[i][1])
				count++;

		int index = 0;
		int[][] ret = new int[count][3];
		for (int i = 0; i < result.length; i++)
			if (result[i][0] < result[i][1]) {
				ret[index] = result[i];
				index++;
			}

		return ret;
	}

	public static void select(String inputPath, String outputPath, int threshold) {
		String temp = null;
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			// Open Buffered Reader & Writer
			br = new BufferedReader(new FileReader(inputPath));
			bw = new BufferedWriter(new FileWriter(outputPath));

			while ((temp = br.readLine()) != null) { // <Original Sentence>
				bw.write(temp + "\n");
				temp = br.readLine(); // <Part Of Speech>
				bw.write(temp + "\n");
				temp = br.readLine(); // <Dependency Grammar>
				bw.write(temp + "\n");

				// Extract Elements Of Dependency Grammar
				String[] elems = temp.trim().split(" ");

				int[][] arc = new int[elems.length][3];

				// For Each Element Of Dependency Grammar
				for (int i = 0; i < elems.length; i++) {
					String[] arcStr = elems[i].split(",");
					arc[i][0] = Integer.parseInt(arcStr[0]); // From
					arc[i][1] = Integer.parseInt(arcStr[1]); // To
					arc[i][2] = 1; // Distance
				}

				int[][] dist = calcDistance(arc);

				StringBuffer positive = new StringBuffer();
				StringBuffer negative = new StringBuffer();
				for (int i = 0; i < dist.length; i++) {
					if (dist[i][2] <= threshold)
						positive.append(dist[i][0] + "," + dist[i][1] + ","
								+ dist[i][2] + " ");
					else
						negative.append(dist[i][0] + "," + dist[i][1] + ","
								+ dist[i][2] + " ");
				}

				bw.write(positive.toString() + "\n");
				bw.write(negative.toString() + "\n");
			}

			// Close Buffered Reader & Writer
			bw.close();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Candidate Select Failed!");
			System.exit(-1);
		}
	}

	public static void main(String[] args) throws Exception {
		select("file/Parsed.txt", "file/Candidate.txt", 1);
	}
}
