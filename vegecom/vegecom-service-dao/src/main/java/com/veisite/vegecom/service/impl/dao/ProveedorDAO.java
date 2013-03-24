package com.veisite.vegecom.service.impl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.Proveedor;

@Repository
public class ProveedorDAO {

	public static final Logger logger = LoggerFactory.getLogger(ProveedorDAO.class);

	@PersistenceContext
	transient EntityManager em;
	
	public Proveedor getById(Long id) {
		Proveedor proveedor = (Proveedor) em.find(Proveedor.class, id);
		proveedor.getPreciosArticulos().size();
		return proveedor;
	}
	
	public Proveedor save(Proveedor proveedor) {
		if (proveedor.getId()==null) {
			em.persist(proveedor);
		} else {
			proveedor = em.merge(proveedor);
		}
		return proveedor;
	}

	public void remove(Proveedor proveedor) {
		if (proveedor.getId()==null) return;
		em.remove(proveedor);
	}
	
	/**
	 * Devuelve la lista de Proveedor.
	 * 
	 * @return
	 */
	public List<Proveedor> getList() {
		Query q = 
				em.createQuery("SELECT p " +
						"FROM Proveedor p " +
						"LEFT JOIN FETCH p.municipio " +
						"LEFT JOIN FETCH p.provincia " +
						"ORDER BY p.nombre");
		@SuppressWarnings("unchecked")
		List<Proveedor> lista = q.getResultList();
		return lista;
	}

	
	/**
	 * 	Recupera la lista de Proveedor y la envia a un buffer de lectura/escritura
	 * @param output
	 * @throws DataIOException
	 */
	public void writeListTo(ObjectOutputFlow<Proveedor> output) throws DataIOException {
		Session session = (Session) em.getDelegate();
		org.hibernate.Query q = session.createQuery("FROM Proveedor p "
				+ "LEFT JOIN FETCH p.municipio "
				+ "LEFT JOIN FETCH p.provincia " 
				+ "ORDER BY p.nombre");
		logger.debug("Querying database for Proveedor List...");
		ScrollableResults sc = q.scroll();
		logger.debug("Begin writing Proveedor list to ObjectOutputFlow...");
		if (!sc.first()) {
			output.close();
			sc.close();
			return;
		}
		do {
			Proveedor p = (Proveedor) sc.get()[0];
			output.write(p);
		} while (sc.next());
		sc.close();
		output.close();
		logger.debug("Writing Proveedor list has ended correctly, exiting...");
	}

	
}
