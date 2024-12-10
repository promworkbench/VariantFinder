package org.processmining.variantfinder.algorithms;

public class VariantFinder {

	/**
	 * This method will detect data attributes to split the event log in. The
	 * idea is that this split will create nice prcess variants. The differneces
	 * between variants will be control-flow-based.
	 * 
	 * @param log
	 * @param settings
	 * @return variants (log)
	 */
//	public static Set<StatisticalDifference> findProcessVariants(XLog log, TreeSettings settings, PluginContext pluginContext,
//			AnnotatedTransitionSystem ats, double frequencyThreshold) {
//
//		List<DecisionPoint> decisionPoints = new ArrayList<DecisionPoint>();
//		Collection<State> dPstates = TSUtils.getDecisionPoints(ats, frequencyThreshold);
//		for (State s : dPstates)
//			decisionPoints.add(new DecisionPoint(s, ats));
//
//		//create decision trees of depth 1 (sublet?)
//
//		Set<StatisticalDifference> splittingPoints = new TreeSet<StatisticalDifference>();
//
//		for (DecisionPoint dP : decisionPoints) {
//			J48TreeRule decisionTree = DTUtils.createDecisionTree(log, dP.getSource(), dP.getTargets(), settings);
//			//AlfDecisionStump stump = new AlfDecisionStump();
//			try {
//				weka.classifiers.Evaluation result = decisionTree.getEvaluation();
//
//				//take the confusion matrix, add the columns, and then see if there is a balanced split
//				//System.out.println(decisionTree.toString());
//				System.out.println(result.toMatrixString());
//
//				Set<Object> splitValues = new HashSet<Object>();
//				String attribute = "";
//				for (Pair<String, Object> pair : DTUtils.getSplitPoints(decisionTree)) {
//					//System.out.println("Split point: att = " + pair.getLeft() + ", value = " + pair.getRight());
//					splitValues.add(pair.getRight());
//					attribute = pair.getLeft();
//				}
//
//				double[][] confusionMatrix = result.confusionMatrix();
//				double[] lines = new double[confusionMatrix.length];
//				for (int i = 0; i < confusionMatrix.length; i++)
//					for (int j = 0; j < confusionMatrix[0].length; j++)
//						lines[i] = lines[i] + confusionMatrix[j][i];
//
//				splittingPoints.add(new SplittingPoint(dP, MathUtils.getMax(lines) / MathUtils.getSum(lines),
//						result.pctCorrect(), attribute, Lists.newArrayList(splitValues), decisionTree));
//
//			} catch (Exception e) {
//				System.out.println("Some error occurred building the Decision Trees");
//				e.printStackTrace();
//			}
//		}
//		return splittingPoints;
//
//	}

}
