package com.veisite.vegecom.service.audit;

import java.util.Calendar;

import com.veisite.vegecom.model.audit.AuditAction;

public class AuditLogSearchCriteria {
	
	private Calendar fromTime = null;
	
	private Calendar toTime = null;
	
	private String agent = null;
	
	private AuditAction auditAction = null;
	
	private String objectClass = null;
	
	private String objectId = null;
	
	public AuditLogSearchCriteria fromTime(Calendar fromTime) {
		this.fromTime = fromTime;
		return this;
	}

	public AuditLogSearchCriteria toTime(Calendar toTime) {
		this.toTime = toTime;
		return this;
	}
	
	public AuditLogSearchCriteria withAgent(String agent) {
		this.agent = agent;
		return this;
	}

	public AuditLogSearchCriteria withAuditAction(AuditAction auditAction) {
		this.auditAction = auditAction;
		return this;
	}

	public AuditLogSearchCriteria onType(String objectClass) {
		this.objectClass = objectClass;
		return this;
	}
	
	public AuditLogSearchCriteria onObject(String objectId) {
		this.objectId = objectId;
		return this;
	}

	/**
	 * @return the fromTime
	 */
	public Calendar getFromTime() {
		return fromTime;
	}

	/**
	 * @param fromTime the fromTime to set
	 */
	public void setFromTime(Calendar fromTime) {
		this.fromTime = fromTime;
	}

	/**
	 * @return the toTime
	 */
	public Calendar getToTime() {
		return toTime;
	}

	/**
	 * @param toTime the toTime to set
	 */
	public void setToTime(Calendar toTime) {
		this.toTime = toTime;
	}

	/**
	 * @return the agent
	 */
	public String getAgent() {
		return agent;
	}

	/**
	 * @return the auditAction
	 */
	public AuditAction getAuditAction() {
		return auditAction;
	}

	/**
	 * @return the objectClass
	 */
	public String getObjectClass() {
		return objectClass;
	}

	/**
	 * @return the objectId
	 */
	public String getObjectId() {
		return objectId;
	}

}
