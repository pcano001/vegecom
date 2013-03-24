package com.veisite.vegecom.model.audit;

public enum AuditAction {
	LOGIN("Acceso"),
	LOGIN_FAIL("Acceso erroneo"),
	LOGOUT("Desconexion"),
	SELECT("Consulta"),
	INSERT("Alta"),
	UPDATE("Modificación"),
	DELETE("Borrado");
	
	private String name;
	
	private AuditAction(String name) {
		this.name = name;
	}
	
	public String toString() {
		return name;
	}

}
