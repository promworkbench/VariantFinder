package org.processmining.variantfinder.plugins;

import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.logenhancement.enriching.AddDurationTimePlugin;
import org.processmining.logenhancement.enriching.AddElapsedTimePlugin;
import org.processmining.logenhancement.enriching.AddNextActivityPlugin;
import org.processmining.logenhancement.enriching.AddRemainingTimePlugin;
import org.processmining.logenhancement.enriching.CascadeAttributesPlugin;
import org.processmining.logenhancement.enriching.TimeResolution;
import org.processmining.logenhancement.transforming.CopyTraceAttributesToFirstEventPlugin;
import org.processmining.variantfinder.controllers.MainController;

@Plugin(name = "Process Variant Finder", parameterLabels = { "Event Log",
		"Process Variant Finder Settings" }, returnLabels = {
				"Process Variant Explorer" }, returnTypes = { MainController.class }, help = "Help is coming!")

public class ProcessVariantFinder {

	@UITopiaVariant(affiliation = "Eindhoven University of Technology", author = "Alfredo Bolt", email = "a.bolt@tue.nl")
	@PluginVariant(variantLabel = "Process Variant Finder, without UI, default parameters", requiredParameterLabels = {
			0 })
	public MainController findVariants(UIPluginContext context, XLog log) {

		enrichLog(context, log);

		MainController mainController = new MainController(context, log);
		return mainController;

	}

	public static void enrichLog(PluginContext context, XLog log) {

		/**
		 * Remove Empty Traces
		 */
		XLog aux = (XLog) log.clone();
		for (XTrace t : aux)
			if (t.isEmpty())
				log.remove(t);


		CopyTraceAttributesToFirstEventPlugin plugin1 = new CopyTraceAttributesToFirstEventPlugin();
		plugin1.copyAttributesToFirstEvent(context, log);

		CascadeAttributesPlugin plugin2 = new CascadeAttributesPlugin();
		plugin2.doCascadeEventsInTrace(context.getProgress(), log);

		AddElapsedTimePlugin plugin3 = new AddElapsedTimePlugin();
		plugin3.doAddElapsedTimeInTrace(context.getProgress(), log, "time:elapsed", TimeResolution.MILLISECONDS);

		AddRemainingTimePlugin plugin4 = new AddRemainingTimePlugin();
		plugin4.doAddRemainingTimeInTrace(context.getProgress(), log, "time:remaining", TimeResolution.MILLISECONDS);

		AddDurationTimePlugin plugin5 = new AddDurationTimePlugin();
		plugin5.doAddTimeBetweenEvents(context.getProgress(), log, "time:duration", TimeResolution.MILLISECONDS);

		AddNextActivityPlugin plugin6 = new AddNextActivityPlugin();
		plugin6.doAddNextActivity(context.getProgress(), log, "concept:nextActivity",
				XFactoryRegistry.instance().currentDefault());

	}

}
