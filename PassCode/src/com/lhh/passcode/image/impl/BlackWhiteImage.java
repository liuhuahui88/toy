package com.lhh.passcode.image.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.lhh.passcode.image.Image;
import com.lhh.passcode.image.util.ImageUtil;

public class BlackWhiteImage implements Image {
	private boolean[][] image;
	private int width;
	private int height;

	private void initFromFile(File file) {
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(file));

			String[] resolution = br.readLine().split(" ");
			width = Integer.parseInt(resolution[0]);
			height = Integer.parseInt(resolution[1]);

			image = new boolean[height][width];
			for (int y = 0; y < height; y++) {
				String row = br.readLine();
				for (int x = 0; x < width; x++)
					if (row.charAt(x) == '*')
						image[y][x] = true;
					else
						image[y][x] = false;
			}

			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initFromArray(boolean[][] img) {
		height = img.length;
		width = img[0].length;

		image = new boolean[height][width];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				image[y][x] = img[y][x];
	}

	public BlackWhiteImage(File file) {
		initFromFile(file);
	}

	public BlackWhiteImage(String filePath) {
		File file = new File(filePath);
		initFromFile(file);
	}

	public BlackWhiteImage(boolean[][] array) {
		initFromArray(array);
	}

	public BlackWhiteImage(Image image) {
		boolean[][] array = ImageUtil.detectDark(image);
		initFromArray(array);
	}
	
	public BlackWhiteImage(int width, int height) {
		this.width = width;
		this.height = height;
		
		image = new boolean[height][width];
	}

	public int[] getPixel(int x, int y) {
		if (!isValidPosition(x, y))
			return null;
		else if (image[y][x] == true)
			return COLOR_BLACK;
		else
			return COLOR_WHITE;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isValidPosition(int x, int y) {
		if (x < 0 || x >= width)
			return false;
		if (y < 0 || y >= height)
			return false;
		return true;
	}

	public void print() {
		System.out.println("Width: " + width);
		System.out.println("Height: " + height);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (image[y][x] == true)
					System.out.print("*");
				else
					System.out.print(" ");
			}
			System.out.println();
		}
	}

	public BlackWhiteImage SubImage(int xStart, int xEnd, int yStart, int yEnd) {
		if (xStart < 0 || xEnd > width)
			return null;
		if (yStart < 0 || yEnd > height)
			return null;

		BlackWhiteImage sub = new BlackWhiteImage(xEnd - xStart, yEnd - yStart);
		for(int y=0 ; y<sub.height ; y++)
			for(int x=0 ; x<sub.width ; x++)
				sub.image[y][x] = image[yStart + y][xStart + x];
		return sub;
	}

	public static void main(String[] args) {
		new BlackWhiteImage(new ColorizedImage("img/WV9W.bmp")).print();
		new BlackWhiteImage("pattern/W.txt").print();
		new BlackWhiteImage("pattern/W.txt").SubImage(1, 9, 1, 12).print();
	}

}
