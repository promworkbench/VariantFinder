package org.processmining.variantfinder.views.panels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.processmining.framework.util.ui.widgets.ProMHeaderPanel;
import org.processmining.framework.util.ui.widgets.ProMScrollPane;
import org.processmining.variantfinder.controllers.SplitCriteriaController;
import org.processmining.variantfinder.models.SplitCriteriaTableModel;

public class SplitCriteriaPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4007206494841733650L;

	private SplitCriteriaController controller;
	private JTable table;

	public SplitCriteriaPanel(SplitCriteriaController controller) {
		this.controller = controller;

		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBackground(Color.DARK_GRAY);
		setPreferredSize(new Dimension(350, 400));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		ProMHeaderPanel panel = new ProMHeaderPanel("Split Criteria");

		table = new JTable(new SplitCriteriaTableModel(controller.getSplitPoints()));
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				if (!arg0.getValueIsAdjusting())
					triggerUpdate();
			}
		});

		ProMScrollPane scroll = new ProMScrollPane(table);
		panel.add(scroll);
		add(panel);

	}

	public int[] getSelectedRows() {
		return table.getSelectedRows();
	}

	private void triggerUpdate() {
		controller.updateSelections();
	}
}
