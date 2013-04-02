package com.veisite.vegecom.ui.framework.component.table;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.veisite.utils.text.MoneyFormat;

public class MoneyTableCellRenderer extends DefaultTableCellRenderer {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 1L;
	
	public Component getTableCellRendererComponent(JTable jTable, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		Component c = super.getTableCellRendererComponent(jTable, value, isSelected, hasFocus, row, column);
		if (c instanceof JLabel && value instanceof Number) {
			JLabel label = (JLabel) c;
			label.setHorizontalAlignment(JLabel.RIGHT);
			Number num = (Number) value;
			String text = MoneyFormat.formatMoney(num.doubleValue());
			label.setText(text);
		}
		return c;
	}	

}
