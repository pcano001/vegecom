package com.veisite.vegecom.ui.framework.component.table;

import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

public class RightAlignTableCellRenderer extends DefaultTableCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 815847133894698532L;

	public RightAlignTableCellRenderer() {
		setHorizontalAlignment(JLabel.RIGHT);
	}
}
