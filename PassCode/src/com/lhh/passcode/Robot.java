package com.lhh.passcode;

import java.io.File;
import java.util.ArrayList;

import com.lhh.passcode.image.Image;
import com.lhh.passcode.image.impl.BlackWhiteImage;
import com.lhh.passcode.image.impl.ColorizedImage;
import com.lhh.passcode.image.util.ImageUtil;

public class Robot {
	private ArrayList<String> tokens;
	private ArrayList<BlackWhiteImage> patterns;

	// Init Single
	private void initPattern(File file, String suffix) {
		String fileName = file.getName();

		System.out.println("Loading " + fileName + " ...");

		// Extract Token From File Name
		int fileNameLength = fileName.length();
		int suffixLength = suffix.length();
		int prefixLength = fileNameLength - suffixLength;
		String token = fileName.substring(0, prefixLength);

		// Extract Pattern Image
		BlackWhiteImage pattern = new BlackWhiteImage(file);

		// Extract Relaxed Pattern Image
		BlackWhiteImage relaxedPattern = pattern.SubImage(1,
				pattern.getWidth() - 1, 1, pattern.getHeight() - 1);

		// Add Pattern
		tokens.add(token);
		patterns.add(pattern);

		// Add Relaxed Pattern
		tokens.add(token);
		patterns.add(relaxedPattern);
	}

	// Init All Pattern
	private void init(File dir, String suffix) {
		tokens = new ArrayList<String>();
		patterns = new ArrayList<BlackWhiteImage>();

		if (!dir.exists()) {
			System.out.println("Robot Init Failed!");
			System.out.println("File Doesn't Exist!");
			System.exit(0);
		}

		if (!dir.isDirectory()) {
			System.out.println("Robot Init Failed!");
			System.out.println("File Isn't Directory!");
			System.exit(0);
		}

		File[] files = dir.listFiles();

		for (int i = 0; i < files.length; i++) {
			String fileName = files[i].getName();
			if (fileName.endsWith(suffix))
				initPattern(files[i], suffix);
		}
	}

	// Recognize Single Pattern
	private void recognize(Image image, int colorThreshold,
			double pixelPercentThreshold, int patternIndex,
			ArrayList<Integer> xArray, ArrayList<Integer> yArray,
			ArrayList<Integer> pArray) {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		BlackWhiteImage pattern = patterns.get(patternIndex);

		int patternWidth = pattern.getWidth();
		int patternHeight = pattern.getHeight();

		int patternSize = patternWidth * patternHeight;
		int pixelThreshold = (int) (patternSize * pixelPercentThreshold);

		// For Each Window
		for (int x = 0; x < imageWidth; x++)
			for (int y = 0; y < imageHeight; y++) {
				// If Is Invalid Window
				if (x + patternWidth > imageWidth)
					continue;
				if (y + patternHeight > imageHeight)
					continue;

				// Count Matched Pixel
				int count = ImageUtil.countMatch(image, pattern, x, y,
						colorThreshold);

				// If Have Enough Matched Pixel
				if (count + pixelThreshold >= patternWidth * patternHeight) {
					xArray.add(new Integer(x));
					yArray.add(new Integer(y));
					pArray.add(new Integer(patternIndex));
				}
			}
	}

	// Check If Two Image Are Overlapped
	// Use Hamming Distance
	private boolean isOverlapped(Image img1, int x1, int y1, Image img2,
			int x2, int y2, int distThreshold) {
		int xCenter1 = x1 + img1.getWidth() / 2;
		int yCenter1 = y1 = img1.getHeight() / 2;

		int xCenter2 = x2 + img2.getWidth() / 2;
		int yCenter2 = y2 = img2.getHeight() / 2;

		int xCenterDist = Math.abs(xCenter1 - xCenter2);
		int yCenterDist = Math.abs(yCenter1 - yCenter2);

		return distThreshold >= xCenterDist + yCenterDist;
	}

