package com.veisite.vegecom.ui.framework.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.veisite.vegecom.ui.framework.UIFramework;

public class UIFrameworkMenuItemGroup implements Serializable, UIFrameworkMenuObject {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -4291179495147569757L;
	
	/**
	 * Constantes
	 */
	public static final String DEFAULT_GROUP="default";
	
	/**
	 * Prioridad de visualización del grupo dentro del menu.
	 */
	private int priority;
	
	/**
	 * Identificador 
	 */
	private String id;
	
	/**
	 * Lista de objetos que tiene el grupo
	 */
	private List<UIFrameworkMenuObject> elements = new ArrayList<UIFrameworkMenuObject>();
	
	
	public UIFrameworkMenuItemGroup() {
		this(DEFAULT_GROUP, UIFramework.DEFAULT_PRIORITY);
	}
	
	public UIFrameworkMenuItemGroup(String id) {
		this(id, UIFramework.DEFAULT_PRIORITY);
	}
	
	public UIFrameworkMenuItemGroup(String id, int priority) {
		this.id = id;
		setPriority(priority);
	}
	
	
	public String getId() {
		return id;
	}


	public int getPriority() {
		return priority;
	}


	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	/**
	 * @return the elements
	 */
	public List<UIFrameworkMenuObject> getElements() {
		return elements;
	}

	/**
	 * Añade un elemento al grupo. Si ya existe un elementos con el
	 * mismo identificador no lo añade
	 * 
	 * @param e
	 * @return
	 */
	public UIFrameworkMenuObject add(UIFrameworkMenuObject e) {
		return addElement(e);
	}
	
	/**
	 * Elimina un elemento del grupo. Si no existe devuelve null,
	 * si existe devuelve el objeto eliminado
	 * 
	 * @param e
	 * @return
	 */
	public UIFrameworkMenuObject remove(UIFrameworkMenuObject e) {
		return remove(e.getId());
	}
	
	/**
	 * Elimina un elemento del grupo. Si no existe devuelve null,
	 * si existe devuelve el objeto eliminado
	 * 
	 * @param e
	 * @return
	 */
	public UIFrameworkMenuObject remove(String id) {
		UIFrameworkMenuObject o = getElement(id);
		if (o!=null) elements.remove(o);
		return o;
	}

	/**
	 * Añade un elemento al grupo. Si ya existe un elementos con el
	 * mismo identificador no lo añade. 
	 * Si lo añade ordena la lista
	 * 
	 * @param e
	 * @return
	 */
	private UIFrameworkMenuObject addElement(UIFrameworkMenuObject e) {
		UIFrameworkMenuObject o = getElement(e.getId());
		if (o!=null) return o;
		// Nuevo elemento, ordenar por prioridad
		elements.add(e);
		Collections.sort(elements, new Comparator<UIFrameworkMenuObject>() {
			@Override
			public int compare(UIFrameworkMenuObject o1,
					UIFrameworkMenuObject o2) {
				return o1.getPriority()-o2.getPriority();
			}
		});
		return e;
	}
	
	/**
	 * Devuelve el objeto identificado por el id o null si no está 
	 */
	public UIFrameworkMenuObject getElement(String id) {
		for (UIFrameworkMenuObject o : elements)
			if (o.getId().equals(id)) return o;
		return null;
	}
	

}
