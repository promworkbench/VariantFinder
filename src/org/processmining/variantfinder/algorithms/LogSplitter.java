package org.processmining.variantfinder.algorithms;

import java.util.Arrays;
import java.util.Date;

import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryNaiveImpl;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.variantfinder.utils.VariantUtils;

public class LogSplitter {

	public static XLog[] splitLogsOnTimestamp(XLog log, Date[] split, XAttribute time) {

		Arrays.sort(split);
		XFactory factory = new XFactoryNaiveImpl();

		XLog[] splittedLogs = new XLog[split.length + 1];
		for (XLog sl : splittedLogs)
			sl = factory.createLog(log.getAttributes());

		for (XTrace t : log) {
			for (XEvent e : t) {
				if (isSameBucket(t, split, time)) //whole trace in same bucket
					splittedLogs[getBucket(e, split, time)].add(t);
				else {//split the trace
					XTrace[] splitTraces = splitTrace(t, split, time);
					for (int i = 0; i < splitTraces.length; i++)
						if (!splitTraces[i].isEmpty())
							splittedLogs[i].add(splitTraces[i]);
				}
			}
		}
		//WARNING: some of these logs might be empty!!
		return splittedLogs;
	}

	private static int getBucket(XEvent e, Date[] split, XAttribute time) {
		for (int i = 0; i < split.length; i++) {
			if (VariantUtils.parseToDate(e.getAttributes().get(time.getKey()).toString()).before(split[i]))
				return i;
		}
		return split.length;
	}

	private static boolean isSameBucket(XTrace t, Date[] split, XAttribute time) {
		XEvent first = t.get(0);
		XEvent last = t.get(t.size() - 1);

		if (first == last)
			return true;
		else if (getBucket(first, split, time) == getBucket(last, split, time))
			return true;
		else
			return false;
	}

	private static XTrace[] splitTrace(XTrace t, Date[] split, XAttribute time) {
		XTrace[] splitTraces = new XTrace[split.length + 1];
		for (XTrace sT : splitTraces) {
			sT = (XTrace) t.clone();
			sT.clear();
		}
		for (XEvent e : t)
			splitTraces[getBucket(e, split, time)].add(e);
		return splitTraces;
	}
}
