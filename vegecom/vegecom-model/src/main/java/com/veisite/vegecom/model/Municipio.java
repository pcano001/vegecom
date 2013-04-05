package com.veisite.vegecom.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.veisite.utils.EqualsUtil;
import com.veisite.utils.HashCodeUtil;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonInclude(Include.NON_NULL)
@Entity
public class Municipio extends ModelObject {

    /**
	 * serial
	 */
	private static final long serialVersionUID = 6520586906555859645L;

	@Id
    @Column(name = "id", length=5)
    private String id;
	
    @Column @NotNull
    private String nombre;
    
    @ManyToOne 
    @Fetch(FetchMode.JOIN) 
    @NotNull
    private Provincia provincia;
    
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
	 * @return the provincia
	 */
	public Provincia getProvincia() {
		return provincia;
	}

	/**
	 * @param provincia the provincia to set
	 */
	public void setProvincia(Provincia newProvincia) {
		Provincia oldValue = this.provincia;
		this.provincia = newProvincia;
		pcs.firePropertyChange("provincia", oldValue, newProvincia);
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
	    if ( !(that instanceof Municipio) ) return false;
	    Municipio o = (Municipio) that;
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
