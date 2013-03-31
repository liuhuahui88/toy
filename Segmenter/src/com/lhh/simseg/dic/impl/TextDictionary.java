package com.lhh.simseg.dic.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

import com.lhh.simseg.dic.Dictionary;

public class TextDictionary implements Dictionary {
	public static TextDictionary newInstance(File file) {
		TextDictionary ret = new TextDictionary();
		return ret.load(file) ? ret : null;
	}

	private HashSet<String> dic;

	private TextDictionary() {
		dic = new HashSet<String>();
	}

	public boolean load(File file) {
		dic.clear();
		
		BufferedReader br = null;
		String temp = null;

		try {
			br = new BufferedReader(new FileReader(file));
			while ((temp = br.readLine()) != null)
				dic.add(temp);
		} catch (IOException e) {
			dic.clear();
			e.printStackTrace();
			return false;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				br = null;
			}
		}

		return true;
	}

	public boolean release() {
		dic.clear();
		return true;
	}

	public void add(String word) {
		dic.add(word);
	}

	public boolean lookup(String word) {
		return dic.contains(word);
	}

	public void delete(String word) {
		dic.remove(word);
	}
}
