package com.veisite.vegecom.ui.framework.component.panels;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class DefaultTableStatusBar extends SimpleStatusBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JProgressBar progressBar;
	
	private JLabel progressLabel; 
	
	public DefaultTableStatusBar() {
		super();
		initComponent();
	}
	
	private void initComponent() {
		Dimension minSize = new Dimension(5, 20);
		Dimension prefSize = new Dimension(5, 20);
		Dimension maxSize = new Dimension(Short.MAX_VALUE, 20);
		add(new Box.Filler(minSize, prefSize, maxSize));
		
		progressBar = new JProgressBar();
		Dimension d = progressBar.getPreferredSize();
		progressBar.setPreferredSize(new Dimension(100,d.height));
		progressBar.setMaximumSize(new Dimension(100,d.height));
		progressBar.setAlignmentX(RIGHT_ALIGNMENT);
		progressBar.setStringPainted(false);
		
		progressLabel = new JLabel("");
		progressLabel.setPreferredSize(new Dimension(120,18));
		progressLabel.setMaximumSize(new Dimension(120,18));

		JSeparator s = new JSeparator(SwingConstants.VERTICAL);
		s.setMaximumSize(new Dimension(5,Short.MAX_VALUE));
		s.setMinimumSize(new Dimension(5,0));
		add(s);

		add(Box.createRigidArea(new Dimension(3, 0)));
		add(progressLabel);
		add(Box.createRigidArea(new Dimension(3, 0)));
		add(progressBar);
		add(Box.createRigidArea(new Dimension(1, 0)));
		
	}

	public void setProgressText(String text) {
		progressLabel.setText(text);
	}
	
	public void startProgress() {
		progressBar.setIndeterminate(true);
	}
	
	public void stopProgress() {
		progressBar.setIndeterminate(false);
		progressBar.setValue(0);
	}
	
}
