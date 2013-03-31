package com.lhh.cggf.cg.gen.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.lhh.cggf.cg.Arc;
import com.lhh.cggf.cg.Graph;
import com.lhh.cggf.cg.Node;
import com.lhh.cggf.cg.gen.Generator;

public class LimitedGenerator implements Generator {
	private HashMap<String, Graph> graphMap;

	public LimitedGenerator(String filePath) {
		graphMap = new HashMap<String, Graph>();

		try {
			String temp;

			// Open File Reader
			BufferedReader br = new BufferedReader(new FileReader(filePath));

			// For Each Graph
			while ((temp = br.readLine()) != null) {
				// Initialize Graph
				Graph g = new Graph();

				// Extract Node Information
				String[] nodeInfo = temp.trim().split(" ");
				
				// Construct Sentence
				StringBuffer sb = new StringBuffer();
				for( int i=0 ; i<nodeInfo.length ; i++)
					sb.append(nodeInfo[i]);
				String sentence = sb.toString();

				// Extract Arc Information
				String[] arcInfo = br.readLine().trim().split(" ");

				// For Each Arc
				for (int i = 0; i < arcInfo.length; i++) {
					String[] elems = arcInfo[i].split(",");

					// Extract First Node
					int id1 = Integer.parseInt(elems[0]);
					String name1 = nodeInfo[id1];
					Node node1 = new Node(id1, name1);

					// Extract Second Node
					int id2 = Integer.parseInt(elems[1]);
					String name2 = nodeInfo[id2];
					Node node2 = new Node(id2, name2);

					// Extract Arc Type
					String type = (elems.length == 3) ? elems[2] : "UNKNOWN";

					// Add Extracted Arc
					g.addArc(new Arc(type, node1, node2));
					g.addArc(new Arc(type, node2, node1));
				}

				// Add Extracted Graph
				graphMap.put(sentence, g);
			}

			// Close File Reader
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed To Load CG File!");
			System.exit(1);
		}
	}

	public Graph generate(String str) {
		return graphMap.get(str);
	}

	public static void main(String[] args) {
		LimitedGenerator lg = new LimitedGenerator("file/CG.txt");

		Set<String> keys = lg.graphMap.keySet();
		Iterator<String> iter = keys.iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			System.out.println("Key Information:");
			System.out.println(key);
			System.out.print(lg.graphMap.get(key));
		}
	}
}
