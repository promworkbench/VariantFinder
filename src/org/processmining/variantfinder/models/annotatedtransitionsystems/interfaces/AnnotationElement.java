package org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces;

import java.util.List;


public interface AnnotationElement<T>{
	
	public String getName();
	
	public void addValue(T newElement);
	
	public List<T> getValues();
	
	public List<T> getDistinctValues();

}
