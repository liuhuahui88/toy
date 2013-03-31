package com.lhh.cggf.algorithm;

// Sequence Similarity
public class SequenceSimilarity {
	// Convert String To Character Array
	private static Character[] convert(String str) {
		char[] ch = str.toCharArray();
		Character[] character = new Character[ch.length];
		for (int i = 0; i < ch.length; i++)
			character[i] = new Character(ch[i]);
		return character;
	}

	// Maximum Of Three Integer
	private static int maximum(int a, int b, int c) {
		if (a >= b && a >= c)
			return a;
		else if (b >= c)
			return b;
		else
			return c;
	}

	// Calculate String Similarity
	// Using Longest Common String Algorithm
	public static int LCS(String str1, String str2) {
		Character[] char1 = convert(str1);
		Character[] char2 = convert(str2);

		return LCS(char1, char2);
	}

	// Calculate Sequence Similarity
	// Using Longest Common String Algorithm
	public static int LCS(Object[] s1, Object[] s2) {

		int[][] count = new int[s1.length + 1][s2.length + 1];

		for (int i = 0; i < s1.length; i++)
			for (int j = 0; j < s2.length; j++) {
				int x = i + 1;
				int y = j + 1;

				if (s1[i].equals(s2[j]))
					count[x][y] = count[x - 1][y - 1] + 1;
				else if (count[x - 1][y] > count[x][y - 1])
					count[x][y] = count[x - 1][y];
				else
					count[x][y] = count[x][y - 1];
			}

		return count[s1.length][s2.length];
	}

	// Calculate String Similarity
	// Using BioInformatics Sequence Comparison Algorithm
	public static int BISC(String str1, String str2) {
		Character[] char1 = convert(str1);
		Character[] char2 = convert(str2);

		return BISC(char1, char2);
	}

	// Calculate Sequence Similarity
	// Using BioInformatics Sequence Comparison Algorithm
	//
	// e.g.
	// | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | = "12345679"
	// | 1 | 2 | 3 | 4 |   |   |   |   |   | = "1234"
	// | +1| +1| +1| +1|         -1        | = -3
	//
	// | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | = "12345679"
	// | 1 |   | 3 |   | 5 |   | 7 |   |   | = "1357"
	// | +1| -1| +1| -1| +1| -1| +1|   -1  | = 0
	//
	// | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | = "12345679"
	// | 1 |   | 3 |   | 5 |   |   |   | 9 | = "1359"
	// | +1| -1| +1| -1| +1|     -1    | +1 | = 1
	public static int BISC(Object[] s1, Object[] s2) {
		int oo, so, os;

		// Score Of Alignment Ending With (Object, Object)
		int[][] ooScore = new int[s1.length + 1][s2.length + 1];

		// Score Of Alignment Ending With (Slot, Object)
		int[][] soScore = new int[s1.length + 1][s2.length + 1];

		// Score Of Alignment Ending With (Object, Slot)
		int[][] osSocre = new int[s1.length + 1][s2.length + 1];

		int matchScore = 1; // If Matched, Each Position +1
		int nmatchScore = -1; // If Not Matched, Each Position -1
		int slotScore = -1; // If Slot, Each Consecutive Slot -1

		for (int i = 0; i < s1.length; i++)
			for (int j = 0; j < s2.length; j++) {
				int x = i + 1;
				int y = j + 1;

				oo = ooScore[x - 1][y - 1];
				so = soScore[x - 1][y - 1];
				os = osSocre[x - 1][y - 1];
				ooScore[x][y] = maximum(oo, so, os);
				ooScore[x][y] += s1[i].equals(s2[j]) ? matchScore : nmatchScore;

				oo = ooScore[x - 1][y] + slotScore;
				so = soScore[x - 1][y];
				os = osSocre[x - 1][y] + slotScore;
				soScore[x][y] = maximum(oo, so, os);

				oo = ooScore[x][y - 1] + slotScore;
				so = soScore[x][y - 1] + slotScore;
				os = osSocre[x][y - 1];
				osSocre[x][y] = maximum(oo, so, os);
			}

		oo = ooScore[s1.length][s2.length];
		so = soScore[s1.length][s2.length];
		os = osSocre[s1.length][s2.length];
		return maximum(oo, so, os);
	}

	public static void main(String[] args) {
		System.out.println("LCS");
		String str1 = "dsfkqewr";
		String str2 = "qeaw";
		System.out.println(LCS(str1, str2));

		System.out.println("BISC");
		String str3 = "123456789";
		String str4 = "1234";
		String str5 = "1357";
		String str6 = "1359";
		System.out.println(BISC(str3, str4));
		System.out.println(BISC(str3, str5));
		System.out.println(BISC(str3, str6));
	}
}
