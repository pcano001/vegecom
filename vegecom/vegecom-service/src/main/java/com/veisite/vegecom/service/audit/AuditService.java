package com.veisite.vegecom.service.audit;

import java.util.List;

import com.veisite.utils.dataio.DataIOException;
import com.veisite.utils.dataio.ObjectOutputFlow;
import com.veisite.vegecom.model.audit.AuditAction;
import com.veisite.vegecom.model.audit.AuditLog;
import com.veisite.vegecom.model.audit.AuditLogLine;

public interface AuditService {

	public void auditAction(String agent, 
			String session, 
			AuditAction action, 
			String objectClass, 
			Object objectId, 
			String message);
	
	public void auditAction(AuditAction action, 
			String objectClass, 
			Object objectId, 
			String message);
	
	public List<? extends AuditLog> getList(AuditLogSearchCriteria criteria);

	public void getList(ObjectOutputFlow<AuditLogLine> output,
			AuditLogSearchCriteria criteria) throws DataIOException;
	
}