	// Check If Image 1 Is Dominated By Image 2
	// Use Image Size = Width * Height
	private boolean isDominated(Image img1, Image img2) {
		int size1 = img1.getWidth() * img1.getHeight();
		int size2 = img2.getWidth() * img2.getHeight();
		return size1 < size2;
	}

	// Disambiguate Matched Pattern
	private void disambiguate(ArrayList<Integer> xArray,
			ArrayList<Integer> yArray, ArrayList<Integer> pArray,
			int distThreshold) {
		for (int i = 0; i < xArray.size(); i++) {
			Image img1 = patterns.get(pArray.get(i));
			int x1 = xArray.get(i);
			int y1 = yArray.get(i);

			for (int j = i + 1; j < xArray.size(); j++) {
				Image img2 = patterns.get(pArray.get(j));
				int x2 = xArray.get(j);
				int y2 = yArray.get(j);

				if (!isOverlapped(img1, x1, y1, img2, x2, y2, distThreshold))
					continue;

				int index = isDominated(img1, img2) ? i : j;
				xArray.remove(index);
				yArray.remove(index);
				pArray.remove(index);

				i--;
				break;
			}
		}
	}

	// Arrange Matched Pattern
	private char[] arrange(Image image, ArrayList<Integer> xArray,
			ArrayList<Integer> yArray, ArrayList<Integer> pArray) {
		int length = xArray.size();

		char[] ret = new char[length];

		for (int i = 0; i < length; i++) {
			int leftMostPos = image.getWidth();
			int leftMostIndex = -1;

			for (int j = 0; j < xArray.size(); j++)
				if (xArray.get(j) < leftMostPos) {
					leftMostPos = xArray.get(j);
					leftMostIndex = j;
				}

			ret[i] = tokens.get(pArray.get(leftMostIndex)).charAt(0);

			xArray.remove(leftMostIndex);
			yArray.remove(leftMostIndex);
			pArray.remove(leftMostIndex);
		}

		return ret;
	}

	public Robot(String dirPath, String suffix) {
		File dir = new File(dirPath);
		init(dir, suffix);
	}

	public Robot(File dir, String suffix) {
		init(dir, suffix);
	}

	public Robot(String dirPath) {
		this(dirPath, ".txt");
	}

	public Robot(File dir) {
		this(dir, ".txt");
	}

	public char[] hack(Image image, int colorThreshold,
			double pixelPercentThreshold, int distThreshold) {
		char[] ret = null;

		ArrayList<Integer> xArray = new ArrayList<Integer>();
		ArrayList<Integer> yArray = new ArrayList<Integer>();
		ArrayList<Integer> pArray = new ArrayList<Integer>();

		int patternCount = patterns.size();

		for (int patternIndex = 0; patternIndex < patternCount; patternIndex++) {
			recognize(image, colorThreshold, pixelPercentThreshold,
					patternIndex, xArray, yArray, pArray);
		}

		disambiguate(xArray, yArray, pArray, distThreshold);

		ret = arrange(image, xArray, yArray, pArray);

		return ret;
	}

	public static void main(String[] args) {
		Robot robot = new Robot("pattern");

		int colorThreshold = 220;
		double pixelPercentThreshold = 0.08;
		int distThreshold = 8;

		int count = 0;

		File dir = new File("img");
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			Image image = new ColorizedImage(files[i]);
			char[] ret = robot.hack(image, colorThreshold, pixelPercentThreshold,
					distThreshold);

			String answer = files[i].getName().substring(0, 4);

			if (answer.equals(new String(ret)))
				count++;
			else {
				System.out.print(answer + " : ");
				for (int j = 0; j < ret.length; j++)
					System.out.print(ret[j]);
				System.out.println();
			}
		}

		System.out.println(count);

		/*
		 * Image image = new ColorizedImage("img/9CAC.bmp"); char[] ret =
		 * robot.hack(image, colorThreshold, pixelThreshold, distThreshold);
		 * for(int i=0 ; i<ret.length ; i++) System.out.print(ret[i]);
		 * System.out.println();
		 */
	}

}
