package org.processmining.variantfinder.models;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.models.graphbased.directed.transitionsystem.Transition;
import org.processmining.variantfinder.models.differences.StatisticalDifference;

public class SplitCriteriaRow {

	public static final String STATE = "State", TRANSITION = "Transition";

	private String type;
	private String name;
	private double frequency;
	private Object tsElement;
	private String attributes;
	private String splitValues;

	public SplitCriteriaRow(StatisticalDifference sd) {

		this.tsElement = sd.getTsElement();
		if (tsElement instanceof State) {
			type = STATE;
			name = ((State) tsElement).getLabel();
		} else {
			type = TRANSITION;
			name = ((Transition) tsElement).getLabel();
		}

		this.frequency = sd.getFrequency();

		Map<String, Set<String>> conditions = sd.getRules().getSplittingValues();
		Set<String> att = new TreeSet<String>();
		att.addAll(conditions.keySet());
		if (att.size() > 1) {
			attributes = "(see details panel)";
			splitValues = "(see details panel)";
		} else {
			attributes = att.iterator().next();
			splitValues = conditions.get(attributes).toString();
		}
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public double getFrequency() {
		return frequency;
	}

	public Object getTsElement() {
		return tsElement;
	}

	public String getSplitValues() {
		return splitValues;
	}

	public String getSplitAttribute() {
		return attributes;
	}

}
