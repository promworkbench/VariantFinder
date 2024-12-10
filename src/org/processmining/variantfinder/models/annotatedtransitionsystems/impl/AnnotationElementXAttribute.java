package org.processmining.variantfinder.models.annotatedtransitionsystems.impl;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.model.XAttribute;
import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.AnnotationElement;

public class AnnotationElementXAttribute implements AnnotationElement<XAttribute> {

	private String name;
	private List<XAttribute> values;

	public AnnotationElementXAttribute(String name) {
		this.name = name;
		values = new ArrayList<XAttribute>();
	}

	public String getName() {
		return name;
	}

	public void addValue(XAttribute newElement) {
		values.add(newElement);
	}

	public List<XAttribute> getValues() {
		return values;
	}

	public List<XAttribute> getDistinctValues() {
		// NEVER use. All attributes are assumed distinct
		return values;
	}

}
