package org.processmining.variantfinder.views.panels;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.processmining.framework.util.ui.widgets.ProMHeaderPanel;
import org.processmining.variantfinder.controllers.ModelController;

public class ModelPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1361829568073543334L;

	private ModelController controller;

	public ModelPanel(ModelController controller) {

		this.controller = controller;

		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBackground(Color.DARK_GRAY);

		ProMHeaderPanel panel = new ProMHeaderPanel("Model View");
	
		if (this.controller.getModelPanel() != null)
			panel.add(this.controller.getModelPanel());

		add(panel);

	}

}
