package org.processmining.variantfinder.renderers;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
// import weka.gui.treevisualizer.TreeVisualizer;

public class TreeRenderer {

	public static JPanel render(ImageIcon plot) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JLabel label = new JLabel("", plot, JLabel.CENTER);
		panel.add(label, BorderLayout.CENTER);
		return panel;
	}
}
