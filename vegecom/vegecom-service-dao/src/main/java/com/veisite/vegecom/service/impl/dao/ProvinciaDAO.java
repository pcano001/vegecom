package com.veisite.vegecom.service.impl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.veisite.vegecom.model.Provincia;

@Repository
public class ProvinciaDAO {

	@PersistenceContext
	transient EntityManager em;
	
	public Provincia getById(String id) {
	      return (Provincia) em.find(Provincia.class, id);
	}

	public Provincia save(Provincia provincia) {
		if (provincia.getId()==null) {
			em.persist(provincia);
		} else {
			provincia = em.merge(provincia);
		}
		return provincia;
	}
	
	@SuppressWarnings("unchecked")
	public List<Provincia> getList() {
		Query q = em.createQuery("SELECT p FROM Provincia p ORDER BY p.nombre");
		return (List<Provincia>) q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Provincia getByNombre(String nombre) {
		Query q = em.createQuery("SELECT p FROM Provincia p WHERE p.nombre=:nombre ORDER BY p.nombre");
		q.setParameter("nombre", nombre);
		List<Provincia> l = q.getResultList();
		if (l.size()>0) return (Provincia) l.get(0);
		return null;
	}

}
