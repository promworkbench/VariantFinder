package org.processmining.variantfinder.controllers;

import java.awt.Component;
import java.util.Set;

import javax.swing.JPanel;

import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.plugins.graphviz.visualisation.DotPanel;
import org.processmining.plugins.transitionsystem.miner.TSMinerTransitionSystem;
import org.processmining.variantfinder.algorithms.EventTranslator;
import org.processmining.variantfinder.algorithms.annotatedtransitionsystems.ATSFactory;
import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.AnnotatedTransitionSystem;
import org.processmining.variantfinder.parameters.TsSettings;
import org.processmining.variantfinder.utils.DrawUtils;
import org.processmining.variantfinder.utils.TSUtils;
import org.processmining.variantfinder.views.panels.ModelPanel;

public class ModelController {

	private MainController mainController;
	private ModelPanel view;

	private AnnotatedTransitionSystem ats;
	private DotPanel model;
	private EventTranslator translator;

	public ModelController(MainController controller) {

		this.mainController = controller;

		//calculateTS();

		//drawModel(new int[0]);

		view = new ModelPanel(this);
	}

	public void calculateTS() {
		//create transition system
		TsSettings tsSettings = mainController.getMenuController().getTsSettings();
		TSMinerTransitionSystem ts = TSUtils.createTS(mainController.getPluginContext(), tsSettings.getTsAbstractions(),
				mainController.getLog());

		for (State s : ts.getNodes())
			s.setLabel(s.getAttributeMap().get(AttributeMap.TOOLTIP).toString());

		translator = new EventTranslator();
		translator.setSettings(tsSettings);
		translator.setTS(ts);
	}

	public void calculateATS() {
		ats = ATSFactory.createATS(mainController.getPluginContext(), translator, mainController.getLog());
	}

	public void drawModel(Set<Object> differences) {

		double threshold = mainController.getMenuController().getTsSettings().isThresholdEnabled()
				? mainController.getMenuController().getTsSettings().getFrequencyThreshold() : 0;

		model = new DotPanel(DrawUtils.createGraph(ats, threshold,
				mainController.getMenuController().getTsSettings().isShowTransitionLabelsEnabled(), differences));
		view = new ModelPanel(this);
	}

	/**
	 * 
	 * @return the Dot panel containing the transition system
	 */
	public Component getModelPanel() {
		return model;
	}

	public JPanel getPanel() {
		return view;
	}

	public void updateModels() {
		TsSettings tsSettings = mainController.getMenuController().getTsSettings();
		if (translator == null || !translator.getSettings().equals(tsSettings))
			calculateTS();

		calculateATS();
	}

	public AnnotatedTransitionSystem getATS() {
		return ats;
	}

}
