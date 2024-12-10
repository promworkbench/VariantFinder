package org.processmining.variantfinder.plugins;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryNaiveImpl;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.log.models.impl.EventLogArrayImpl;
import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.models.graphbased.directed.transitionsystem.payload.event.EventPayloadTransitionSystem;
import org.processmining.plugins.transitionsystem.miner.TSMinerTransitionSystem;
import org.processmining.processcomparator.algorithms.Utils;
import org.processmining.variantfinder.algorithms.EventTranslator;
import org.processmining.variantfinder.algorithms.annotatedtransitionsystems.ATSFactory;
import org.processmining.variantfinder.models.annotatedtransitionsystems.constants.AnnotationConstants;
import org.processmining.variantfinder.models.annotatedtransitionsystems.impl.AnnotationElementDiscrete;
import org.processmining.variantfinder.models.annotatedtransitionsystems.impl.AnnotationElementXEvent;
import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.AnnotatedTransitionSystem;
import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.Annotation;
import org.processmining.variantfinder.models.differences.StatisticalDifference;
import org.processmining.variantfinder.models.trees.Rule;
import org.processmining.variantfinder.models.trees.RuleSet;
import org.processmining.variantfinder.parameters.Settings;
import org.processmining.variantfinder.parameters.TreeSettings;
import org.processmining.variantfinder.parameters.TsSettings;
import org.processmining.variantfinder.utils.RengineUtils;
import org.processmining.variantfinder.utils.TSUtils;
import org.processmining.variantfinder.utils.VariantUtils;

import com.google.common.collect.Lists;

@Plugin(name = "Find Concept Drift", parameterLabels = { "Event Log", "Settings" }, returnLabels = {
		"Process Variants (Concept Drift)" }, returnTypes = { EventLogArrayImpl.class }, help = "Help is coming!")
public class ConceptDriftFinder {

	@UITopiaVariant(affiliation = "Eindhoven University of Technology", author = "Alfredo Bolt", email = "a.bolt@tue.nl")
	@PluginVariant(variantLabel = "Find Concept Drift, with UI, default parameters", requiredParameterLabels = { 0 })
	public EventLogArrayImpl findConceptDrift(UIPluginContext context, XLog log) {

		List<String> attributeList = new ArrayList<String>();
		attributeList.add("time:timestamp");

		/**
		 * settings objects
		 */
		TreeSettings treeSettings = new TreeSettings(attributeList, "concept:nextActivity", 0.01, false, 0.05, 600,
				400);
		TsSettings tsSettings = new TsSettings(TSUtils.createTsAbstractionsObject(context, log), false, 0, false);

		Settings settings = new Settings(tsSettings, treeSettings);

		//Return 
		return goFetch(context, log, settings);

	}

	/**
	 * No UI
	 * 
	 * @param context
	 * @param log
	 * @return
	 */
	@PluginVariant(variantLabel = "Find Concept Drift, without UI, default parameters", requiredParameterLabels = { 0 })
	public EventLogArrayImpl findConceptDrift(PluginContext context, XLog log) {

		List<String> attributeList = new ArrayList<String>();
		attributeList.add("time:timestamp");

		TreeSettings treeSettings = new TreeSettings(attributeList, "concept:nextActivity", 0.05, false, 0.01, 600,
				400);
		TsSettings tsSettings = new TsSettings(Utils.getTSSettings(context, log), false, 0, false);

		Settings settings = new Settings(tsSettings, treeSettings);

		//Return 
		return goFetch(context, log, settings);

	}

	/**
	 * Adapter method in order to access
	 * {@link #calculateDriftPoints(PluginContext, XLog, Settings)} from other
	 * software directly. This method will only be used by ProM.
	 * 
	 * @param context
	 * @param log
	 * @param settings
	 * @return
	 */
	@PluginVariant(variantLabel = "Find Concept Drift, without UI", requiredParameterLabels = { 0, 1 })
	public EventLogArrayImpl goFetch(PluginContext context, XLog log, Settings settings) {

		EventLogArrayImpl array = new EventLogArrayImpl();

		List<XLog> variants = splitLogByDriftPoints(log, calculateDriftPoints(context, log, settings));
		for (XLog variant : variants)
			array.addLog(variant);

		return array;
	}

