package com.veisite.vegecom.service.impl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.veisite.vegecom.model.TipoIva;

@Repository
public class TipoIvaDAO {

	@PersistenceContext
	transient EntityManager em;
	
	public TipoIva getById(Long id) {
		TipoIva tipoIva = (TipoIva) em.find(TipoIva.class, id);
		tipoIva.getTramosTiposIva().size();
		return tipoIva;
	}
	
	
	public TipoIva save(TipoIva tipoIva) {
		if (tipoIva.getId()==null) {
			em.persist(tipoIva);
		} else {
			tipoIva = em.merge(tipoIva);
		}
		return tipoIva;
	}

	/**
	 * Devuelve la lista de iva con sus porcentajes y vigencias.
	 * 
	 * @return
	 */
	public List<TipoIva> getList() {
		Query q = em.createQuery("SELECT i FROM TipoIva i ORDER BY i.nombre");
		@SuppressWarnings("unchecked")
		List<TipoIva> lista = q.getResultList();
		for (TipoIva ti : lista) ti.getTramosTiposIva().size();
		return lista;
	}
	

}
