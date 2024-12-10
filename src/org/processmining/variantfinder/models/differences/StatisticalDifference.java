package org.processmining.variantfinder.models.differences;

import javax.swing.ImageIcon;

import org.processmining.variantfinder.models.trees.RuleSet;

public class StatisticalDifference {

	private Object tsElement;
	private double frequency;
	private RuleSet rules;

	public StatisticalDifference(Object tsElement, double frequency, RuleSet rules) {
		this.tsElement = tsElement;
		this.frequency = frequency;
		this.rules = rules;
	}

	public Object getTsElement() {
		return tsElement;
	}

	public double getFrequency() {
		return frequency;
	}

	public RuleSet getRules() {
		return rules;
	}

	public ImageIcon getPlot() {
		return rules.getPlot();
	}

}
