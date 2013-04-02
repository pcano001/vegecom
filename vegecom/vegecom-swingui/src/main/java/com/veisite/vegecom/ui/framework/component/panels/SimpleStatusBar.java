package com.veisite.vegecom.ui.framework.component.panels;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

public class SimpleStatusBar extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	JLabel statusLabel;
	
	public SimpleStatusBar() {
		super();
		setBorder(new BevelBorder(BevelBorder.LOWERED));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		statusLabel = new JLabel("");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		add(Box.createRigidArea(new Dimension(3, 0)));
		add(statusLabel);
	}
	
	public void clear() {
		statusLabel.setText("");
	}
	
	public void setText(String text) {
		statusLabel.setText(text);
	}
}
