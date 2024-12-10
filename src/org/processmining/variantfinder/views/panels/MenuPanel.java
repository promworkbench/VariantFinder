package org.processmining.variantfinder.views.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.processmining.framework.util.ui.widgets.ProMHeaderPanel;
import org.processmining.variantfinder.controllers.MenuController;
import org.processmining.variantfinder.utils.LogUtils;

public class MenuPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -455864125009526521L;

	private JRadioButton rdbtnUseDefaultSettings, rdbtnUseCustomSettings;
	private JCheckBox chckbxFrequencyThreshold, chckbxShowTransitionLabels, chckbxUseIndividually;
	private JComboBox<String> classAttribute;

	private JList<String> attributeList;
	private JTextField frequencyThresholdTextField, minInstancesPerLeafTextField;
	private JButton btnFindVariants, btnSplitLogUsing, btnOpenSettings;
	private JTextField alpha;
	private JLabel lblImageWidth;
	private JLabel lblImageHeight;
	private JTextField widthTextField;
	private JTextField heightTextField;

	public MenuPanel(MenuController menuController) {

		setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(null);
		setBackground(Color.DARK_GRAY);
		setPreferredSize(new Dimension(350, 750));
		
		List<String> attributes = LogUtils.getAttributeNames(menuController.getMainController().getLog());

		ProMHeaderPanel tsSettingsPanel = new ProMHeaderPanel("Transition System Settings");
		tsSettingsPanel.setBounds(12, 13, 326, 166);

		add(tsSettingsPanel);

		JPanel panel = new JPanel();
		tsSettingsPanel.add(panel);
		panel.setLayout(null);

		ButtonGroup group = new ButtonGroup();
		rdbtnUseDefaultSettings = new JRadioButton("Use Default Settings");
		rdbtnUseDefaultSettings.setSelected(true);
		rdbtnUseDefaultSettings.setBounds(8, 9, 160, 25);
		panel.add(rdbtnUseDefaultSettings);

		rdbtnUseCustomSettings = new JRadioButton("Use Custom Settings");
		rdbtnUseCustomSettings.setBounds(8, 30, 147, 25);
		panel.add(rdbtnUseCustomSettings);

		group.add(rdbtnUseCustomSettings);
		group.add(rdbtnUseDefaultSettings);

		btnOpenSettings = new JButton("Open Settings");
		btnOpenSettings.setBounds(175, 30, 113, 25);
		panel.add(btnOpenSettings);

		chckbxFrequencyThreshold = new JCheckBox("Frequency Threshold:");
		chckbxFrequencyThreshold.setSelected(true);
		chckbxFrequencyThreshold.setBounds(8, 60, 160, 25);
		panel.add(chckbxFrequencyThreshold);

		frequencyThresholdTextField = new JTextField();
		frequencyThresholdTextField.setBounds(175, 61, 33, 22);
		frequencyThresholdTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		frequencyThresholdTextField.setText("5");
		panel.add(frequencyThresholdTextField);
		frequencyThresholdTextField.setColumns(10);

		JLabel label = new JLabel("%");
		label.setBounds(211, 64, 12, 16);
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(label);

		chckbxShowTransitionLabels = new JCheckBox("Show Transition Labels");
		chckbxShowTransitionLabels.setBounds(8, 84, 200, 25);
		panel.add(chckbxShowTransitionLabels);

		add(Box.createVerticalStrut(5));

		ProMHeaderPanel dtSettingsPanel = new ProMHeaderPanel("Partitioning Settings");
		dtSettingsPanel.setBounds(12, 192, 326, 388);

		add(dtSettingsPanel);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EmptyBorder(5, 5, 5, 5));
		dtSettingsPanel.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblMinimumPercentageOf = new JLabel("Min % of instances in a leaf:");
		lblMinimumPercentageOf.setBounds(5, 35, 163, 16);
		panel_1.add(lblMinimumPercentageOf);

		minInstancesPerLeafTextField = new JTextField();
		minInstancesPerLeafTextField.setBounds(175, 32, 33, 22);
		minInstancesPerLeafTextField.setText("5");
		minInstancesPerLeafTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		minInstancesPerLeafTextField.setColumns(10);
		panel_1.add(minInstancesPerLeafTextField);

		JLabel label_1 = new JLabel("%");
		label_1.setBounds(211, 35, 12, 16);
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(label_1);

		JLabel lblSelectedAttributes = new JLabel("Selected Attributes: ");
		lblSelectedAttributes.setBounds(12, 124, 119, 16);
		panel_1.add(lblSelectedAttributes);

		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for(String s :  attributes)
			listModel.addElement(s);
		
		attributeList = new JList<String>(listModel);
		attributeList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane scrollList = new JScrollPane(attributeList);
		scrollList.setBounds(12, 146, 282, 175);
		panel_1.add(scrollList);
		
		JLabel lblAttributeToPredict = new JLabel("Class Attribute:");
		lblAttributeToPredict.setBounds(5, 95, 99, 16);
		panel_1.add(lblAttributeToPredict);
		
		classAttribute = new JComboBox<String>(attributes.toArray(new String[attributes.size()]));
		classAttribute.setBounds(104, 92, 190, 22);
		panel_1.add(classAttribute);
		
		JLabel lblAlphastatisticalTest = new JLabel("Alpha (statistical test):");
		lblAlphastatisticalTest.setBounds(5, 63, 163, 16);
		panel_1.add(lblAlphastatisticalTest);
		
		alpha = new JTextField();
		alpha.setText("5");
		alpha.setHorizontalAlignment(SwingConstants.RIGHT);
		alpha.setColumns(10);
		alpha.setBounds(175, 60, 33, 22);
		panel_1.add(alpha);
		
		JLabel label_4 = new JLabel("%");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setBounds(211, 63, 12, 16);
		panel_1.add(label_4);
		
		chckbxUseIndividually = new JCheckBox("use individually");
		chckbxUseIndividually.setSelected(true);
		chckbxUseIndividually.setHorizontalAlignment(SwingConstants.TRAILING);
		chckbxUseIndividually.setBounds(163, 120, 131, 25);
		panel_1.add(chckbxUseIndividually);
		
		lblImageWidth = new JLabel("Image Width:");
		lblImageWidth.setBounds(5, 6, 78, 16);
		panel_1.add(lblImageWidth);
		
		lblImageHeight = new JLabel("Image Height:");
		lblImageHeight.setBounds(145, 6, 88, 16);
		panel_1.add(lblImageHeight);
		
		widthTextField = new JTextField();
		widthTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		widthTextField.setText("800");
		widthTextField.setBounds(89, 3, 42, 22);
		panel_1.add(widthTextField);
		widthTextField.setColumns(10);
		
		heightTextField = new JTextField();
		heightTextField.setText("400");
		heightTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		heightTextField.setColumns(10);
		heightTextField.setBounds(236, 3, 42, 22);
		panel_1.add(heightTextField);

		add(Box.createVerticalStrut(5));

		JPanel actionsPanel = new ProMHeaderPanel("Actions");
		actionsPanel.setBounds(12, 593, 326, 144);
		add(actionsPanel);

		JPanel panel_2 = new JPanel();
		actionsPanel.add(panel_2);
		panel_2.setLayout(null);

		btnFindVariants = new JButton("Apply Settings");
		btnFindVariants.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnFindVariants.setBounds(12, 13, 282, 25);
		panel_2.add(btnFindVariants);

		btnSplitLogUsing = new JButton("Split Log using Selected Criteria");
		btnSplitLogUsing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnSplitLogUsing.setBounds(12, 51, 282, 25);
		panel_2.add(btnSplitLogUsing);

		add(Box.createVerticalGlue());

	}

	public void addOpenSettingsListeners(ActionListener listener) {
		btnOpenSettings.addActionListener(listener);
	}

	public void addFindVariantsListener(ActionListener listener) {
		btnFindVariants.addActionListener(listener);
	}

	public void addSplitLogListener(ActionListener listener) {
		btnSplitLogUsing.addActionListener(listener);
	}

	public boolean isDefaultTsSettingsSelected() {
		return rdbtnUseDefaultSettings.isSelected();
	}

	public boolean isFrequencyThresholdActivated() {
		return chckbxFrequencyThreshold.isSelected();
	}

	public double getFrequencyThreshold() {
		return Double.parseDouble(frequencyThresholdTextField.getText());
	}

	public Double getMinInstancesPerLeaf() {
		return Double.parseDouble(minInstancesPerLeafTextField.getText());
	}
	
	public double getAlpha() {
		return Double.parseDouble(alpha.getText());
	}

	public int getWidthValue(){
		return Integer.parseInt(widthTextField.getText());
	}
	
	public int getHeightValue(){
		return Integer.parseInt(heightTextField.getText());
	}

	public List<String> getSelectedAttributes() {
		return attributeList.getSelectedValuesList();
	}

	public boolean isShowTransitionLabelsSelected() {
		return chckbxShowTransitionLabels.isSelected();
	}
	
	public String getClassAttribute(){
		return (String) classAttribute.getSelectedItem();
	}
	
	public boolean isUseIndividually(){
		return chckbxUseIndividually.isSelected();
	}
}
