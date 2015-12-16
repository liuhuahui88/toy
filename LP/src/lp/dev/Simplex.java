package lp.dev;

import java.util.HashMap;

public class Simplex {
	
	public static final Double EPS = 1e-10;
	
	public int r = 0;
	public static final Integer C_ROW_NAME = -1;
	public static final Integer B_COL_NAME = -2;
	
	private Dictionary dict;
	
	public static enum State {
		INFEASIBLE, UNBOUNDED, OPTIMAL, SOLVING;
	}
	
	public Simplex(Dictionary dict) {
		this.dict = dict;
	}
	
	public State optimizeIP() {
		State state = optimize();
		while (state == State.OPTIMAL && addCuts() != 0) {
			state = solveDual();
		}
		return state;
	}
	
	public State optimize() {
		if (needsInit() && init() == State.INFEASIBLE) {
			return State.INFEASIBLE;
		}
		return solvePrimal();
	}
	
	public boolean needsInit() {
		for (int i = 1; i < dict.numRows(); i++) {
			if (dict.getB(i) < 0) {
				return true;
			}
		}
		return false;
	}
	
	public State init() {
		Double oldZ = dict.getZ();

		// save old c and make c negative so that dual will be feasible
		HashMap<Integer, Double> cMap = new HashMap<Integer, Double>();
		for (int j = 1; j < dict.numCols(); j++) {
			cMap.put(dict.getColName(j), dict.getC(j));
			dict.setC(j, -1D);
		}

		State state = solveDual();
		if (state != State.OPTIMAL) {
			return state;
		}

		dict.setZ(oldZ);

		for (int j = 1; j < dict.numCols(); j++) {
			Double c = cMap.get(dict.getColName(j));
			dict.setC(j, c == null ? 0D : c);
		}
		for (int i = 1; i < dict.numRows(); i++) {
			Double c = cMap.get(dict.getRowName(i));
			if (c != null) {
				dict.addRow(i, 0, c);				
			}
		}

		return State.SOLVING;
	}
	
	public State solvePrimal() {
		r++;
		State state;
		while((state = iter()) == State.SOLVING) ;
		return state;
	}
	
	public State solveDual() {
		dict.dualize();
		State state = solvePrimal();
		dict.dualize();
		if (state == State.INFEASIBLE) return State.UNBOUNDED;
		else if (state == State.UNBOUNDED) return State.INFEASIBLE;
		else return State.OPTIMAL;
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
		dict.pivot(enterIndex, leaveIndex);
		return State.SOLVING;
	}
	
	public int selectEnterIndex() {
		int enterIndex = -1;
		Integer name = null;
		for (int j = 1; j < dict.numCols(); j++) {
			Integer nonBasicName = dict.getColName(j);
			if (dict.getC(j) > 0 && (name == null ||
					nonBasicName.compareTo(name) < 0)) {
				enterIndex = j;
				name = nonBasicName;
			}
		}
		return enterIndex;
	}
	
	public int selectLeaveIndex(int enterIndex) {
		int leaveIndex = -1;
		Integer name = null;
		double minNewB = Double.POSITIVE_INFINITY;
		for (int i = 1; i < dict.numRows(); i++) {
			Integer basicName = dict.getRowName(i);
			double newB = calcNewB(i, enterIndex);
			if (newB < minNewB || (newB == minNewB && name != null &&
					basicName.compareTo(name) < 0)) {
				leaveIndex = i;
				name = basicName;
				minNewB = newB;
			}
		}
		return leaveIndex;
	}
	
	public int addCuts() {
		int numCuts = 0;
		int offset = dict.numRows() + dict.numCols() - 1;
		int numOldRows = dict.numRows();
		for (int i = 1; i < numOldRows; i++) {
			double b = dict.getB(i);
			if (!isInteger(b)) {
				Integer name = offset + numCuts;
				int row = dict.newRow(name, 0D);
				dict.setB(row, -fraction(b));
				for (int j = 1; j < dict.numCols(); j++) {
					double a = dict.get(i, j);
					dict.set(row, j, fraction(-a));
				}
				numCuts++;
			}
		}
		return numCuts;
	}
	
	private Double calcNewB(int i, int j) {
		Double a = dict.get(i, j);
		return (a >= 0) ? Double.POSITIVE_INFINITY : (-dict.getB(i) / a);
	}
	
	private Double fraction(Double d) {
		if (isInteger(d)) return 0D;
		else return d - Math.floor(d);
	}
	
	private boolean isInteger(Double a) {
		return eq(a, Double.valueOf(Math.round(a)));
	}
	
	private boolean eq(Double a, Double b) {
		return a - EPS <= b && b < a + EPS;
	}

	public static void main(String[] args) {
		Dictionary dict = new Dictionary();

		dict.newCol(-1, 0D);
		dict.newCol(1, 0D);
		dict.newCol(2, 0D);
		
		dict.newRow(-2, 0D);
		dict.newRow(3, 0D);
		dict.newRow(4, 0D);
		dict.newRow(5, 0D);
		
		dict.setZ(0D);

		dict.setB(1, 3D);
		dict.setB(2, -1D);
		dict.setB(3, -1D);

		dict.setC(1, 2D);
		dict.setC(2, 1D);

		dict.set(1, 1, -1D);
		dict.set(1, 2, -1D);
		dict.set(2, 1, 1D);
		dict.set(3, 2, 1D);
		
		dict.print();

		Simplex s = new Simplex(dict);
		System.out.println(s.optimize());
		dict.print();
	}
}
