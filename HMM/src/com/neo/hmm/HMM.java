package com.neo.hmm;

public interface HMM {
	
	public int getOrder();

	public double decode(Sequence sequence);
}
