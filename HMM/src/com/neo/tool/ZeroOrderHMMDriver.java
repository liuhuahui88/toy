package com.neo.tool;

import java.io.File;

import com.neo.hmm.Sequence;
import com.neo.hmm.TransmitionModel;
import com.neo.hmm.ZeroOrderHMM;
import com.neo.util.FileUtility;
import com.neo.util.MapTransformer;
import com.neo.util.Transformer;

public class ZeroOrderHMMDriver {
	
	private static final File INPUT_FILE = new File("data/gene.test");
	private static final File OUTPUT_FILE = new File("data/gene.test.predict");
	
	private static final File TRANSMITION_FILE =
		new File("data/gene.counts.new");

	private static final File MAP_FILE = new File("data/gene.map");

	public static void main(String args[]) {
		String transmitionString = FileUtility.read(TRANSMITION_FILE);
		TransmitionModel transmitionModel =
			TransmitionModel.read(transmitionString);
		
		String mapString = FileUtility.read(MAP_FILE);
		Transformer transformer = MapTransformer.read(mapString);
		
		ZeroOrderHMM zeroOrderHMM = new ZeroOrderHMM(transmitionModel,
				transformer);

		String inputString = FileUtility.read(INPUT_FILE);
		
		StringBuilder stringBuilder = new StringBuilder();

		String segments[] = inputString.split("\n\n");
		for (String segment : segments) {
			Sequence sequence = Sequence.read(segment);
			System.out.println(zeroOrderHMM.decode(sequence));
			stringBuilder.append(Sequence.write(sequence));
		}
		
		FileUtility.write(OUTPUT_FILE, stringBuilder.toString());
	}
}
