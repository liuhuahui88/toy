package lp.dev;

public class DoubleMatrix<N> extends Matrix<N, Double> {

	public void scale(Double s) {
		for (int i = 0; i < numRows(); i++) {
			scaleRow(i, s);
		}
	}
	
	public void scaleRow(int i, Double s) {
		for (int j = 0; j < numCols(); j++) {
			set(i, j, get(i, j) * s);
		}
	}
	
	public void addRow(int f, int t, Double s) {
		for (int j = 0; j < numCols(); j++) {
			Double fv = get(f, j);
			Double tv = get(t, j);
			set(t, j, tv + fv * s);
		}
	}
}
