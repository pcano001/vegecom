package com.veisite.vegecom.ui.tercero.proveedor;

import com.veisite.vegecom.VegecomException;
import com.veisite.vegecom.model.Proveedor;
import com.veisite.vegecom.service.ProveedorService;

public class ProveedorListProvider extends TerceroListProvider<Proveedor> {

	/**
	 * Constructor
	 */
	public ProveedorListProvider(ProveedorService dataService) throws VegecomException {
		super(dataService);
	}
	
}
