package org.processmining.variantfinder.controllers;

import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.processmining.variantfinder.models.differences.StatisticalDifference;
import org.processmining.variantfinder.renderers.TreeRenderer;
import org.processmining.variantfinder.views.panels.DetailsPanel;

public class DetailsController {

	private MainController controller;
	private DetailsPanel view;

	private JPanel dtPanel;

	public DetailsController(MainController controller) {

		this.controller = controller;
		view = new DetailsPanel(this);

	}

	public void update(int[] selectedCriteria) {
		List<StatisticalDifference> sp = new ArrayList<StatisticalDifference>();
		for (int i : selectedCriteria)
			sp.add(controller.getSplitPoints().get(i));

		//for now, just use the first selected
		if (sp != null && !sp.isEmpty()){
			//dtPanel = DecisionTreeRenderer.visualizeDecisionTree(new DecisionTree(sp.get(0).getDecisionTree()));
			dtPanel = TreeRenderer.render(sp.get(0).getPlot());
		}
		
		view = new DetailsPanel(this);
		
	}

	public Component getAttributePanel() {
		return new JPanel();
	}

	public Component getDecisionPointPanel() {
		return dtPanel;
	}

	public JPanel getPanel() {
		return view;
	}
}
