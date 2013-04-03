package com.veisite.vegecom.ui.component;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.BadLocationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veisite.vegecom.ui.framework.component.VUpTextField;
import com.veisite.vegecom.ui.framework.component.util.LimitLengthKeyListener;
import com.veisite.vegecom.ui.framework.component.util.RegexFilterKeyListener;


public class VNifField extends VUpTextField {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(VNifField.class);

	public VNifField() {
		super();
		initComponent();
	}

	public VNifField(int columns) {
		super(columns);
		initComponent();
	}

	public VNifField(String promptText, int columns) {
		super(promptText, columns);
		initComponent();
	}

	public VNifField(String promptText) {
		super(promptText);
		initComponent();
	}

	private void initComponent() {
		/* Añadir un listener de teclado para permitir sólo caracteres A-Z y 0-9 */
		addKeyListener(new RegexFilterKeyListener("[0-9|A-Z|a-z]"));
		addKeyListener(new LimitLengthKeyListener(this,9));
		// Add validation
		addFocusListener(new ValidationProcess(this));
	}
	
	public void setValidationState() {
		String s = getText();
		if (s!=null && s.length()<9 && s.length()>0 && 
				"0123456789".contains(s.subSequence(0, 1))) {
			int np = 9-s.length();
			if ("0123456789".contains(s.subSequence(s.length()-1,s.length()))) np--;
			for (int i=0;i<np;i++) {
				try {
					getDocument().insertString(0, "0", null);
				} catch (BadLocationException e) {
					logger.debug("Error left padding 0 in cif field", e);
					break;
				}
			}
		}
		// TODO Incluir validaciones de cif, nif, nie, etc
	}
	
	private class ValidationProcess implements FocusListener {
		
		public ValidationProcess(VNifField field) {
		}

		@Override
		public void focusLost(FocusEvent e) {
			setValidationState();
		}
		
		@Override
		public void focusGained(FocusEvent e) {
			setValidationState();
		}
	}

}
