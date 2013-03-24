package com.veisite.vegecom.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.veisite.utils.EqualsUtil;
import com.veisite.utils.HashCodeUtil;

/**
 * Una referencia es un item que se vende y/o compra en un negocio.
 * Esta formado por la conjunción de uno o más artículos en una 
 * determinada cantidad.
 * Cada referencia puede tener un precio de coste y otro de venta
 * pero ademas se pueden definir precios de venta y compra 
 * para cada cliente o proveedor respectivamente.
 * Igualmente cada referencia puede tener un identificador de
 * venta y varias referencias por cada proveedor. 
 * 
 * @author josemaria
 *
 */
@Entity
public class Referencia extends VersionableObject {

	/**
	 * serial
	 */
	private static final long serialVersionUID = 5477907634588805610L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Version
	private Long version;

    @Column(unique=true)
    @NotNull @NotEmpty 
    private String codigo;

    @Column
    @NotNull @NotEmpty
    private String descripcion;
    
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "referencia", orphanRemoval = true)
    private List<ArticuloEnReferencia> articulos;
    
    @Column 
    private double precioCoste;
    
    @Column
    private double precioVenta;
    
    @Column
    private boolean controlStock;
    
    @Column
    private boolean permiteStockNegativo;

    /**
     * Atriubuto que mantiene el stock de la referencia
     * Este atributo se actualiza con las entradas y salidas
     * y en realidad es calculado pero se desnormaliza por
     * rendimiento.
     */
    @Column
    private double stock;

    /**
     * Tipo de iva de la referencia.
     * Puede heredar el del artículo o artículos que lo componen sin son todos iguales
     */
    @ManyToOne
    private TipoIva tipoIva;
    
    /**
     * Precios de la referencia para clientes
     */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "referencia", targetEntity=PrecioReferenciaTercero.class, orphanRemoval = true)
	private List<PrecioReferenciaCliente> preciosClientes;

    /**
     * Precios de la referencia para clientes
     */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "referencia", targetEntity=PrecioReferenciaTercero.class, orphanRemoval = true)
	private List<PrecioReferenciaProveedor> preciosProveedor;

    /**
     * Campo de observaciones generales para la referencia
     */
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
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		pcs.firePropertyChange("codigo", this.codigo, this.codigo = codigo);
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		pcs.firePropertyChange("descripcion", this.descripcion,
				this.descripcion = descripcion);
	}

	/**
	 * @return the articulos
	 */
	public List<ArticuloEnReferencia> getArticulos() {
		return articulos;
	}

	/**
	 * @param articulos the articulos to set
	 */
	public void setArticulos(List<ArticuloEnReferencia> articulos) {
		pcs.firePropertyChange("articulos", this.articulos,
				this.articulos = articulos);
	}

	/**
	 * @return the precioCoste
	 */
	public double getPrecioCoste() {
		return precioCoste;
	}

	/**
	 * @param precioCoste the precioCoste to set
	 */
	public void setPrecioCoste(double precioCoste) {
		pcs.firePropertyChange("precioCoste", this.precioCoste,
				this.precioCoste = precioCoste);
	}

	/**
	 * @return the precioVenta
	 */
	public double getPrecioVenta() {
		return precioVenta;
	}

	/**
	 * @param precioVenta the precioVenta to set
	 */
	public void setPrecioVenta(double precioVenta) {
		pcs.firePropertyChange("precioVenta", this.precioVenta,
				this.precioVenta = precioVenta);
	}

	/**
	 * @return the controlStock
	 */
	public boolean isControlStock() {
		return controlStock;
	}

	/**
	 * @param controlStock the controlStock to set
	 */
	public void setControlStock(boolean controlStock) {
		pcs.firePropertyChange("controlStock", this.controlStock,
				this.controlStock = controlStock);
	}

	/**
	 * @return the permiteStockNegativo
	 */
	public boolean isPermiteStockNegativo() {
		return permiteStockNegativo;
	}

	/**
	 * @param permiteStockNegativo the permiteStockNegativo to set
	 */
	public void setPermiteStockNegativo(boolean permiteStockNegativo) {
		pcs.firePropertyChange("permiteStockNegativo", this.permiteStockNegativo,
				this.permiteStockNegativo = permiteStockNegativo);
	}

	/**
	 * @return the stock
	 */
	public double getStock() {
		return stock;
	}

	/**
	 * @param stock the stock to set
	 */
	public void setStock(double stock) {
		pcs.firePropertyChange("stock", this.stock, this.stock = stock);
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
	 * @return the preciosClientes
	 */
	public List<PrecioReferenciaCliente> getPreciosClientes() {
		return preciosClientes;
	}

	/**
	 * @param preciosClientes the preciosClientes to set
	 */
	public void setPreciosClientes(List<PrecioReferenciaCliente> preciosClientes) {
		pcs.firePropertyChange("preciosClientes", this.preciosClientes,
				this.preciosClientes = preciosClientes);
	}

	/**
	 * @return the preciosProveedor
	 */
	public List<PrecioReferenciaProveedor> getPreciosProveedor() {
		return preciosProveedor;
	}

	/**
	 * @param preciosProveedor the preciosProveedor to set
	 */
	public void setPreciosProveedor(List<PrecioReferenciaProveedor> preciosProveedor) {
		pcs.firePropertyChange("preciosProveedor", this.preciosProveedor,
				this.preciosProveedor = preciosProveedor);
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
		return getDescripcion();
	}

	/**
	 * equals
	 */
	@Override 
	public boolean equals(Object that) {
	    if ( this == that ) return true;
	    if ( !(that instanceof Referencia) ) return false;
	    Referencia o = (Referencia) that;
	    return (EqualsUtil.areEqual(this.id,o.id) &&
	    		EqualsUtil.areEqual(this.codigo,o.codigo) &&
	    		EqualsUtil.areEqual(this.descripcion,o.descripcion)); 
	}

	/**
	 * hashCode
	 */
	@Override
	public int hashCode() {
		int result = HashCodeUtil.SEED;
		result = HashCodeUtil.hash(result, id);
		result = HashCodeUtil.hash(result, descripcion);
		return result;
	}	
	
}
