package com.veisite.vegecom.model;

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
public class Proveedor extends TerceroComercial {


	/**
	 * serial
	 */
	private static final long serialVersionUID = -792277261858752284L;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "proveedor", orphanRemoval = true)
	private List<PrecioReferenciaProveedor> preciosArticulos;
	

	/**
	 * @return the preciosArticulos
	 */
	public List<PrecioReferenciaProveedor> getPreciosArticulos() {
		return preciosArticulos;
	}

}
