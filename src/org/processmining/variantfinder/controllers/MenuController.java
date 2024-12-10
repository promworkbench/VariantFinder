package org.processmining.variantfinder.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import org.processmining.plugins.transitionsystem.miner.TSMinerInput;
import org.processmining.processcomparator.algorithms.Utils;
import org.processmining.processcomparator.view.dialog.ProgressBarDialog;
import org.processmining.variantfinder.parameters.TreeSettings;
import org.processmining.variantfinder.parameters.TsSettings;
import org.processmining.variantfinder.runners.TsSettingsRunner;
import org.processmining.variantfinder.views.panels.MenuPanel;

public class MenuController {

	private MenuPanel menuPanel;
	private MainController mainController;

	private TSMinerInput tsAbstractions;
	private Thread thread;

	public MenuController(final MainController mainController) {
		this.mainController = mainController;
		menuPanel = new MenuPanel(this);

		menuPanel.addFindVariantsListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				MenuController.this.updateResults();
			}

		});
		menuPanel.addOpenSettingsListeners(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				ProgressBarDialog pb = new ProgressBarDialog(5); //it can have 5 steps
				pb.addButtonListener(new CancelRunner(pb));
				pb.setUndecorated(true);

				menuPanel.revalidate();
				pb.setLocationRelativeTo(menuPanel);
				pb.setAlwaysOnTop(true);
				pb.setVisible(true);
				//run!
				thread = new Thread(new TsSettingsRunner(pb, mainController.getPluginContext(), mainController.getLog(),
						MenuController.this));
				thread.start();
				pb.dispose();
			}
		});
	}

	public JPanel getPanel() {
		return menuPanel;
	}

	/**
	 * method used by the TsSettingsRunner to store the TS abstractions object
	 * here
	 * 
	 * @param tsAbstractions
	 */
	public void setTsAbstractions(TSMinerInput tsAbstractions) {
		this.tsAbstractions = tsAbstractions;
	}

	public TsSettings getTsSettings() {
		if (menuPanel.isDefaultTsSettingsSelected() || tsAbstractions == null)
			tsAbstractions = Utils.getTSSettings(mainController.getPluginContext(), mainController.getLog());
		return new TsSettings(tsAbstractions, menuPanel.isFrequencyThresholdActivated(),
				menuPanel.getFrequencyThreshold() / 100, menuPanel.isShowTransitionLabelsSelected());
	}

	public TreeSettings getTreeSettings() {
		return new TreeSettings(menuPanel.getSelectedAttributes(), menuPanel.getClassAttribute(),
				menuPanel.getMinInstancesPerLeaf() / 100, menuPanel.isUseIndividually(), menuPanel.getAlpha() / 100,
				menuPanel.getWidthValue(), menuPanel.getHeightValue());
	}

	/**
	 * Button listeners
	 * 
	 * @author abolt
	 *
	 */

	public void updateResults() {
		mainController.findVariants();
	}

	class CancelRunner implements ActionListener {
		ProgressBarDialog pb;

		public CancelRunner(ProgressBarDialog p) {
			pb = p;
		}

		@SuppressWarnings("deprecation")
		public void actionPerformed(ActionEvent e) {
			pb.setVisible(false);
			pb.dispose();
			thread.stop(); //not safe... but screw you ProM for not letting me do it naturally!!!
		}

	}

	public MainController getMainController() {
		return mainController;
	}

}