	/**
	 * Here we bake the cake :)
	 * 
	 * @param context
	 * @param log
	 * @param settings
	 * @return a list of event logs. Each event log is a process variant.
	 */
	public List<String> calculateDriftPoints(PluginContext context, XLog log, Settings settings) {

		
		ProcessVariantFinder.enrichLog(context, log);

		TSMinerTransitionSystem ts = TSUtils.createTS(context, settings.getTsSettings().getTsAbstractions(), log);

		for (State s : ts.getNodes())
			s.setLabel(s.getAttributeMap().get(AttributeMap.TOOLTIP).toString());

		EventTranslator et = new EventTranslator();
		et.setSettings(settings.getTsSettings());
		et.setTS(ts);

		AnnotatedTransitionSystem ats = ATSFactory.createATS(context, et, log);
		EventPayloadTransitionSystem transitionSystem = ats.getTransitionSystem();

		Set<StatisticalDifference> significantDifferences = new HashSet<StatisticalDifference>();

		List<Object> tsElements = new ArrayList<Object>();
		tsElements.addAll(transitionSystem.getNodes());
		tsElements.addAll(transitionSystem.getEdges());

		for (Object element : tsElements) {

			Annotation annotation = ats.hasElementAnnotation(element) ? ats.getAnnotation(element) : null;
			if (annotation == null)
				continue;

			if (annotation.containsElement(AnnotationConstants.FREQUENCY)) //not enough frequency
				if (VariantUtils.getAvg(
						(AnnotationElementDiscrete) annotation.getElement(AnnotationConstants.FREQUENCY)) < settings
								.getTsSettings().getFrequencyThreshold())
					continue;

			AnnotationElementXEvent annotationEvents = (AnnotationElementXEvent) (annotation.containsElement(
					AnnotationConstants.EVENT) ? annotation.getElement(AnnotationConstants.EVENT) : null);
			if (annotationEvents == null)
				continue;

			if (annotationEvents.getDistinctValuesAsString(settings.getTreeSettings().getClassAttribute()).isEmpty())
				continue;

			List<RuleSet> results = new ArrayList<RuleSet>();
			try {
				List<RuleSet> rules = RengineUtils.analyzeElement(annotationEvents, settings.getTreeSettings());
				if (rules != null && !rules.isEmpty())
					for (RuleSet r : rules)
						if (!r.getRules().isEmpty())
							results.add(r);

			} catch (Exception e1) {
				e1.printStackTrace();
			}

			for (RuleSet rule : results)
				significantDifferences.add(new StatisticalDifference(element,
						VariantUtils.getAvg(
								(AnnotationElementDiscrete) annotation.getElement(AnnotationConstants.FREQUENCY)),
						rule));

		}

		List<String> driftPoints = new ArrayList<String>();

		for (StatisticalDifference split : significantDifferences)
			for (Rule rule : split.getRules().getRules())
				driftPoints.addAll(rule.getSplitValuesAsString());
		
		Set<String> dp = new TreeSet<String>();
		dp.addAll(driftPoints);

		return Lists.newArrayList(dp);
	}

	public List<XLog> splitLogByDriftPoints(XLog log, List<String> driftPoints) {

		List<XLog> result = new ArrayList<XLog>();

		XFactory factory = new XFactoryNaiveImpl();
		XLog temp = (XLog) log.clone();

		for (String d : driftPoints) {
			
			System.out.println("drift in: " + d);

			Date drift = new Date(Long.parseLong(d));

			XLog newLog = factory.createLog(log.getAttributes());

			for (XTrace trace : temp) {
				Date date =  ((XAttributeTimestamp) trace.get(0).getAttributes().get("time:timestamp")).getValue();
				if (date.before(drift))
					newLog.add(trace);
			}

			result.add(newLog);

			for (XTrace t : newLog)
				temp.remove(t);
		}
		return result;
	}

}
