package org.processmining.variantfinder.models.annotatedtransitionsystems.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.AnnotationElement;

import com.google.common.collect.Lists;

/**
 * We use floats to save space Here we dont use maps for cardinality. Instead,
 * we just store all the numbers.
 * 
 * @author abolt
 *
 */
public class AnnotationElementContinuous implements AnnotationElement<Float> {

	private String name;
	private List<Float> values;

	public AnnotationElementContinuous(String name) {
		this.name = name;
		values = new ArrayList<Float>();
	}

	public String getName() {
		return name;
	}

	public void addValue(Float newElement) {
		values.add(newElement);
	}

	public List<Float> getValues() {
		return values;
	}

	public List<Float> getDistinctValues() {
		Set<Float> unique = new HashSet<Float>(values);
		return Lists.newArrayList(unique);
	}

}
