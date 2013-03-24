package com.veisite.vegecom.service;

import java.util.List;

import com.veisite.vegecom.model.Provincia;

public interface ProvinciaService {

	public Provincia save(Provincia provincia);
	
	public Provincia getById(String id);

	public List<Provincia> getList();

}
