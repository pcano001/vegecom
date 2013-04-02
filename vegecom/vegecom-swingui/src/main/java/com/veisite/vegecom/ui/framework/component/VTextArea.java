package com.veisite.vegecom.ui.framework.component;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import com.veisite.utils.binding.BindTarget;
import com.veisite.utils.binding.IBindableTo;

public class VTextArea extends JTextArea implements IActivableComponent, 
													IBindableTo<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * binding text to a String property on this target
	 */
	List<BindTarget<String>> bindList = new ArrayList<BindTarget<String>>();
	
	
	public VTextArea() {
		super();
		initComponent();
	}

	public VTextArea(Document doc, String text, int rows, int columns) {
		super(doc, text, rows, columns);
		initComponent();
	}

	public VTextArea(int rows, int columns) {
		super(rows, columns);
		initComponent();
	}

	public VTextArea(String text, int rows, int columns) {
		super(text, rows, columns);
		initComponent();
	}

	public VTextArea(String text) {
		super(text);
		initComponent();
	}

	
	private void initComponent() {
		getDocument().addDocumentListener(new BindingDocumentListener());
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

}
