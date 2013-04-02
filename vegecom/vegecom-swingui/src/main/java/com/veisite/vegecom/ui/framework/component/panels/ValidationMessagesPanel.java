package com.veisite.vegecom.ui.framework.component.panels;

import java.util.Iterator;
import java.util.Set;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.validation.ConstraintViolation;

import com.veisite.vegecom.ui.framework.component.VMultiLineJLabel;

/**
 * Construye un panel con los mensajes de validación devueltos
 * por un objeto validator.
 * Se puede usar para mostrar los errores de validación de un
 * objeto. 
 * 
 * @author josemaria
 *
 */
public class ValidationMessagesPanel<T> extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6413655060371877069L;

	public ValidationMessagesPanel(String message, Set<ConstraintViolation<T>> validations) {
		super();
		initPanel(message, validations);
	}
	
	private void initPanel(String message, Set<ConstraintViolation<T>> validations) {
		BoxLayout bl = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		setLayout(bl);
		
		VMultiLineJLabel m = new VMultiLineJLabel(message);
		m.setColumns(50);
		add(m);
		add(Box.createVerticalStrut(10));
		
		Iterator<ConstraintViolation<T>> it = validations.iterator();
		while (it.hasNext()) {
			ConstraintViolation<T> cv = it.next();
			VMultiLineJLabel m1 = new VMultiLineJLabel(cv.getMessage());
			add(m1);
		}
	}

}
