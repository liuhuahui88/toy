package com.lhh.cggf.nlp;

import java.util.ArrayList;

// Parse Chunk
public class Chunker {

	// Parse Chunk
	public static String[] chunk(String str) {
		ArrayList<String> strArray = new ArrayList<String>();
		
		// Define Chunk Mark
		char[] marks = new char[] { ',', '.', '，', '。' };

		// Start Index Of Each Latent Chunk
		int start = 0;
		
		int strLength = str.length();
		for (int i = 0; i < strLength; i++)
			for (int j = 0; j < marks.length; j++) {
				// If Encountered Any Chunk Mark
				// Then Handle It, Update "start", And Break
				if (str.charAt(i) == marks[j]) {
					// If Not An Empty Chunk
					// Then Record It
					if (start != i)
						strArray.add(str.substring(start, i));
					start = i + 1;
					break;
				}
			}
		if (start != strLength)
			strArray.add(str.substring(start, strLength));

		// Transform from ArryList<String> to String[]
		String[] chunks = new String[strArray.size()];
		strArray.toArray(chunks);

		return chunks;
	}

	public static void main(String[] args) {
		String str = "..abc,,acc,a.aa";
		
		String[] chunks = chunk(str);
		
		System.out.println("Total Chunks: " + chunks.length);
		for (int i = 0; i < chunks.length; i++)
			System.out.print(chunks[i] + "  ");
		System.out.println();
	}

}
