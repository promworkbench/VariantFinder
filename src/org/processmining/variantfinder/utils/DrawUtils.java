package org.processmining.variantfinder.utils;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.NodeID;
import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.models.graphbased.directed.transitionsystem.Transition;
import org.processmining.models.graphbased.directed.transitionsystem.payload.event.EventPayloadTransitionSystem;
import org.processmining.plugins.graphviz.colourMaps.ColourMap;
import org.processmining.plugins.graphviz.dot.AbstractDotElement;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotEdge;
import org.processmining.plugins.graphviz.dot.DotNode;
import org.processmining.processcomparator.model.ConstantDefinitions;
import org.processmining.variantfinder.models.annotatedtransitionsystems.constants.AnnotationConstants;
import org.processmining.variantfinder.models.annotatedtransitionsystems.impl.AnnotationElementDiscrete;
import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.AnnotatedTransitionSystem;
import org.processmining.variantfinder.models.annotatedtransitionsystems.interfaces.Annotation;

public class DrawUtils {

	public static Dot createGraph(AnnotatedTransitionSystem ats, double threshold, boolean showLabels,
			Set<Object> selectedElements) {
		Dot graph = new Dot();
		Map<NodeID, DotNode> nodes = new HashMap<NodeID, DotNode>(); //store all the nodes
		Map<Object, DotEdge> edges = new HashMap<Object, DotEdge>(); //store all the edges	

		//Analyze states
		for (State s : ats.getTransitionSystem().getNodes()) {

			s.setLabel(s.getAttributeMap().get(AttributeMap.TOOLTIP).toString());

			Annotation refAnnotation = ats.getAnnotation(s);

			//add and initialize a state
			nodes.put(s.getId(), graph.addNode(s.getLabel()));
			nodes.get(s.getId()).setOption("shape", "oval");
			nodes.get(s.getId()).setOption("style", "solid");
			nodes.get(s.getId()).setSelectable(true);
			nodes.get(s.getId()).setLabel(s.getLabel());

			if (refAnnotation == null) {
				//the root node
				nodes.get(s.getId()).setOption("penwidth", Integer.toString(ConstantDefinitions.MAX_EDGE_WIDTH)); //border width
				continue;
			}

			nodes.get(s.getId()).setOption("penwidth",
					Integer.toString((int) (VariantUtils
							.getAvg((AnnotationElementDiscrete) refAnnotation.getElement(AnnotationConstants.FREQUENCY))
							* ConstantDefinitions.MAX_EDGE_WIDTH) + 1)); //border width
		}
		// Analyze transitions
		for (Transition t : ats.getTransitionSystem().getEdges()) {

			//apply thresholds to the merged TS
			if (VariantUtils.getAvg((AnnotationElementDiscrete) ats.getAnnotation(t)
					.getElement(AnnotationConstants.FREQUENCY)) > threshold) {
				Annotation refAnnotation = ats.getAnnotation(t);

				//add and initialize an edge
				edges.put(t, graph.addEdge(nodes.get(t.getSource().getId()), nodes.get(t.getTarget().getId())));
				edges.get(t).setOption("color", ColourMap.toHexString(Color.BLACK));
				edges.get(t).setSelectable(true);

				edges.get(t).setOption("penwidth",
						Integer.toString((int) (VariantUtils.getAvg(
								(AnnotationElementDiscrete) refAnnotation.getElement(AnnotationConstants.FREQUENCY))
								* ConstantDefinitions.MAX_EDGE_WIDTH) + 1)); //edge width
				edges.get(t).setOption("style", "filled");

				if (showLabels)
					edges.get(t).setLabel(t.getLabel());
				else
					edges.get(t).setLabel("");
			}
		}

		clearUnconnectedNodes(graph, nodes, ats.getTransitionSystem());

		for (Object s : selectedElements) {
			if (s instanceof State)
				highlightNode(nodes.get(((State) s).getId()), Color.RED);
			else if (s instanceof Transition)
				highlightNode(edges.get((s)), Color.RED);
		}

		return graph;
	}

	private static Dot clearUnconnectedNodes(Dot graph, Map<NodeID, DotNode> nodes, EventPayloadTransitionSystem ts) {
		DotEdge aux;
		boolean connected;

		for (State s : ts.getNodes()) { // if a node is not connected through edges, remove it from the graph
			DotNode node1 = nodes.get(s.getId());
			connected = false;
			for (DotNode node2 : graph.getNodesRecursive())
			//if(!node1.getLabel().matches(node2.getLabel())) // this is for not considering self-loops
			{
				aux = graph.getFirstEdge(node1, node2); // outgoing edges
				if (aux != null)
					connected = true;
				aux = graph.getFirstEdge(node2, node1); // incoming edges
				if (aux != null)
					connected = true;
			}
			if (!connected) {
				graph.removeNode(node1);
				nodes.remove(s.getId());
			}
		}
		return graph;
	}

	public static void highlightNode(Object node, Color c) {
		if (node instanceof DotNode) {
			((AbstractDotElement) node).setOption("style", "filled");
			((AbstractDotElement) node).setOption("fillcolor", ColourMap.toHexString(c));
		} else {
			((AbstractDotElement) node).setOption("color", ColourMap.toHexString(c));
		}

	}

}
