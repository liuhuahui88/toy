package lp.dev;

import java.util.ArrayList;

public class Matrix<N, T> {

	private boolean isTransposed = false;

	private int numRows = 0;
	private int numCols = 0;

	private ArrayList<ArrayList<T>> m = new ArrayList<ArrayList<T>>();

	private ArrayList<N> rowNames = new ArrayList<N>();
	private ArrayList<N> colNames = new ArrayList<N>();
	
	public final void transpose() {
		isTransposed = !isTransposed;
	}
	
	public final int newRow(N rowName, T v) {
		return isTransposed ? innerNewCol(rowName, v) : innerNewRow(rowName, v);		
	}
	
	public final int newCol(N colName, T v) {
		return isTransposed ? innerNewRow(colName, v) : innerNewCol(colName, v);
	}
	
	public final int numRows() {
		return isTransposed ? numCols : numRows;
	}
	
	public final int numCols() {
		return isTransposed ? numRows : numCols;
	}
	
	public final T get(int i, int j) {
		return isTransposed ? innerGet(j, i) : innerGet(i, j);
	}
	
	public final void set(int i, int j, T elem) {
		if (isTransposed) innerSet(j, i, elem);
		else innerSet(i, j, elem);
	}
	
	public final N getRowName(int i) {
		return isTransposed ? colNames.get(i) : rowNames.get(i);
	}
	
	public final void setRowName(int i, N rowName) {
		if (isTransposed) colNames.set(i, rowName);
		else rowNames.set(i, rowName);
	}
	
	public final N getColName(int j) {
		return isTransposed ? rowNames.get(j) : colNames.get(j);
	}
	
	public final void setColName(int j, N colName) {
		if (isTransposed) rowNames.set(j, colName);
		else colNames.set(j, colName);
	}
	
	private static <T> void expend(ArrayList<T> list, int n, T elem) {
		for (int i = 0; i < n; i++) {
			list.add(elem);
		}
	}
	
	private T innerGet(int i, int j) {
		return m.get(i).get(j);
	}
	
	private void innerSet(int i, int j, T elem) {
		m.get(i).set(j, elem);
	}
	
	private int innerNewRow(N rowName, T v) {
		ArrayList<T> row = new ArrayList<T>();
		expend(row, numCols, v);
		m.add(row);
		rowNames.add(rowName);
		return numRows++;
	}
	
	private int innerNewCol(N colName, T v) {
		for (int i = 0; i < numRows; i++) {
			expend(m.get(i), 1 , v);
		}		
		colNames.add(colName);
		return numCols++;
	}
}
