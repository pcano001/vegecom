package com.veisite.vegecom.service.impl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.veisite.vegecom.model.Municipio;
import com.veisite.vegecom.model.Provincia;

@Repository
public class MunicipioDAO {

	@PersistenceContext
	transient EntityManager em;
	
	public Municipio getById(String id) {
	      return (Municipio) em.find(Municipio.class, id);
	}

	public Municipio save(Municipio municipio) {
		if (municipio.getId()==null) {
			em.persist(municipio);
		} else {
			municipio = em.merge(municipio);
		}
		return municipio;
	}
	
	@SuppressWarnings("unchecked")
	public List<Municipio> getList() {
		Query q = em.createQuery("SELECT m FROM Municipio m ORDER BY m.nombre");
		return (List<Municipio>) q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Municipio> getListbyProvincia(Provincia provincia) {
		Query q = em.createQuery("SELECT m FROM Municipio m WHERE provincia=:provinciaId ORDER BY m.nombre");
		q.setParameter ("provinciaId", provincia);
		return (List<Municipio>) q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public Municipio getByNombre(String nombre) {
		if (nombre==null) return null;
		Query q = em.createQuery("SELECT m FROM Municipio m WHERE m.nombre=:nombre ORDER BY m.nombre");
		q.setParameter("nombre", nombre);
		List<Municipio> l = q.getResultList();
		if (l.size()>0) return (Municipio) l.get(0);
		return null;
	}

}
