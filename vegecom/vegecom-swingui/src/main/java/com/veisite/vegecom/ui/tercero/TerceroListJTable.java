package com.veisite.vegecom.ui.tercero;

import java.util.List;

import com.veisite.vegecom.model.TerceroComercial;
import com.veisite.vegecom.ui.framework.component.table.AbstractListJTable;
import com.veisite.vegecom.ui.framework.component.table.AbstractListTableModel;
import com.veisite.vegecom.ui.service.TerceroUIService;

public class TerceroListJTable<T extends TerceroComercial> extends AbstractListJTable<T> {

	/**
	 * serial
	 */
	private static final long serialVersionUID = -5157503773928583494L;
	
	
	public TerceroListJTable(TerceroUIService<T> uiService) {
		super(new TerceroListTableModel<T>(uiService));
	}

	public TerceroListJTable(List<T> dataList, TerceroUIService<T> uiService) {
		super(new TerceroListTableModel<T>(dataList, uiService));
	}

	public TerceroListJTable(AbstractListTableModel<T> model) {
		super(model);
	}

	public TerceroListJTable(TerceroListProvider<T> dataProvider, TerceroUIService<T> uiService) {
		super(new TerceroListTableModel<T>(dataProvider, uiService));
	}

}
