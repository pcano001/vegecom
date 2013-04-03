package com.veisite.vegecom.ui.component;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import com.veisite.vegecom.ui.framework.component.VTextField;
import com.veisite.vegecom.ui.framework.component.util.LimitLengthKeyListener;
import com.veisite.vegecom.ui.framework.component.util.RegexFilterKeyListener;

public class VCuentaBancariaField extends VTextField {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 1L;
	
	public VCuentaBancariaField() {
		super();
		initComponent();
	}

	public VCuentaBancariaField(String promptText) {
		super(promptText);
		initComponent();
	}

	private void initComponent() {
		/* Añadir un listener de teclado para permitir sólo caracteres 0-9 */
		addKeyListener(new RegexFilterKeyListener("[0-9]"));
		addKeyListener(new LimitLengthKeyListener(this,20));
		// Add validation
		addFocusListener(new ValidationProcess(this));
	}

	public void setValidationState() {
	}
	
	private class ValidationProcess implements FocusListener {
		public ValidationProcess(VCuentaBancariaField field) {
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
