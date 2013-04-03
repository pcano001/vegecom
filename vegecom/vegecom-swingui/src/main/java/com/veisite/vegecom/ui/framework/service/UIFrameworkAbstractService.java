package com.veisite.vegecom.ui.framework.service;

import java.awt.Window;

import javax.validation.Validator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;

import com.veisite.vegecom.ui.framework.module.UIFrameworkModule;

public abstract class UIFrameworkAbstractService implements UIFrameworkService {

	/**
	 * instancia de framework
	 */
	protected UIFrameworkModule uiModule;
	
	public UIFrameworkAbstractService(UIFrameworkModule uiModule) {
		Assert.notNull(uiModule);
		this.uiModule = uiModule;
	}
	
	/**
	 * @return the uiInstance
	 */
	public UIFrameworkModule getUiModule() {
		return uiModule;
	}

	public String getMessage(String code, Object[] args, String defaultMessage) {
		return uiModule.getMessage(code, args, defaultMessage);
	}

	public Validator getValidator() {
		return uiModule.getValidator();
	}

	public MessageSource getMessageSource() {
		return uiModule.getMessageSource();
	}
	
	public ApplicationContext getContext() {
		return uiModule.getContext();
	}
	
	/* (non-Javadoc)
	 * @see com.veisite.vegecom.ui.framework.service.UIFrameworkService#getParentWindow()
	 */
	@Override
	public Window getParentWindow() {
		return uiModule.getParentWindow();
	}
	
}
