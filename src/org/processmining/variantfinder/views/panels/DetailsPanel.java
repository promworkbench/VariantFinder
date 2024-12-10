package org.processmining.variantfinder.views.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.processmining.framework.util.ui.widgets.ProMHeaderPanel;
import org.processmining.variantfinder.controllers.DetailsController;

public class DetailsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4787465249388389568L;

	private DetailsController controller;
	private Component component;

	public DetailsPanel(DetailsController controller) {
		this.controller = controller;
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(Color.DARK_GRAY);
		setPreferredSize(new Dimension(350, 400));
		setLayout(new BorderLayout());

		ProMHeaderPanel panel = new ProMHeaderPanel("Selected Split Criteria Details");
		//		JTabbedPane slickerTabbedPane = new JTabbedPane();
		//		component = this.controller.getDecisionPointPanel();
		//		//slickerTabbedPane.addTab("Attribute", this.controller.getAttributePanel());
		//		slickerTabbedPane.addTab("Conditional Inference Tree", component);
		if (controller.getDecisionPointPanel() != null)
			panel.add(controller.getDecisionPointPanel());

		add(panel, BorderLayout.CENTER);

	}
}
