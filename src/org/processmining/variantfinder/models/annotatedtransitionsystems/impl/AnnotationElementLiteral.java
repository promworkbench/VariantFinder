package org.processmining.variantfinder.models.annotatedtransitionsystems.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.AnnotationElement;

import com.google.common.collect.Lists;

/**
 * This class represents literal values. To save space, we store strings only
 * once, and have a cardinality assigned to them
 * 
 * @author abolt
 *
 */
public class AnnotationElementLiteral implements AnnotationElement<String> {

	private String name;
	private Map<String, Long> values;

	public AnnotationElementLiteral(String name) {
		this.name = name;
		values = new HashMap<String,Long>();
	}

	public String getName() {
		return name;
	}

	public void addValue(String newElement) {
		if(values.containsKey(newElement))
			values.put(newElement, values.get(newElement) + 1);
		else
			values.put(newElement, (long) 1);
	}

	public List<String> getValues() {
		List<String> results = new ArrayList<String>();
		for(String s : values.keySet())
			for(int i = 0 ; i < values.get(s) ; i++)
				results.add(s);
		return results;
	}

	public List<String> getDistinctValues() {
		return Lists.newArrayList(values.keySet());
	}

}
