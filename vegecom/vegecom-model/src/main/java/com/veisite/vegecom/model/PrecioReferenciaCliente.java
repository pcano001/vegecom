package com.veisite.vegecom.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class PrecioReferenciaCliente extends PrecioReferenciaTercero {

    /**
	 * serial
	 */
	private static final long serialVersionUID = -6331606996565244276L;
	
	@ManyToOne @NotNull
    protected Cliente cliente;

	/**
	 * @return the proveedor
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * @param proveedor the proveedor to set
	 */
	public void setCliente(Cliente cliente) {
		pcs.firePropertyChange("cliente", this.cliente, this.cliente = cliente);
	}

	@Override
	public void setTercero(TerceroComercial tercero) {
		if (tercero instanceof Cliente)
			setCliente((Cliente) tercero);
		else
			throw new IllegalArgumentException("setTercero on PrecioArticuloCliente expect a Cliente: "+tercero.getClass().toString());
	}

	@Override
	public TerceroComercial getTercero() {
		return getCliente();
	}
    
	
}
