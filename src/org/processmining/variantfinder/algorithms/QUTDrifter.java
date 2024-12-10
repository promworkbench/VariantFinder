//package org.processmining.variantfinder.algorithms;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.PrintStream;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//
//import ee.ut.eventstr.main.Main;
//
//public class QUTDrifter {
//	
//	public static final String RUNS = "runs", EVENTS = "events";
//
//	public static List<Date> doer(String filePath, String type) {
//		
//		//System.out.println("Path: " + System.getProperty("java.library.path"));
//		
//		Main m = new Main();
//		// Create a stream to hold the output
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		PrintStream ps = new PrintStream(baos);
//		// IMPORTANT: Save the old System.out!
//		PrintStream old = System.out;
//		// Tell Java to use your special stream
//		System.setOut(ps);
//
//		
//		
//		try {
//			//m.RunCommandLine(new String[] { "D:/ConceptDrift/log/logCD5Sudden.xes", "events" });
//			m.RunCommandLine(new String[] { filePath, type });
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		// Put things back
//		System.out.flush();
//		System.setOut(old);
//		// Show what happened
//		//System.out.println("Here: " + baos.toString());
//
//		String output = baos.toString();
//
//		String[] outputArray = output.split("\n");
//		String[] splitter = null;
//
//		List<Date> splitPoints = new ArrayList<Date>();
//		for (String s : outputArray) {
//			if (s != null && !s.isEmpty())
//				if (s.contains("time:")) {
//					splitter = s.split("time:");
//					if (splitter.length > 1)
//					{
//						Date d = parseDate(splitter[1].trim());
//						splitPoints.add(d);
//						System.out.println(d.getTime());
//					}
//
//				}
//		}
//		
//		return splitPoints;
//
//	}
//
//	public static Date parseDate(String date) {
//		DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
//		Date d = null;
//		try {
//			d = dateFormat.parse(date);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return d;
//	}
//}
