package com.veisite.vegecom.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.veisite.utils.EqualsUtil;
import com.veisite.utils.HashCodeUtil;

@JsonInclude(Include.NON_NULL)
@Entity
public class Provincia extends ModelObject {

    /**
	 * serial
	 */
	private static final long serialVersionUID = -6584205124588347468L;

	@Id
    @Column(name = "id", length=2)
    private String id;

    @Column @NotNull
    private String nombre;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	public void setNombre(String newNombre) {
		String oldValue = this.nombre;
		this.nombre = newNombre;
		pcs.firePropertyChange("nombre", oldValue, newNombre );
	}
	
	/**
	 * toString 
	 */
	public String toString() {
		return getNombre();
	}
	
	/**
	 * equals
	 */
	@Override 
	public boolean equals(Object that) {
	    if ( this == that ) return true;
	    if ( !(that instanceof Provincia) ) return false;
	    Provincia o = (Provincia) that;
	    return (EqualsUtil.areEqual(this.id,o.id) && EqualsUtil.areEqual(this.nombre,o.nombre));
	}

	/**
	 * hashCode
	 */
	@Override
	public int hashCode() {
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, id);
		result = HashCodeUtil.hash(result, nombre);
		return result;
	}

}
