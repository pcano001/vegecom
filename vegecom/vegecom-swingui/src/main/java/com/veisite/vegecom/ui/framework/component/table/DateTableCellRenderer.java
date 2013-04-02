package com.veisite.vegecom.ui.framework.component.table;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DateTableCellRenderer extends DefaultTableCellRenderer {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 1L;
	
	private SimpleDateFormat df;
	
	public DateTableCellRenderer(String simpleDateFormat) {
		df = new SimpleDateFormat(simpleDateFormat);
	}
	
	public Component getTableCellRendererComponent(JTable jTable, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = super.getTableCellRendererComponent(jTable, value, isSelected, hasFocus, row, column);
		if (c instanceof JLabel && (value instanceof Calendar || value instanceof Date)) {
			JLabel label = (JLabel) c;
			Date d;
			if (value instanceof Calendar) d = ((Calendar) value).getTime();
			else d = (Date) value;
			String text = df.format(d);
			label.setText(text);
		}
		return c;
	}	

}
