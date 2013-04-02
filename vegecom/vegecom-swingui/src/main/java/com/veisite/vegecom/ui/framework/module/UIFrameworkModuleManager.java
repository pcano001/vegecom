package com.veisite.vegecom.ui.framework.module;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.veisite.vegecom.model.exception.VegecomException;
import com.veisite.vegecom.ui.framework.UIFrameworkObject;

public class UIFrameworkModuleManager implements UIFrameworkObject {
	
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
	List<UIFrameworkModule> moduleList = new ArrayList<UIFrameworkModule>();

	
	public UIFrameworkModuleManager(String id) {
		Assert.notNull(id);
	}
	
	@Override
	public String getId() {
		return id;
	}

	public void installModule(UIFrameworkModule module) throws Throwable {
		logger.debug("Trying to install module with id "+module.getId());
		try {
			module.initModule();
		} catch (Throwable t) {
			logger.error("Error installing module with id "+module.getId(),t);
			throw t;
		}
		logger.debug("module with id "+module.getId()+" installed");
		moduleList.add(module);
	}

	/**
	 * Desintala un modulo
	 * 
	 * @param id
	 * @throws VegecomException
	 */
	public void uninstallModule(String id) throws Throwable {
		UIFrameworkModule module = getModule(id);
		if (module!=null) uninstallModule(module);
	}
	
	public void uninstallModule(UIFrameworkModule module) throws Throwable {
		logger.debug("Trying to uninstall module with id "+module.getId());
		int i = moduleList.indexOf(module);
		if (i>=0) {
			try {
				module.disposeModule();
			} catch (Throwable t) {
				logger.error("Error uninstalling module with id "+module.getId(),t);
				throw t;
			}
			moduleList.remove(i);
			logger.debug("module with id "+module.getId()+" uninstalled");
		}
	}
	
	
	private UIFrameworkModule getModule(String id) {
		for (UIFrameworkModule m : moduleList)
			if (m.getId().equals(id)) return m;
		return null;
	}
}
