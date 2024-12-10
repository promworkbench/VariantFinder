package org.processmining.variantfinder.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.deckfour.xes.classification.XEventAndClassifier;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventLifeTransClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.classification.XEventResourceClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.models.graphbased.directed.transitionsystem.Transition;
import org.processmining.plugins.transitionsystem.miner.TSMiner;
import org.processmining.plugins.transitionsystem.miner.TSMinerInput;
import org.processmining.plugins.transitionsystem.miner.TSMinerOutput;
import org.processmining.plugins.transitionsystem.miner.TSMinerTransitionSystem;
import org.processmining.plugins.transitionsystem.miner.ui.TSMinerUI;
import org.processmining.plugins.tsanalyzer2.AnnotatedTransitionSystem;
import org.processmining.plugins.tsanalyzer2.TSAnalyzer;

public class TSUtils {

	public static Collection<State> getDecisionPoints(AnnotatedTransitionSystem ats, double frequencyThreshold) {

		Set<State> result = new HashSet<State>();
		for (State s : ats.getTransitionSystem().getNodes())
			if (ats.getNodeAnnotation(s).getElement(TSAnalyzer.trace_frequency).getMean() > frequencyThreshold)
				if (getNextStates(ats, s, frequencyThreshold).size() > 1)
					result.add(s);
		return result;
	}

	public static Set<State> getNextStates(AnnotatedTransitionSystem ats, State source, double frequencyThreshold) {

		Set<State> result = new TreeSet<State>();
		for (Transition t : ats.getTransitionSystem().getEdges()) {
			if (t.getSource().equals(source)
					&& ats.getNodeAnnotation(t).getElement(TSAnalyzer.trace_frequency).getMean() > frequencyThreshold)
				result.add(t.getTarget());
		}
		return result;
	}

	public static TSMinerTransitionSystem createTS(PluginContext pluginContext, TSMinerInput settings, XLog log) {
		TSMiner miner = new TSMiner(pluginContext);
		TSMinerOutput output = miner.mine(settings);
		return output.getTransitionSystem();
	}

	public static TSMinerInput createTsAbstractionsObject(PluginContext pluginContext, XLog log) {
		TSMinerUI gui = new TSMinerUI((UIPluginContext) pluginContext);
		List<XEventClassifier> stateClassifier = new ArrayList<XEventClassifier>();

		stateClassifier.add(new XEventNameClassifier());
		stateClassifier.add(new XEventLifeTransClassifier());
		stateClassifier.add(new XEventResourceClassifier());

		XEventClassifier transitionClassifier = new XEventAndClassifier(new XEventNameClassifier(),
				new XEventLifeTransClassifier());
		return gui.getInputWithGUI(log, stateClassifier, transitionClassifier);
	}

}
