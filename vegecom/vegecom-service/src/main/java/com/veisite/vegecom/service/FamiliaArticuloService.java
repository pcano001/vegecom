package com.veisite.vegecom.service;

import java.util.List;

import com.veisite.vegecom.model.FamiliaArticulo;

public interface FamiliaArticuloService {

	public FamiliaArticulo save(FamiliaArticulo familiaArticulo);
	
	public FamiliaArticulo getById(Long id);

	public List<FamiliaArticulo> getList();

}
