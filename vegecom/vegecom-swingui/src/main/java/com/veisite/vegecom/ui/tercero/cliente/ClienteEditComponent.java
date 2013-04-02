package com.veisite.vegecom.ui.tercero.cliente;

import com.veisite.vegecom.model.Cliente;
import com.veisite.vegecom.ui.service.ClienteUIService;
import com.veisite.vegecom.ui.tercero.TerceroEditComponent;

public class ClienteEditComponent extends TerceroEditComponent<Cliente> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2239647577118086267L;

	public ClienteEditComponent(Cliente tercero, ClienteUIService uiService) {
		super(tercero, uiService);
		add(new ClienteSelectTextComponent("Introduzca texto para buscar cliente", uiService));
	}

}
