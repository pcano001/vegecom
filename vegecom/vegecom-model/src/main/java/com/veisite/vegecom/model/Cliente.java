package com.veisite.vegecom.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Cliente extends TerceroComercial {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 5564033182137895254L;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente", orphanRemoval = true)
	private List<PrecioReferenciaCliente> preciosArticulos;
    

	/**
	 * @return the tarifas
	 */
	public List<PrecioReferenciaCliente> getPreciosArticulos() {
		return preciosArticulos;
	}

}
