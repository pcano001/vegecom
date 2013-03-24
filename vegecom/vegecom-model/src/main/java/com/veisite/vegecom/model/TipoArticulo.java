package com.veisite.vegecom.model;

public enum TipoArticulo {
	M("Mercancía"),
	F("Fabricado"),
	S("Servicio");
	
	/**
	 * Nombre del tipo de artículo
	 */
	private String nombre;
	
	private TipoArticulo(String nombre) {
		this.setNombre(nombre);
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	private void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	public String toString() {
		return getNombre();
	}

}
