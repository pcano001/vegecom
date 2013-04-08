package com.veisite.vegecom.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonInclude(Include.NON_NULL)
@Entity
public class Cliente extends TerceroComercial {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 5564033182137895254L;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cliente", orphanRemoval = true)
	private List<PrecioReferenciaCliente> preciosArticulos = new ArrayList<PrecioReferenciaCliente>();
    

	/**
	 * @return the tarifas
	 */
	public List<PrecioReferenciaCliente> getPreciosArticulos() {
		return preciosArticulos;
	}

}
