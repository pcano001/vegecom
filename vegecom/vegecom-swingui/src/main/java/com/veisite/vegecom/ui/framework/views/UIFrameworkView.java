package com.veisite.vegecom.ui.framework.views;

import javax.swing.JPanel;

import com.veisite.vegecom.ui.framework.UIFramework;
import com.veisite.vegecom.ui.framework.UIFrameworkObject;

public class UIFrameworkView extends JPanel implements UIFrameworkObject {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -8529583009908420653L;
	
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
	 * Titulo de la vista. Es lo que se muestra en el tab  
	 * @param id
	 */
	private String title;
	
	
	public UIFrameworkView(String id) {
		this(id,id);
	}
	
	public UIFrameworkView(String id, String title) {
		super();
		this.id = id;
		this.title = title;
	}


	@Override
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
