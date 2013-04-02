package com.veisite.vegecom.ui.framework.component.util;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

public class LimitLengthKeyListener extends KeyAdapter {

	private JTextField field;
	
	private int maxLength;
	
	public LimitLengthKeyListener(JTextField field, int maxLength) {
		this.field = field;
		this.maxLength = maxLength;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		String s = field.getSelectedText();
		if (field.getText().length()>=this.maxLength && (s==null || s.length()<=0)) e.consume();
		super.keyTyped(e);
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		super.keyPressed(e);
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		super.keyReleased(e);
	}

	/**
	 * @return the field
	 */
	public JTextField getField() {
		return field;
	}

	/**
	 * @return the maxLength
	 */
	public int getMaxLength() {
		return maxLength;
	}

}
