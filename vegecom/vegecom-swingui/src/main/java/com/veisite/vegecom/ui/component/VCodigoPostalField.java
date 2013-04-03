package com.veisite.vegecom.ui.component;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.BadLocationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veisite.vegecom.ui.framework.component.VTextField;
import com.veisite.vegecom.ui.framework.component.util.LimitLengthKeyListener;
import com.veisite.vegecom.ui.framework.component.util.RegexFilterKeyListener;

public class VCodigoPostalField extends VTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory.getLogger(VCodigoPostalField.class);

	public VCodigoPostalField() {
		super();
		initComponent();
	}

	public VCodigoPostalField(String promptText) {
		super(promptText);
		initComponent();
	}

	private void initComponent() {
		/* Añadir un listener de teclado para permitir sólo caracteres 0-9 */
		addKeyListener(new RegexFilterKeyListener("[0-9]"));
		addKeyListener(new LimitLengthKeyListener(this,5));
		// Add validation
		addFocusListener(new ValidationProcess(this));
	}

	public void setValidationState() {
		String s = getText();
		if (s!=null && s.length()<5 && s.length()>0) {
			int np = 5-s.length();
			for (int i=0;i<np;i++) {
				try {
					getDocument().insertString(0, "0", null);
				} catch (BadLocationException e) {
					logger.debug("Error al rellenar de ceros el codigo postal", e);
					break;
				}
			}
		}
	}
	
	private class ValidationProcess implements FocusListener {
		
		public ValidationProcess(VCodigoPostalField field) {
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
