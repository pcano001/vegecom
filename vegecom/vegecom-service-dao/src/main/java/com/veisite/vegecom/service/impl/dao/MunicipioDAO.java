package com.veisite.vegecom.service.impl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.Municipio;
import com.veisite.vegecom.model.Provincia;

@Repository
public class MunicipioDAO {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
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
	
	public List<Municipio> getList() {
		Query q = em.createQuery("SELECT m FROM Municipio m LEFT JOIN FETCH m.provincia ORDER BY m.nombre");
		@SuppressWarnings("unchecked")
		List<Municipio> l = (List<Municipio>) q.getResultList();
		return l;
	}

	public List<Municipio> getListbyProvincia(Provincia provincia) {
		Query q = em.createQuery("SELECT m FROM Municipio m LEFT JOIN FETCH m.provincia WHERE provincia=:provinciaId ORDER BY m.nombre");
		q.setParameter ("provinciaId", provincia);
		@SuppressWarnings("unchecked")
		List<Municipio> l = (List<Municipio>) q.getResultList();
		return l;
	}
	
	public Municipio getByNombre(String nombre) {
		if (nombre==null) return null;
		Query q = em.createQuery("SELECT m FROM Municipio m LEFT JOIN FETCH m.provincia WHERE m.nombre=:nombre ORDER BY m.nombre");
		q.setParameter("nombre", nombre);
		@SuppressWarnings("unchecked")
		List<Municipio> l = (List<Municipio>) q.getResultList();
		if (l.size()>0) return (Municipio) l.get(0);
		return null;
	}

	public void getList(ObjectOutputFlow<Municipio> output) throws DataIOException {
		org.hibernate.Session session = (Session) em.getDelegate();
		org.hibernate.Query q = session.createQuery("FROM Municipio m "
				+ "LEFT JOIN FETCH m.provincia "
				+ "ORDER BY m.nombre");

		logger.debug("Quering database for Municipio List...");
		ScrollableResults sc = q.scroll(ScrollMode.FORWARD_ONLY);
		logger.debug("Begin writing municipio to ObjectOutputStream...");
		while (sc.next()) {
			Municipio m = (Municipio) sc.get()[0];
			output.write(m);
		}
		sc.close();
		output.close();
		logger.debug("Writing municipio has ended correctly, exiting...");
	}

}
