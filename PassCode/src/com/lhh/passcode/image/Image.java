package com.lhh.passcode.image;

public interface Image {
	public static final int COLOR_INDEX_COUNT = 3;

	public static final int COLOR_INDEX_RED = 0;
	public static final int COLOR_INDEX_GREEN = 1;
	public static final int COLOR_INDEX_BLUE = 2;
	
	public static final int COLOR_RANGE_MIN = 0;
	public static final int COLOR_RANGE_MAX = 255;

	public static final int[] COLOR_WHITE = new int[] { 255, 255, 255 };
	public static final int[] COLOR_BLACK = new int[] { 0, 0, 0 };

	public int[] getPixel(int x, int y);
	
	public int getWidth();

	public int getHeight();

	public boolean isValidPosition(int x, int y);
}
