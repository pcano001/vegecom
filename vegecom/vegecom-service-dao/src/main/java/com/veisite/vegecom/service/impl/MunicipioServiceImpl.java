package com.veisite.vegecom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

	@Override @Transactional
	public Municipio getById(String id) {
		return dao.getById(id);
	}

	@Override @Transactional
	public List<Municipio> getList() {
		return dao.getList();
	}

	@Override @Transactional
	public List<Municipio> getListbyProvincia(Provincia provincia) {
		return dao.getListbyProvincia(provincia);
		
	}

	@Override @Transactional
	public Municipio getByNombre(String nombre) {
		return dao.getByNombre(nombre);
		
	}

}
