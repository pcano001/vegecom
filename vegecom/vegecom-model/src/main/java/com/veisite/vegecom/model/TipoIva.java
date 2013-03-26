package com.veisite.vegecom.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.veisite.utils.EqualsUtil;
import com.veisite.utils.HashCodeUtil;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
@JsonInclude(Include.NON_NULL)
@Entity
public class TipoIva extends VersionableObject {

    /**
	 * serial
	 */
	private static final long serialVersionUID = 2805755662130383355L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Version
	private Long version;

    @Column @NotNull
    private String nombre;
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoIva", orphanRemoval = true)
    private List<TramoTiposIva> tramosTiposIva;
    
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(Long version) {
		this.version = version;
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
	public void setNombre(String nombre) {
		pcs.firePropertyChange("nombre", this.nombre, this.nombre = nombre);
	}
	
	/**
	 * @return the tramoTiposIva
	 */
	public List<TramoTiposIva> getTramosTiposIva() {
		return tramosTiposIva;
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
	    if ( !(that instanceof TipoIva) ) return false;
	    TipoIva o = (TipoIva) that;
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
