package com.neo.formfiller;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.neo.formfiller.client.Client;
import com.neo.formfiller.record.Record;
import com.neo.formfiller.ui.AbstractUI;
import com.neo.formfiller.ui.AbstractUI.ClassLoginInfo;
import com.neo.formfiller.ui.AbstractUI.SystemLoginInfo;
import com.neo.formfiller.ui.cmd.CommandLineUI;
import com.neo.formfiller.ui.graphic.GraphicUI;
import com.neo.formfiller.util.Parser;

public class FormFiller {
	
	private Client client;
	
	public FormFiller(String host) {
		client = new Client(host);
	}
	
	public void run() {
		AbstractUI ui = new GraphicUI();
//		AbstractUI ui = new CommandLineUI();
		
		ui.showMessage("Welcome!");
		
		String root = getRoot();
		System.out.println(root);
		if (root == null) {
			ui.showMessage("Can't find root! Abort!");
			System.exit(0);
		}
		
		String mainPath = null;
		while (mainPath == null) {
			Image codeImage = getCodeImage(root);
			System.out.println(codeImage);

			SystemLoginInfo systemLoginInfo = ui.getSystemLoginInfo(codeImage);
			System.out.println(systemLoginInfo);
			if (systemLoginInfo == null) {
				System.exit(0);
			}
			
			mainPath = loginSystem(root, systemLoginInfo);
			System.out.println(mainPath);
			if (mainPath == null) {
				ui.showMessage("System login failed! Retry!");
			}
		}
		
		Map<String, String> classes = extractClasses(root, mainPath);
		System.out.println(classes);
		if (classes.isEmpty()) {
			ui.showMessage("Can't find classes! Abort!");
			System.exit(0);
		}

		while (true) {
			List<String> classnames = new ArrayList<String>(classes.keySet());
			Collections.sort(classnames);
			ClassLoginInfo classLoginInfo = ui.getClassLoginInfo(classnames);
			System.out.println(classLoginInfo);
			if (classLoginInfo == null) {
				System.exit(0);
			}
			
			String scorePath = loginClass(root, classes, classLoginInfo);
			System.out.println(scorePath);
			if (scorePath == null) {
				ui.showMessage("Class login failed! Retry!");
				continue;
			}
			
			File file = ui.getRecordFile(classLoginInfo.classname);
			System.out.println(file);
			if (file == null) {
				System.exit(0);
			}
			
			List<Record> records = Record.getRecords(file);
			System.out.println(records);
			ui.showMessage(records.size() + " records loaded!");
			
			int result[] = sendScores(scorePath, records);
			ui.showMessage(result[1] + "/" + result[0] + " records sent!");
		}
	}

	private String getRoot() {
		String location = client.getAndFetchLocation("");
		return location.substring(0, location.lastIndexOf("/"));
	}
	
	private Image getCodeImage(String root) {
		String path = root + "/CheckCode.aspx";
		return client.getAndFetchImage(path);
	}
	
	private String loginSystem(String root, SystemLoginInfo info) {
		String path = root + "/default2.aspx";

        String content = client.getAndFetchContent(path);
        String viewState = extractViewState(content);

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		appendParam(parameters, "__VIEWSTATE", viewState);
		appendParam(parameters, "TextBox1", info.username);
		appendParam(parameters, "TextBox2", info.password);
		appendParam(parameters, "TextBox3", info.code);
		appendParam(parameters, "RadioButtonList1", "教师");
		appendParam(parameters, "Button1", "");
		appendParam(parameters, "lbLanguage", "");
		return client.submit(path, parameters);
	}
	
	public Map<String, String> extractClasses(String root, String path) {
        Map<String, String> classPaths = new HashMap<String, String>();
        String subPath = path.replaceAll("js_main.aspx", "js_cjcd.aspx") +
        		"&gnmkdm=N1221";
        String content = client.getAndFetchContent(subPath);
        Parser parser = new Parser(content);
        String key = null;
        String value = null;
        while((value = parser.next("js_cjmm.aspx", "\"")) != null) {
            key = parser.next(">", "</a>").trim() + "--->" + value;
            classPaths.put(key, "js_cjmm.aspx" + value);
        }
        return classPaths;
    }

	private String loginClass(String root, Map<String, String> classes,
			ClassLoginInfo info) {
		String path = root + "/" + classes.get(info.classname);

        String content = client.getAndFetchContent(path);
        String viewState = extractViewState(content);

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		appendParam(parameters, "__VIEWSTATE", viewState);
		appendParam(parameters, "hidLanguage", "");
		appendParam(parameters, "TextBox1", info.password);
		appendParam(parameters, "Button1", "确  定");
		return client.submit(path, parameters);
	}
	
	private int[] sendScores(String path, List<Record> records) {
		Map<String, Record> map = new HashMap<String, Record>();
		for (Record record : records) {
			map.put(record.name, record);
		}

		String content = client.getAndFetchContent(path);
        String viewState = extractViewState(content);

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		appendParam(parameters, "__EVENTTARGET", "");
		appendParam(parameters, "__EVENTARGUMENT", "");
		appendParam(parameters, "__VIEWSTATE", viewState);
		appendParam(parameters, "hidLanguage", "");
		appendParam(parameters, "sfts", "");
		appendParam(parameters, "txtChanged", "0");
		appendParam(parameters, "ddlBJMC", "");
		appendParam(parameters, "jfz", "百分制");
		appendParam(parameters, "psb", "0");
		appendParam(parameters, "qzb", "0");
		appendParam(parameters, "qmb", "100");
		appendParam(parameters, "syb", "0");
		appendParam(parameters, "Dropdownlist1", "百分制");

		Parser parser = new Parser(content);
		String name = null;
		int expectedCount = 0;
		int actualCount = 0;
        while((name = parser.next("<td nowrap=\"nowrap\">", "</td>")) != null) {
            String id = parser.next("DataGrid1:_ctl", ":");
            expectedCount++;
            Record record = map.get(name);
            appendRecord(parameters, id, record);
            if (record != null) {
                actualCount++;
            }
        }
        
        appendParam(parameters, "Button1", "保  存");
		appendParam(parameters, "TextBox1", "");
		
		client.submit(path, parameters);

        return new int[]{expectedCount, actualCount};
	}
	
	private void appendRecord(List<NameValuePair> parameters,
			String id, Record record) {
		String prefix = "DataGrid1:_ctl" + id + ":";
		if (record == null) {
			appendParam(parameters, prefix + "ps", "");
			appendParam(parameters, prefix + "qz", "");
			appendParam(parameters, prefix + "qm", "");
			appendParam(parameters, prefix + "sy", "");
			appendParam(parameters, prefix + "dbz", "");
        } else {
			appendParam(parameters, prefix + "ps", record.ps);
			appendParam(parameters, prefix + "qz", record.qz);
			appendParam(parameters, prefix + "qm", record.qm);
			appendParam(parameters, prefix + "sy", record.sy);
			appendParam(parameters, prefix + "dbz", record.dbz);
        }
	}
	
	private static String extract(String content,
			String prefix, String suffix) {
        return new Parser(content).next(prefix, suffix);
	}

	private static String extractViewState(String content) {
        return extract(content, "name=\"__VIEWSTATE\" value=\"", "\"");
    }
	
	private static void appendParam(List<NameValuePair> parameters,
			String key, String value) {
		parameters.add(new BasicNameValuePair(key, value));
	}
	
	public static void main(String args[]) {
		new FormFiller("202.197.120.56").run();
	}
}
