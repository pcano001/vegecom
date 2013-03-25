package com.veisite.vegecom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public Municipio save(Municipio municipio) {
		return dao.save(municipio);
	}

	@Transactional
	public Municipio getById(String id) {
		return dao.getById(id);
	}

	@Transactional
	public List<Municipio> getList() {
		return dao.getList();
	}
	
	@Transactional
	public void getList(ObjectOutputFlow<Municipio> output) throws DataIOException {
		dao.getList(output);
	}

	@Transactional
	public List<Municipio> getListbyProvincia(Provincia provincia) {
		return dao.getListbyProvincia(provincia);
		
	}

	@Transactional
	public Municipio getByNombre(String nombre) {
		return dao.getByNombre(nombre);
	}

}
