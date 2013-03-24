package com.veisite.vegecom.model.audit;

import java.util.Calendar;

public interface AuditLog {
	
	public Calendar getTimeStamp();
	
	public String getAgent();
	
	public String getSession();
	
	public AuditAction getAuditAction();
	
	public String getObjectClass();
	
	public String getObjectId();
	
	public String getMessage();
	
}
