package org.processmining.variantfinder.models;

import java.util.List;

import javax.swing.table.AbstractTableModel;

public class SplitCriteriaTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7564169126420855848L;

	private List<SplitCriteriaRow> rows;

	public SplitCriteriaTableModel(List<SplitCriteriaRow> rows) {
		this.rows = rows;
	}

	public int getColumnCount() {
		return 5;
	}

	public int getRowCount() {
		return rows.size();
	}

	public SplitCriteriaRow getRow(int index) {
		return rows.get(index);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {

		Object value = "??";
		SplitCriteriaRow row = rows.get(rowIndex);
		switch (columnIndex) {
			case 0 :
				value = row.getType();
				break;
			case 1 :
				value = row.getName();
				break;
			case 2 :
				value = row.getSplitAttribute();
				break;
			case 3 :
				value = row.getSplitValues();
				break;
			case 4 :
				value = row.getFrequency();
				break;
		}
		return value;
	}
	public String getColumnName(int columnIndex){
		String name = "?";
		switch (columnIndex) {
			case 0 :
				name = "Element Type";
				break;
			case 1 :
				name = "Element Name";
				break;
			case 2 :
				name = "Split Attribute";
				break;
			case 3 :
				name = "Split Values";
				break;
			case 4:
				name = "Frequency of occurrence";
				break;
		}
		return name;
	}
	
	public boolean isCellEditable(){
		return false;
	}

}
