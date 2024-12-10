package org.processmining.variantfinder.runners;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.processcomparator.view.dialog.ProgressBarDialog;
import org.processmining.variantfinder.controllers.MenuController;
import org.processmining.variantfinder.utils.TSUtils;

public class TsSettingsRunner implements Runnable {

	private PluginContext pluginContext;
	private XLog log;
	private ProgressBarDialog dialog;

	private MenuController controller;

	public TsSettingsRunner(ProgressBarDialog d, PluginContext context, XLog log, MenuController controller) {

		if (d == null)
			dialog = new ProgressBarDialog(1);
		else
			dialog = d;
		
		this.controller = controller;
		pluginContext = context;
		this.log = log;
	}

	public void run() {

		dialog.setVisible(true);

		dialog.toBack();

		controller.setTsAbstractions(TSUtils.createTsAbstractionsObject(pluginContext, log));

		dialog.toFront();
		dialog.dispose();
	}
}