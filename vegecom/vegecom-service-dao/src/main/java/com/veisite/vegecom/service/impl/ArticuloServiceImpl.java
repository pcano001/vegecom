package com.veisite.vegecom.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.Articulo;
import com.veisite.vegecom.service.ArticuloService;
import com.veisite.vegecom.service.impl.dao.ArticuloDAO;

@Service
public class ArticuloServiceImpl implements ArticuloService {

	@Autowired
	ArticuloDAO dao;
	
	@Override @Transactional
	public Articulo save(Articulo articulo) {
		return dao.save(articulo);
	}

	@Override @Transactional
	public Articulo getById(Long id) {
		return dao.getById(id);
	}

	@Override @Transactional
	public List<Articulo> getList() {
		return dao.getList();
	}
	
	@Override @Transactional
	public void writeListTo(ObjectOutputFlow<Articulo> output) throws DataIOException {
		dao.writeListTo(output);
	}

}
