package com.veisite.vegecom.service;

import java.util.List;

import com.veisite.vegecom.model.TipoIva;

public interface TipoIvaService {

	/**
	 * Graba en BD un tipo de de Iva
	 * @param tipoIva
	 * @return
	 */
	public TipoIva save(TipoIva tipoIva);
	
	/**
	 * Recupera un tipo de iva por su id
	 * @param id
	 * @return
	 */
	public TipoIva getById(Long id);

	/**
	 * Devuelve la lista de tipos de Iva con todos sus
	 * rangos de vigencias y tipos. Debe recuperar cada
	 * @return la lista de tipos de iva
	 */
	public List<TipoIva> getList();

}
