package com.veisite.vegecom.ui.framework.component.table;

import java.awt.Component;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DecimalTableCellRenderer extends DefaultTableCellRenderer {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Formato para numero
	 * @param decimals
	 */
	private DecimalFormat formato = new DecimalFormat();
	
	public DecimalTableCellRenderer(int decimals) {
		formato.setMaximumFractionDigits(decimals);
		formato.setMinimumFractionDigits(decimals);
		formato.setRoundingMode(RoundingMode.HALF_UP);
	}
	
	public Component getTableCellRendererComponent(JTable jTable, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = super.getTableCellRendererComponent(jTable, value, isSelected, hasFocus, row, column);
		if (c instanceof JLabel && value instanceof Number) {
			JLabel label = (JLabel) c;
			label.setHorizontalAlignment(JLabel.RIGHT);
			Number num = (Number) value;
			String text = formato.format(num.doubleValue());
			label.setText(text);
		}
		return c;
	}	

}
