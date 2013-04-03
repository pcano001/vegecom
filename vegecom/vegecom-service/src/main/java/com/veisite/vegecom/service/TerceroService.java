package com.veisite.vegecom.service;

import java.util.List;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.TerceroComercial;

public abstract interface TerceroService<T extends TerceroComercial> {

	public T save(T terceroComercial);
	
	public T remove(Long id);
	
	public T getById(Long id);

	public List<T> getList();
	
	public void writeListTo(ObjectOutputFlow<T> output) throws DataIOException;
	
	public void addDataChangeListener(DataChangeListener<T> listener);

	public void removeDataChangeListener(DataChangeListener<T> listener);

}
