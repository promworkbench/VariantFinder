package org.processmining.variantfinder.parameters;

import org.processmining.plugins.transitionsystem.miner.TSMinerInput;

public class TsSettings {

	private TSMinerInput tsSettings;
	private boolean isThresholdEnabled, isShowTransitionLabelsEnabled;
	private double frequencyThreshold;

	public TsSettings(TSMinerInput input, boolean isThresholdEnabled, double frequencyThreshold,
			boolean isShowTransitionLabelsEnabled) {
		tsSettings = input;
		this.isThresholdEnabled = isThresholdEnabled;
		this.frequencyThreshold = frequencyThreshold;
		this.isShowTransitionLabelsEnabled = isShowTransitionLabelsEnabled;
	}

	public TSMinerInput getTsAbstractions() {
		return tsSettings;
	}

	public boolean isThresholdEnabled() {
		return isThresholdEnabled;
	}

	public boolean isShowTransitionLabelsEnabled() {
		return isShowTransitionLabelsEnabled;
	}

	public double getFrequencyThreshold() {
		return frequencyThreshold;
	}

	public boolean isEqual(TsSettings o1) {
		return (isThresholdEnabled == o1.isThresholdEnabled
				&& isShowTransitionLabelsEnabled == o1.isShowTransitionLabelsEnabled
				&& frequencyThreshold == o1.frequencyThreshold);
	}

	public boolean isTsEqual(TsSettings o1) {
		return tsSettings.equals(o1.getTsAbstractions());
	}

}
