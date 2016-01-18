package com.neo.formfiller.record;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;

public class Record {

    public final String name;
    public final String ps;
    public final String qz;
    public final String qm;
    public final String sy;
    public final String dbz;

    public Record() {
    	this(null, null, null, null, null, null);
    }

    public Record(String name, String ps, String qz, String qm,
    		String sy, String dbz) {
        this.name = name;
        this.ps = ps;
        this.qz = qz;
        this.qm = qm;
        this.sy = sy;
        this.dbz = dbz;
    }
    
    @Override
    public String toString() {
    	return getClass().getSimpleName() +
				" = { name = " + name +
				"; ps = " + ps +
				"; qz = " + qz +
				"; qm = " + qm +
				"; sy = " + sy +
				"; dbz = " + dbz +
				"; }";
    }

    public static List<Record> getRecords(File excel) {
        List<Record> records = new ArrayList<Record>();
		try {
			Workbook wb = Workbook.getWorkbook(excel);
			Sheet sht = wb.getSheet(0);
			Map<String, Integer> columnMap = getColumnMap(sht);
			int numRows = sht.getRows();
			for (int i = 1; i < numRows; i++) {
				Record record = getRecord(sht, i, columnMap);
				if (record.name != null && !record.name.isEmpty()) {
					records.add(record);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return records;
    }
    
    private static Map<String, Integer> getColumnMap(Sheet sht) {
		int numColumns = sht.getColumns();
		Map<String, Integer> columnMap = new HashMap<String, Integer>();
		for (int i = 0; i < numColumns; i++) {
			columnMap.put(sht.getCell(i, 0).getContents(), i);
		}
		return columnMap;
    }
    
    private static Record getRecord(Sheet sht, int row,
    		Map<String, Integer> columnMap) {
		String name = sht.getCell(columnMap.get("姓名"), row).getContents();
		String ps = sht.getCell(columnMap.get("平时成绩"), row).getContents();
		String qz = sht.getCell(columnMap.get("期中成绩"), row).getContents();
		String qm = sht.getCell(columnMap.get("期末成绩"), row).getContents();
		String sy = sht.getCell(columnMap.get("实验成绩"), row).getContents();
		String dbz = sht.getCell(columnMap.get("备注"), row).getContents();
		return new Record(name, ps, qz, qm, sy, dbz);
    }
    
    public static void main(String args[]) {
    	List<Record> records = getRecords(new File("template/template.xls"));
    	for (Record record : records) {
    		System.out.println(record);
    	}
    }
}