package com.veisite.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePeriod implements IDatePeriod, Comparable<IDatePeriod>, Cloneable {
	
	private Calendar desde;
	private Calendar hasta;
	
	private SimpleDateFormat dFormat = new SimpleDateFormat("dd/MM/yyyy");
	
	public DatePeriod() {
	}

	public DatePeriod(Calendar desde, Calendar hasta) {
		setDesde(desde);
		setHasta(hasta);
	}

	/**
	 * Espera la fechas en formato dd/MM/yyyy
	 * @param desde
	 * @param hasta
	 */
	public DatePeriod(String desde, String hasta) throws ParseException {
		Date d = dFormat.parse(desde);
		Calendar cd = new GregorianCalendar();
		cd.setTime(d);
		setDesde(cd);
		d = dFormat.parse(hasta);
		cd = new GregorianCalendar();
		cd.setTime(d);
		setHasta(cd);
	}
	
	public Calendar getDesde() {
		return desde;		
	}

	public void setDesde(Calendar newDesde) {
		this.desde=newDesde;

	}

	public Calendar getHasta() {
		return hasta;
		
	}

	public void setHasta(Calendar newHasta) {
		this.hasta=newHasta;

	}

	public int compareTo(IDatePeriod o) {
		int i = getDesde().compareTo(o.getDesde());
		if (i!=0) return i;
		return getHasta().compareTo(o.getHasta());
	}

	@Override
	public DatePeriod clone() {
		return new DatePeriod((Calendar) getDesde().clone(), 
									(Calendar) getHasta().clone());
	}
	
	@Override
	public String toString() {
		return DateUtil.toString(this);
	}
	
}
