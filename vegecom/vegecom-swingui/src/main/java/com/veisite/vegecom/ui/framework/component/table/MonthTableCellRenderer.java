package com.veisite.vegecom.ui.framework.component.table;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Renderer especifico para implementar el nombre del mes a partir de un 
 * entero (numero de mes), con formato largo (mes completo) o corto (tres letras) 
 * 
 * @author josemaria
 *
 */
public class MonthTableCellRenderer extends DefaultTableCellRenderer {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 1L;
	
	private SimpleDateFormat df;
	
	public MonthTableCellRenderer(boolean longFormat) {
		this.df = new SimpleDateFormat(longFormat ? "MMMMM" : "MMM");
	}
	
	public Component getTableCellRendererComponent(JTable jTable, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = super.getTableCellRendererComponent(jTable, value, isSelected, hasFocus, row, column);
		if (c instanceof JLabel && value instanceof Integer) {
			int m = ((Integer)value).intValue();
			if (m>=1 && m<=12) {
				Calendar cal = new GregorianCalendar(2012, m-1, 1); 
				JLabel label = (JLabel) c;
				String text = df.format(cal.getTime());
				label.setText(text);
			}
		}
		return c;
	}	

}
