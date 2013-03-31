package com.lhh.cggf.algorithm;

// Pearson Distance
public class PearsonDistance {

	// Calculate Pearson Distance
	public static double calc(double[] X, double[] Y) {
		double xSum = 0; // Sum(x[i])
		double ySum = 0; // Sum(y[i])
		double xySum = 0; // Sum(x[i]*y[i])
		double xxSum = 0; // Sum(x[i]*x[i])
		double yySum = 0; // Sum(y[i]*y[i])

		// Number Of Pairs
		int N = X.length;
		for (int i = 0; i < N; i++) {
			xSum += X[i];
			ySum += Y[i];
			xySum += X[i] * Y[i];
			xxSum += X[i] * X[i];
			yySum += Y[i] * Y[i];
		}
		
		double up = xySum - xSum * ySum / N;
		double down = Math.sqrt((xxSum - xSum * xSum / N)
				* (yySum - ySum * ySum / N));
		return up / down;
	}

	public static void main(String[] args) {
		double[] X = new double[] { 1, 2, 3 };
		double[] Y = new double[] { 0.33, 0.66, 1 };
		System.out.println(calc(X, Y));
	}
}
