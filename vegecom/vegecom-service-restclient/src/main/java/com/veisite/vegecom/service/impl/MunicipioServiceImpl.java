package com.veisite.vegecom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.Municipio;
import com.veisite.vegecom.model.Provincia;
import com.veisite.vegecom.service.MunicipioService;
import com.veisite.vegecom.service.impl.dao.MunicipioDAO;

@Service
public class MunicipioServiceImpl implements MunicipioService {

	@Autowired
	MunicipioDAO dao;

	@Override
	public Municipio save(Municipio municipio) {
		return dao.save(municipio);
	}

	@Override
	public Municipio getById(String id) {
		return dao.getById(id);
	}

	@Override
	public List<Municipio> getList() {
		return dao.getList();
	}
	
	@Override
	public void getList(ObjectOutputFlow<Municipio> output, Provincia provincia) throws DataIOException {
		dao.getList(output, provincia);
	}

	@Override
	public List<Municipio> getListbyProvincia(Provincia provincia) {
		return dao.getListbyProvincia(provincia);
		
	}

	@Override
	public Municipio getByNombre(String nombre) {
		return dao.getByNombre(nombre);
	}

}
