package com.veisite.vegecom.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.veisite.utils.cache.AbstractCacheableDataService;
import com.veisite.vegecom.model.Municipio;
import com.veisite.vegecom.model.Provincia;
import com.veisite.vegecom.service.AddressService;
import com.veisite.vegecom.service.MunicipioService;
import com.veisite.vegecom.service.ProvinciaService;

@Service
public class AddressServiceImpl implements AddressService {

	private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

	@Autowired
	ProvinciaService pService;

	@Autowired
	MunicipioService mService;

	/**
	 * Lista que mantiene lo municipios cacheados por provincia
	 */
	private HashMap<String,MunicipiosProvinciaItem> cachedMunList = new HashMap<String,MunicipiosProvinciaItem>();

	
	@Override
	public List<Provincia> getProvincias() {
		return pService.getList();
	}

	@Override
	public List<Municipio> getAllMunicipios() {
		logger.debug("Solicitando lista de todos los municipios a MunicipioService");
		return mService.getList();
	}

	@Override
	public List<Municipio> getMunicipiosByProvincia(Provincia provincia) {
		// Buscar si tenemos la provincia en la lista
		MunicipiosProvinciaItem item = cachedMunList.get(provincia.getId());
		if (item==null) {
			// Cachear lista de municipios para esa provincia
			item = new MunicipiosProvinciaItem(provincia,mService);
			cachedMunList.put(provincia.getId(), item);
			logger.debug("Se incluye provincia {}-{} en cache de municipios",provincia.getId(),provincia.getNombre());
		}
		return item.getMunicipios();
	}
	
	
	/**
	 * Clase para implementar la cache de los municipios de una provincia.
	 * @author josemaria
	 *
	 */
	private class MunicipiosProvinciaItem extends AbstractCacheableDataService<Municipio> {
		
		private Provincia provincia;
		private MunicipioService ms;

		public MunicipiosProvinciaItem(Provincia provincia, MunicipioService ms) {
			this.provincia = provincia;
			this.ms = ms;
			// Tiempo de cache de 1 hora.
			setEvictionTime(3600000L);
		}
		
		public List<Municipio> getMunicipios() {
			return getCacheList();
		}
		
		@Override
		protected List<Municipio> loadCacheList() {
			if (ms!=null && provincia!=null) {
				logger.debug("Solicitando lista de municipios de {}-{} a MunicipioService",
						           provincia.getId(),provincia.getNombre());
				return ms.getListbyProvincia(provincia);
			} 
			return new ArrayList<Municipio>();
		} 
	}
	
}
