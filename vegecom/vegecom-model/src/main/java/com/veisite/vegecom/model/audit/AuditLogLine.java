package com.veisite.vegecom.model.audit;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
public class AuditLogLine implements AuditLog {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar timeStamp;
    
    @Column
    private String agent;
    
    @Column
    private String session;
    
    @Enumerated(EnumType.STRING)
    @Column(length=20)
    private AuditAction auditAction;
    
    @Column
    private String objectClass;
    
    @Column
    private String objectId;
    
    @Column
    private String message;

	/**
	 * return the id
	 */
	public Long getId() {
		return this.id;
	}

    
	@Override
	public Calendar getTimeStamp() {
		return timeStamp;
	}

	@Override
	public String getAgent() {
		return agent;
	}

	@Override
	public String getSession() {
		return session;
	}

	@Override
	public AuditAction getAuditAction() {
		return auditAction;
	}

	@Override
	public String getObjectClass() {
		return objectClass;
	}

	@Override
	public String getObjectId() {
		return objectId;
	}

	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Calendar timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * @param agent the agent to set
	 */
	public void setAgent(String agent) {
		this.agent = agent;
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(String session) {
		this.session = session;
	}

	/**
	 * @param auditAction the auditAction to set
	 */
	public void setAuditAction(AuditAction auditAction) {
		this.auditAction = auditAction;
	}

	/**
	 * @param objectClass the objectClass to set
	 */
	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}

	/**
	 * @param objectId the objectId to set
	 */
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

}
