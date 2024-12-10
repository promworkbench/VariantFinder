package org.processmining.variantfinder.views;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.processmining.framework.util.ui.widgets.ProMSplitPane;
import org.processmining.variantfinder.controllers.MainController;

import com.fluxicon.slickerbox.factory.SlickerFactory;

public class MainView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8509348322414698126L;

	JPanel modelPane;
	
	private ProMSplitPane resultsSplitPane;

	private JScrollPane menuPane, splitCriteriaPane, detailsPane;
	private MainController mainController;

	public MainView(MainController mainController) {
		setLayout(new BorderLayout(0, 0));
		setBackground(Color.DARK_GRAY);
		
		menuPane = new JScrollPane();
		add(menuPane, BorderLayout.EAST);
		
		resultsSplitPane = new ProMSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		add(resultsSplitPane, BorderLayout.CENTER);
		
//		modelPane = new JPanel();
//		resultsSplitPane.setLeftComponent(modelPane);
		
		JSplitPane analysisSplitPane = new JSplitPane();
		analysisSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		resultsSplitPane.setRightComponent(analysisSplitPane);
		
		splitCriteriaPane = new JScrollPane();
		analysisSplitPane.setLeftComponent(splitCriteriaPane);
		
		detailsPane = new JScrollPane();
		analysisSplitPane.setRightComponent(detailsPane);
		
		this.mainController = mainController;
		
		
		SlickerFactory.instance().createRoundedPanel();
		
	}

	public void setModelPane(JPanel content) {
		//modelPane.add(content);
		resultsSplitPane.setLeftComponent(content);
	}

	public void setMenuPane(JPanel content) {
		menuPane.setViewportView(content);
	}

	public void setSplitCriteriaPane(JPanel content) {
		splitCriteriaPane.setViewportView(content);
	}

	public void setDetailsPane(JPanel content) {
		detailsPane.setViewportView(content);
	}

}
