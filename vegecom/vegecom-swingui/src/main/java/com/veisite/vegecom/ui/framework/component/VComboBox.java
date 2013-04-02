package com.veisite.vegecom.ui.framework.component;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import com.veisite.utils.binding.BindTarget;
import com.veisite.utils.binding.IBindableTo;

public class VComboBox<T> extends JComboBox<T> implements IActivableComponent, 
													IBindableTo<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * binding selectedItem to an Object property on this target
	 */
	List<BindTarget<T>> bindList = new ArrayList<BindTarget<T>>();
	
	
	public VComboBox() {
		initComponent();
	}

	public VComboBox(ComboBoxModel<T> aModel) {
		super(aModel);
		initComponent();
	}

	public VComboBox(T[] items) {
		super(items);
		initComponent();
	}

	public VComboBox(Vector<T> items) {
		super(items);
		initComponent();
	}

	private void initComponent() {
		addItemListener(new ItemChangeListener());
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
	public void addBindTo(BindTarget<T> target) {
		if (target==null) return;
		bindList.add(target);
	}

	@Override
	public void removeBindTo(BindTarget<T> target) {
		if (target==null) return;
		bindList.remove(target);
	}

	
	private void itemChanged() {
		for (BindTarget<T> b : bindList) {
			@SuppressWarnings("unchecked")
			T value = (T) getSelectedItem();
			b.setValue(value);
		}
	}
	
	
	/**
	 * Manage changes on selectedItem
	 * @author josemaria
	 *
	 */
	private class ItemChangeListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			itemChanged();
		}
	}
	
}
