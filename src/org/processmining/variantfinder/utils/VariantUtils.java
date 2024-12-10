package org.processmining.variantfinder.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.processmining.variantfinder.models.annotatedtransitionsystems.impl.AnnotationElementDiscrete;

public class VariantUtils {

	public static String parseToString(double date) {
		Date newdate = new Date((long) date);
		SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return df2.format(newdate);
	}
	
	public static Date parseToDate(String date) {
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		Date result = null ;
		try {
			result =  df2.parse(date);
		} catch (ParseException e) {
			df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			try {
				result =  df2.parse(date);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}			
		}
		return result;
	}
	
	public static double getAvg(AnnotationElementDiscrete annotation){
		
		double sum = 0;
		for(Long value : annotation.getValues())
			sum = sum + value;
		
		return sum / annotation.getValues().size();
	}

}
