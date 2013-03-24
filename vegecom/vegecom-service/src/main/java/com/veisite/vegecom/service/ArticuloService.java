package com.veisite.vegecom.service;

import java.util.List;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.Articulo;

public interface ArticuloService {

	public Articulo save(Articulo articulo);
	
	public Articulo getById(Long id);

	public List<Articulo> getList();
	
	public void writeListTo(ObjectOutputFlow<Articulo> output) throws DataIOException;

}
