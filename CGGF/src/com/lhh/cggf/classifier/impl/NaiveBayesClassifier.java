package com.lhh.cggf.classifier.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.lhh.cggf.classifier.Classifier;

public class NaiveBayesClassifier implements Classifier {
	private static final BigInteger smoothFactor = BigInteger.ONE;

	private BigInteger posFactor;
	private BigInteger negFactor;

	ArrayList<HashMap<String, Long>> posMaps;
	ArrayList<HashMap<String, Long>> negMaps;

	public static NaiveBayesClassifier trainInstance(String inputPath) {
		NaiveBayesClassifier nbc = new NaiveBayesClassifier();

		// Initialize Positive & Negative Maps
		nbc.posMaps = new ArrayList<HashMap<String, Long>>();
		nbc.negMaps = new ArrayList<HashMap<String, Long>>();

		long posCount = 0;
		long negCount = 0;

		BufferedReader br = null;
		String temp = null;
		try {
			// If Is First Time, Determine Number Of Feature Types
			boolean firstTime = true;

			// Open Buffered Reader
			br = new BufferedReader(new FileReader(inputPath));

			// For Each Training Case
			while ((temp = br.readLine()) != null) {
				// Extract Training Case Mark
				String mark = temp.substring(0, 8);

				// Extract Elements Of This Training Case
				String[] elems = temp.substring(9).trim().split(" ");

				if (firstTime) {
					for (int i = 0; i < elems.length; i++) {
						nbc.posMaps.add(new HashMap<String, Long>());
						nbc.negMaps.add(new HashMap<String, Long>());
					}
					firstTime = false;
				}

				ArrayList<HashMap<String, Long>> maps = null;

				// Handle Different Process
				if (mark.equals("POSITIVE")) { // Positive Case
					posCount++;
					maps = nbc.posMaps;
				} else if (mark.equals("NEGATIVE")) { // Negative Case
					negCount++;
					maps = nbc.negMaps;
				}

				// Handle Uniform Process
				for (int i = 0; i < elems.length; i++) {
					HashMap<String, Long> map = maps.get(i);
					if (map.containsKey(elems[i])) {
						long num = map.get(elems[i]).longValue();
						map.put(elems[i], new Long(num + 1));
					} else
						map.put(elems[i], new Long(1));
				}
			}

			// Close Buffered Reader
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("NBC Train Failed!");
			System.exit(-1);
		}

		nbc.posFactor = BigInteger.valueOf(posCount);
		nbc.negFactor = BigInteger.valueOf(negCount);

		return nbc;
	}

	public static NaiveBayesClassifier loadInstance(String inputPath) {
		NaiveBayesClassifier nbc = new NaiveBayesClassifier();

		// Initialize Positive & Negative Maps
		nbc.posMaps = new ArrayList<HashMap<String, Long>>();
		nbc.negMaps = new ArrayList<HashMap<String, Long>>();

		BufferedReader br = null;
		String temp = null;
		try {
			// Open Buffered Reader
			br = new BufferedReader(new FileReader(inputPath));

			// Read Positive & Negative Factor
			nbc.posFactor = BigInteger.valueOf(Long.parseLong(br.readLine()));
			nbc.negFactor = BigInteger.valueOf(Long.parseLong(br.readLine()));

			// For Each Feature Type
			while ((temp = br.readLine()) != null) {
				// Initialize & Add Positive & Negative Map
				HashMap<String, Long> posMap = new HashMap<String, Long>();
				HashMap<String, Long> negMap = new HashMap<String, Long>();
				nbc.posMaps.add(posMap);
				nbc.negMaps.add(negMap);

				// Read Elements Of One Feature Type
				String[] elems = temp.trim().split(" ");

				// For Each Element Of This Feature Type
				for (int i = 0; i < elems.length; i++) {
					// <label>:<positive>:<negative>
					String[] tokens = elems[i].split(":");
					String label = tokens[0];
					long pos = Long.parseLong(tokens[1]);
					long neg = Long.parseLong(tokens[2]);

					if (pos != 0)
						posMap.put(label, new Long(pos));
					if (neg != 0)
						negMap.put(label, new Long(neg));
				}
			}

			// Close Buffered Reader
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("NBC Load Failed!");
			System.exit(-1);
		}

		return nbc;
	}

	public static void saveInstance(NaiveBayesClassifier nbc, String outputPath) {
		BufferedWriter bw = null;
		try {
			// Open Buffered Writer
			bw = new BufferedWriter(new FileWriter(outputPath));

			bw.write(nbc.toString());

			// Close Buffered Writer
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("NBC Save Failed!");
			System.exit(-1);
		}
	}

	private NaiveBayesClassifier() {
		posFactor = negFactor = null;
		posMaps = negMaps = null;
	}

	public String classify(String[] features) {
		BigInteger posProb = BigInteger.ONE;
		BigInteger negProb = BigInteger.ONE;

		// For Each Feature Type
		for (int i = 0; i < features.length; i++) {
			// Get Corresponding Positive & Negative Map
			HashMap<String, Long> posMap = posMaps.get(i);
			HashMap<String, Long> negMap = negMaps.get(i);

			if (posMap.containsKey(features[i])) {
				long pos = posMap.get(features[i]).longValue();
				posProb = posProb.multiply(BigInteger.valueOf(pos));
				negProb = negProb.multiply(negFactor);
			} else
				negProb = negProb.multiply(smoothFactor);

			if (negMap.containsKey(features[i])) {
				long neg = negMap.get(features[i]).longValue();
				negProb = negProb.multiply(BigInteger.valueOf(neg));
				posProb = posProb.multiply(posFactor);
			} else
				posProb = posProb.multiply(smoothFactor);
		}

		return (posProb.compareTo(negProb) > 0) ? "<true>" : "<false>";
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append(posFactor + "\n");
		sb.append(negFactor + "\n");

		for (int i = 0; i < posMaps.size(); i++) {
			HashMap<String, Long> posMap = posMaps.get(i);
			HashMap<String, Long> negMap = negMaps.get(i);

			HashSet<String> keySet = new HashSet<String>();
			keySet.addAll(posMap.keySet());
			keySet.addAll(negMap.keySet());

			Iterator<String> iter = keySet.iterator();
			while (iter.hasNext()) {
				String label = iter.next();
				long pos = posMap.containsKey(label) ? posMap.get(label)
						.longValue() : 0;
				long neg = negMap.containsKey(label) ? negMap.get(label)
						.longValue() : 0;

				sb.append(label + ":" + pos + ":" + neg + " ");
			}

			sb.append("\n");
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		NaiveBayesClassifier nbc1 = NaiveBayesClassifier
				.trainInstance("file/Example.txt");

		System.out.println(nbc1);

		NaiveBayesClassifier.saveInstance(nbc1, "file/NBC.txt");

		NaiveBayesClassifier nbc2 = NaiveBayesClassifier
				.loadInstance("file/NBC.txt");

		System.out.println(nbc2);
	}
}
