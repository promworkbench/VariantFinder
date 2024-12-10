package org.processmining.variantfinder.renderers;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import weka.core.Instance;
import weka.core.Instances;

public class CategoricalClassChart implements NodeChart{

	public ChartPanel getChart(Instances inst, String chartName) {
		Enumeration<Object> enumerateValues = inst.classAttribute().enumerateValues();

		Map<String, Long> classCounts = new HashMap<String, Long>();

		while (enumerateValues.hasMoreElements()) {
			classCounts.put(enumerateValues.nextElement().toString(), (long) 0);
		}

		Iterator<Instance> iterator = inst.iterator();
		//Attribute classAttribute = inst.classAttribute();

		while (iterator.hasNext()) {
			Instance i = iterator.next();
			classCounts.put(i.classAttribute().value((int) i.classValue()),
					classCounts.get(i.classAttribute().value((int) i.classValue())) + 1);
		}

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for (String c : classCounts.keySet()) {
			if (classCounts.get(c) > 0)
				dataset.addValue(classCounts.get(c), "Class Count", c);
		}

		JFreeChart barChart = ChartFactory.createBarChart("Node: " + chartName, "Class", "Count",
				dataset, PlotOrientation.VERTICAL, true, true, false);

		ChartPanel chartPanel = new ChartPanel(barChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));


		return chartPanel;
	}

}
