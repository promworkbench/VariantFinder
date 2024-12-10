package org.processmining.variantfinder.models.trees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.ImageIcon;

import org.apache.commons.lang3.tuple.Triple;

public class RuleSet {

	private List<Rule> rules;
	private ImageIcon plot;

	public RuleSet(String[] treeRules, ImageIcon plot) {
		rules = new ArrayList<Rule>();

		for (String line : treeRules)
			if (line != null && !line.isEmpty() && !line.trim().isEmpty()) {
				line = line.replace("%in%", "=");
				line = line.replace("c(", "");
				line = line.replace("\\\"", "");
				line = line.replace("\\", "");

				line = line.replace(")", "");
				String[] firstSplit = line.split("\"");
				for (String f : firstSplit) {
					Rule newRule = new Rule(f.split("&"));
					if (newRule != null && !newRule.getAttributeAsString().isEmpty()
							&& !newRule.getSplitValuesAsString().isEmpty()) {
						rules.add(newRule);
					}
				}
			}
		this.plot = plot;
	}

	@Override
	public String toString() {
		String result = "";
		int i = 0;
		for (Rule rule : rules) {
			result = result + "Rule " + i + ": ";
			for (Triple<String, String, String> condition : rule.getConditions()) {
				result = result + condition.getLeft() + " " + condition.getMiddle() + " " + condition.getRight()
						+ " & ";
			}
			result = result + "\n";
			i++;
		}

		return result;
	}

	public List<Rule> getRules() {
		return rules;
	}

	public ImageIcon getPlot() {
		return plot;
	}

	/**
	 * 
	 * @return a map that binds attributes (key) with sets of splitting values
	 *         (value)
	 */
	public Map<String, Set<String>> getSplittingValues() {

		Map<String, Set<String>> attributeSplitValues = new HashMap<String, Set<String>>();

		for (Rule rule : rules)
			for (Triple<String, String, String> condition : rule.getConditions()) {
				if (!attributeSplitValues.containsKey(condition.getLeft()))
					attributeSplitValues.put(condition.getLeft(), new TreeSet<String>());
				attributeSplitValues.get(condition.getLeft()).add(condition.getRight());
			}
		return attributeSplitValues;
	}

}
