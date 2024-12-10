package org.processmining.variantfinder.models.annotatedtransitionsystems.impl;

import java.util.HashMap;
import java.util.Map;

import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.models.graphbased.directed.transitionsystem.Transition;
import org.processmining.models.graphbased.directed.transitionsystem.payload.event.EventPayloadTransitionSystem;
import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.AnnotatedTransitionSystem;
import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.Annotation;

public class AnnotatedTransitionSystemImpl implements AnnotatedTransitionSystem {

	//list of annotations. Extendable to cost, resource, etc...
	private Map<State, Annotation> stateAnnotations;
	private Map<Transition, Annotation> transitionAnnotations;
	private EventPayloadTransitionSystem ts;
	
	public AnnotatedTransitionSystemImpl(EventPayloadTransitionSystem ts){
		this.ts = ts;
		stateAnnotations = new HashMap<State,Annotation>();
		transitionAnnotations = new HashMap<Transition,Annotation>();
	}

	public EventPayloadTransitionSystem getTransitionSystem() {
		return ts;
	}

	public Annotation getAnnotation(Object tsElement) {
		if (tsElement instanceof State)
			return stateAnnotations.get(tsElement);
		else if (tsElement instanceof Transition)
			return transitionAnnotations.get(tsElement);
		return null;
	}

	public boolean hasElementAnnotation(Object tsElement) {
		if (tsElement instanceof State)
			return stateAnnotations.containsKey(tsElement);
		else if (tsElement instanceof Transition)
			return transitionAnnotations.containsKey(tsElement);
		return false;
	}

	public void addAnnotation(Object tsElement, Annotation annotation) {
		if (tsElement instanceof State)
			stateAnnotations.put((State) tsElement, annotation);
		else if (tsElement instanceof Transition)
			transitionAnnotations.put((Transition) tsElement, annotation);
	}

}
