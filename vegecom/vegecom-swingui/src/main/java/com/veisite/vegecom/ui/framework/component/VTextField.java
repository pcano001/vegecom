package com.veisite.vegecom.ui.framework.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.jdesktop.swingx.JXTextField;
import org.jdesktop.swingx.prompt.BuddySupport;

import com.veisite.utils.binding.BindTarget;
import com.veisite.utils.binding.IBindableTo;
import com.veisite.vegecom.ui.framework.component.util.IValidatableComponent;

public class VTextField extends JXTextField implements IActivableComponent, IValidatableComponent,  
														IBindableTo<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * binding text to a String property on this target
	 */
	List<BindTarget<String>> bindList = new ArrayList<BindTarget<String>>();
	
	/**
	 * Implementación de las validaciones
	 */
	private Validator validator;
	/**
	 * Objeto a validar
	 */
	private Object validatableObject;
	/**
	 * Propiedad a validar
	 */
	private String validatableProperty;
	/**
	 * flag de activacion de validaciones
	 */
	private boolean validationEnabled=false;
	/**
	 * Componente para mostrar errores de validacion
	 */
	private JLabel validationBuddy;
	

	
	public VTextField() {
		super();
		initComponent();
	}

	public VTextField(String promptText, Color promptForeground,
			Color promptBackground) {
		super(promptText, promptForeground, promptBackground);
		initComponent();
	}

	public VTextField(String promptText, Color promptForeground) {
		super(promptText, promptForeground);
		initComponent();
	}

	public VTextField(String promptText) {
		super(promptText);
		initComponent();
	}


	private void initComponent() {
		getDocument().addDocumentListener(new BindingDocumentListener());
		addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                int key = evt.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    transferFocus();
                    evt.consume();
                }
            }
        });
		String il = "images/emblem-important-316.png";
		ImageIcon im = new ImageIcon(getClass().getResource(il));
		if (im!=null) validationBuddy = new JLabel(im);
	}

	
	/**
	 * Simulate a property to enable/disable component
	 */
	@Override
	public Boolean getActivado() {
		return new Boolean(super.isEnabled());
	}

	@Override
	public void setActivado(Boolean newActivado) {
		Boolean oldValue = getActivado();
		boolean b = (newActivado!=null && newActivado.booleanValue());
		super.setEnabled(b);
		firePropertyChange("activado", oldValue, getActivado());
	}
	
	@Override
	public void addBindTo(BindTarget<String> target) {
		if (target==null) return;
		bindList.add(target);
	}

	public void removeBindTo(BindTarget<String> target) {
		if (target==null) return;
		bindList.remove(target);
	}

	/**
	 * Ha cambiado el texto. 
	 * Sincronizar los binding
	 */
	private void documentChanged() {
		for (BindTarget<String> b : bindList) 
			b.setValue(getText());
		doValidation();
	}
	
	private class BindingDocumentListener implements DocumentListener {
		@Override
		public void removeUpdate(DocumentEvent arg0) {
			documentChanged();
		}
		@Override
		public void insertUpdate(DocumentEvent arg0) {
			documentChanged();
		}
		@Override
		public void changedUpdate(DocumentEvent arg0) {
			documentChanged();
		}
	}
	
	@Override
	public void configureValidation(Validator validator, Object target,
			String property) {
		this.validator = validator;
		this.validatableObject = target;
		this.validatableProperty = property;
		enableValidation(true);
		doValidation();
	}

	@Override
	public void enableValidation(boolean validationEnabled) {
		this.validationEnabled=validationEnabled;
	}

	@Override
	public boolean isValidationEnabled() {
		return validationEnabled;
	}
	
	/**
	 * Metodo que realiza las validaciones
	 */
	private void doValidation() {
		if (!isValidationEnabled()) return;
		if (validator==null || validatableObject==null || validatableProperty==null) return;
		Set<ConstraintViolation<Object>> cv =
			validator.validateProperty(validatableObject,validatableProperty);
		if (cv.size()==0) {
			setToolTipText(getPrompt());
			removeAllBuddies();
		} else {
			Iterator<ConstraintViolation<Object>> it = cv.iterator();
			ConstraintViolation<Object> o = it.next();
			setToolTipText(o.getMessage());
			List<Component> lb = getBuddies(BuddySupport.Position.RIGHT);
			if (lb.contains(validationBuddy)) return;
			else {
				addBuddy(validationBuddy, BuddySupport.Position.RIGHT);
			}
		}
	}

}
