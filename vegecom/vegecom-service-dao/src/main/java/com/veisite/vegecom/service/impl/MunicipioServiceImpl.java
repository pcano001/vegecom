package com.veisite.vegecom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.Municipio;
import com.veisite.vegecom.model.Provincia;
import com.veisite.vegecom.service.MunicipioService;
import com.veisite.vegecom.service.impl.dao.MunicipioDAO;
import com.veisite.vegecom.service.impl.dao.MunicipioRepository;

@Service
@Transactional(readOnly=true)
public class MunicipioServiceImpl implements MunicipioService {

	@Autowired
	MunicipioDAO daol;

	@Autowired
	MunicipioRepository dao;

	@Transactional
	public Municipio save(Municipio municipio) {
		return dao.save(municipio);
	}

	@Override
	public Municipio getById(String id) {
		return dao.findOne(id);
	}

	@Override
	public List<Municipio> getList() {
		return dao.findAll(sortByNombreAsc());
	}
	
	@Override
	public void getList(ObjectOutputFlow<Municipio> output, Provincia provincia) throws DataIOException {
		List<Municipio> lm;
		if (provincia==null) lm = getList();
		else lm = getListbyProvincia(provincia);
		for (Municipio m : lm) output.write(m);
	}

	@Override
	public List<Municipio> getListbyProvincia(Provincia provincia) {
		return dao.findByProvincia(provincia,sortByNombreAsc());
	}

	@Override
	public Municipio getByNombre(String nombre) {
		return dao.findByNombre(nombre);
	}

    private Sort sortByNombreAsc() {
        return new Sort(Sort.Direction.ASC, "nombre");
    }
	
}
