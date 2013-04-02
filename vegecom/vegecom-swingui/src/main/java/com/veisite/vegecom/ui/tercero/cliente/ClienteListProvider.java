package com.veisite.vegecom.ui.tercero.cliente;

import com.veisite.vegecom.model.Cliente;
import com.veisite.vegecom.service.ClienteService;
import com.veisite.vegecom.ui.tercero.TerceroListProvider;

public class ClienteListProvider extends TerceroListProvider<Cliente> {

	/**
	 * Constructor
	 */
	public ClienteListProvider(ClienteService dataService) {
		super(dataService);
	}
	
}
