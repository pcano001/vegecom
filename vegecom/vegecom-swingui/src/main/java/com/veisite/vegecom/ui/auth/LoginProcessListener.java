package com.veisite.vegecom.ui.auth;


public interface LoginProcessListener {
	
	/**
	 * Se llama al iniciar la carga del contexto.
	 */
	public void init();
	
	/**
	 * Se llama si se produce un error en la autenticación del usuario.
	 * 
	 * @param exception
	 */
	public void error(Throwable exception, String user);
	
	/**
	 * Se llama cuando se finaliza correctamente la autenticacioń y el
	 * usuario se ha autenticado.
	 */
	public void authenticated();

}
