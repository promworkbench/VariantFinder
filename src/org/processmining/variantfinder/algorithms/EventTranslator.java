package org.processmining.variantfinder.algorithms;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;
import org.processmining.models.graphbased.directed.transitionsystem.State;
import org.processmining.models.graphbased.directed.transitionsystem.Transition;
import org.processmining.models.graphbased.directed.transitionsystem.payload.event.EventPayloadTransitionSystem;
import org.processmining.plugins.transitionsystem.miner.TSMinerPayload;
import org.processmining.plugins.transitionsystem.miner.TSMinerPayloadHandler;
import org.processmining.variantfinder.parameters.TsSettings;

/**
 * This class stores a pointer to a transition system and the settings object it
 * was created with. This enables us to map events into elements of the
 * transition system in order to annotate them.
 * 
 * @author abolt
 *
 */
public class EventTranslator {

	private TsSettings settings;
	private EventPayloadTransitionSystem ts;

	public void setTS(EventPayloadTransitionSystem ts) {
		this.ts = ts;
	}

	public EventPayloadTransitionSystem getTS() {
		return ts;
	}

	public void setSettings(TsSettings settings) {
		this.settings = settings;
	}

	public TsSettings getSettings() {
		return settings;
	}

	public State getStatefromEvent(XEvent event, XTrace trace) {
		TSMinerPayloadHandler payloadHandler = new TSMinerPayloadHandler(settings.getTsAbstractions());
		TSMinerPayload toPayload = (TSMinerPayload) payloadHandler.getTargetStateIdentifier(trace,
				trace.indexOf(event));
		return ts.getNode(toPayload);
	}

	public Transition getTransitionfromEvent(XEvent event, XTrace trace) {
		return ts.getTransition(trace, trace.indexOf(event));
	}

}
