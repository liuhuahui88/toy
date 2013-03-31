package com.lhh.cggf.se;

public class Result {
	private long count; // Total Count
	private Item[] items; // Items We Have

	public Result(long count, Item[] items) {
		this.count = count;
		this.items = items;
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public Item[] getItems() {
		return items;
	}

	public void setItems(Item[] items) {
		this.items = items;
	}

	public String toString() {
		int i;

		StringBuffer sb = new StringBuffer();

		sb.append("Total Count: " + count + "\n");
		for (i = 0; i < items.length; i++) {
			sb.append("[" + (i + 1) + "]\n");
			sb.append(items[i]);
			if (i != items.length)
				sb.append("\n");
		}

		return sb.toString();
	}
}
