package com.veisite.utils.text;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DoubleFormat extends DecimalFormat {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DoubleFormat(int fractionDigits) {
		super();
		setMaximumFractionDigits(fractionDigits);
		setMinimumFractionDigits(fractionDigits);
		setRoundingMode(RoundingMode.HALF_UP);
	}

}
