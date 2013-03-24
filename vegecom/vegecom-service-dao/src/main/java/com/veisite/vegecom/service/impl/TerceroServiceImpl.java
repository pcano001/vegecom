package com.veisite.vegecom.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.veisite.vegecom.model.TerceroComercial;
import com.veisite.vegecom.service.DataChangeListener;
import com.veisite.vegecom.service.TerceroService;

public abstract class TerceroServiceImpl<T extends TerceroComercial> 
			implements TerceroService<T> {
	
	private List<DataChangeListener<T>> _listeners = new ArrayList<DataChangeListener<T>>();

	/**
	 * add listener
	 */
	@Override
	public void addDataChangeListener(DataChangeListener<T> listener) {
		_listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see com.veisite.vegecom.service.TerceroService#removeDataChangeListener(com.veisite.vegecom.service.DataChangeListener)
	 */
	@Override
	public void removeDataChangeListener(DataChangeListener<T> listener) {
		_listeners.remove(listener);
	}
	

	/**
	 * Fire new Item event
	 */
	protected void fireItemAddedEvent(T item) {
		Iterator<DataChangeListener<T>> i = _listeners.iterator();
		while (i.hasNext()) {
			((DataChangeListener<T>) i.next()).itemAdded(item);
		}
	}
	
	/**
	 * Fire modified Item event
	 */
	protected void fireItemChangedEvent(T item) {
		Iterator<DataChangeListener<T>> i = _listeners.iterator();
		while (i.hasNext()) {
			((DataChangeListener<T>) i.next()).itemChanged(item);
		}
	}
	
	/**
	 * Fire removed Item event
	 */
	protected void fireItemRemovedEvent(T item) {
		Iterator<DataChangeListener<T>> i = _listeners.iterator();
		while (i.hasNext()) {
			((DataChangeListener<T>) i.next()).itemRemoved(item);
		}
	}
	

}
