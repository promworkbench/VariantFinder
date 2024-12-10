package org.processmining.variantfinder.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import com.google.common.collect.Lists;

public class LogUtils {

	public static List<String> getAttributeNames(XLog log) {

		Set<String> names = new HashSet<String>();

		for (XTrace t : log) {
			names.addAll(t.getAttributes().keySet());
			for (XEvent e : t)
				names.addAll(e.getAttributes().keySet());
		}

		return Lists.newArrayList(names);

	}

}
