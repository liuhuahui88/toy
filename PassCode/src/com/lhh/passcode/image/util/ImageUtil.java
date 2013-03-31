package com.lhh.passcode.image.util;

import com.lhh.passcode.image.Image;
import com.lhh.passcode.image.impl.BlackWhiteImage;
import com.lhh.passcode.image.impl.ColorizedImage;

public class ImageUtil {
	public static int colorDist(int[] c1, int[] c2) {
		int redDist = c1[Image.COLOR_INDEX_RED] - c2[Image.COLOR_INDEX_RED];
		int greenDist = c1[Image.COLOR_INDEX_GREEN]
				- c2[Image.COLOR_INDEX_GREEN];
		int blueDist = c1[Image.COLOR_INDEX_BLUE] - c2[Image.COLOR_INDEX_BLUE];

		int sum = redDist * redDist + greenDist * greenDist + blueDist
				* blueDist;

		int dist = (int) Math.sqrt(sum);

		return dist;
	}

	public static boolean[][] detectEdge(Image image, int threshold) {
		int width = image.getWidth();
		int height = image.getHeight();

		boolean[][] ret = new boolean[height][width];

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				int maxColorDist = 0;

				int[] color = image.getPixel(x, y);

				for (int i = -1; i <= 1; i++)
					for (int j = -1; j <= 1; j++)
						if (i == 0 && j == 0)
							continue;
						else if (!image.isValidPosition(x + i, y + j))
							continue;
						else {
							int[] otherColor = image.getPixel(x + i, y + j);
							int tmpColorDist = colorDist(color, otherColor);
							if (tmpColorDist > maxColorDist)
								maxColorDist = tmpColorDist;
						}

				ret[y][x] = maxColorDist >= threshold;
			}

		return ret;
	}

	public static boolean[][] detectDark(Image image) {
		int width = image.getWidth();
		int height = image.getHeight();

		boolean[][] ret = new boolean[height][width];

		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				int[] color = image.getPixel(x, y);

				int whiteDist = colorDist(color, Image.COLOR_WHITE);
				int blackDist = colorDist(color, Image.COLOR_BLACK);

				ret[y][x] = whiteDist > blackDist;
			}

		return ret;
	}

	public static int countMatch(Image image, Image pattern, int xOffset,
			int yOffset, int colorThreshold) {
		int count = 0;

		int patternWidth = pattern.getWidth();
		int patternHeight = pattern.getHeight();

		for (int y = 0; y < patternHeight; y++)
			for (int x = 0; x < patternWidth; x++) {
				int[] imageColor = image.getPixel(x + xOffset, y + yOffset);
				int[] patternColor = pattern.getPixel(x, y);

				if (colorDist(imageColor, patternColor) <= colorThreshold)
					count++;
			}

		return count;
	}

	public static void main(String[] args) {
		Image image = new ColorizedImage("img/4J97.bmp");
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		Image pattern = new BlackWhiteImage("pattern/4.txt");
		int patternWidth = pattern.getWidth();
		int patternHeight = pattern.getHeight();

		int colorThreshold = 200;
		int pixelThreshold = 5;

		for (int y = 0; y <= imageHeight - patternHeight; y++)
			for (int x = 0; x <= imageWidth - patternWidth; x++) {
				int count = countMatch(image, pattern, x, y, colorThreshold);
				if(count + pixelThreshold >= patternWidth * patternHeight)
					System.out.println("(" + y + "," + x + ")");
			}

		new BlackWhiteImage((ColorizedImage) image).print();
	}
}
