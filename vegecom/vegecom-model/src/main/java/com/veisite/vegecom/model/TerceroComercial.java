package com.veisite.vegecom.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.veisite.utils.EqualsUtil;
import com.veisite.utils.HashCodeUtil;
import com.veisite.vegecom.model.validation.CheckNif;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class TerceroComercial extends VersionableObject {

    /**
	 * serial
	 */
	private static final long serialVersionUID = 934482483702442714L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Version
	private Long version;

    @Column(length=20)
    @CheckNif
    private String nif;
    
    @Column 
    @NotNull 
    @NotEmpty(message="{com.veisite.vegecom.constraints.model.Tercero.Nombre.NotEmpty}")
    private String nombre;

    @Column
	private String domicilio;

    @Column(length=5)
	private String codigoPostal;

    @ManyToOne
    private Municipio municipio;
    
    @Column
    private String localidad;
    
    @ManyToOne
    private Provincia provincia;
    
    @Column
    private String telefono;
    
    @Column
    private String cuentaBancaria;
    
    @Column
    @Email(message="{com.veisite.vegecom.constraints.model.Tercero.Email.Incorrect}")
    private String email;
    
	@Column(length=1024)
    private String observaciones;
    
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
	 * @return the cif
	 */
	public String getNif() {
		return nif;
	}

	/**
	 * @param nif the cif to set
	 */
	public void setNif(String nif) {
		pcs.firePropertyChange("nif", this.nif, this.nif = nif );
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
		pcs.firePropertyChange("nombre", this.nombre, this.nombre = nombre );
	}
	
	/**
	 * @return the domicilio
	 */
	public String getDomicilio() {
		return domicilio;
	}

	/**
	 * @param domicilio the domicilio to set
	 */
	public void setDomicilio(String domicilio) {
		pcs.firePropertyChange("domicilio", this.domicilio, this.domicilio = domicilio);
	}


	/**
	 * @return the codigoPostal
	 */
	public String getCodigoPostal() {
		return codigoPostal;
	}

	/**
	 * @param codigoPostal the codigoPostal to set
	 */
	public void setCodigoPostal(String codigoPostal) {
		pcs.firePropertyChange("codigoPostal", this.codigoPostal,
				this.codigoPostal = codigoPostal);
	}

	/**
	 * @return the municipio
	 */
	public Municipio getMunicipio() {
		return municipio;
	}

	/**
	 * @param municipio the municipio to set
	 */
	public void setMunicipio(Municipio municipio) {
		pcs.firePropertyChange("municipio", this.municipio,
				this.municipio = municipio);
	}

	/**
	 * @return the localidad
	 */
	public String getLocalidad() {
		return localidad;
	}

	/**
	 * @param localidad the localidad to set
	 */
	public void setLocalidad(String localidad) {
		pcs.firePropertyChange("localidad", this.localidad,
				this.localidad = localidad);
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
	public void setProvincia(Provincia provincia) {
		pcs.firePropertyChange("provincia", this.provincia,
				this.provincia = provincia);
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(String telefono) {
		pcs.firePropertyChange("telefono", this.telefono, this.telefono = telefono);
	}

	/**
	 * @return the cuentaBancaria
	 */
	public String getCuentaBancaria() {
		return cuentaBancaria;
	}

	/**
	 * @param cuentaBancaria the cuentaBancaria to set
	 */
	public void setCuentaBancaria(String cuentaBancaria) {
		pcs.firePropertyChange("cuentaBancaria", this.cuentaBancaria,
				this.cuentaBancaria = cuentaBancaria);
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		pcs.firePropertyChange("email", this.email, this.email = email);
	}

	/**
	 * @return the observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones the observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		pcs.firePropertyChange("observaciones", this.observaciones,
				this.observaciones = observaciones);
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
	    if ( !(that instanceof TerceroComercial) ) return false;
	    TerceroComercial o = (TerceroComercial) that;
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
