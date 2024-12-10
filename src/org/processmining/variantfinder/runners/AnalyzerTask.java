package org.processmining.variantfinder.runners;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingWorker;

import org.processmining.models.graphbased.directed.transitionsystem.payload.event.EventPayloadTransitionSystem;
import org.processmining.variantfinder.controllers.MainController;
import org.processmining.variantfinder.models.annotatedtransitionsystems.constants.AnnotationConstants;
import org.processmining.variantfinder.models.annotatedtransitionsystems.impl.AnnotationElementDiscrete;
import org.processmining.variantfinder.models.annotatedtransitionsystems.impl.AnnotationElementXEvent;
import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.AnnotatedTransitionSystem;
import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.Annotation;
import org.processmining.variantfinder.models.differences.StatisticalDifference;
import org.processmining.variantfinder.models.trees.RuleSet;
import org.processmining.variantfinder.parameters.TreeSettings;
import org.processmining.variantfinder.utils.RengineUtils;
import org.processmining.variantfinder.utils.VariantUtils;

import com.google.common.collect.Lists;

public class AnalyzerTask extends SwingWorker<List<StatisticalDifference>, Void> {

	AnnotatedTransitionSystem ats;
	double freqThreshold;
	TreeSettings settings;
	private List<StatisticalDifference> results;
	MainController controller;

	public AnalyzerTask(MainController controller, AnnotatedTransitionSystem ats, double freqThreshold, TreeSettings settings) {

		this.ats = ats;
		this.freqThreshold = freqThreshold;
		this.settings = settings;
		this.controller = controller;
	}

	protected List<StatisticalDifference> doInBackground() throws Exception {

		EventPayloadTransitionSystem transitionSystem = ats.getTransitionSystem();
		Set<StatisticalDifference> significantDifferences = new HashSet<StatisticalDifference>();

		List<Object> tsElements = new ArrayList<Object>();
		tsElements.addAll(transitionSystem.getNodes());
		tsElements.addAll(transitionSystem.getEdges());

		int total = tsElements.size();
		
		
		int counter = 0;

		setProgress(0);

		for (Object element : tsElements) {
			if (isCancelled())
				return null;
			
			counter++;
			setProgress((int) (((double) counter / (double) total)*100));

			Annotation annotation = ats.hasElementAnnotation(element) ? ats.getAnnotation(element) : null;
			if (annotation == null)
				continue;

			if (annotation.containsElement(AnnotationConstants.FREQUENCY)) //not enough frequency
				if (VariantUtils.getAvg((AnnotationElementDiscrete) annotation
						.getElement(AnnotationConstants.FREQUENCY)) < freqThreshold)
					continue;

			AnnotationElementXEvent annotationEvents = (AnnotationElementXEvent) (annotation.containsElement(
					AnnotationConstants.EVENT) ? annotation.getElement(AnnotationConstants.EVENT) : null);
			if (annotationEvents == null)
				continue;

			if (annotationEvents.getDistinctValuesAsString(settings.getClassAttribute()).isEmpty())
				continue;

			List<RuleSet> results = new ArrayList<RuleSet>();
			try {
				List<RuleSet> rules = RengineUtils.analyzeElement(annotationEvents, settings);
				if (rules != null && !rules.isEmpty())
					for (RuleSet r : rules)
						if (!r.getRules().isEmpty())
							results.add(r);

			} catch (Exception e1) {
				e1.printStackTrace();
			}

			for (RuleSet rule : results)
				significantDifferences.add(new StatisticalDifference(element,
						VariantUtils.getAvg((AnnotationElementDiscrete) annotation.getElement(AnnotationConstants.FREQUENCY)),
						rule));

			
		}
		return Lists.newArrayList(significantDifferences);
	}
	
	@Override
	public void done(){
		controller.killProgressBar();
		try {
			controller.setSplitPoints(get());
			controller.updateElements();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
}
