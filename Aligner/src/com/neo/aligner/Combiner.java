package com.neo.aligner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.neo.util.FileUtility;

public class Combiner {
	
	public static class Alignment {
		
		public HashMap<Integer, ArrayList<int[]>> alignmentMap;
		
		public Alignment(HashMap<Integer, ArrayList<int[]>> alignmentMap) {
			this.alignmentMap = alignmentMap;
		}
		
		public static Alignment load(String string, boolean isReversed) {
			HashMap<Integer, ArrayList<int[]>> alignmentMap =
					new HashMap<Integer, ArrayList<int[]>>();
			String lines[] = string.split("\n");
			for (int i = 0; i < lines.length; i++) {
				String elements[] = lines[i].split("\\s");
				int index = Integer.parseInt(elements[0]);
				if (!alignmentMap.containsKey(index)) {
					alignmentMap.put(index, new ArrayList<int[]>());
				}
				ArrayList<int[]> list = alignmentMap.get(index);
				if (!isReversed) {
					list.add(new int[]{
							Integer.parseInt(elements[1]),
							Integer.parseInt(elements[2])});
				} else {
					list.add(new int[]{
							Integer.parseInt(elements[2]),
							Integer.parseInt(elements[1])});
				}
			}
			return new Alignment(alignmentMap);
		}
	}

	public static void main(String[] args) {
		StringBuilder builder = new StringBuilder();
		File ordinaryFile = new File("data/test.en2es");
		File reservedFile = new File("data/test.es2en");
		String ordinaryString = FileUtility.read(ordinaryFile);
		String reservedString = FileUtility.read(reservedFile);
		Alignment ordinaryAlignment = Alignment.load(ordinaryString, false);
		Alignment reservedAlignment = Alignment.load(reservedString, true);
		for (Integer index : ordinaryAlignment.alignmentMap.keySet()) {
			ArrayList<int[]> ordinaryList =
					ordinaryAlignment.alignmentMap.get(index);
			ArrayList<int[]> reservedList =
					reservedAlignment.alignmentMap.get(index);
			for (int i = 0; i < ordinaryList.size(); i++) {
				int ordinaryArray[] = ordinaryList.get(i);
				for (int j = 0; j < reservedList.size(); j++) {
					int reservedArray[] = reservedList.get(j);
					if (ordinaryArray[0] == reservedArray[0]
							&& ordinaryArray[1] == reservedArray[1]) {
						builder.append(index).append(" ");
						builder.append(ordinaryArray[0]).append(" ");
						builder.append(ordinaryArray[1]).append("\n");
						break;
					}
				}
			}
		}
		FileUtility.write(new File("data/output"), builder.toString());
	}
}
