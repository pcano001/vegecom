package com.veisite.vegecom.ui.tercero;

import com.veisite.utils.dataio.AbstractPipedListProvider;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.TerceroComercial;
import com.veisite.vegecom.service.TerceroService;

public abstract class TerceroListProvider<T extends TerceroComercial> 
						extends AbstractPipedListProvider<T> {


	private TerceroService<T> dataService;
	
	public TerceroListProvider(TerceroService<T> dataService) {
		this.dataService = dataService;
	}
	
	/**
	 * @see com.veisite.vegecom.dataio.AbstractPipedListProvider#writeOutputStream(com.veisite.vegecom.dataio.ObjectOutputFlow)
	 */
	@Override
	protected void writeOutputStream(ObjectOutputFlow<T> output)
			throws Throwable {
		dataService.writeListTo(output);
	}


	/**
	 * @return the dataService
	 */
	public TerceroService<T> getDataService() {
		return dataService;
	}

}
