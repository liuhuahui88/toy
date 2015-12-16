package lp.dev;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Dictionary extends DoubleMatrix<Integer> {

	private boolean isDualized = false;
	
	public void dualize() {
		transpose();
		scale(-1D);
		isDualized = !isDualized;
	}
	
	public void pivot(int enterIndex, int leaveIndex) {
		exchange(enterIndex, leaveIndex);
		for (int i = 0; i < numRows(); i++) {
			if (i != leaveIndex) {
				substitute(enterIndex, leaveIndex, i);
			}
		}
		Integer name = getRowName(leaveIndex);
		setRowName(leaveIndex, getColName(enterIndex));
		setColName(enterIndex, name);
	}
	
	public Double getZ() {
		return get(0, 0);
	}
	
	public void setZ(Double d) {
		set(0, 0, d);
	}
	
	public Double getB(int i) {
		return get(i, 0);
	}
	
	public void setB(int i, Double d) {
		set(i, 0, d);
	}
	
	public Double getC(int j) {
		return get(0, j);
	}
	
	public void setC(int j, Double d) {
		set(0, j, d);
	}
	
	private void exchange(int enterIndex, int leaveIndex) {
		Double a = get(leaveIndex, enterIndex);
		set(leaveIndex, enterIndex, -1D);
		scaleRow(leaveIndex, -1D / a);
	}
	
	private void substitute(int enterIndex, int leaveIndex, int i) {
		Double a = get(i, enterIndex);
		set(i, enterIndex, 0D);
		addRow(leaveIndex, i, a);
	}
	
	public void print() {
		System.out.printf("numBasic = %d\n", numRows() - 1);
		System.out.printf("numNonBasic = %d\n", numCols() - 1);
		for (int i = 1; i < numRows(); i++) {
			System.out.printf("x%d = %+3.4f", getRowName(i), getB(i));
			for (int j = 1; j < numCols(); j++) {
				System.out.printf(" %+3.4fx%d", get(i, j), getColName(j));
			}
			System.out.println();
		}
		System.out.printf("Z  = %+3.4f", getZ());
		for (int j = 1; j < numCols(); j++) {
			System.out.printf(" %+3.4fx%d", getC(j), getColName(j));
		}
		System.out.println();
	}
	
	public static Dictionary create(File file) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
			br.close();
			return create(sb.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	public static Dictionary create(String content) {
		String lines[] = content.split("\n");
		
		String size[] = lines[0].trim().split("\\s+");
		int numBasic = Integer.parseInt(size[0]);
		int numNonBasic = Integer.parseInt(size[1]);
		
		Dictionary dict = new Dictionary();
		dict.newRow(-1, 0D);
		dict.newCol(-2, 0D);
		
		String basicIDs[] = lines[1].trim().split("\\s+");
		for (int i = 0; i < basicIDs.length; i++) {
			dict.newRow(Integer.parseInt(basicIDs[i]), 0D);
		}
		
		String nonBasicIDs[] = lines[2].trim().split("\\s+");
		for (int i = 0; i < nonBasicIDs.length; i++) {
			dict.newCol(Integer.parseInt(nonBasicIDs[i]), 0D);
		}
		
		String bs[] = lines[3].trim().split("\\s+");
		for (int i = 0; i < bs.length; i++) {
			dict.setB(i + 1, Double.parseDouble(bs[i]));
		}
		
		for (int i = 0; i < numBasic; i++) {
			String as[] = lines[4 + i].trim().split("\\s+");
			for (int j = 0; j < as.length; j++) {
				dict.set(i + 1, j + 1, Double.parseDouble(as[j]));
			}
		}
		
		String cs[] = lines[4 + numBasic].trim().split("\\s+");
		dict.setZ(Double.parseDouble(cs[0]));
		for (int i = 0; i < numNonBasic; i++) {
			dict.setC(i + 1, Double.parseDouble(cs[i + 1]));
		}
		
		return dict;
	}
	
	public static void write(File file, String content) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(content);
			bw.close();
		} catch (Exception ex) {
			
		}
	}
}
