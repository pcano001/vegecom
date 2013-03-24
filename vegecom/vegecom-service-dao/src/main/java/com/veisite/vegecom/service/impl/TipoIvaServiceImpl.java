package com.veisite.vegecom.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veisite.utils.cache.AbstractCacheableDataService;
import com.veisite.vegecom.model.TipoIva;
import com.veisite.vegecom.service.TipoIvaService;
import com.veisite.vegecom.service.impl.dao.TipoIvaDAO;

@Service
public class TipoIvaServiceImpl extends AbstractCacheableDataService<TipoIva> implements TipoIvaService {
	
	private static final Logger logger = LoggerFactory.getLogger(TipoIvaServiceImpl.class);
	
	@Autowired
	TipoIvaDAO dao;

	@Override @Transactional
	public TipoIva save(TipoIva tipoIva) {
		invalidateCache();
		return dao.save(tipoIva);
	}

	@Override @Transactional
	public TipoIva getById(Long id) {
		return dao.getById(id);
	}

	@Override @Transactional
	public List<TipoIva> getList() {
		return getCacheList();
	}

	@Override
	protected List<TipoIva> loadCacheList() {
		logger.debug("Requesting VAT list type to dao DB");
		return dao.getList();
	}

}
