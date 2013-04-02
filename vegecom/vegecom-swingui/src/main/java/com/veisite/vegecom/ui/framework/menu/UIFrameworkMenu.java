package com.veisite.vegecom.ui.framework.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.Action;
import javax.swing.JMenu;

import com.veisite.vegecom.model.exception.VegecomException;
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
public class UIFrameworkMenu extends JMenu implements UIFrameworkMenuObject {
	
	/**
	 * serial
	 */
	private static final long serialVersionUID = 8054128119941151990L;

	/**
	 * Identificador 
	 */
	private String id;
	
	/**
	 * Prioridad de visualización dentro de su contenedor
	 */
	private int priority;
	
	/**
	 * Lista de grupos que contiene el menu
	 * @param id
	 * @param action
	 */
	List<UIFrameworkMenuItemGroup> groups = new ArrayList<UIFrameworkMenuItemGroup>();
	
	public UIFrameworkMenu(String id, String name) {
		this(id, name, UIFramework.DEFAULT_PRIORITY);
	}
	
	public UIFrameworkMenu(String id, String name, int priority) {
		super(name);
		this.id = id;
		setPriority(priority);
		initMenu();
	}
	
	public UIFrameworkMenu(String id, Action action) {
		this(id, action, UIFramework.DEFAULT_PRIORITY);
	}
	
	public UIFrameworkMenu(String id, Action action, int priority) {
		super(action);
		this.id = id;
		setPriority(priority);
		initMenu();
	}
	
	protected void initMenu() {
		// Creamos el grupo por defecto
		groups.add(new UIFrameworkMenuItemGroup());
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
	 * Añadimos un nuevo elementos de menu en el grupo
	 * por defecto 
	 * @throws VegecomException 
	 */
	public UIFrameworkMenuItem add(UIFrameworkMenuItem menuItem) {
		return add(UIFrameworkMenuItemGroup.DEFAULT_GROUP, menuItem);
	}

	
	/**
	 * Añadimos un nuevo elementos de menu en el grupo
	 * indicado
	 * @throws VegecomException 
	 */
	public UIFrameworkMenuItem add(String groupId, UIFrameworkMenuItem menuItem) {
		UIFrameworkMenuItemGroup g = getGroup(groupId);
		if (g==null) g = addGroup(groupId);
		g.add(menuItem);
		constructMenu();
		return menuItem;
	}
	
	/**
	 * Añadimos un nuevo elementos de menu en el grupo
	 * por defecto 
	 * @throws VegecomException 
	 */
	public UIFrameworkMenu add(UIFrameworkMenu menuItem) {
		return add(UIFrameworkMenuItemGroup.DEFAULT_GROUP, menuItem);
	}
	
	/**
	 * Añadimos un nuevo elementos de menu en el grupo
	 * indicado
	 * @throws VegecomException 
	 */
	public UIFrameworkMenu add(String groupId, UIFrameworkMenu menuItem) {
		UIFrameworkMenuItemGroup g = getGroup(groupId);
		if (g==null) g = addGroup(groupId);
		g.add(menuItem);
		constructMenu();
		return menuItem;
	}
	

	public UIFrameworkMenuObject add(String groupId, UIFrameworkMenuObject menuItem) {
		if (menuItem instanceof UIFrameworkMenu)
			return add(groupId, (UIFrameworkMenu) menuItem);
		if (menuItem instanceof UIFrameworkMenuItem)
			return add(groupId, (UIFrameworkMenuItem) menuItem);
		return null;
	}
	
	/**
	 * Elimina un item de un menu del grupo por defecto
	 * @throws VegecomException 
	 */
	public UIFrameworkMenuObject remove(String id) {
		return remove(UIFrameworkMenuItemGroup.DEFAULT_GROUP, id);
	}
	
	/**
	 * Elimina un item de un menu del grupo indicado
	 * @throws VegecomException 
	 */
	public UIFrameworkMenuObject remove(String groupId, String id) {
		UIFrameworkMenuItemGroup g = getGroup(groupId);
		if (g==null)
			throw new IllegalArgumentException("add: Menu group '"+groupId+"' not found.");
		UIFrameworkMenuObject o = g.remove(id);
		constructMenu();
		return o;
	}
	
	
	/**
	 * Añade un nuevo grupo de menus vacio al menu con prioridad por defecto 
	 */
	public UIFrameworkMenuItemGroup addGroup(String id) {
		return addGroup(id, UIFramework.DEFAULT_PRIORITY);
	}

	/**
	 * Añade un nuevo grupo de menus vacio al menu con prioridad indicada
	 */
	public UIFrameworkMenuItemGroup addGroup(String id, int priority) {
		UIFrameworkMenuItemGroup g = new UIFrameworkMenuItemGroup(id, priority);
		return addGroup(g);
	}

	/**
	 * Añade un nuevo grupo de menus al menu. 
	 * Supone la reordenacion de todos los menus en función de 
	 * las nuevas prioridades. 
	 * Si hay algun error devuelve null, en otro caso el grupo añadido
	 * 
	 * Si ya existe un grupo son el mismo id lanza una excepcion.
	 */
	private UIFrameworkMenuItemGroup addGroup(UIFrameworkMenuItemGroup group) {
		UIFrameworkMenuItemGroup g = getGroup(group.getId());
		if (g!=null) return g;
		groups.add(group);
		Collections.sort(groups, new Comparator<UIFrameworkMenuItemGroup>() {
			@Override
			public int compare(UIFrameworkMenuItemGroup o1,
					UIFrameworkMenuItemGroup o2) {
				return o1.getPriority()-o2.getPriority();
			}
		});
		constructMenu();
		return group;
	}

	
	public UIFrameworkMenuItemGroup getGroup(String id) {
		for (UIFrameworkMenuItemGroup g : groups)
			if (g.getId().equals(id)) return g;
		return null;
	}
	
	/**
	 * Metodo que monta el menu con todas sus opciones.
	 * Primero elimina el contenido y vuelve a montarlo.
	 */
	private void constructMenu() {
		removeAll();
		// Empezamos a meter items por orden de grupos
		for (UIFrameworkMenuItemGroup g : groups) {
			if (getItemCount()>0 && g.getElements().size()>0) addSeparator();
			for (UIFrameworkMenuObject o : g.getElements()) {
				if (o instanceof UIFrameworkMenu) {
					UIFrameworkMenu m = (UIFrameworkMenu) o;
					super.add(m);
				}
				if (o instanceof UIFrameworkMenuItem) {
					UIFrameworkMenuItem mi = (UIFrameworkMenuItem) o;
					super.add(mi);
				}
			}
		}
		super.invalidate();
		super.repaint();
	}

	/**
	 * Devuelve el objeto de tipo menu soicitado en el grupo que se indica 
	 * 
	 * @param groupId
	 * @param menuId
	 * @return
	 * 		null si no se encuentra el grupo o el menu.
	 */
	public UIFrameworkMenu getMenu(String groupId, String menuId) {
		UIFrameworkMenuItemGroup g = getGroup(groupId);
		if (g==null) return null;
		UIFrameworkMenuObject o = g.getElement(menuId);
		if (o instanceof UIFrameworkMenu)
			return (UIFrameworkMenu) o;
		return null;
	}

}
