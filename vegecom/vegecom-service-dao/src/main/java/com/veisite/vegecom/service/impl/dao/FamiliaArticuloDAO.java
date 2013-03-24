package com.veisite.vegecom.service.impl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.veisite.vegecom.model.FamiliaArticulo;

@Repository
public class FamiliaArticuloDAO {

	@PersistenceContext
	transient EntityManager em;
	
	public FamiliaArticulo getById(Long id) {
	      return (FamiliaArticulo) em.find(FamiliaArticulo.class, id);
	}

	public FamiliaArticulo save(FamiliaArticulo familiaArticulo) {
		if (familiaArticulo.getId()==null) {
			em.persist(familiaArticulo);
		} else {
			familiaArticulo = em.merge(familiaArticulo);
		}
		return familiaArticulo;
	}
	
	@SuppressWarnings("unchecked")
	public List<FamiliaArticulo> getList() {
		Query q = em.createQuery("SELECT f FROM FamiliaArticulo f ORDER BY f.nombre");
		return (List<FamiliaArticulo>) q.getResultList();
	}

	public FamiliaArticulo getByNombre(String nombre) {
		Query q = em.createQuery("SELECT f FROM FamiliaArticulo f WHERE f.nombre=:nombre ORDER BY f.nombre");
		q.setParameter("nombre", nombre);
		@SuppressWarnings("unchecked")
		List<FamiliaArticulo> l = q.getResultList();
		if (l.size()>0) return (FamiliaArticulo) l.get(0);
		return null;
	}

}
