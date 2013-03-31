package com.lhh.passcode.image.impl;

import java.io.File;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.IOException;

import com.lhh.passcode.image.Image;

public class ColorizedImage implements Image {
	private int[][][] image;
	private int width;
	private int height;

	private void init(File file) {
		BufferedImage bi = null;
		
		try {
			bi = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (bi == null) {
			System.out.println("ColorizedImage Init Failed!");
			System.out.println("Can't Read File!");
			System.exit(0);
		}

		width = bi.getWidth();
		height = bi.getHeight();

		image = new int[height][width][];
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++)
				image[y][x] = transformRGB(bi.getRGB(x, y));
	}

	private int[] transformRGB(int rgb) {
		int[] ret = new int[COLOR_INDEX_COUNT];

		ret[COLOR_INDEX_RED] = (rgb >> 16) & 0xFF;
		ret[COLOR_INDEX_GREEN] = (rgb >> 8) & 0xFF;
		ret[COLOR_INDEX_BLUE] = (rgb >> 0) & 0xFF;

		return ret;
	}

	public ColorizedImage(String filePath) {
		File file = new File(filePath);
		init(file);
	}

	public ColorizedImage(File file) {
		init(file);
	}

	public int[] getPixel(int x, int y) {
		if (!isValidPosition(x, y))
			return null;
		else
			return image[y][x];
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
		
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				int[] rgb = image[y][x];
				System.out.print("(" + y + "," + x + ") : ");
				System.out.print(rgb[COLOR_INDEX_RED] + " ");
				System.out.print(rgb[COLOR_INDEX_GREEN] + " ");
				System.out.print(rgb[COLOR_INDEX_BLUE] + "\n");
			}
	}

	public static void main(String[] args) {
		new ColorizedImage("img/2SKK.bmp").print();
	}

}
