package org.processmining.variantfinder.models.annotatedtransitionsystems.impl;

import java.util.HashMap;
import java.util.Set;

import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.Annotation;
import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.AnnotationElement;

public class AnnotationImpl implements Annotation {

	private HashMap<String, AnnotationElement<?>> elements;

	public AnnotationImpl() {
		elements = new HashMap<String, AnnotationElement<?>>();
	}

	public void addElement(AnnotationElement<?> element) {
		elements.put(element.getName(), element);
	}

	public Set<String> getElementNames() {
		return elements.keySet();
	}

	public boolean containsElement(String elementName) {
		return elements.containsKey(elementName);
	}

	public AnnotationElement<?> getElement(String elementName) {
		return elements.get(elementName);
	}

}
