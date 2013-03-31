package com.neo.fc;

import java.io.File;
import java.util.ArrayList;

public class FileInfo {

	private File file;
	private long lastModifiedTime;

	private FileInfo(File file) {
		this.file = file;
		this.lastModifiedTime = file.lastModified();
	}

	public File getFile() {
		return file;
	}

	public long getLastModifiedTime() {
		return lastModifiedTime;
	}

	public String toString() {
		return file.toString();
	}

	public static FileInfo[] load(File file) {
		if (file == null || !file.exists() || file.isHidden()
				|| !file.isDirectory()) {
			return null;
		}

		ArrayList<FileInfo> fileInfoList = new ArrayList<FileInfo>();
		internalLoad(file, fileInfoList);
		return fileInfoList.toArray(new FileInfo[0]);
	}

	private static void internalLoad(File file, ArrayList<FileInfo> fileInfoList) {
		if (file == null || !file.exists() || file.isHidden()) {
			return;
		} else if (file.isFile()) {
			FileInfo fileInfo = new FileInfo(file);
			fileInfoList.add(fileInfo);
		} else {
			File subFileArray[] = file.listFiles();
			for (File subFile : subFileArray) {
				internalLoad(subFile, fileInfoList);
			}
		}
	}
}
