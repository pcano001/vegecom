package com.veisite.vegecom.ui.service;

import java.awt.Component;

import com.veisite.vegecom.model.Cliente;

public interface ClienteUIService extends TerceroUIService<Cliente> {
	
	public Cliente newCliente(Component parent);
	
	public Cliente editCliente(Cliente cliente, Component parent);
	
	public Cliente removeCliente(Cliente cliente, boolean askConfirmation, Component parent);
	
}
