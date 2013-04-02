package com.veisite.vegecom.ui.framework.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.veisite.vegecom.ui.framework.UIFrameworkObject;

public class UIFrameworkServiceManager implements UIFrameworkObject {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * id
	 */
	private String id;
	
	/**
	 * Lista de modulos que tenemos cargados e iniciados 
	 * 
	 * @param id
	 */
	List<UIFrameworkService> serviceList = new ArrayList<UIFrameworkService>();
	

	public UIFrameworkServiceManager(String id) {
		this.id = id;
	}
	
	
	@Override
	public String getId() {
		return id;
	}

	/**
	 * Metodo para registrar un servicio en el framework
	 */
	public void registerService(UIFrameworkService service) throws Throwable {
		// Si el servicio ya está registrado, salir.
		if (serviceList.contains(service)) {
			logger.warn("registerService. Service '"+service.getId()+"' already registered");
			return;
		}
		service.initService();
		serviceList.add(service);
		logger.debug("Service {} registered.",service.getId());
	}
	
	/**
	 * Metodo para desregistrar un servicio en el framework
	 */
	public void deregisterService(UIFrameworkService service) throws Throwable {
		// Si el servicio no está registrado, salir.
		if (!serviceList.contains(service)) {
			logger.warn("deregisterService. Service '"+service.getId()+"' not found");
			return;
		}
		service.disposeService();
		serviceList.remove(service);
		logger.debug("Service {} deregistered.",service.getId());
	}
	
	/**
	 * Devuelve el servicio registrado con el Id indicado
	 */
	public UIFrameworkService getService(String id) {
		for (UIFrameworkService s : serviceList)
			if (s.getId().equals(id)) return s;
		return null;
	}
	
	/**
	 * Devuelve el servicio registrado con el Id indicado
	 */
	@SuppressWarnings("unchecked")
	public <T extends UIFrameworkService> T getService(Class<T> serviceType) {
		for (UIFrameworkService s : serviceList)
			if (s.getClass().equals(serviceType)) return (T) s;
		return null;
	}
	
}
