package org.processmining.variantfinder.models.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.Lists;

public class Rule {

	private List<Triple<String, String, String>> conditions;

	public Rule(String[] treeAsString) {
		conditions = new ArrayList<Triple<String, String, String>>();

		for (String s : treeAsString) {
			Triple<String, String, String> newRule = parseTreeLine(s);
			if (newRule != null && !newRule.getLeft().isEmpty() && !newRule.getRight().isEmpty())
				conditions.add(newRule);
		}
	}

	private Triple<String, String, String> parseTreeLine(String treeLine) {

		if (treeLine != null && !treeLine.isEmpty() && !treeLine.trim().isEmpty()) {
			String[] parser = treeLine.trim().split(" ");
			if (parser.length < 2)
				return null;

			Triple<String, String, String> newRule = new ImmutableTriple<String, String, String>(parser[0], parser[1],
					parser[2]);

			return newRule;
		} else
			return null;
	}

	@Override
	public String toString() {

		String result = "";
		for (int i = 0; i < conditions.size(); i++) {
			Triple<String, String, String> rule = conditions.get(i);
			result = result + "Rule " + i + ": " + rule.getLeft() + ", " + rule.getMiddle() + ", " + rule.getRight()
					+ "\n";
		}
		return result;

	}

	public List<String> getSplitValuesAsString() {
		Set<String> result = new TreeSet<String>();

		for (Triple<String, String, String> rule : conditions) {
			result.add(rule.getRight());
		}

		return Lists.newArrayList(result);
	}

	public List<String> getAttributeAsString() {
		Set<String> result = new TreeSet<String>();

		for (Triple<String, String, String> rule : conditions) {
			result.add(rule.getLeft());
		}

		return Lists.newArrayList(result);
	}

	public List<Triple<String, String, String>> getConditions() {
		return conditions;
	}

}
