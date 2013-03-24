package com.veisite.vegecom.service;

import java.util.List;

import com.veisite.vegecom.model.Municipio;
import com.veisite.vegecom.model.Provincia;

public interface AddressService {

	public List<Provincia> getProvincias();
	
	public List<Municipio> getAllMunicipios();

	public List<Municipio> getMunicipiosByProvincia(Provincia provincia);
	
}
