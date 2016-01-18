package com.neo.formfiller.ui;

import java.awt.Image;
import java.io.File;
import java.util.List;

public interface AbstractUI {
	
	public static class SystemLoginInfo {
		
		public String username;
		public String password;
		public String code;
		
		@Override
		public String toString() {
			return getClass().getSimpleName() +
					" = { username = " + username +
					"; password = " + password +
					"; code = " + code +
					"; }";
		}
	}
	
	public static class ClassLoginInfo {
		
		public String classname;
		public String password;
		
		@Override
		public String toString() {
			return getClass().getSimpleName() +
					" = { classname = " + classname +
					"; password = " + password +
					"; }";
		}
	}
	
	public void showMessage(String message);

	public SystemLoginInfo getSystemLoginInfo(Image codeImage);
	
	public ClassLoginInfo getClassLoginInfo(List<String> classnameList);
	
	public File getRecordFile(String classname);
}
