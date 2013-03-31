package com.neo.hmm;

import com.neo.util.Transformer;

public class ZeroOrderHMM implements HMM {

	private TransmitionModel transmitionModel;
	private Transformer transformer;
	
	public ZeroOrderHMM(TransmitionModel transmitionModel,
			Transformer transformer) {
		this.transmitionModel = transmitionModel;
		this.transformer = transformer;
	}
	
	public int getOrder() {
		return 0;
	}
	
	public double decode(Sequence sequence) {
		double logProb = 0;
		for (Token token : sequence.tokens) {
			String transformedObservation =
				transformer.transform(token.observation);
			double maxSubProb = 0.0;
			for (String status : transmitionModel.getStatusSet()) {
				double subProb = transmitionModel.getProb(
						status, transformedObservation);
				if (subProb >= maxSubProb) {
					maxSubProb = subProb;
					token.status = status;
				}
			}
			logProb += Math.log(maxSubProb);
		}
		return logProb;
	}
}
