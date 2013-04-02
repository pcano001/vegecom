package com.veisite.vegecom.ui.framework.component;

import javax.swing.text.AbstractDocument;

import com.veisite.vegecom.ui.framework.component.util.UppercaseDocumentFilter;

public class VUpTextField extends VTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public VUpTextField() {
		initComponent();
	}

	public VUpTextField(String promptText) {
		super(promptText);
		initComponent();
	}

	public VUpTextField(int columns) {
		super();
		setColumns(columns);
		initComponent();
	}

	public VUpTextField(String promptText, int columns) {
		super(promptText);
		setColumns(columns);
		initComponent();
	}

	private void initComponent() {
		((AbstractDocument) this.getDocument()).setDocumentFilter(new UppercaseDocumentFilter());
	}
    
}
