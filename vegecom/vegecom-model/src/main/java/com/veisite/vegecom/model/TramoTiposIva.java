package com.veisite.vegecom.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
public class TramoTiposIva extends VersionableObject {

    /**
	 * serial
	 */
	private static final long serialVersionUID = 6125518384897112328L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
	@Version
	private Long version;

    @ManyToOne @NotNull
    private TipoIva tipoIva;

    @Temporal(TemporalType.DATE) @NotNull
    private Calendar fechaInicioVigencia;

    @Temporal(TemporalType.DATE)
    private Calendar fechaFinVigencia;

    @Column @NotNull
    private double tipo;
    
    @Column @NotNull
    private double tipoRecEq;
    

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
	 * @return the tipoIva
	 */
	public TipoIva getTipoIva() {
		return tipoIva;
	}

	/**
	 * @param tipoIva the tipoIva to set
	 */
	public void setTipoIva(TipoIva tipoIva) {
		pcs.firePropertyChange("tipoIva", this.tipoIva, this.tipoIva = tipoIva);
	}

	/**
	 * @return the fechaInicioVigencia
	 */
	public Calendar getFechaInicioVigencia() {
		return fechaInicioVigencia;
	}

	/**
	 * @param fechaInicioVigencia the fechaInicioVigencia to set
	 */
	public void setFechaInicioVigencia(Calendar fechaInicioVigencia) {
		pcs.firePropertyChange("fechaInicioVigencia", this.fechaInicioVigencia,
				this.fechaInicioVigencia = fechaInicioVigencia);
	}

	/**
	 * @return the fechaFinVigencia
	 */
	public Calendar getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * @param fechaFinVigencia the fechaFinVigencia to set
	 */
	public void setFechaFinVigencia(Calendar fechaFinVigencia) {
		pcs.firePropertyChange("fechaFinVigencia", this.fechaFinVigencia,
				this.fechaFinVigencia = fechaFinVigencia);
	}

	/**
	 * @return the tipo
	 */
	public double getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(double tipo) {
		pcs.firePropertyChange("tipo", this.tipo, this.tipo = tipo);
	}

	/**
	 * @return the tipoRecEq
	 */
	public double getTipoRecEq() {
		return tipoRecEq;
	}

	/**
	 * @param tipoRecEq the tipoRecEq to set
	 */
	public void setTipoRecEq(double tipoRecEq) {
		pcs.firePropertyChange("tipoRecEq", this.tipoRecEq,
				this.tipoRecEq = tipoRecEq);
	}

    
}
