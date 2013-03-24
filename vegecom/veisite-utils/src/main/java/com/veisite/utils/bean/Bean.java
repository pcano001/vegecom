package com.veisite.utils.bean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Bean {

	protected transient final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(propertyName,listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(propertyName, listener);
	}

}
