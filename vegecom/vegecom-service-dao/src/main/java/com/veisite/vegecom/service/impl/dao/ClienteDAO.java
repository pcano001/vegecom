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
import com.veisite.vegecom.model.Cliente;

@Repository
public class ClienteDAO {

	public static final Logger logger = LoggerFactory.getLogger(ClienteDAO.class);

	@PersistenceContext
	transient EntityManager em;
	
	public Cliente getById(Long id) {
		Cliente cliente = (Cliente) em.find(Cliente.class, id);
		if (cliente!=null) {
			cliente.getPreciosArticulos().size();
		}
		return cliente;
	}
	
	public Cliente save(Cliente cliente) {
		if (cliente.getId()==null) {
			em.persist(cliente);
		} else {
			cliente = em.merge(cliente);
		}
		return cliente;
	}

	public Cliente remove(Long id) {
		Cliente cliente = getById(id);
		if (cliente!=null) em.remove(cliente);
		return cliente;
	}
	
	/**
	 * Devuelve la lista de cliente.
	 * 
	 * @return
	 */
	public List<Cliente> getList() {
		Query q = 
				em.createQuery("SELECT c " +
						"FROM Cliente c " +
						"LEFT JOIN FETCH c.municipio " +
						"LEFT JOIN FETCH c.provincia " +
						"ORDER BY c.nombre");
		@SuppressWarnings("unchecked")
		List<Cliente> lista = q.getResultList();
		return lista;
	}

	
	/**
	 * 	Recupera la lista de cliente y la envia a un buffer de lectura/escritura
	 * @param output
	 * @throws DataIOException
	 */
	public void writeListTo(ObjectOutputFlow<Cliente> output) throws DataIOException {
		Session session = (Session) em.getDelegate();
		org.hibernate.Query q = session.createQuery("FROM Cliente c "
				+ "LEFT JOIN FETCH c.municipio "
				+ "LEFT JOIN FETCH c.provincia " 
				+ "ORDER BY c.nombre");
		logger.debug("Querying database for Cliente List...");
		ScrollableResults sc = q.scroll();
		logger.debug("Begin writing Cliente list to ObjectOutputFlow...");
		if (!sc.first()) {
			output.close();
			sc.close();
			return;
		}
		do {
			Cliente c = (Cliente) sc.get()[0];
			output.write(c);
		} while (sc.next());
		sc.close();
		logger.debug("Writing Cliente list has ended correctly, exiting...");
	}

	
}
