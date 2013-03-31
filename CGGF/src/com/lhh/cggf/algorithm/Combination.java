package com.lhh.cggf.algorithm;

import java.util.ArrayList;

// Combination
public class Combination {
	// Generate Combination
	// Select x Elements From [0, y)
	public static int[][] generate(int x, int y) {
		// Handle Special Case
		if (x <= 0 || y <= 0 || x > y) return new int[0][0];

		ArrayList<int[]> arrays = new ArrayList<int[]>();

		// Two More Elements In Case Of OutOfBound
		int[] rec = new int[x + 2];
		
		int index = 1;
		rec[index] = 0;

		while (index > 0) {
			// If Found A Valid Combination, Then Record It And Go Back
			if (index == x + 1) {
				int[] temp = new int[x];
				for (int i = 0; i < x; i++)
					temp[i] = rec[i + 1];
				arrays.add(temp);
				rec[--index]++;
				continue;
			}
			
			// If Reach Upper-Bound, Then Go Back
			if (rec[index] == y) {
				rec[--index]++;
				continue;
			}
			
			// Forward
			index++;
			rec[index] = rec[index-1]+1;
		}

		int[][] result = new int[arrays.size()][];
		arrays.toArray(result);
		return result;
	}

	public static void main(String[] args) {
		int[][] result = generate(2, 5);
		for (int i = 0; i < result.length; i++) {
			for (int j = 0; j < result[i].length; j++)
				System.out.print(result[i][j] + "  ");
			System.out.println();
		}
	}

}
