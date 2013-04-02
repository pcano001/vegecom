package com.veisite.vegecom.ui.framework.menu;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JMenuBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veisite.vegecom.ui.framework.UIFramework;


public class UIFrameworkMenuBar extends JMenuBar implements UIFrameworkMenuObject {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -2571354235060606706L;
	
	/**
	 * logger
	 */
	private Logger logger = LoggerFactory.getLogger(getClass()); 
	
	/**
	 * String property to notify changed to menuBar
	 */
	public static final String CHANGED_PROPERTY = "menuBarChanged";
	
	/**
	 * Cadena de texto con el identificador 
	 */
	private String id;
	
	/**
	 * Prioridad 
	 */
	private int priority;
	
	/**
	 * Lista de menus que componen la barra de menu 
	 */
	private List<UIFrameworkMenu> menuList = new ArrayList<UIFrameworkMenu>();
	
	
	public UIFrameworkMenuBar(String id) {
		this(id, UIFramework.DEFAULT_PRIORITY);
	}
	
	public UIFrameworkMenuBar(String id, int priority) {
		super();
		this.id = id;
		setPriority(priority);
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
	
	public UIFrameworkMenu add(String id, String name) {
		return add(id, name, UIFramework.DEFAULT_PRIORITY);
	}
	
	public UIFrameworkMenu add(String id, String name, int priority) {
		return add(new UIFrameworkMenu(id, name, priority));
	}
	
	public UIFrameworkMenu add(UIFrameworkMenu menu) {
		UIFrameworkMenu o = getMenu(menu.getId());
		if (o!=null) return o;
		menuList.add(menu);
		Collections.sort(menuList, new Comparator<UIFrameworkMenu>() {
			@Override
			public int compare(UIFrameworkMenu o1, UIFrameworkMenu o2) {
				return o1.getPriority()-o2.getPriority();
			}
		});
		constructMenuBar();
		return menu;
	}
	
	public UIFrameworkMenu remove(String id) {
		UIFrameworkMenu o = getMenu(id);
		if (o!=null) {
			menuList.remove(o);
			constructMenuBar();
		}
		return o;
	}
	
	public UIFrameworkMenu getMenu(String id) {
		for (UIFrameworkMenu m : menuList)
			if (m.getId().equals(id)) return m;
		return null;
	}
	
	
	private void constructMenuBar() {
		super.removeAll();
		for (UIFrameworkMenu m : menuList) super.add(m);
		super.invalidate();
		super.repaint();
	}
	
	
	
	/**
	 * Añade un nuevo menu a la barra de menu
	 * 
	 * Recibe el nombre del menu que se quiere añadir
	 */
	public UIFrameworkMenu addMenu(String id, String name) {
		UIFrameworkMenu menu = add(id, name);
		fireMenuBarChange();
		return menu;
	}
	
	/**
	 * Elimina un menu de la barra de menu
	 * 
	 * Recibe el identificador del menu a eliminar
	 */
	public UIFrameworkMenu removeMenu(String id) {
		UIFrameworkMenu menu = remove(id);
		if (menu!=null) fireMenuBarChange();
		return menu;
	}
	
	/**
	 * Añade una opción de menu a la barra de menu en el grupo por defecto
	 * 
	 * @param menuPath
	 * @param groupId
	 * @param newMenuElement
	 * @return
	 */
	public UIFrameworkMenuObject addMenuItem(String[] menuPath, UIFrameworkMenuObject newMenuElement) {
		return addMenuItem(menuPath, UIFrameworkMenuItemGroup.DEFAULT_GROUP, newMenuElement);
	}

	/**
	 * Añade una opción de menu a la barra de menu
	 * 
	 * Recibe un array de identificadores de menus jerarquico en el que 
	 * se quiere incluir el nuevo item o nuevo menu
	 * 
	 *  El formato de cada elemento de menu es 
	 *  	"nombreMenu"
	 *  	"nombreGrupo[nombreMenu]
	 *  
	 *  El primer elemento siempre debe ser 
	 *  	"nombreMenu"
	 * 
	 * menu[grupo]
	 */
	public UIFrameworkMenuObject addMenuItem(String[] menuPath, String groupId, UIFrameworkMenuObject newMenuElement) {
		UIFrameworkMenuObject o = getMenuFromPath(menuPath);
		if (o==null || !(o instanceof UIFrameworkMenu))
			throw new IllegalArgumentException("Menu on path "+menuPath+" not found");
		UIFrameworkMenu menu = (UIFrameworkMenu) o; 
		o = menu.add(groupId, newMenuElement);
		if (o!=null) fireMenuBarChange();
		return o;
	}
	
	/**
	 * Elimina una opción de menu de la barra de menu
	 * 
	 * y en el grupo por defecto
	 * 
	 */
	public UIFrameworkMenuObject removeMenuItem(String[] menuPath, String id) {
		return removeMenuItem(menuPath, UIFrameworkMenuItemGroup.DEFAULT_GROUP, id);
	}

	/**
	 * Elimina una opción de menu de la barra de menu
	 * 
	 * Recibe un array de identificadores de menus jerarquico en el que 
	 * buscar el elemento
	 * 
	 *  El formato de cada elemento de menu es 
	 *  	"nombreMenu"
	 *  	"nombreGrupo[nombreMenu]
	 *  
	 *  El primer elemento siempre debe ser 
	 *  	"nombreMenu"
	 * 
	 * menu[grupo]
	 */
	public UIFrameworkMenuObject removeMenuItem(String[] menuPath, String groupId, String id) {
		UIFrameworkMenuObject o = getMenuFromPath(menuPath);
		if (o==null || !(o instanceof UIFrameworkMenu))
			throw new IllegalArgumentException("Menu on path "+menuPath+" not found");
		UIFrameworkMenu menu = (UIFrameworkMenu) o; 
		o = menu.remove(groupId, id);
		if (o!=null) fireMenuBarChange();
		return o;
	}
	
	/**
	 * Return the menu object locate at this path.
	 *
	 *  Return null is no item found at this path
	 * @param path
	 * @return
	 */
	private UIFrameworkMenuObject getMenuFromPath(String[] path) {
		if (path==null || path.length==0)
			throw new IllegalArgumentException("Path for menu must be not null nor empty.");
		String m = "";
		int i = 0;
		String[] mid = {"",""};
		try {
			mid = parseMenuEntry(path[i]);
		} catch (ParseException pe) {}
		UIFrameworkMenu menu = getMenu(mid[1]);
		m += "'"+path[i]+"'";
		while (menu!=null && i<path.length-1) {
			mid[0] = ""; mid[1] = "";
			try {
				mid = parseMenuEntry(path[++i]);
			} catch (ParseException pe) {}
			menu = menu.getMenu(mid[0],mid[1]);
			m += "|'"+path[i]+"'";
		};
		if (menu==null)
			logger.warn("Path menu "+m+" not found");
		return menu;
	}
	
	
	/**
	 * Devuelve dos cadenas de texto de una cadena con los posibles formato
	 * 	"nombreMenu"
	 *  "nombreGrupoMenu[nombreMenu]
	 * 
	 * @return
	 */
	private String[] parseMenuEntry(String menuText) throws ParseException {
		String[] ret = {"", ""};
		// Veamos si hay corchetes, si los hay 
		int c1 = menuText.indexOf('[');
		int c2 = menuText.indexOf(']');
		if (c2<c1 || c2!=c1 && c1<0)
			throw new ParseException("Format is not correct. Expected 'string[string]'", c2);
		if (c1<0) {
			// No hay corchetes
			ret[1] = menuText;
		} else {
			// Hay corchetes
			ret[0] = menuText.substring(0, c1-1);
			ret[1] = menuText.substring(c1+1,c2-1);
		}
		return ret;
	}

	private void fireMenuBarChange() {
		firePropertyChange(CHANGED_PROPERTY, false, true);
	}
	
}
