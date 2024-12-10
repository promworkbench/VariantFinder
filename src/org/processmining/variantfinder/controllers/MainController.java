package org.processmining.variantfinder.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JPanel;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.processcomparator.view.dialog.ProgressBarDialog;
import org.processmining.variantfinder.models.differences.StatisticalDifference;
import org.processmining.variantfinder.parameters.TreeSettings;
import org.processmining.variantfinder.parameters.TsSettings;
import org.processmining.variantfinder.runners.AnalyzerTask;
import org.processmining.variantfinder.views.MainView;

public class MainController {

	/**
	 * Main View Panel
	 */
	private MainView mainView;

	/**
	 * Controllers for each component in the UI.
	 */
	private MenuController menuController;
	private SplitCriteriaController splitCriteriaController;
	private DetailsController detailsController;
	private ModelController modelController;

	/**
	 * Pointers to the Log and PluginContext;
	 */
	private XLog log;
	private PluginContext pluginContext;
	private ProgressBarDialog progressBar;
	private List<StatisticalDifference> splitPoints;

	/**
	 * Pointers to the current state of the settings. Some calculations can be
	 * avoided if the object has not changed.
	 */
	private TsSettings tsSettings;
	private TreeSettings treeSettings;

	public MainController(PluginContext context, XLog log) {

		this.log = log;
		this.pluginContext = context;

		mainView = new MainView(this);
		menuController = new MenuController(this);
		splitCriteriaController = new SplitCriteriaController(this);
		detailsController = new DetailsController(this);
		modelController = new ModelController(this);

		mainView.setMenuPane(menuController.getPanel());

		mainView.setSplitCriteriaPane(splitCriteriaController.getPanel());

		mainView.setDetailsPane(detailsController.getPanel());

		mainView.setModelPane(modelController.getPanel());

		mainView.doLayout();
		mainView.repaint();
	}

	public JPanel getMainView() {
		return mainView;
	}

	public void findVariants() {

		tsSettings = menuController.getTsSettings();
		treeSettings = menuController.getTreeSettings();

		modelController.updateModels();

		double threshold = tsSettings.isThresholdEnabled() ? tsSettings.getFrequencyThreshold() : 0;

		progressBar = new ProgressBarDialog(100);

		//progressBar.addButtonListener(new CancelRunner(progressBar));
		progressBar.setUndecorated(true);

		mainView.revalidate();
		progressBar.setLocationRelativeTo(mainView);
		progressBar.setAlwaysOnTop(true);
		progressBar.setVisible(true);

		//		Set<StatisticalDifference> differences = ATSAnalyzer.analyzeATS(modelController.getATS(),
		//				threshold, treeSettings);

		final AnalyzerTask task = new AnalyzerTask(this, modelController.getATS(), threshold, treeSettings);
		task.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					progressBar.setProgress((Integer) evt.getNewValue());
					progressBar.repaint();
				}
			}
		});
		progressBar.addButtonListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				task.cancel(true);				
				killProgressBar();
			}			
		});
		task.execute();
	}
	
	public void killProgressBar(){
		progressBar.setVisible(false);
		progressBar.dispose();
	}

	public void updateElements() {

		
		modelController.drawModel(new TreeSet<Object>());
		mainView.setModelPane(modelController.getPanel());

		splitCriteriaController.initialize();
		mainView.setSplitCriteriaPane(splitCriteriaController.getPanel());

		mainView.doLayout();
		mainView.repaint();
	}

	public List<StatisticalDifference> getSplitPoints() {
		return splitPoints;
	}

	public void updatedSelection() {

		detailsController.update(splitCriteriaController.getSelectedSplitCriteria());
		mainView.setDetailsPane(detailsController.getPanel());

		Set<Object> diff_elements = new HashSet<Object>();
		for (int i : splitCriteriaController.getSelectedSplitCriteria())
			diff_elements.add(getSplitPoints().get(i).getTsElement());

		modelController.drawModel(diff_elements);
		mainView.setModelPane(modelController.getPanel());

		mainView.doLayout();
		mainView.repaint();

	}

	public PluginContext getPluginContext() {
		return pluginContext;
	}

	public XLog getLog() {
		return log;
	}

	public MenuController getMenuController() {
		return menuController;
	}

	public SplitCriteriaController getSplitCriteriaController() {
		return splitCriteriaController;
	}

	public DetailsController getDetailsController() {
		return detailsController;
	}

	public ModelController getModelController() {
		return modelController;
	}

	public void setSplitPoints(List<StatisticalDifference> splitPoints) {
		this.splitPoints = splitPoints;
	}

}
