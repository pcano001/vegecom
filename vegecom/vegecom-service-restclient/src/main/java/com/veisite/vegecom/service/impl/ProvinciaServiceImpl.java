package com.veisite.vegecom.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veisite.utils.cache.AbstractCacheableDataService;
import com.veisite.vegecom.model.Provincia;
import com.veisite.vegecom.service.ProvinciaService;
import com.veisite.vegecom.service.impl.dao.ProvinciaDAO;

@Service
public class ProvinciaServiceImpl extends AbstractCacheableDataService<Provincia>
                                        implements ProvinciaService {

	private static final Logger logger = LoggerFactory.getLogger(ProvinciaServiceImpl.class);
	
	@Autowired
	ProvinciaDAO dao;
	
	public ProvinciaServiceImpl() {
		super();
	}

	@Override
	public Provincia save(Provincia provincia) {
		return dao.save(provincia);
	}

	@Override
	public Provincia getById(String id) {
		return dao.getById(id);
	}

	@Override
	public List<Provincia> getList() {
		return getCacheList();
	}

	@Override
	protected List<Provincia> loadCacheList() {
		logger.debug("Solicitanto lista de Provincias a dao");
		return dao.getList();
	}

}
