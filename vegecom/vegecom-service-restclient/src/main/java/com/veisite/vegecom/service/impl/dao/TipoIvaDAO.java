package com.veisite.vegecom.service.impl.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.veisite.vegecom.model.TipoIva;

@Repository
public class TipoIvaDAO {

	public TipoIva getById(Long id) {
		return new TipoIva();
	}
	
	
	public TipoIva save(TipoIva tipoIva) {
		return tipoIva;
	}

	/**
	 * Devuelve la lista de iva con sus porcentajes y vigencias.
	 * 
	 * @return
	 */
	public List<TipoIva> getList() {
		List<TipoIva> lista = new ArrayList<TipoIva>();
		lista.add(new TipoIva());
		return lista;
	}
	

}
