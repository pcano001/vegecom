package com.veisite.utils.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.veisite.utils.math.MathUtil;

public class DateUtil {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	private static final SimpleDateFormat ldf = new SimpleDateFormat("d 'de' MMMMM 'de' yyyy");
	
	/**
	 * Devuelve el numero de meses del perido inicio-fin
	 * El valor devuelto puede no ser entero ya que computa también
	 * el número de días de un mes no completo como la parte proporcional 
	 * de dias de ese mes entre 0 y 1
	 * @param inicio
	 * @param fin
	 * @return
	 */
	public static double monthsBetween(Calendar desde, Calendar hasta) {
		Calendar inicio = new GregorianCalendar();
		inicio.setTime(desde.getTime());
		Calendar fin = new GregorianCalendar();
		fin.setTime(hasta.getTime());
		double meses = 0.0d;
		if (fin.compareTo(inicio)<0) return meses;
		int yeari = inicio.get(Calendar.YEAR);
		int monthi = inicio.get(Calendar.MONTH);
		int dayi =  inicio.get(Calendar.DAY_OF_MONTH);
		int yearf = fin.get(Calendar.YEAR);
		int monthf = fin.get(Calendar.MONTH);
		int dayf =  fin.get(Calendar.DAY_OF_MONTH);
		double maxi = (double) inicio.getActualMaximum(Calendar.DAY_OF_MONTH);
		double maxf = (double) fin.getActualMaximum(Calendar.DAY_OF_MONTH);
		/* Si es el mismo mes, dar la proporcion de dias */
		if (yearf==yeari && monthf==monthi) {
			return MathUtil.round4(((double)(dayf-dayi+1))/maxi);
		}
		/* Los meses son distintos, hay que ajustar el inicio y el fin */
		if (dayi!=1) {
			meses += MathUtil.round4(((double)(maxi-dayi+1))/maxi);
			inicio.add(Calendar.MONTH, 1);
			yeari = inicio.get(Calendar.YEAR);
			monthi = inicio.get(Calendar.MONTH);
		}
		if (yearf==yeari && monthf==monthi) {
			meses += MathUtil.round4(((double)(dayf-1+1))/maxf); 
			return meses;
		}
		if (dayf<maxf) {
			meses += MathUtil.round4(((double)(dayf-1+1))/maxf); 
			fin.add(Calendar.MONTH, -1);
			yearf = fin.get(Calendar.YEAR);
			monthf = fin.get(Calendar.MONTH);
		}
		meses += (double) ( ((yearf-yeari)*(inicio.getMaximum(Calendar.MONTH)+1)) +    
				 (monthf-monthi+1) );
		return meses;
	}  


	/**
	 * Devuelve el numero de meses que hay en un tramo
	 * @param tramo
	 * @return
	 */
	public static double monthsBetween(IDatePeriod tramo) {
		return monthsBetween(tramo.getDesde(),tramo.getHasta());
	}
	
	/**
	 * Devuelve el nuemro de meses en los que el tramos intersecta con la lista
	 * de tramos
	 * @param tramo
	 * @return
	 */
	public static double monthsIntersect(IDatePeriod tramo, IDatePeriod[] lista) {
		double total = 0.0d;
		Calendar i,f;
		for (int j=0;j<lista.length; j++) 
			if (intersect(tramo, lista[j])) {
				i = tramo.getDesde();
				f = tramo.getHasta();
				if (i.compareTo(lista[j].getDesde())<0) i=lista[j].getDesde();
				if (f.compareTo(lista[j].getHasta())>0) f=lista[j].getHasta();
				total += monthsBetween(i,f);
			}
		return total;
	}
	
	/**
	 * devuelve el numero de meses de una lista de periodos
	 * @param listaTramosDesempleo
	 * @return
	 */
	public static double monthsBetween(List<IDatePeriod> lista) {
		double months = 0.0d;
		for (IDatePeriod p : lista) months += monthsBetween(p);
		return months;
	}

	
	/**
	 * Devuelve true si el rango indicado se superpone con alguno de los
	 * rangos de la lista.
	 * @param range el rango 
	 * @param rangeList la lista de rangos
	 * @return
	 */
	public static boolean intersectList(IDatePeriod range, List<IDatePeriod> rangeList) {
		for (IDatePeriod p : rangeList) {
			if (intersect(range, p)) return true;
		}
		return false;
	}

	/**
	 * Devuelve true si los rangos indicados se superponen en el tiempo.
	 * @param range1
	 * @param range2
	 * @return
	 */
	public static boolean intersect(IDatePeriod range1, IDatePeriod range2) {
		Calendar ini = range1.getDesde();
		Calendar fin = range1.getHasta();
		if (ini.compareTo(range2.getDesde())<0) ini = range2.getDesde();
		if (fin.compareTo(range2.getHasta())>0) fin = range2.getHasta();
		return (ini.compareTo(fin)<=0);
	}
	
	/**
	 * Devuelve true si la lista pasada tiene superposición de fechas o false
	 * si no la tiene 
	 */
	public static boolean isListIntersecting(IDatePeriod[] lista) {
		for (int i = 0; i < lista.length; i++) 
			for (int j = i+1; j < lista.length; j++) 
				if (intersect(lista[i],lista[j])) return true;
		return false;
	}
	
	public static boolean isListIntersecting(List<IDatePeriod> lista) {
		return isListIntersecting((IDatePeriod[]) lista.toArray(new IDatePeriod[]{}));
	}

