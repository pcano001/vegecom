package com.veisite.vegecom.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class PrecioReferenciaProveedor extends PrecioReferenciaTercero {

    /**
	 * serial
	 */
	private static final long serialVersionUID = -8441118589955932813L;
	
	@ManyToOne @NotNull
    protected Proveedor proveedor;

    /**
	 * @return the proveedor
	 */
	public Proveedor getProveedor() {
		return proveedor;
	}

	/**
	 * @param proveedor the proveedor to set
	 */
	public void setProveedor(Proveedor proveedor) {
		pcs.firePropertyChange("proveedor", this.proveedor, this.proveedor = proveedor);
	}
	
	@Override
	public void setTercero(TerceroComercial tercero) {
		if (tercero instanceof Proveedor)
			setProveedor((Proveedor) tercero);
		else
			throw new IllegalArgumentException("setTercero on PrecioArticuloProveedor expect a Proveedor: "+tercero.getClass().toString());
	}

	@Override
	public TerceroComercial getTercero() {
		return getProveedor();
	}
    
}
