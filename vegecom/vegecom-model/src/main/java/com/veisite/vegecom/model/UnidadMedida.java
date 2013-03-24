package com.veisite.vegecom.model;

public enum UnidadMedida {
	U("Unidad","Unidades","u"),
	K("Kilogramo","Kilogramos","kg"),
	L("Litro","Litros","L"),
	M("Metro","Metros","m");
	
	/**
	 * Nombre singular de la unidad
	 */
	private String singular;
	
	/**
	 * Nombre plural de la unidad
	 */
	private String plural;
	
	/**
	 * Abreviatura
	 */
	private String abreviatura;

	
	private UnidadMedida(String singular, String plural, String abreviatura) {
		this.setSingular(singular);
	}

	/**
	 * @return the nombre
	 */
	public String getSingular() {
		return singular;
	}

	/**
	 * @param singular the singular to set
	 */
	private void setSingular(String singular) {
		this.singular = singular;
	}

	
	/**
	 * @return the plural
	 */
	public String getPlural() {
		return plural;
	}

	/**
	 * @param plural the plural to set
	 */
	public void setPlural(String plural) {
		this.plural = plural;
	}

	
	/**
	 * @return the abreviatura
	 */
	public String getAbreviatura() {
		return abreviatura;
	}

	/**
	 * @param abreviatura the abreviatura to set
	 */
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	/**
	 * To string.
	 */
	public String toString() {
		return getPlural();
	}

}
