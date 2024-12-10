package org.processmining.variantfinder.algorithms.annotatedtransitionsystems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.deckfour.xes.model.XAttributeContinuous;
import org.deckfour.xes.model.XAttributeDiscrete;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.graphbased.directed.transitionsystem.payload.event.EventPayloadTransitionSystem;
import org.processmining.variantfinder.algorithms.EventTranslator;
import org.processmining.variantfinder.models.annotatedtransitionsystems.constants.AnnotationConstants;
import org.processmining.variantfinder.models.annotatedtransitionsystems.impl.AnnotatedTransitionSystemImpl;
import org.processmining.variantfinder.models.annotatedtransitionsystems.impl.AnnotationElementDiscrete;
import org.processmining.variantfinder.models.annotatedtransitionsystems.impl.AnnotationElementXEvent;
import org.processmining.variantfinder.models.annotatedtransitionsystems.impl.AnnotationImpl;
import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.Annotation;
import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.AnnotationElement;

import com.google.common.collect.Lists;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Class used to create annotated transition systems
 * 
 * @author abolt
 *
 */
public class ATSFactory {

	/**
	 * Method to create an annotated transition system based on selected event
	 * attributes.
	 * 
	 * @param context
	 * @param et
	 *            (provides the mappings between events and elements of the
	 *            transition system)
	 * @param log
	 *            (This log will be enriched, make sure that you give this
	 *            method a clone and not the original)
	 * @param attributes
	 *            (only these will be used as annotations, besides the
	 *            standards)
	 * @return ATS
	 */
	public static AnnotatedTransitionSystemImpl createATS(final PluginContext context, EventTranslator et,
			final XLog log) {

		EventPayloadTransitionSystem ts = et.getTS();
		AnnotatedTransitionSystemImpl ats = new AnnotatedTransitionSystemImpl(ts);

		// Context  progress bar setup
		if (context.getProgress() != null) {
			context.getProgress().setMinimum(0);
			context.getProgress().setMaximum(log.size());
			context.getProgress().setIndeterminate(false);
		}

		for (XTrace trace : log) {

			List<Object> visited = new ArrayList<Object>();

			for (XEvent event : trace) {
				@SuppressWarnings("rawtypes")
				AnnotationElement annotationElement;
				Annotation annotation;

				List<Object> selectedElements = new ArrayList<Object>();
				selectedElements.add(et.getStatefromEvent(event, trace));
				selectedElements.add(et.getTransitionfromEvent(event, trace)); 
				
				if(selectedElements.contains(null)) //protection against missing self-loops
					continue;

				/**
				 * First put the complete event, then for each attribute, add
				 * the annotation
				 * 
				 * o is either a State or a Transition
				 */

				for (Object o : selectedElements) {
					visited.add(o);
					if (!ats.hasElementAnnotation(o))
						ats.addAnnotation(o, new AnnotationImpl());
					annotation = ats.getAnnotation(o);

					if (!annotation.containsElement(AnnotationConstants.EVENT))
						annotation.addElement(new AnnotationElementXEvent(AnnotationConstants.EVENT));
					annotationElement = annotation.getElement(AnnotationConstants.EVENT);
					((AnnotationElementXEvent) annotationElement).addValue(event);

					//					for (String attribute : attributes) {
					//						if (!annotation.containsElement(attribute))
					//							annotation.addElement(new AnnotationElementXAttribute(attribute));
					//						annotationElement = annotation.getElement(attribute);
					//						((AnnotationElementXAttribute) annotationElement)
					//								.addValue(event.getAttributes().get(attribute));
					//					}
				}
			}

			// Add frequency
			List<Object> allElements = new ArrayList<Object>();
			allElements.addAll(ts.getNodes());
			allElements.addAll(ts.getEdges());
			
			

			for (Object o : allElements) {
				Annotation annotation = ats.getAnnotation(o);
				if(annotation == null)
					continue;
				if (!annotation.containsElement(AnnotationConstants.FREQUENCY))
					annotation.addElement(new AnnotationElementDiscrete(AnnotationConstants.FREQUENCY));
				AnnotationElementDiscrete annotationElement = (AnnotationElementDiscrete) annotation
						.getElement(AnnotationConstants.FREQUENCY);
				if (visited.contains(o))
					annotationElement.addValue(1l);
				else
					annotationElement.addValue(0l);
			}
			if (context.getProgress() != null) {
				context.getProgress().inc(); // increase the progress bar
			}
		}
		return ats;
	}

	/**
	 * Creates a set of instances (weka) that can be used to build a classifier
	 * 
	 * @param ats
	 * @param tsElement
	 * @param attributes
	 *            The last element in this list is the class attribute
	 * @param instanceNum
	 * @return
	 */
	public static Instances buildInstances(AnnotationElementXEvent annotationEvents, List<String> attributes, String classAttribute,
			int instanceNum) {

		
		Map<String, Attribute> attMap = new HashMap<String, Attribute>();
		
		List<String> allAttributes = new ArrayList<String>();
		allAttributes.addAll(attributes);
		allAttributes.add(classAttribute);

		// Build the header
		for (String attribute : allAttributes)
			for (XEvent event : annotationEvents.getValues()) {
				if (event.getAttributes().containsKey(attribute)) {
					if (event.getAttributes().get(attribute) instanceof XAttributeLiteral)
						//get all possible values
						attMap.put(attribute,
								new Attribute(attribute, annotationEvents.getDistinctValuesAsString(attribute)));
					else if (event.getAttributes().get(attribute) instanceof XAttributeTimestamp)
						attMap.put(attribute, new Attribute(attribute, AnnotationConstants.WEKA_DATEFORMAT));
					else
						attMap.put(attribute, new Attribute(attribute));
					break;
				}

			}

		Instances instances = new Instances("Instances", Lists.newArrayList(attMap.values()), instanceNum);
		//now fill the instances, each event is a row (Instance)
		for (XEvent event : annotationEvents.getValues()) {
			Instance instance = new DenseInstance(allAttributes.size());
			for (String attribute : attMap.keySet())
				if (event.getAttributes().containsKey(attribute)) {
					if (event.getAttributes().get(attribute) instanceof XAttributeLiteral)
						instance.setValue(attMap.get(attribute),
								((XAttributeLiteral) event.getAttributes().get(attribute)).getValue());
					else if (event.getAttributes().get(attribute) instanceof XAttributeTimestamp)
						instance.setValue(attMap.get(attribute),
								((XAttributeTimestamp) event.getAttributes().get(attribute)).getValueMillis());
					else if (event.getAttributes().get(attribute) instanceof XAttributeDiscrete)
						instance.setValue(attMap.get(attribute),
								((XAttributeDiscrete) event.getAttributes().get(attribute)).getValue());
					else if (event.getAttributes().get(attribute) instanceof XAttributeContinuous)
						instance.setValue(attMap.get(attribute),
								((XAttributeContinuous) event.getAttributes().get(attribute)).getValue());
					
				}
			instance.setDataset(instances);
			instances.add(instance);
		}
		instances.setClass(attMap.get(classAttribute));
		return instances;
	}
}
