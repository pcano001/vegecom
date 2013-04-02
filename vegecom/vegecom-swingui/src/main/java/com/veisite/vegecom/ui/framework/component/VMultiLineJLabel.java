package com.veisite.vegecom.ui.framework.component;

import javax.swing.JTextArea;
import javax.swing.UIManager;

public class VMultiLineJLabel extends JTextArea {

	private static final long serialVersionUID = 1L;

	public VMultiLineJLabel(String text) {
		super(text);
		setEditable(false);
		setCursor(null);
		setOpaque(false);
		setFocusable(false);
		setFont(UIManager.getFont("Label.font"));
		setWrapStyleWord(true);
		setLineWrap(true);
	}
	
}
