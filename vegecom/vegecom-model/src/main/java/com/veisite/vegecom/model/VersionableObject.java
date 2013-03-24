package com.veisite.vegecom.model;

public abstract class VersionableObject extends ModelObject {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 2002397477830156176L;
	
	public abstract Long getVersion();
	
	public abstract void setVersion(Long version);
	
}
