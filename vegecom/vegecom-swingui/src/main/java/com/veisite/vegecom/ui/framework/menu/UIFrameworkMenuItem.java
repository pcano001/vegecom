package com.veisite.vegecom.ui.framework.menu;

import javax.swing.Action;
import javax.swing.JMenuItem;

import com.veisite.vegecom.ui.framework.UIFramework;

/**
 * Implementa un menu que forma parte de la barra de menu del 
 * framework grafico. Este menu tiene a su vez una serie de 
 * opciones de menu. 
 * 
 * Los items del menu se clasifican en grupos de elementos que
 * son separados por un separador. 
 * 
 * Un itemd menu que no tenga grupo se incluye en el grupo por
 * defecto.
 * 
 * @author josemaria
 *
 */
public class UIFrameworkMenuItem extends JMenuItem implements UIFrameworkMenuObject {
	
	/**
	 * serial
	 */
	private static final long serialVersionUID = -8313047387730673883L;

	/**
	 * Prioridad de visualización del item dentro del menu o grupo.
	 */
	private int priority;
	
	/**
	 * Identificador de menu
	 */
	private String id;
	
	
	public UIFrameworkMenuItem(String id, Action action) {
		this(id, action, UIFramework.DEFAULT_PRIORITY);
	}

	public UIFrameworkMenuItem(String id, Action action, int priority) {
		super(action);
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

}
