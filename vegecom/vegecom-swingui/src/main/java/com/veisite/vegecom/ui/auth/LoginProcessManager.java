package com.veisite.vegecom.ui.auth;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.util.Assert;

import com.veisite.vegecom.model.exception.VegecomException;
import com.veisite.vegecom.service.security.SecurityService;

/**
 * Gestiona el proceso de autenticacion de un usuario.
 * 
 * Realiza el proceso de validación en segundo plano  
 * 
 * @author josemaria
 *
 */
public class LoginProcessManager {
	
	/**
	 * Lista de listener que escuchan el progreso de la tarea.
	 */
	private List<LoginProcessListener> _listeners = new ArrayList<LoginProcessListener>();
	
	/**
	 * Servicio de seguridad de la aplicacion.
	 */
	private SecurityService ss = null;

	/**
	 * Constructor
	 */
	public LoginProcessManager(SecurityService ss) {
		Assert.notNull(ss);
		this.ss = ss;
	}

	public void doBackgroundAuthentication(final String user, final String password) {
		Thread th = new Thread(new Runnable() {
			@Override
			public void run() {
				startAuthentication(user, password);
			}
		});
		th.start();
	}

	private synchronized void startAuthentication(String user, String password) {
		initAuthentication();
		try {
			doUserAuthentication(user, password);
		} catch (Throwable t) {
			errorAuthentication(t, user);
			return;
		}
		okAuthentication();
	}
	
	private void doUserAuthentication(String user, String password)
			 throws Throwable {
		if (ss==null)
			throw new VegecomException("Internal Error. Security Service not found.");
		ss.login(user, password);
	}
	
	private void initAuthentication() {
		Iterator<LoginProcessListener> i = _listeners.iterator();
		while (i.hasNext()) {
			((LoginProcessListener) i.next()).init();
		}
	}
	
	private void errorAuthentication(Throwable t, String user) {
		Iterator<LoginProcessListener> i = _listeners.iterator();
		while (i.hasNext()) {
			((LoginProcessListener) i.next()).error(t, user);
		}
	}

	private void okAuthentication() {
		Iterator<LoginProcessListener> i = _listeners.iterator();
		while (i.hasNext()) {
			((LoginProcessListener) i.next()).authenticated();
		}
	}

	
	/**
	 * Gestión de listener.
	 * @param listener
	 */
	public synchronized void addConfigurationListener(LoginProcessListener listener) {
		_listeners.add(listener);
	}

	public synchronized void removeConfigurationListener(LoginProcessListener listener) {
		_listeners.remove(listener);
	}

	
}
