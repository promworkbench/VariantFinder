package org.processmining.variantfinder.models.annotatedtransitionsystems.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.model.XEvent;
import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.AnnotationElement;

import com.google.common.collect.Lists;

public class AnnotationElementXEvent implements AnnotationElement<XEvent> {

	private String name;
	private List<XEvent> values;

	public AnnotationElementXEvent(String name) {
		this.name = name;
		values = new ArrayList<XEvent>();
	}

	public String getName() {
		return name;
	}

	public void addValue(XEvent newElement) {
		values.add(newElement);
	}

	public List<XEvent> getValues() {
		return values;
	}

	public List<XEvent> getDistinctValues() {
		// NEVER use. All events are assumed distinct
		return values;
	}
	
	/**
	 * Get all the distinct values for a given attribute
	 * @param attributeName
	 * @return
	 */
	public List<String> getDistinctValuesAsString(String attributeName){
		Set<String> attributes = new HashSet<String>();
		
		for(XEvent event : values)
			if(event.getAttributes().containsKey(attributeName))
				attributes.add(event.getAttributes().get(attributeName).toString());
		
		return Lists.newArrayList(attributes);
	}
}
