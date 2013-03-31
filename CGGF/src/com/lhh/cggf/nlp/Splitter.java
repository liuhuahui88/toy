package com.lhh.cggf.nlp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import ICTCLAS.I3S.AC.ICTCLAS50;

// Split Sentence
public class Splitter {
	private static ICTCLAS50 ictclas = null;

	// Initialize Before Splitting
	public static void init() {
		ictclas = new ICTCLAS50();

		try {
			String argu = "./ictclas";
			if (ictclas.ICTCLAS_Init(argu.getBytes()) == false) {
				System.out.println("ICTCIAS Init Fail!");
				System.exit(1);
			}

			ictclas.ICTCLAS_SetPOSmap(0);

			String sUserDict = "./ictclas/userdic.txt";
			ictclas.ICTCLAS_ImportUserDictFile(sUserDict.getBytes(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Destroy After Splitting
	public static void destroy() {
		ictclas.ICTCLAS_Exit();
		ictclas = null;
	}

	// Split With Tag-Of-Speech
	public static String[] splitWithPOS(String str) {
		String[] words = null;

		String temp = split(str);
		words = temp.split(" ");

		return words;
	}

	// Split Without Tag-Of-Speech
	public static String[] splitWithoutPOS(String str) {
		String[] words = null;

		words = splitWithPOS(str);
		for (int i = 0; i < words.length; i++)
			words[i] = words[i].replaceAll("/.*", "");

		return words;
	}

	public static String split(String str) {
		String result = null;
		try {
			byte nativeBytes[] = ictclas.ICTCLAS_ParagraphProcess(
					str.getBytes(), 0, 1);
			String nativeStr = new String(nativeBytes, 0, nativeBytes.length);
			result = nativeStr.trim();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	public static void splitFile(String inputPath, String outputPath)
			throws Exception {
		String temp;

		File input = new File(inputPath);
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(input)));
		File output = new File(outputPath);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(output)));

		while ((temp = br.readLine()) != null) {
			String str = Splitter.split(temp);
			bw.write(str + "\n");
		}

		br.close();
		bw.flush();
		bw.close();
	}

	public static void main(String[] args) throws Exception {
		String str = "山东省长简历";

		init();

		String[] words = splitWithPOS(str);

		System.out.println("Total Words: " + words.length);
		for (String s : words)
			System.out.print(s + "  ");
		System.out.println();

		destroy();
	}
}
