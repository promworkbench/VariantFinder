package org.processmining.variantfinder.models.annotatedtransitionsystems.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.AnnotationElement;

import com.google.common.collect.Lists;

/**
 * Discrete values can be repeated, so we also store the cardinality.
 * In the worst case, we use double the space of a list of longs
 * 
 * @author abolt
 *
 */
public class AnnotationElementDiscrete implements AnnotationElement<Long>{

	private String name;
	private Map<Long, Long> values;

	public AnnotationElementDiscrete(String name) {
		this.name = name;
		values = new HashMap<Long,Long>();
	}

	public String getName() {
		return name;
	}

	public void addValue(Long newElement) {
		if(values.containsKey(newElement))
			values.put(newElement, values.get(newElement) + 1);
		else
			values.put(newElement, (long) 1);
	}

	public List<Long> getValues() {
		List<Long> results = new ArrayList<Long>();
		for(Long s : values.keySet())
			for(int i = 0 ; i < values.get(s) ; i++)
				results.add(s);
		return results;
	}

	public List<Long> getDistinctValues() {
		return Lists.newArrayList(values.keySet());
	}

}
