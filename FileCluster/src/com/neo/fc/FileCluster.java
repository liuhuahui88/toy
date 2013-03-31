package com.neo.fc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class FileCluster {

	public static final int BY_DAY = 0;
	public static final int BY_MONTH = 1;
	public static final int BY_YEAR = 2;

	public static HashMap<String, ArrayList<FileInfo>> clusterByDay(
			File sourceDir) {
		return cluster(sourceDir, BY_DAY);
	}

	public static HashMap<String, ArrayList<FileInfo>> clusterByMonth(
			File sourceDir) {
		return cluster(sourceDir, BY_MONTH);
	}

	public static HashMap<String, ArrayList<FileInfo>> clusterByYear(
			File sourceDir) {
		return cluster(sourceDir, BY_YEAR);
	}

	public static HashMap<String, ArrayList<FileInfo>> cluster(File sourceDir,
			int mode) {
		FileInfo fileInfoArray[] = FileInfo.load(sourceDir);

		HashMap<String, ArrayList<FileInfo>> map = new HashMap<String, ArrayList<FileInfo>>();

		for (FileInfo fileInfo : fileInfoArray) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(fileInfo.getLastModifiedTime());

			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int month = calendar.get(Calendar.MONTH) + 1;
			int year = calendar.get(Calendar.YEAR);

			String dirName;
			if (mode == BY_DAY) {
				dirName = year + "-" + month + "-" + day;
			} else if (mode == BY_MONTH) {
				dirName = year + "-" + month;
			} else if (mode == BY_YEAR) {
				dirName = year + "";
			} else {
				dirName = "default";
			}

			if (map.containsKey(dirName)) {
				ArrayList<FileInfo> fileInfoList = map.get(dirName);
				fileInfoList.add(fileInfo);
			} else {
				ArrayList<FileInfo> fileInfoList = new ArrayList<FileInfo>();
				map.put(dirName, fileInfoList);
				fileInfoList.add(fileInfo);
			}
		}

		return map;
	}

	public static void save(File targetDir,
			HashMap<String, ArrayList<FileInfo>> map) {
		for (String dirName : map.keySet()) {
			File dir = new File(targetDir, dirName);
			dir.mkdir();

			ArrayList<FileInfo> fileInfoList = map.get(dirName);
			for (FileInfo fileInfo : fileInfoList) {
				File sourceFile = fileInfo.getFile();
				File targetFile = new File(dir, sourceFile.getName());
				
				try {
					BufferedInputStream inputStream = new BufferedInputStream(
							new FileInputStream(sourceFile));
					BufferedOutputStream outputStream = new BufferedOutputStream(
							new FileOutputStream(targetFile));

					int binary;
					while ((binary = inputStream.read()) != -1) {
						outputStream.write(binary);
					}

					inputStream.close();
					outputStream.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
				
				targetFile.setLastModified(sourceFile.lastModified());
			}
		}
	}

	public static void main(String[] args) throws Exception {
		JFileChooser sourceDirChooser = new JFileChooser();
		sourceDirChooser.setDialogTitle("Choose source directory");
		sourceDirChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		sourceDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		sourceDirChooser.showOpenDialog(null);
		File sourceDir = sourceDirChooser.getSelectedFile();

		JFileChooser targetDirChooser = new JFileChooser();
		targetDirChooser.setDialogTitle("Choose target directory");
		targetDirChooser.setDialogType(JFileChooser.OPEN_DIALOG);
		targetDirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		targetDirChooser.showOpenDialog(null);
		File targetDir = targetDirChooser.getSelectedFile();

		String options[] = new String[] { "Day", "Month", "Year" };
		String defaultOption = "Day";
		int mode = JOptionPane.showOptionDialog(null, "Choose cluster mode",
				"Option", 0, JOptionPane.INFORMATION_MESSAGE, null, options,
				defaultOption);

		HashMap<String, ArrayList<FileInfo>> map = cluster(sourceDir, mode);
		save(targetDir, map);

		JOptionPane.showMessageDialog(null, "Done", "Message",
				JOptionPane.INFORMATION_MESSAGE);
	}
}
