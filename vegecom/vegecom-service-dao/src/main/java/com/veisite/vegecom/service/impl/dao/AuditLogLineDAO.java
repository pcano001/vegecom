package com.veisite.vegecom.service.impl.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.audit.AuditLog;
import com.veisite.vegecom.model.audit.AuditLogLine;
import com.veisite.vegecom.service.audit.AuditLogSearchCriteria;

@Repository
public class AuditLogLineDAO {

	private static Logger logger = LoggerFactory.getLogger(AuditLogLineDAO.class);
	
	@PersistenceContext
	transient EntityManager em;
	
	public AuditLogLine save(AuditLogLine auditLine) {
		if (auditLine.getId()==null) {
			em.persist(auditLine);
		} else {
			auditLine = em.merge(auditLine);
		}
		return auditLine;
	}

	public List<? extends AuditLog> getList(AuditLogSearchCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	
	public void getList(ObjectOutputFlow<AuditLogLine> output,
			AuditLogSearchCriteria criteria) throws DataIOException {
		org.hibernate.Query q = getHibernateQueryFromCriteria(criteria);
		logger.debug("Quering database for AuditLog List...");
		ScrollableResults sc = q.scroll();
		logger.debug("Begin writing AuditLog to ObjectOutputStream...");
		if (!sc.first()) {
			output.close();
			sc.close();
			return;
		}
		do {
			AuditLogLine l = (AuditLogLine) sc.get()[0];
			output.write(l);
		} while (sc.next());
		sc.close();
		output.close();
		logger.debug("Writing AuditLog has ended correctly, exiting...");
	}

	
	/**
	 * Devuelve la query para Hibernate para buscar segun los criterios especificados
	 * TODO
	 * De momento solo se soporta criterios de fecha.
	 * 
	 * @param criteria
	 * @return
	 */
	private org.hibernate.Query getHibernateQueryFromCriteria(AuditLogSearchCriteria criteria) {
		String sql = "FROM AuditLogLine l WHERE 1=1 ";
		if (criteria!=null && criteria.getFromTime()!=null)
			sql += "AND l.timeStamp >= :fromTime ";
		if (criteria!=null && criteria.getToTime()!=null)
			sql += "AND l.timeStamp <= :toTime ";
		sql += "ORDER BY l.timeStamp DESC";
		org.hibernate.Session session = (Session) em.getDelegate();
		org.hibernate.Query query = session.createQuery(sql);
		if (criteria!=null && criteria.getFromTime()!=null)
			query.setParameter("fromTime", criteria.getFromTime());
		if (criteria!=null && criteria.getToTime()!=null)
			query.setParameter("toTime", criteria.getToTime());
		return query;
	}

}
