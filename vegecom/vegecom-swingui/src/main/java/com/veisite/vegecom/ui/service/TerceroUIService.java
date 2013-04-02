package com.veisite.vegecom.ui.service;

import com.veisite.vegecom.model.TerceroComercial;
import com.veisite.vegecom.ui.framework.service.UIFrameworkService;
import com.veisite.vegecom.ui.tercero.TerceroListTableModel;

public interface TerceroUIService<T extends TerceroComercial> extends UIFrameworkService {
	
	public TerceroListTableModel<T> getListTableModel();

}
