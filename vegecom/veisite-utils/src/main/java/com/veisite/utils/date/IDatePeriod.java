package com.veisite.utils.date;

import java.util.Calendar;

public interface IDatePeriod extends Comparable<IDatePeriod> {

	/**
	 * Devuelve la fecha de inicio del periodo
	 * @return
	 */
	public Calendar getDesde();
	
	/**
	 * Establece la nueva fecha de inicio del periodo
	 * Siempre será <= hasta 
	 * @param newDesde
	 */
	public void setDesde(Calendar newDesde);
	
	/**
	 * Devuelve la fecha final del periodo
	 * @return
	 */
	public Calendar getHasta();
	
	/**
	 * Establece la nueva fecha final del periodo
	 * Siempre será >= desde 
	 * @param newDesde
	 */
	public void setHasta(Calendar newHasta);
	
}
