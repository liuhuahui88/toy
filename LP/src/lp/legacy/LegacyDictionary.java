package lp.legacy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class LegacyDictionary {
	
	public int numBasic;
	public int numNonBasic;
	
	public int basicIDs[];
	public int nonBasicIDs[];
	
	// 2 basic & 3 non-basic
	//
	// a00  a01  a02  b0
	// a10  a11  a12  b1
	// c0   c1   c2   z
	
	public double m[][];
	
	public LegacyDictionary(int numBasic, int numNonBasic) {
		this.numBasic = numBasic;
		this.numNonBasic = numNonBasic;
		
		basicIDs = new int[numBasic];
		nonBasicIDs = new int[numNonBasic];
		m = new double[numBasic + 1][numNonBasic + 1];
	}
	
	public static enum State {
		INFEASIBLE, UNBOUNDED, OPTIMAL, SOLVING;
	}
	
	public LegacyDictionary dualize() {
		LegacyDictionary dict = new LegacyDictionary(numNonBasic, numBasic);
		System.arraycopy(basicIDs, 0, dict.nonBasicIDs, 0, numBasic);
		System.arraycopy(nonBasicIDs, 0, dict.basicIDs, 0, numNonBasic);
		for (int i = 0; i <= numBasic; i++) {
			for (int j = 0; j <= numNonBasic; j++) {
				dict.m[j][i] = -m[i][j];
			}
		}
		return dict;
	}
	
	public void print() {
		System.out.printf("numBasic = %d\n", numBasic);
		System.out.printf("numNonBasic = %d\n", numNonBasic);
		for (int i = 0; i < numBasic; i++) {
			System.out.printf("x%d = %+3.4f", basicIDs[i], getB(i));
			for (int j = 0; j < numNonBasic; j++) {
				System.out.printf(" %+3.4fx%d", getA(i, j), nonBasicIDs[j]);
			}
			System.out.println();
		}
		System.out.printf("Z  = %+3.4f", getZ());
		for (int j = 0; j < numNonBasic; j++) {
			System.out.printf(" %+3.4fx%d", getC(j), nonBasicIDs[j]);
		}
		System.out.println();
	}
	
	public State optimize() {
		if (needsInit() && init() == State.INFEASIBLE) {
			return State.INFEASIBLE;
		}
		return solve();
	}
	
	public boolean needsInit() {
		for (int i = 0; i < numBasic; i++) {
			if (getB(i) < 0) {
				return true;
			}
		}
		return false;
	}
	
	public State init() {
		LegacyDictionary dualDict = dualize();
		
		// make the dual dictionary feasible
		for (int i = 0; i < dualDict.numBasic; i++) {
			dualDict.setB(i, 1);
		}
		

		if (dualDict.solve() == State.UNBOUNDED) {
			return State.INFEASIBLE;
		}
		
		LegacyDictionary primalDict = dualDict.dualize();
		
		// recover objective

		// recover z0
		primalDict.setZ(getZ());

		// clear c vector
		for (int i = 0; i < numNonBasic; i++) {
			primalDict.setC(i, 0);
		}
		
		// recover original non basic which is still non basic
		for (int i = 0; i < numNonBasic; i++) {
			int id = nonBasicIDs[i];
			for (int j = 0; j < numNonBasic; j++) {
				if (id == primalDict.nonBasicIDs[j]) {
					primalDict.setC(j, getC(i));
					break;
				}
			}
		}
		
		// recover original non basic which is now basic
		for (int i = 0; i < numNonBasic; i++) {
			int id = nonBasicIDs[i];
			for (int j = 0; j < numBasic; j++) {
				if (id == primalDict.basicIDs[j]) {
					primalDict.add(j, numBasic, getC(i));
					break;
				}
			}
		}

		m = primalDict.m;
		basicIDs = primalDict.basicIDs;
		nonBasicIDs = primalDict.nonBasicIDs;

		return State.SOLVING;
	}
	
	public State solve() {
		State state;
		while((state = iter()) == State.SOLVING) ;
		return state;
	}
	
	public State iter() {
		int enterIndex = selectEnterIndex();
		if (enterIndex < 0) {
			return State.OPTIMAL;
		}
		int leaveIndex = selectLeaveIndex(enterIndex);
		if (leaveIndex < 0) {
			return State.UNBOUNDED;
		}
		pivot(enterIndex, leaveIndex);
		return State.SOLVING;
	}
	
	public int selectEnterIndex() {
		int enterIndex = -1;
		int id = Integer.MAX_VALUE;
		for (int j = 0; j < numNonBasic; j++) {
			if (getC(j) > 0 && nonBasicIDs[j] < id) {
				enterIndex = j;
				id = nonBasicIDs[j];
			}
		}
		return enterIndex;
	}
	
	public int selectLeaveIndex(int enterIndex) {
		int leaveIndex = -1;
		int id = Integer.MIN_VALUE;
		double minNewB = Double.POSITIVE_INFINITY;
		for (int i = 0; i < numBasic; i++) {
			double newB = calcNewB(i, enterIndex);
			if (newB < minNewB || newB == minNewB && basicIDs[i] < id) {
				leaveIndex = i;
				id = basicIDs[i];
				minNewB = newB;
			}
		}
		return leaveIndex;
	}
	
	public void pivot(int enterIndex, int leaveIndex) {
		exchange(enterIndex, leaveIndex);
		for (int i = 0; i <= numBasic; i++) {
			if (i != leaveIndex) {
				substitute(enterIndex, leaveIndex, i);
			}
		}
		int id = basicIDs[leaveIndex];
		basicIDs[leaveIndex] = nonBasicIDs[enterIndex];
		nonBasicIDs[enterIndex] = id;
	}
	
	public double getZ() {
		return m[numBasic][numNonBasic];
	}
	
	public void setZ(double value) {
		m[numBasic][numNonBasic] = value;
	}
	
	public double getC(int j) {
		return m[numBasic][j];
	}
	
	public void setC(int j, double value) {
		m[numBasic][j] = value;
	}
	
	public double getB(int i) {
		return m[i][numNonBasic];
	}
	
	public void setB(int i, double value) {
		m[i][numNonBasic] = value;
	}
	
	public double getA(int i, int j) {
		return m[i][j];
	}
	
	public void setA(int i, int j, double value) {
		m[i][j] = value;
	}
	
	private double calcNewB(int i, int j) {
		double a = getA(i, j);
		return (a >= 0) ? Double.POSITIVE_INFINITY : (getB(i) / -a);
	}
	
	private void exchange(int enterIndex, int leaveIndex) {
		double coef = getA(leaveIndex, enterIndex);
		setA(leaveIndex, enterIndex, -1);
		divide(leaveIndex, -coef);
	}
	
	private void substitute(int enterIndex, int leaveIndex, int row) {
		double coef = getA(row, enterIndex);
		setA(row, enterIndex, 0);
		add(leaveIndex, row, coef);
	}
	
	private void divide(int row, double coef) {
		for (int j = 0; j <= numNonBasic; j++) {
			m[row][j] /= coef;
		}
	}
	
	private void add(int from, int to, double coef) {
		for (int j = 0; j <= numNonBasic; j++) {
			m[to][j] += coef * m[from][j]; 
		}
	}
	
	public static LegacyDictionary create(File file) {
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
	
	public static LegacyDictionary create(String content) {
		String lines[] = content.split("\n");
		
		String size[] = lines[0].trim().split("\\s+");
		int numBasic = Integer.parseInt(size[0]);
		int numNonBasic = Integer.parseInt(size[1]);
		
		LegacyDictionary dict = new LegacyDictionary(numBasic, numNonBasic);
		
		String basicIDs[] = lines[1].trim().split("\\s+");
		for (int i = 0; i < basicIDs.length; i++) {
			dict.basicIDs[i] = Integer.parseInt(basicIDs[i]);
		}
		
		String nonBasicIDs[] = lines[2].trim().split("\\s+");
		for (int i = 0; i < nonBasicIDs.length; i++) {
			dict.nonBasicIDs[i] = Integer.parseInt(nonBasicIDs[i]);
		}
		
		String bs[] = lines[3].trim().split("\\s+");
		for (int i = 0; i < bs.length; i++) {
			dict.setB(i, Double.parseDouble(bs[i]));
		}
		
		for (int i = 0; i < numBasic; i++) {
			String as[] = lines[4 + i].trim().split("\\s+");
			for (int j = 0; j < as.length; j++) {
				dict.setA(i, j, Double.parseDouble(as[j]));
			}
		}
		
		String cs[] = lines[4 + numBasic].trim().split("\\s+");
		dict.setZ(Double.parseDouble(cs[0]));
		for (int i = 0; i < numNonBasic; i++) {
			dict.setC(i, Double.parseDouble(cs[i + 1]));
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
	
	public static void main(String args[]) {
		LegacyDictionary dict = new LegacyDictionary(3, 2);
		dict.basicIDs[0] = 3;
		dict.basicIDs[1] = 4;
		dict.basicIDs[2] = 5;
		dict.nonBasicIDs[0] = 1;
		dict.nonBasicIDs[1] = 2;
		dict.setB(0, 3);
		dict.setB(1, -1);
		dict.setB(2, -1);
		dict.setC(0, 2);
		dict.setC(1, 1);
		dict.setA(0, 0, -1);
		dict.setA(0, 1, -1);
		dict.setA(1, 0, 1);
		dict.setA(2, 1, 1);
		dict.print();
		
		System.out.println(dict.optimize());
		dict.print();
	}
}
