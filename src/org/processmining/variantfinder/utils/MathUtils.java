package org.processmining.variantfinder.utils;

public class MathUtils {
	
	public static double getMax(double[] input){
		if(input == null)
			return -1;
		else{
			double result = input[0];
			for(double d : input)
				result = Math.max(result, d);
			return result;
		}
	}
	
	public static double getSum(double[] input){
		if(input == null)
			return -1;
		else{
			double result = 0;
			for(double d : input)
				result = result + d;
			return result;
		}
	}

}
