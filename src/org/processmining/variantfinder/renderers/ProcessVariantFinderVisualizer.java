package org.processmining.variantfinder.renderers;

import javax.swing.JComponent;

import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.variantfinder.controllers.MainController;

@Plugin(name = "Visualize Process Variant Finder Panel", parameterLabels = { "MainController" }, returnLabels = {
		"visualization" }, returnTypes = { JComponent.class })
@Visualizer
public class ProcessVariantFinderVisualizer {

	@PluginVariant(requiredParameterLabels = { 0 })
	public static JComponent visualize(PluginContext context, MainController output) {
		return output.getMainView();
	}
}