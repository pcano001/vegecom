package com.veisite.vegecom.ui.framework.service;

import java.awt.Window;

import javax.validation.Validator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import com.veisite.vegecom.ui.framework.UIFrameworkObject;

public interface UIFrameworkService extends UIFrameworkObject {

	public void initService() throws Throwable;
	
	public void disposeService();
	
	public String getMessage(String code, Object[] args, String defaultMessage);
	
	public MessageSource getMessageSource();

	public Validator getValidator();
	
	public Window getParentWindow();
	
	public ApplicationContext getContext();
	
}
