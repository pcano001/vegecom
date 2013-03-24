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

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
public abstract class PrecioReferenciaTercero extends VersionableObject {

    /**
	 * serial
	 */
	private static final long serialVersionUID = -2862952215148055269L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
	@Version
	private Long version;

    @ManyToOne @NotNull
    protected Referencia referencia;
    
    @Column
    private double precio;
    
    /**
     * Procentaje de descuento sobre tarifa general del artículo
     *  Se utiliza para obviar precio anterior y calcularlo sobre el
     *  precio de tarifa. Si null, el precio será el anterior.
     */
    @Column 
    private double descuento;

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
	 * @return the proveedor
	 */
	public abstract TerceroComercial getTercero();

	/**
	 * @param proveedor the proveedor to set
	 */
	public abstract void setTercero(TerceroComercial tercero);


	/**
	 * @return the referencia
	 */
	public Referencia getReferencia() {
		return referencia;
	}

	/**
	 * @param referencia the referencia to set
	 */
	public void setReferencia(Referencia referencia) {
		pcs.firePropertyChange("referencia", this.referencia,
				this.referencia = referencia);
	}

	/**
	 * @return the precio
	 */
	public double getPrecio() {
		return precio;
	}

	/**
	 * @param precio the precio to set
	 */
	public void setPrecio(double precio) {
		pcs.firePropertyChange("precio", this.precio, this.precio = precio);
	}

	/**
	 * @return the descuento
	 */
	public double getDescuento() {
		return descuento;
	}

	/**
	 * @param descuento the descuento to set
	 */
	public void setDescuento(double descuento) {
		pcs.firePropertyChange("descuento", this.descuento,
				this.descuento = descuento);
	}

    
}
