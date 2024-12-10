package org.processmining.variantfinder.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeContinuous;
import org.deckfour.xes.model.XAttributeDiscrete;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XAttributeTimestamp;
import org.deckfour.xes.model.XEvent;
import org.processmining.variantfinder.models.annotatedtransitionsystems.impl.AnnotationElementXEvent;
import org.processmining.variantfinder.models.trees.RuleSet;
import org.processmining.variantfinder.parameters.TreeSettings;
import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

import com.google.common.collect.Lists;

public class RengineUtils { 

	private static RConnection connection;

	public static RConnection getConnection() {
		if (connection == null)
			try {
				connection = new RConnection();
			} catch (RserveException e) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Rserve is not running! Please make sure to start it.");
				e.printStackTrace();
				return null;
			}
		return connection;
	}

	public static List<RuleSet> analyzeElement(AnnotationElementXEvent annotationEvents, TreeSettings settings) {

		List<RuleSet> differences = new ArrayList<RuleSet>();

		if (settings.isUseIndividually()) {
			for (String attribute : settings.getAttributes()) {
				RuleSet result = null;
				try {
					result = buildTree(annotationEvents, Lists.newArrayList(attribute), settings);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (result != null)
					differences.add(result);
			}
		} else {
			RuleSet result = null;
			try {
				result = buildTree(annotationEvents, settings.getAttributes(), settings);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (result != null)
				differences.add(result);
		}
		return differences;
	}

	/**
	 * Synchronized method to build a tree using R.
	 * 
	 * @param annotationEvents
	 * @param allAttributes
	 * @param classAttribute
	 * @param width
	 * @param height
	 * @return
	 * @throws IOException
	 * @throws REngineException
	 * @throws REXPMismatchException
	 */
	public static synchronized RuleSet buildTree(AnnotationElementXEvent annotationEvents, List<String> attributes,
			TreeSettings settings) throws IOException, REngineException, REXPMismatchException {

		RConnection c = getConnection();

		assert (c != null);

		REXP rao2 = c.parseAndEval("try(eval(capture.output(library('partykit'))),silent=TRUE)");
		if (rao2.inherits("try-error")) {
			System.out.println("rao: " + rao2.asString());
		}

		List<String> allAttributes = new ArrayList<String>();
		allAttributes.addAll(attributes);
		allAttributes.add(settings.getClassAttribute());

		String dataStructure = "";
		Map<String, String> colTypes = new HashMap<String, String>();
		// Build the header
		for (String attribute : allAttributes)
			for (XEvent event : annotationEvents.getValues()) {
				if (event.getAttributes().containsKey(attribute)) {
					if (event.getAttributes().get(attribute) instanceof XAttributeLiteral)
						colTypes.put(escapeCharacters(attribute), "character()");
					else if (event.getAttributes().get(attribute) instanceof XAttributeDiscrete)
						colTypes.put(escapeCharacters(attribute), "double()");
					else if (event.getAttributes().get(attribute) instanceof XAttributeTimestamp)
						colTypes.put(escapeCharacters(attribute), "numeric()");
					else
						colTypes.put(escapeCharacters(attribute), "double()");
					break;
				}
			}
		for (String att : allAttributes) {
			String col = escapeCharacters(att);
			dataStructure = dataStructure + col + " = " + colTypes.get(col) + ", ";
		}
		dataStructure = dataStructure + "stringsAsFactors = FALSE";

		try {
			rao2 = c.parseAndEval("mydata <- data.frame(" + dataStructure + ")");
			if (rao2.inherits("try-error")) {
				System.out.println("rao: " + rao2.asString());
			}

		} catch (REngineException | REXPMismatchException e1) {
			e1.printStackTrace();
		}

		int nrows = 0;
		for (XEvent event : annotationEvents.getValues()) {
			String dataRow = "";
			for (String attribute : allAttributes) {
				if (event.getAttributes().containsKey(attribute)) {
					XAttribute att = event.getAttributes().get(attribute);
					if (att instanceof XAttributeLiteral)
						dataRow = dataRow + "'" + ((XAttributeLiteral) att).getValue() + "',";
					else if (att instanceof XAttributeDiscrete)
						dataRow = dataRow + ((XAttributeDiscrete) att).getValue() + ",";
					else if (att instanceof XAttributeTimestamp)
						dataRow = dataRow + ((XAttributeTimestamp) att).getValue().getTime() + ",";
					else if (att instanceof XAttributeContinuous)
						dataRow = dataRow + ((XAttributeContinuous) att).getValue() + ",";
					else
						dataRow = dataRow + "'" + att.toString() + "',";
				} else
					dataRow = dataRow + "NA,";
			}
			if (dataRow.endsWith(","))
				dataRow = dataRow.substring(0, dataRow.length() - 1);

			if (!dataRow.contains("NA")) { //TODO deal with missing values
				c.eval("mydata[nrow(mydata)+1,] <-c(" + dataRow + ")");
				nrows++;
			}
		}

		for (String att : allAttributes) {
			String col = escapeCharacters(att);
			if (colTypes.get(col) == "character()")
				print(c, col + "<- as.factor(mydata$" + col + ")");
			else if (colTypes.get(col) == "integer()")
				print(c, col + "<- as.integer(mydata$" + col + ")");
			else if (colTypes.get(col) == "double()")
				print(c, col + "<- as.double(mydata$" + col + ")");
			else if (colTypes.get(col) == "numeric()")
				//print(c, col + "<- as.Date(mydata$" + col + ", origin = \"1970-01-01\")");
				print(c, col + "<- as.numeric(mydata$" + col + ")");
		}

		String treebuild = "";

		for (String a : attributes) {
			treebuild = treebuild + " " + escapeCharacters(a) + " +";
		}
		if (treebuild.endsWith("+"))
			treebuild = treebuild.substring(0, treebuild.length() - 1);

		treebuild = treebuild.trim();

		String control = "ctree_control(testtype = \"Bonferroni\", mincriterion = " + (1d - settings.getAlpha())
				+ ", minbucket = " + (int) (settings.getMinPercentageOnLeaf() * nrows) + ")";

		try {//mydata2$
			String tree = "ct2 <- ctree(" + escapeCharacters(settings.getClassAttribute()) + " ~ " + treebuild
					+ ", control = " + control + ")";
			rao2 = c.parseAndEval("try(eval(capture.output(" + tree + ")),silent=FALSE)");
			if (rao2.inherits("try-error")) {
				System.out.println("rao: " + rao2.asString());
				return null;
			}
		} catch (REngineException | REXPMismatchException e1) {
			e1.printStackTrace();
		}
		print(c,"png(filename='SampleGraph.png', width = " + settings.getWidth() + ", height = " + settings.getHeight()
				+ ")");
		print(c, "plot(ct2)");
		c.eval("dev.off()");

		String treeprinter = "print(t(partykit:::.list.rules.party(ct2)))";
		rao2 = c.parseAndEval("try(eval(capture.output(" + treeprinter + ")),silent=FALSE)");

		String path = print(c, "getwd()");

		path = path.substring(path.indexOf('"') + 1, path.lastIndexOf('"')) + "/SampleGraph.png";
		
		print(c, "rm(list=ls())");

		BufferedImage img = ImageIO.read(new File(path));
		ImageIcon icon = new ImageIcon(img);

		RuleSet ruleList = new RuleSet(rao2.asStrings(), icon);

		//System.out.println(ruleList.toString());

		//		JLabel label = new JLabel(icon);
		//		JOptionPane.showMessageDialog(null, label);

		return ruleList;

	}

	public static String print(RConnection c, String rCode) {

		String result = "";
		REXP rao2 = null;
		try {
			rao2 = c.parseAndEval("try(eval(capture.output(" + rCode + ")),silent=FALSE)");
			String[] results = rao2.asStrings();
			for (int i = 0; i < results.length; i++)
				result = result + results[i] + "\n";
			if (rao2.inherits("try-error")) {
				System.out.println("rao: " + rao2.asString());
			}
		} catch (REngineException | REXPMismatchException e1) {
			e1.printStackTrace();
		}
		return result;

	}

	public static void evaluate(RConnection c, String code) {

		REXP rao2 = null;
		try {
			rao2 = c.parseAndEval("try(eval(capture.output(" + code + ")),silent=FALSE)");
			if (rao2.inherits("try-error")) {
				System.out.println("rao: " + rao2.asString());
			}
		} catch (REngineException | REXPMismatchException e1) {
			e1.printStackTrace();
		}
	}

	public static String escapeCharacters(String string) {
		String result = string;
		result = result.replace(":", "_");
		result = result.replace(".", "_");
		result = result.replace(",", "_");
		result = result.replace("+", "_");
		result = result.replace("-", "_");
		result = result.replace(" ", "_");
		return result;
	}

}
