package com.veisite.utils.text;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MoneyFormat extends DecimalFormat {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static MoneyFormat formato = new MoneyFormat();
	
	public MoneyFormat() {
		super();
		setMaximumFractionDigits(2);
		setMinimumFractionDigits(2);
		setRoundingMode(RoundingMode.HALF_UP);
	}

	public static String formatMoney(double number) {
		return formato.format(number);
	}
	
}
