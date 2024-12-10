package org.processmining.variantfinder.renderers;

import org.jfree.chart.ChartPanel;

import weka.core.Instances;

public interface NodeChart {
	
	public ChartPanel getChart(Instances instances, String chartName);

}
