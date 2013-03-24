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
import com.veisite.vegecom.model.Articulo;

@Repository
public class ArticuloDAO {

	public static final Logger logger = LoggerFactory.getLogger(ArticuloDAO.class);

	@PersistenceContext
	transient EntityManager em;
	
	public Articulo getById(Long id) {
		Articulo articulo = (Articulo) em.find(Articulo.class, id);
		return articulo;
	}
	
	public Articulo save(Articulo articulo) {
		if (articulo.getId()==null) {
			em.persist(articulo);
		} else {
			articulo = em.merge(articulo);
		}
		return articulo;
	}

	/**
	 * Devuelve la lista de articulo.
	 * 
	 * @return
	 */
	public List<Articulo> getList() {
		Query q = 
				em.createQuery("SELECT a " +
						"FROM Articulo a " +
						"LEFT JOIN FETCH a.familia " +
						"ORDER BY a.codigo");
		@SuppressWarnings("unchecked")
		List<Articulo> lista = q.getResultList();
		return lista;
	}

	
	/**
	 * 	Recupera la lista de articulo y la envia a un buffer de lectura/escritura
	 * @param output salida hacia la que se dirigen los objectos. 
	 * @throws DataIOException
	 */
	public void writeListTo(ObjectOutputFlow<Articulo> output) throws DataIOException {
		Session session = (Session) em.getDelegate();
		org.hibernate.Query q = session.createQuery("FROM Articulo a "
				+ "LEFT JOIN FETCH a.familia "
				+ "ORDER BY a.codigo");
		logger.debug("Querying database for Articulo List...");
		ScrollableResults sc = q.scroll();
		logger.debug("Begin writing Articulo list to ObjectOutputFlow...");
		if (!sc.first()) {
			output.close();
			sc.close();
			return;
		}
		do {
			Articulo c = (Articulo) sc.get()[0];
			output.write(c);
		} while (sc.next());
		sc.close();
		output.close();
		logger.debug("Writing Articulo list has ended correctly, exiting...");
	}
	
	
}
