package com.veisite.vegecom.ui.framework.views;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Icon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veisite.vegecom.ui.framework.UIFramework;
import com.veisite.vegecom.ui.framework.component.tabbedpane.CloseableTabbedPane;
import com.veisite.vegecom.ui.framework.menu.UIFrameworkMenuObject;

/**
 * Componente grafico que mantiene una serie de vistas en forma
 * de paneles seleccionable (tabbedPane) con la opción de poder
 * cerrarlos.
 * 
 * @author josemaria
 *
 */
public class UIFrameworkViewArea extends CloseableTabbedPane 
						implements UIFrameworkMenuObject, PropertyChangeListener {
	
	/**
	 * serial
	 */
	private static final long serialVersionUID = -2020596505332632511L;

	/**
	 * logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * identificador 
	 * @param id
	 */
	private String id;
	
	/**
	 * priority
	 * @param priority
	 */
	private int priority = UIFramework.DEFAULT_PRIORITY;
	
	/**
	 * Lista de vistas que se muestran 
	 * @param id
	 */
	private List<UIFrameworkView> viewList = new ArrayList<UIFrameworkView>();
	
	
	public UIFrameworkViewArea(String id) {
		super();
		this.id = id;
		addPropertyChangeListener(CloseableTabbedPane.REMOVEDTAB_PROPERTY, this);
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	@Override
	public void setPriority(int priority) {
		this.priority = priority;
	}
	

	/**
	 * Añade una vista al area de vista. 
	 * La seleccina por defecto
	 * 
	 * @param view
	 */
	public void addView(UIFrameworkView view) {
		logger.debug("Adding new view, title {}",view.getTitle());
		if (!viewList.contains(view)) {
			viewList.add(view);
			Collections.sort(viewList, new Comparator<UIFrameworkView>() {
				@Override
				public int compare(UIFrameworkView o1,
						UIFrameworkView o2) {
					return o1.getPriority()-o2.getPriority();
				}
			});
			constructViewArea();
		}
		setSelectedComponent(view);
	}

	/**
	 * Elimina una vista del area
	 * 
	 * @param view
	 */
	public UIFrameworkView removeView(UIFrameworkView view) {
		int i = viewList.indexOf(view);
		logger.debug("Removing view at index "+i);
		if (i>=0) {
			UIFrameworkView v = viewList.remove(i);
			constructViewArea();
			return v;
		} else return null;
	}
	
	/**
	 * Devuelve la vista con el id indicado
	 */
	public UIFrameworkView getView(String id) {
		for (UIFrameworkView v : viewList)
			if (v.getId().equals(id)) return v;
		return null;
	}

	
	private void constructViewArea() {
		removeAll();
		// Vamos añadiendo las areas en orden de prioridad
		for (UIFrameworkView v : viewList) {
			super.addTab(v.getTitle(), v, true);
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(CloseableTabbedPane.REMOVEDTAB_PROPERTY)) {
			// A tab has been removed
			if (evt.getOldValue() instanceof UIFrameworkView) {
				UIFrameworkView v = (UIFrameworkView) evt.getOldValue();
				removeView(v);
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.veisite.vegecom.ui.components.tabbedpane.CloseableTabbedPane#addTab(java.lang.String, java.awt.Component)
	 */
	@Override
	public void addTab(String title, Component component) {
		if (component instanceof UIFrameworkView)
			addView((UIFrameworkView) component);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTabbedPane#addTab(java.lang.String, javax.swing.Icon, java.awt.Component, java.lang.String)
	 */
	@Override
	public void addTab(String title, Icon icon, Component component, String tip) {
		addTab(title, component);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTabbedPane#addTab(java.lang.String, javax.swing.Icon, java.awt.Component)
	 */
	@Override
	public void addTab(String title, Icon icon, Component component) {
		addTab(title, component);
	}
	
}
