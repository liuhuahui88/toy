package com.lhh.cggf.algorithm;

import java.util.HashSet;
import java.util.LinkedList;

// Calculate Pattern Distribution
public class PatternDistribution {
	private static class DataWrapper {
		public Object obj;
		public int loc;

		public DataWrapper(Object obj, int loc) {
			this.obj = obj;
			this.loc = loc;
		}

		public String toString() {
			return "(" + obj.toString() + ", " + loc + ")";
		}

		public boolean equals(Object other) {
			if (other == null)
				return false;
			if (other == this)
				return true;
			if (!(other instanceof DataWrapper))
				return false;
			DataWrapper otherDW = (DataWrapper) other;
			return this.obj.equals(otherDW.obj);
		}

		public int hashCode() {
			return obj.hashCode();
		}
	}

	//////////////////////////////////////////////////////////
	// Calculate The Shortest Pattern Distribution Width
	//////////////////////////////////////////////////////////
	// 1. Handle Special Cases
	//////////////////////////////////////////////////////////
	// 2. Detect First Distribution.
	// If Duplicated Patterns Are Found,
	// Then Only Record The Last Found Position.
	// e.g.
	// Pattern: a, b, c
	// Context: b, c, c, a, c, b
	// When "index" Is 3, All Patterns Are Found.
	// "a" Is Found Once, And Last Found At 3.
	// "b" Is Found Once, And Last Found At 0.
	// "c" Is Found Twice, And Last Found At 2.
	// List: (b,0), (c,2), (a,3)
	//////////////////////////////////////////////////////////
	// 3. If No Distribution Detected, Then Width Is -1.
	//////////////////////////////////////////////////////////
	// 4. Set Current Shortest Width
	//////////////////////////////////////////////////////////
	// 5. Detect More Distributions.
	// If Duplicated Patterns Are Found,
	// Then Only Record The Last Found Position.
	// If Shorter Width Is Found
	// Then Update Current Shortest Width
	// e.g.
	// In Above Example
	// When "index" Is 4,
	// List: (b,0), (a,3), (c,4)
	// Width: 4 (0~3 : b, c, c, a)
	// When "index" Is 6,
	// Since "context[6]" Is "b" And "b" Is The First In List,
	// This "b" Might Be The End Of A Better Distribution.
	// We Calculate The New Width, And It Turns 3.
	// So We Update Current Shortest Width As 3.
	// List: (a,3), (c,4), (b,5)
	// Width: 3 (3~5 : a, c, b)
	//////////////////////////////////////////////////////////
	// 6. Return Current Shortest Width
	//////////////////////////////////////////////////////////
	
	public static int calc(Object[] context, Object[] pattern) {
		// 1. Handle Special Cases
		if (pattern.length == 0)
			return 0;
		if (pattern.length == 1) {
			for (int i = 0; i < context.length; i++)
				if (context[i].equals(pattern[0]))
					return 1;
			return -1;
		}

		HashSet<Object> patternSet = new HashSet<Object>();
		for (int i = 0; i < pattern.length; i++)
			patternSet.add(pattern[i]);

		LinkedList<DataWrapper> dataList = new LinkedList<DataWrapper>();

		int index = 0;

		// 2. Detect First Distribution.
		for (; index < context.length && dataList.size() < pattern.length; index++)
			if (patternSet.contains(context[index])) {
				DataWrapper dw = new DataWrapper(context[index], index);
				dataList.remove(dw);
				dataList.addLast(dw);
			}

		// 3. If No Distribution Detected, Then Width Is -1.
		if (dataList.size() < pattern.length)
			return -1;

		// 4. Set Current Shortest Width
		int width = 1 + dataList.getLast().loc - dataList.getFirst().loc;

		// 5. Detect More Distributions.
		for (; index < context.length; index++)
			if (patternSet.contains(context[index])) {
				DataWrapper dw = new DataWrapper(context[index], index);
				if (dataList.getFirst().equals(dw)) {
					DataWrapper secondData = dataList.get(1);
					int tmpWidth = 1 + index - secondData.loc;
					width = width < tmpWidth ? width : tmpWidth;
				}
				dataList.remove(dw);
				dataList.addLast(dw);
			}

		// 6. Return Current Shortest Width
		return width;
	}

	public static void main(String[] args) {
		String[] context = { "b", "c", "c", "a", "c", "b"};
		String[] pattern = { "a", "b", "c" };
		System.out.println(calc(context, pattern));
	}

}
