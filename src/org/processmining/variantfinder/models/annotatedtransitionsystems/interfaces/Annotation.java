package org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces;

import java.util.Set;

/**
 * Wrapper for all the annotations that can be assigned to a single element of a
 * transition system (i.e., state or transition)
 * 
 * @author abolt
 *
 */
public interface Annotation {

	/**
	 * Add an annotation element (e.g., frequency annotation, elapsed time)
	 * The element should provide its own name (to be stored here)
	 * 
	 * @param element
	 * @param elementName
	 */
	public void addElement(AnnotationElement<?> element);

	/**
	 * 
	 * @return the names of the annotation elements stored in this annotation
	 *         wrapper
	 */
	public Set<String> getElementNames();

	/**
	 * 
	 * @param elementName
	 * @return true if it contains an annotation element with that name
	 */
	public boolean containsElement(String elementName);

	/**
	 * 
	 * @param elementName
	 * @return the annotation element
	 */

	public AnnotationElement<?> getElement(String elementName);

}
