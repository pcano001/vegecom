package com.veisite.utils.math;



/* Utilidades de calculo */

public class MathUtil {	

	public static double round2(double number){
		return Math.round(number*100.0d)/100.0d;
	}
	
	public static double round4(double number){
		return Math.round(number*10000.0d)/10000.0d;
	}
	
}

