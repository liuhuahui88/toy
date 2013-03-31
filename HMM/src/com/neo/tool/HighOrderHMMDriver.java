package com.neo.tool;

import java.io.File;

import com.neo.hmm.HighOrderHMM;
import com.neo.hmm.Sequence;
import com.neo.hmm.TransmitionModel;
import com.neo.lm.NGramLanguageModel;
import com.neo.util.CombinedTransformer;
import com.neo.util.FileUtility;
import com.neo.util.MapTransformer;
import com.neo.util.Transformer;

public class HighOrderHMMDriver {
	
	private static final File INPUT_FILE = new File("data/gene.test");
	private static final File OUTPUT_FILE = new File("data/gene.test.predict2");
	
	private static final String START_STATUS = "*";
	private static final String STOP_STATUS = "STOP";
	private static final File N_GRAM_FILE = new File("data/gene.counts.new1");
	private static final int N = 3;
	
	private static final File TRANSMITION_FILE =
		new File("data/gene.counts.new1");

	private static final File MAP_FILE = new File("data/gene.map");
	
	private static final boolean USES_COMBINED = true;

	public static void main(String[] args) {
		String nGramString = FileUtility.read(N_GRAM_FILE);
		NGramLanguageModel nGramLanguageModel =
			NGramLanguageModel.read(nGramString, N);
		
		String transmitionString = FileUtility.read(TRANSMITION_FILE);
		TransmitionModel transmitionModel =
			TransmitionModel.read(transmitionString);
		
		String mapString = FileUtility.read(MAP_FILE);
		Transformer transformer;
		if (USES_COMBINED) {
			transformer = CombinedTransformer.read(mapString);
		} else {
			transformer = MapTransformer.read(mapString);
		}

		HighOrderHMM highOrderHMM = new HighOrderHMM(START_STATUS, STOP_STATUS,
				nGramLanguageModel, transmitionModel, transformer);
		
		String inputString = FileUtility.read(INPUT_FILE);
		
		StringBuilder stringBuilder = new StringBuilder();

		String segments[] = inputString.split("\n\n");
		for (String segment : segments) {
			Sequence sequence = Sequence.read(segment);
			System.out.println(highOrderHMM.decode(sequence));
			stringBuilder.append(Sequence.write(sequence));
		}
		
		FileUtility.write(OUTPUT_FILE, stringBuilder.toString());
	}

}
