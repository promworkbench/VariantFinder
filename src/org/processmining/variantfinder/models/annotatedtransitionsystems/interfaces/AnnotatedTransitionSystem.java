package org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces;

import org.processmining.models.graphbased.directed.transitionsystem.payload.event.EventPayloadTransitionSystem;

/**
 * @author abolt
 * 
 *         Wrapper for all the perspectives that we can annotate in a transition
 *         system
 *
 */
public interface AnnotatedTransitionSystem {

	/**
	 * get access to the underlying transition system
	 * 
	 * @return the transition system
	 */
	public EventPayloadTransitionSystem getTransitionSystem();

	/**
	 * get the annotation object for any element of the transition system.
	 * 
	 * @param tsElement
	 * @return the annotation wrapper for that transition system element
	 */
	public Annotation getAnnotation(Object tsElement);

	/**
	 * Does the TS contain an annotation node for this element?
	 * 
	 * @param element
	 * @return
	 */
	public boolean hasElementAnnotation(Object tsElement);

	/**
	 * adds an annotation wrapper to an element of the transition system
	 * 
	 * @param tsElement
	 * @param annotation
	 * @return
	 */
	public void addAnnotation(Object tsElement, Annotation annotation);

}
