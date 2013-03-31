package com.neo.tool;

import java.io.File;

import com.neo.util.CombinedTransformer;
import com.neo.util.FileUtility;
import com.neo.util.MapTransformer;
import com.neo.util.Transformer;

public class TrainingTextProcessor {
	
	private static final File INPUT_FILE = new File("data/gene.train");
	private static final File OUTPUT_FILE = new File("data/gene.train.new1");
	
	private static final File MAP_FILE = new File("data/gene.map");
	
	private static final boolean USES_COMBINED = true;

	public static void main(String[] args) {
		String mapString = FileUtility.read(MAP_FILE);
		
		Transformer transformer;
		if (USES_COMBINED) {
			transformer = CombinedTransformer.read(mapString);
		} else {
			transformer = MapTransformer.read(mapString);
		}

		String inputString = FileUtility.read(INPUT_FILE);
		
		StringBuilder stringBuilder = new StringBuilder();

		String lines[] = inputString.split("\n");
		for (String line : lines) {
			if (line.isEmpty()) {
				stringBuilder.append("\n");
			} else {
				String elements[] = line.split("\\s+");
				elements[0] = transformer.transform(elements[0]);
				stringBuilder.append(elements[0]).append(" ");
				stringBuilder.append(elements[1]).append("\n");
			}
		}
		
		FileUtility.write(OUTPUT_FILE, stringBuilder.toString());
	}
}
