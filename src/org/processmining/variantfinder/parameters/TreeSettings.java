package org.processmining.variantfinder.parameters;

import java.util.List;

public class TreeSettings {

	private List<String> attributes;
	private String classAttribute;

	private double minPercentageOnLeaf, alpha;

	private int width, height;

	private boolean useIndividually;

	public TreeSettings(List<String> attributes, String classAttribute, double minpercentage, boolean useIndividually,
			double alpha, int width, int height) {
		this.attributes = attributes;
		this.classAttribute = classAttribute;
		this.minPercentageOnLeaf = minpercentage;
		this.alpha = alpha;
		this.useIndividually = useIndividually;
		this.width = width;
		this.height = height;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public String getClassAttribute() {
		return classAttribute;
	}

	public double getMinPercentageOnLeaf() {
		return minPercentageOnLeaf;
	}

	public double getAlpha() {
		return alpha;
	}

	public boolean isUseIndividually() {
		return useIndividually;
	}

	public boolean isEqual(TreeSettings o1) {
		return (attributes.equals(o1.getAttributes()) && classAttribute.equals(o1.getClassAttribute())
				&& minPercentageOnLeaf == o1.getMinPercentageOnLeaf() && width == o1.getWidth()
				&& height == o1.getHeight() && alpha == o1.getAlpha() && useIndividually == o1.isUseIndividually());
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

}