	/**
	 * A esta función se le pasa una lista de periodos y un tramo de periodo y devuelve
	 * los periodos en los que el tramo pasado intersecta con los periodos de la lista 
	 */
	public static List<IDatePeriod> getPeriodosIntersectingLista(IDatePeriod tramo, IDatePeriod[] lista) {
		List<IDatePeriod> tramos = new ArrayList<IDatePeriod>();
		for (IDatePeriod t : lista) {
			Calendar i, f;
			i = tramo.getDesde();
			f = tramo.getHasta();
			if (i.compareTo(t.getDesde())<0) i=t.getDesde();
			if (f.compareTo(t.getHasta())>0) f=t.getHasta();
			if (i.compareTo(f)<=0) {
				Calendar d = (Calendar) i.clone();
				Calendar h = (Calendar) f.clone();
				IDatePeriod idp = new DatePeriod();
				idp.setDesde(d);
				idp.setHasta(h);
				tramos.add(idp);
			}
		}
		return tramos;
	}
	
	
	/**
	 * Recibe una lista de tramos que puede intresecan unos con otros
	 * y devuelve una lista con esos mismos tramos de fecha pero en 
	 * periodos que no interesectan
	 * Siempre se devuelve una lista distinta. La pasada queda intacta
	 */
	public static List<IDatePeriod> getListNotIntersecting(List<IDatePeriod> lista) {
		// Creamos nueva lista a devolver
		List<IDatePeriod> newLista = new ArrayList<IDatePeriod>();
		// Si es lista vacia devolver vacia
		if (lista==null || lista.size()==0) return newLista;
		// Tenemos que ordenar la lista así que hacemos una copia
		List<IDatePeriod> slista = new ArrayList<IDatePeriod>();
		for (int i=0;i<lista.size();i++) slista.add(lista.get(i));
		// Ordenamos la lista
		Collections.sort(slista);
		// Inicializamos
		newLista.add(slista.get(0));
		Calendar lastDate = (Calendar) slista.get(0).getHasta().clone();
		// Recorremos temporalemente la lista creando la nueva
		for (IDatePeriod d : slista) {
			// Obviamos el primero
			if (d==slista.get(0)) continue;
			// Añadimo un día más para no tener periodos consecutivos sin unir
			lastDate.add(Calendar.DAY_OF_MONTH, 1);
			if (lastDate.compareTo(d.getDesde())<0) {
				// El periodo nuevo no intersecta, meterlo sin más
				newLista.add(d);
				lastDate.setTime(d.getHasta().getTime());
			} else {
				// El periodo nuevo intersecta, modificar el periodo de destino
				if (lastDate.compareTo(d.getHasta())<0) 
					lastDate.setTime(d.getHasta().getTime());
				newLista.get(newLista.size()-1).getHasta().setTime(lastDate.getTime());
			}
		}
		return newLista;
	}
	
	/**
	 * Devuelve el objeto Calendar en forma de String con formato dd/MM/yyyy
	 * @param calendar
	 * @return
	 */
	public static String toString(Calendar calendar) {
		return calendar == null ? "" : toString(calendar.getTime());
	}
	
	public static String toMesString(int mes) {
		if (mes<1 || mes>12) return "";
		Calendar c = new GregorianCalendar(2012,mes-1,1);
		return new SimpleDateFormat("MMMMM").format(c.getTime());
	}
	
	/**
	 * Devuelve el objeto Date en forma de String con formato dd/MM/yyyy
	 * @param date
	 * @return
	 */
	public static String toString(Date date) {
		return sdf.format(date);
	}

	/**
	 * Devuelve el objeto Date en forma de String con formato dd/MM/yyyy
	 * @param date
	 * @return
	 */
	public static String toString(IDatePeriod tramo) {
		return "("+toString(tramo.getDesde())+"-"+toString(tramo.getHasta())+")";
	}

	/**
	 * Devuelve el objeto Date en forma de String con formato dd/MM/yyyy
	 * @param date
	 * @return
	 */
	public static String toString(List<IDatePeriod> lista) {
		String r = "";
		for (IDatePeriod t : lista) {
			r += toString(t);
			if (t!=lista.get(lista.size()-1)) r+=", ";
		}
		return r;
	}
	
	 /**
 	  * Devuelve la representación de una fecha en formato "12 de julio de 2012"
	  * @param calendar
	  * 	Si calendar es null devuelve la fecha actual
	  * @return
	  */
	 public static String toLongString(Calendar calendar) {
		return ldf.format(calendar!=null ? calendar.getTime() : new GregorianCalendar().getTime());
	}


	 /**
 	  * Devuelve la fecha correspondiente a la cadena pasada en formato corto
 	  * 	dd/MM/yyyy
	  * @return
	  * 	el objeto Calendar correspondiente a la fecha
	 * @throws ParseException 
	  */
	 public static Calendar fromString(String fecha) throws ParseException {
		 Calendar c = new GregorianCalendar();
		 c.setTime(sdf.parse(fecha));
		 return c;
	}

	 /**
 	  * Devuelve la representación de una fecha en formato "12 de julio de 2012"
	  * @param date
	  * 	Si date es null devuelve la fecha actual
	  * @return
	  */
	public static String toLongString(Date date) {
		return ldf.format(date!=null ? date : new Date());
	}
	
	public static int compareToWithoutHour(Calendar c1, Calendar c2) {
		Calendar o1 = new GregorianCalendar(c1.get(Calendar.YEAR),c1.get(Calendar.MONTH),c1.get(Calendar.DAY_OF_MONTH));
		Calendar o2 = new GregorianCalendar(c2.get(Calendar.YEAR),c2.get(Calendar.MONTH),c2.get(Calendar.DAY_OF_MONTH));
		return o1.compareTo(o2); 
	}


}
