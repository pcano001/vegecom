package com.veisite.vegecom.service.impl;

import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.audit.AuditAction;
import com.veisite.vegecom.model.audit.AuditLog;
import com.veisite.vegecom.model.audit.AuditLogLine;
import com.veisite.vegecom.service.audit.AuditLogSearchCriteria;
import com.veisite.vegecom.service.audit.AuditService;
import com.veisite.vegecom.service.impl.dao.AuditLogLineDAO;
import com.veisite.vegecom.service.security.SecurityService;

@Service
public class AuditServiceImpl implements AuditService {

	/**
	 * log service
	 */
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	AuditLogLineDAO dao;
	
	@Autowired
	SecurityService ss;
	
	@Override
	@Transactional
	public void auditAction(String agent, String session, AuditAction action,
			String objectClass, Object objectId, String message) {
		if (ss!=null) ss.touchSession();
		AuditLogLine line = new AuditLogLine();
		line.setTimeStamp(new GregorianCalendar());
		line.setAgent(agent);
		line.setSession(session);
		line.setAuditAction(action);
		line.setObjectClass(objectClass);
		if (objectId!=null)
			line.setObjectId(objectId.toString());
		line.setMessage(message);
		try {
			dao.save(line);
		} catch (Throwable t) {
			logger.error("Error inserting audit line.",t);
		}
	}

	@Override
	@Transactional
	public void auditAction(AuditAction action, String objectClass,
			Object objectId, String message) {
		String agent=null;
		String session=null;
		// Intentemos averiguar el usuario actual.
		if (ss!=null) {
			agent = ss.getAuthenticatedUser();
			session = ss.getSessionId();
		}
		auditAction(agent, session, action, objectClass, objectId, message);
	}

	@Override
	@Transactional
	public List<? extends AuditLog> getList(AuditLogSearchCriteria criteria) {
		return dao.getList(criteria);
	}

	@Override
	@Transactional
	public void getList(ObjectOutputFlow<AuditLogLine> output,
			AuditLogSearchCriteria criteria) throws DataIOException {
		dao.getList(output,criteria);
	}

}
