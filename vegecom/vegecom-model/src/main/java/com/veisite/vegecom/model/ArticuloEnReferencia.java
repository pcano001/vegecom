package com.veisite.vegecom.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
public class ArticuloEnReferencia extends VersionableObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4240645860695009230L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Version
	private Long version;
	
	@ManyToOne
	@NotNull
	private Referencia referencia;
	
	@ManyToOne
	@NotNull
	private Articulo articulo;
	
	@Column
	@NotNull
	private double cantidad;
	
	
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
	 * @return the articulo
	 */
	public Articulo getArticulo() {
		return articulo;
	}

	/**
	 * @param articulo the articulo to set
	 */
	public void setArticulo(Articulo articulo) {
		pcs.firePropertyChange("articulo", this.articulo, this.articulo = articulo);
	}

	/**
	 * @return the cantidad
	 */
	public double getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(double cantidad) {
		pcs.firePropertyChange("cantidad", this.cantidad, this.cantidad = cantidad);
	}

}
