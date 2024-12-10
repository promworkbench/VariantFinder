package org.processmining.variantfinder.controllers;

import java.util.ArrayList;
import java.util.List;

import org.processmining.variantfinder.models.SplitCriteriaRow;
import org.processmining.variantfinder.models.differences.StatisticalDifference;
import org.processmining.variantfinder.views.panels.SplitCriteriaPanel;

public class SplitCriteriaController {

	private SplitCriteriaPanel panel;
	private MainController mainController;

	private List<SplitCriteriaRow> splitCriteria;

	public SplitCriteriaController(MainController controller) {
		this.mainController = controller;
		initialize();
	}

	public void initialize() {

		splitCriteria = new ArrayList<SplitCriteriaRow>();

		List<StatisticalDifference> splitPoints = mainController.getSplitPoints();
		if (splitPoints != null && !splitPoints.isEmpty())
			for (StatisticalDifference sp : splitPoints)
				splitCriteria.add(new SplitCriteriaRow(sp));

		panel = new SplitCriteriaPanel(this);

	}

	public List<SplitCriteriaRow> getSplitPoints() {
		return splitCriteria;
	}

	public int[] getSelectedSplitCriteria() {
		return panel.getSelectedRows();
	}

	public SplitCriteriaPanel getPanel() {
		return panel;
	}

	public void updateSelections() {
		mainController.updatedSelection();
	}

}
