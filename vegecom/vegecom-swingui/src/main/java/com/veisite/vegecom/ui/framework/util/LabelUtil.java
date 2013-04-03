package com.veisite.vegecom.ui.framework.util;

import java.awt.Font;

import javax.swing.JLabel;

public class LabelUtil {
	
	public static void bold(JLabel label) {
		label.setFont(new Font(label.getFont().getName(),
				Font.BOLD,label.getFont().getSize()));
	}

}
