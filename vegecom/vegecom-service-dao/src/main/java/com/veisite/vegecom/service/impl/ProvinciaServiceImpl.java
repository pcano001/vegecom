package com.veisite.vegecom.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veisite.utils.cache.AbstractCacheableDataService;
import com.veisite.vegecom.model.Provincia;
import com.veisite.vegecom.service.ProvinciaService;
import com.veisite.vegecom.service.impl.dao.ProvinciaDAO;
import com.veisite.vegecom.service.impl.dao.ProvinciaRepository;

@Service
@Transactional(readOnly=true)
public class ProvinciaServiceImpl extends AbstractCacheableDataService<Provincia>
                                        implements ProvinciaService {

	private static final Logger logger = LoggerFactory.getLogger(ProvinciaServiceImpl.class);
	
	@Autowired
	ProvinciaDAO daol;
	
	@Autowired
	ProvinciaRepository dao;

	public ProvinciaServiceImpl() {
		super();
	}

	@Transactional @Override
	public Provincia save(Provincia provincia) {
		return dao.save(provincia);
	}

	@Override
	public Provincia getById(String id) {
		return dao.findOne(id);
	}

	@Override
	public List<Provincia> getList() {
		return getCacheList();
	}

	@Override
	protected List<Provincia> loadCacheList() {
		logger.debug("Solicitanto lista de Provincias a dao BD");
		return dao.findAll(new Sort(new Order(Sort.Direction.ASC, "nombre")));
	}

}
