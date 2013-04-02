package com.veisite.vegecom.ui.framework.component.tabbedpane;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

public class CloseableTabbedPane extends JTabbedPane implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Cuando se cierra una pestaña se lanza un cambio de propiedad
	 * en la propiedad supuesta "removedTab". El valor old lleva
	 * el componente eliminado, el valor nuevo es null
	 */
	public static String REMOVEDTAB_PROPERTY = "removedTab";

	/** Creates a new instance of ClosableTabbedPane */
	public CloseableTabbedPane() {
		super();
		initializeMouseListener();
	}

	/**
	 * Appends a tab without closing-capabilities, just as the standard
	 * JTabbedPane would do.
	 * @see javax.swing.JTabbedPane#addTab(String title, Component component) addTab
	 */
	@Override
	public void addTab(String title, Component component) {
		this.addTab(title, component, null, false);
	}

	/**
	 * Appends a tab with or without closing-capabilities, depending on the flag
	 * isClosable. If isClosable is true, a close-icon ('X') is displayed left
	 * of the title.
	 * @param title Title of this tab.
	 * @param component Contents of this tab.
	 * @param isClosable en-/disable closing-capabilities
	 * @see javax.swing.JTabbedPane#addTab(String title, Component component) addTab
	 */
	public void addTab(String title, Component component, boolean isClosable) {
		this.addTab(title, component, null, isClosable);
	}

	/**
	 * Appends a tab with or without closing-capabilities, depending on the flag
	 * isClosable. If isClosable is true, a close-icon ('X') is displayed left
	 * of the title. If extraIcon is not null, it will be displayed between the
	 * closing icon (if present) and the tab's title. The extraIcon will be
	 * displayed indepently of the closing-icon.
	 * @param title Title of this tab.
	 * @param component Contents of this tab.
	 * @param extraIcon Extra icon to be displayed.
	 * @param isClosable en-/disable closing-capabilities
	 * @see javax.swing.JTabbedPane#addTab(String title, Component component) addTab
	 */
	public void addTab(String title, Component component, Icon extraIcon, boolean isClosable) {
		if(isClosable) {
			super.addTab(title, new CloseTabIcon(extraIcon), component);
		} else {
			if(extraIcon != null) {
				super.addTab(title, extraIcon, component);
			} else {
				super.addTab(title, component);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		int tabIndex = getUI().tabForCoordinate(this, evt.getX(), evt.getY());
		if (tabIndex < 0) {
			return;
		}

		Icon icon = getIconAt(tabIndex);

		if((icon == null) || !(icon instanceof CloseTabIcon)) {
			// This tab is not intended to be closeable.
			return;
		}

		Rectangle rect=((CloseTabIcon)icon).getBounds();
		if (rect.contains(evt.getX(), evt.getY())) {
			//the tab is being closed
			Component c = this.getComponentAt(tabIndex);
			this.removeTabAt(tabIndex);
			firePropertyChange(REMOVEDTAB_PROPERTY, c, null);
		}
	}

	@Override
	public void mouseEntered(MouseEvent evt) {}
	@Override
	public void mouseExited(MouseEvent evt) {}
	@Override
	public void mousePressed(MouseEvent evt) {}
	@Override
	public void mouseReleased(MouseEvent evt) {}

	private void initializeMouseListener() {
		addMouseListener(this);
	}
	
}
