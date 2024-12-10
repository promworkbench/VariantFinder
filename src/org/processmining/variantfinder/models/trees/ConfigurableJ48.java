package org.processmining.variantfinder.models.trees;

import weka.classifiers.trees.J48;
import weka.classifiers.trees.M5P;
import weka.classifiers.trees.j48.ClassifierTree;

public class ConfigurableJ48 extends J48 {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConfigurableJ48() {
		super();
	}

	public ClassifierTree getClassifierTree() {
		return m_root;
	}
	
	public void stuff() throws Exception{
		m_root.buildClassifier(null);
		
		M5P classifier;
	}

}