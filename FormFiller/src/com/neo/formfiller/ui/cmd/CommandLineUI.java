package com.neo.formfiller.ui.cmd;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import com.neo.formfiller.ui.AbstractUI;

public class CommandLineUI implements AbstractUI {
	
	private static final String SYSTEM_USERNAME_PROMPT = "username: ";
	private static final String SYSTEM_PASSWORD_PROMPT = "password: ";
	private static final String SYSTEM_CODE_PROMPT = "code: ";

	private static final String CLASS_INDEX_PROMPT = "index: ";
	private static final String CLASS_PASSWORD_PROMPT = "password: ";
	
	private static final String RECORD_FILE_PROMPT = "path: ";

	@Override
	public void showMessage(String message) {
		System.out.println(message);
	}

	@Override
	public SystemLoginInfo getSystemLoginInfo(Image codeImage) {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in));
		SystemLoginInfo info = new SystemLoginInfo();
		try {
			System.out.print(SYSTEM_USERNAME_PROMPT);
			info.username = reader.readLine();
			System.out.print(SYSTEM_PASSWORD_PROMPT);
			info.password = reader.readLine();
			System.out.print(SYSTEM_CODE_PROMPT);
			info.code = reader.readLine();
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return info;
	}

	@Override
	public ClassLoginInfo getClassLoginInfo(List<String> classnameList) {
		int currentIndex = 0;
		for (String classname : classnameList) {
			currentIndex++;
			System.out.println(currentIndex + ") " + classname);
		}
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in));
		ClassLoginInfo info = new ClassLoginInfo();
		try {
			System.out.print(CLASS_INDEX_PROMPT);
			int index = Integer.valueOf(reader.readLine());
			info.classname = classnameList.get(index - 1);
			System.out.print(CLASS_PASSWORD_PROMPT);
			info.password = reader.readLine();
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return info;
	}

	@Override
	public File getRecordFile(String classname) {
		System.out.println("*) " + classname);
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(System.in));
		try {
			System.out.print(RECORD_FILE_PROMPT);
			String path = reader.readLine();
			return new File(path);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
