package com.veisite.vegecom.ui.framework.module;

import java.awt.Window;

import javax.validation.Validator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.util.Assert;

import com.veisite.vegecom.ui.framework.UIFrameworkInstance;

public abstract class UIFrameworkAbstractModule implements UIFrameworkModule {
	
	/**
	 * UIInstance
	 */
	protected UIFrameworkInstance uiInstance;
	
	/**
	 * context
	 */
	protected ApplicationContext context;

	public UIFrameworkAbstractModule(UIFrameworkInstance uiInstance,
			ApplicationContext context) {
		Assert.notNull(uiInstance);
		Assert.notNull(context);
		this.uiInstance = uiInstance;
		this.context = context;
	}


	@Override
	public String getMessage(String code, Object[] args, String defaultMessage) {
		return uiInstance.getMessage(code, args, defaultMessage);
	}

	@Override
	public Validator getValidator() {
		return uiInstance.getValidator();
	}


	@Override
	public Window getParentWindow() {
		return uiInstance;
	}
	
	public ApplicationContext getContext() {
		return this.context;
	}
	
	public MessageSource getMessageSource() {
		return uiInstance.getMessageSource();
	}

}
