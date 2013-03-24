package com.veisite.vegecom.service;

import java.util.List;

import com.veisite.vegecom.model.Municipio;
import com.veisite.vegecom.model.Provincia;

public interface MunicipioService {
	
	public Municipio save(Municipio municipio);
	
	public Municipio getById(String id);

	public List<Municipio> getList();

	public List<Municipio> getListbyProvincia(Provincia provincia);
	
	public Municipio getByNombre(String nombre);
}
