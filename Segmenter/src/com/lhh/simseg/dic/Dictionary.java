package com.lhh.simseg.dic;

import java.io.File;

public interface Dictionary {
	public boolean load(File file);
	public boolean release();
	public void add(String word);
	public boolean lookup(String word);
	public void delete(String word);
}
