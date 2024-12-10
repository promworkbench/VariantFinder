package org.processmining.variantfinder.parameters;

public class Settings {
	
	private TsSettings tsSettings;
	private TreeSettings treeSettings;
	
	public Settings(TsSettings tsSettings, TreeSettings treeSettings){
		this.tsSettings = tsSettings;
		this.treeSettings = treeSettings;
	}
	
	public TsSettings getTsSettings() {
		return tsSettings;
	}

	public TreeSettings getTreeSettings() {
		return treeSettings;
	}

}
